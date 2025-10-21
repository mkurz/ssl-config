/*
 * Copyright (C) 2015 - 2025 Lightbend Inc. <https://www.lightbend.com>
 */

package com.typesafe.sslconfig

private[sslconfig] object Compat {
  type Once[+A] = TraversableOnce[A]

  implicit final class OnceOps[A](private val xs: TraversableOnce[A]) extends AnyVal {
    def iterator: Iterator[A] = xs.toIterator
    def foreach(f: A => Unit): Unit = xs.foreach(f)
  }

  implicit final class MapOps[K, V](private val m: Map[K, V]) extends AnyVal {
    def mapValuesView[R](f: V => R) = m.mapValues(f)
  }
}
