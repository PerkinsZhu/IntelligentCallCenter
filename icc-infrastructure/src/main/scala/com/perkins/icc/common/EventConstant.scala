package com.perkins.icc.common

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/16 10:56
 * @description:
 * */
object EventConstant {

  val ESL_EVENT_NAME_CUSTOM = "CUSTOM"
  val ESL_EVENT_CHANNEL_HANGUP_COMPLETE = "CHANNEL_HANGUP_COMPLETE"
  val ESL_EVENT_CHANNEL_EXECUTE_COMPLETE = "CHANNEL_EXECUTE_COMPLETE"

  val ESL_EVENT_SUBCLASS_NAME_SOFIA_UNREGISTER = "sofia::unregister"
  val ESL_EVENT_SUBCLASS_NAME_SOFIA_REGISTER = "sofia::register"

  val ESL_EVENT_STATUS = "CUSTOM"



  val EVENT_KEY_HANGUP_CAUSE = "Hangup-Cause";
  val EVENT_KEY_CALLER_CALLER_ID_NUMBER = "Caller-Caller-ID-Number";
  val EVENT_KEY_CALLER_DESTINATION_NUMBER = "Caller-Destination-Number";
}
