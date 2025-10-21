/*
 * Copyright (C) 2015 - 2025 Lightbend Inc. <https://www.lightbend.com>
 */

package com.typesafe.sslconfig.ssl

object Protocols {

  /**
   * Protocols which are known to be insecure.
   */
  val deprecatedProtocols = Set("TLSv1.1", "TLSv1", "SSL", "SSLv2Hello", "SSLv3")

  val recommendedProtocols = Array("TLSv1.3", "TLSv1.2")

  // Use 1.3 as a default
  def recommendedProtocol = "TLSv1.3"

}
