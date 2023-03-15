package com.perkins.icc.api

/**
 *
 * @author: perkins Zhu
 * @date: 2023/3/15 16:59
 * @description:
 * */
trait CallServiceI {

  def call(phone: String): Boolean
}
