package com.perkins.icc.fs.job;

import com.perkins.icc.agent.TransferServiceImpl;
import com.perkins.icc.cache.executor.CacheExe;
import com.perkins.icc.domain.common.Constant;
import com.perkins.icc.dto.cache.RedisCmd;
import com.perkins.icc.fs.executor.FsClientExe;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: perkins Zhu
 * @date: 2021/8/2 14:21
 * @description:
 **/
@Component
@EnableScheduling
@EnableAsync
@Slf4j
@ConditionalOnExpression("${job.enabled:true}")
public class CallJob implements ApplicationRunner {

    private ExecutorService tenantSPool = Executors.newFixedThreadPool(100);

    @Autowired
    private CacheExe cacheExe;

    @Autowired
    private TransferServiceImpl transferService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //这里要考虑到租户的概念，每个租户都应该有一个where消费者
        List<Long> tenantList = new ArrayList();
        tenantList.add(1001L);
        tenantList.forEach(i -> {
            new JobHandle(i).start();
        });
    }

    class JobHandle extends Thread {
        private Long tenantId;

        public JobHandle(Long tenantId) {
            this.tenantId = tenantId;
            this.setName("jobhandle-" + tenantId);
        }

        /**
         * 消费层设置标准的消费逻辑
         * 在控制层控制并发外呼的数量
         * 当有空闲坐席时，则从队列中取出和空闲数量相同的电话
         */
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                int availableSeatCount = 10;
                log.info("当前空闲坐席数量:{},tenantId:{}", availableSeatCount, tenantId);
                if (availableSeatCount > 0) {
                    //如果有空闲坐席，则从消费队列中弹出availableSeatCount - 2 个数据
                    List callList = getCallList(availableSeatCount - 2);
                    callList.stream().forEach(uuid -> {
                        //把外呼电话转接给空闲坐席
                        tenantSPool.execute(() -> {
                            transferService.bridgeTo(uuid.toString());
                        });
                    });
                }
                Thread.sleep(5000);
            }

        }
    }

    private List getCallList(int count) {
        RedisCmd cmd = RedisCmd.builder()
                .key(Constant.r_call_queue_key)
                .limit(count)
                .build();
        List list = cacheExe.getQueue(cmd);
        return list;
    }


}
