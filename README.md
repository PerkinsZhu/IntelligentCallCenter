# IntelligentCallCenter
智能呼叫中心

项目框架基于阿里 [COLA 4.0](https://github.com/alibaba/COLA) DDD 领域驱动框架

基础依赖：

1. [freeswitch 1.10](https://github.com/PerkinsZhu/freeswitch.git)
2. [sip.js](https://github.com/onsip/SIP.js.git)
3. [uckefu-softphone](https://gitee.com/perkins_zhu/uckefu-softphone)

功能模块:

1. ​		新增分机号	
2. ​		webrtc外呼
3. ​		批量拨打



实现方案：

​	1、批量外呼：

​			通过job每3S秒轮训一次外呼任务。

​			外呼逻辑：

​				a、从redis中获取当前空闲坐席量n，计算n/接通率(通过历史数据预设出来的固定数值) - 2 =m。则m为此次外呼的名单量。通过inBount方式调用freeswitch api 把m条新名单外呼出去。当呼叫失败

