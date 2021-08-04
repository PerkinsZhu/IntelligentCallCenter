package com.perkins.icc.fs.listener;

import com.alibaba.cola.dto.Response;
import com.perkins.icc.domain.cache.CacheService;
import com.perkins.icc.domain.common.Constant;
import com.perkins.icc.domain.event.FsEventType;
import com.perkins.icc.domain.cache.RedisCmd;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: perkins Zhu
 * @date: 2021/8/1 18:13
 * @description:
 **/
@Slf4j
@Component
public class InBoundListener implements IEslEventListener {

    @Autowired
    private CacheService cacheService;

    @Override
    public void eventReceived(EslEvent eslEvent) {
        log.info("Event received [{}]", eslEvent);
    }

    @Override
    public void backgroundJobResultReceived(EslEvent eslEvent) {
        /**
         * 这里可以接受到 bgapi 的事件
         * 接收到外呼事件之后，开启子线程查询空闲坐席，转接给空闲坐席
         * 这里需要注意的是，加入并发量大的话，这里会开很多线程，这时候怎么设置线程池最大线程数量?
         *
         * 子线程执行的任务包括:
         *      1、根究策略查询出接听该通话的空闲坐席
         *      2、呼叫空闲坐席
         *      3、呼叫成功后桥接给客户
         *
         * 这里的最大线程池的数量会限制到外呼系统的最大并发外呼数量。要保证最大线程池大于外呼最大并发数量
         * 这里不建议使用线程池实时处理，一旦外呼并大过大，线程池资源不足，会导致任务被丢弃掉。
         * 最好使用生产者/消费者模式，把以接听的电话放入待处理队列中，然后消费者逐个处理。这样不会导致电话丢失掉。
         */
        log.info("Background job result received [{}]", eslEvent);
        if (FsEventType.BACKGROUND_JOB.getCode().equals(eslEvent.getEventName())) {
            String uuid = getUuid(eslEvent);
            RedisCmd cmd = RedisCmd.builder()
                    .key(Constant.r_call_queue_key)
                    .value(uuid)
                    .build();
            Response response = cacheService.execute(cmd);
            if (!response.isSuccess()) {
                log.error(response.getErrMessage());
            }
        }
    }

    private String getUuid(EslEvent eslEvent) {
        if (eslEvent == null || eslEvent.getEventBodyLines().isEmpty()) {
            return null;
        }
        String res = eslEvent.getEventBodyLines().get(0);
        if (res.startsWith("+OK")) {
            return res.substring(4);
        }
        return null;
    }
}
