/*
 * Copyright (C) 2015 - 2025 Lightbend Inc. <https://www.lightbend.com>
 */

package com.typesafe.sslconfig

private[sslconfig] object Compat {
  type Once[+A] = IterableOnce[A]

  implicit final class MapOps[K, V](private val m: Map[K, V]) extends AnyVal {
    def mapValuesView[R](f: V => R) = m.view.mapValues(f)
  }

  val CollectionConverters = scala.jdk.CollectionConverters
}
