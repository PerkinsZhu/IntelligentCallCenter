package com.perkins.icc.freeswitch

import org.freeswitch.esl.client.dptools.Execute
import org.freeswitch.esl.client.internal.IModEslApi

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/22 14:24
 * @description:
 * */
class ICCExecute(api: IModEslApi, uuid: String) extends Execute(api, uuid) {

}
