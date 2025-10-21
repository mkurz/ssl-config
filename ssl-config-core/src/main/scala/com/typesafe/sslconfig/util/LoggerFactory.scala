/*
 * Copyright (C) 2015 - 2025 Lightbend Inc. <https://www.lightbend.com>
 */

package com.typesafe.sslconfig.util

trait LoggerFactory {
  def apply(clazz: Class[?]): NoDepsLogger
  def apply(name: String): NoDepsLogger
}
