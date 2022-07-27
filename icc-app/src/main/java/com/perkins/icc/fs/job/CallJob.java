package com.perkins.icc.fs.job;

import com.alibaba.cola.dto.MultiResponse;
import com.perkins.icc.api.CustomerServiceI;
import com.perkins.icc.domain.agents.AgentService;
import com.perkins.icc.domain.agents.AgentStatus;
import com.perkins.icc.domain.cache.CacheService;
import com.perkins.icc.domain.call.TransferToAgent;
import com.perkins.icc.domain.common.Constant;
import com.perkins.icc.domain.cache.RedisCmd;
import com.perkins.icc.domain.customer.Customer;
import com.perkins.icc.dto.CustomerListQry;
import com.perkins.icc.dto.data.CustomerDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 预测式外呼逻辑
 *
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
    private CacheService cacheService;
    @Autowired
    private TransferToAgent transferToAgent;


    @Autowired
    private AgentService agentService;

    @Autowired
    private CustomerServiceI customerService;
    //接通率，可以从数据库中查询
    private Float connectionRate = 0.96F;

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
                Integer availableSeatCount = agentService.getCountByState(AgentStatus.Available.getStatus());
                log.info("当前空闲坐席数量:{},tenantId:{}", availableSeatCount, tenantId);
                if (availableSeatCount > 0) {
                    Integer customerCount = getCustomerCount(availableSeatCount);
                    if (customerCount == null) {
                        return;
                    }
                    List<CustomerDTO> callList = getCallList(customerCount);
                    callList.stream().forEach(customer -> {
                        tenantSPool.execute(() -> {
                            transferToAgent.transfer(customer);
                        });
                    });
                }
                Thread.sleep(5000);
            }

        }
    }

    /**
     * 计算本次外呼名单数量
     * 外呼数量 = (当前空闲坐席 / 电话接通率) - 2
     * 可以自定义外呼数量策略
     *
     * @param availableSeatCount
     * @return
     */
    private Integer getCustomerCount(Integer availableSeatCount) {
        Float customerCountTemp = availableSeatCount / connectionRate;
        Integer customerCount = customerCountTemp.intValue() - 2;
        if (customerCount < 1) {
            return null;
        }
        return customerCount;
    }


    private List<CustomerDTO> getCallList(Integer count) {
        //TODO 从数据库中查询出待呼出客户
        CustomerListQry customerListQry = new CustomerListQry();
        customerListQry.setPageIndex(0);
        customerListQry.setPageSize(count);
        MultiResponse<CustomerDTO> response = customerService.list(customerListQry);
        return response.getData();
    }


}
