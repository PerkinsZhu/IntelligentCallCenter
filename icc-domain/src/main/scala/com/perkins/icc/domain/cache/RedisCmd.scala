package com.perkins.icc.domain.cache


case class RedisCmd[T](key: String, value: T, expire: Option[Long] = None, limit: Integer = 1, isPop: Boolean = false)
