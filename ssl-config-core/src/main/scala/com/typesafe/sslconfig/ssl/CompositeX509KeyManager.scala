/*
 * Copyright (C) 2015 - 2025 Lightbend Inc. <https://www.lightbend.com>
 */

package com.typesafe.sslconfig.ssl

import javax.net.ssl.{ SSLEngine, X509ExtendedKeyManager, X509KeyManager }
import java.security.{ Principal, PrivateKey }
import java.security.cert.{ CertificateException, X509Certificate }
import java.net.Socket

import com.typesafe.sslconfig.util.LoggerFactory

import scala.collection.mutable.ArrayBuffer

/**
 * A keymanager that wraps other X509 key managers.
 */
class CompositeX509KeyManager(mkLogger: LoggerFactory, keyManagers: Seq[X509KeyManager]) extends X509ExtendedKeyManager {
  // Must specify X509ExtendedKeyManager: otherwise you get
  // "X509KeyManager passed to SSLContext.init():  need an X509ExtendedKeyManager for SSLEngine use"

  private val logger = mkLogger(getClass)

  logger.debug(s"CompositeX509KeyManager start: keyManagers = $keyManagers")

  // You would think that from the method signature that you could use multiple key managers and trust managers
  // by passing them as an array in the init method.  However, the fine print explicitly says:
  // "Only the first instance of a particular key and/or trust manager implementation type in the array is used.
  // (For example, only the first javax.net.ssl.X509KeyManager in the array will be used.)"
  //
  // This doesn't mean you can't have multiple key managers, but that you can't have multiple key managers of
  // the same class, i.e. you can't have two X509KeyManagers in the array.

  def getClientAliases(keyType: String, issuers: Array[Principal]): Array[String] = {
    logger.debug(s"getClientAliases: keyType = $keyType, issuers = ${issuersToString(issuers)}")

    val clientAliases = ArrayBuffer[String]()
    withKeyManagers { keyManager =>
      val aliases = keyManager.getClientAliases(keyType, issuers)
      if (aliases != null) {
        clientAliases ++= aliases
      }
    }
    logger.debug(s"getCertificateChain: clientAliases = $clientAliases")

    nullIfEmpty(clientAliases.toArray)
  }

  def chooseClientAlias(keyType: Array[String], issuers: Array[Principal], socket: Socket): String = {
    logger.debug(s"chooseClientAlias: keyType = ${keyType.toSeq}, issuers = ${issuersToString(issuers)}, socket = $socket")

    keyManagers.iterator.find { keyManager =>
      try {
        val clientAlias = keyManager.chooseClientAlias(keyType, issuers, socket)
        if (clientAlias != null) {
          logger.debug(s"chooseClientAlias: using clientAlias $clientAlias with keyManager $keyManager")
          true
        } else false
      } catch {
        case _: CertificateException => false
      }
    }.flatMap(keyManager => Option(keyManager.chooseClientAlias(keyType, issuers, socket))).orNull
  }

  override def chooseEngineClientAlias(keyType: Array[String], issuers: Array[Principal], engine: SSLEngine): String = {
    logger.debug(s"chooseEngineClientAlias: keyType = ${keyType.toSeq}, issuers = ${issuersToString(issuers)}, engine = $engine")

    keyManagers.iterator.find { (keyManager: X509KeyManager) =>
      try {
        keyManager match {
          case extendedKeyManager: X509ExtendedKeyManager =>
            val clientAlias = extendedKeyManager.chooseEngineClientAlias(keyType, issuers, engine)
            if (clientAlias != null) {
              logger.debug(s"chooseEngineClientAlias: using clientAlias $clientAlias with keyManager $extendedKeyManager")
              true
            } else false
          case _ => false // do nothing
        }
      } catch {
        case _: CertificateException => false
      }
    }.flatMap(keyManager => Option(keyManager.asInstanceOf[X509ExtendedKeyManager].chooseEngineClientAlias(keyType, issuers, engine))).orNull
  }

  override def chooseEngineServerAlias(keyType: String, issuers: Array[Principal], engine: SSLEngine): String = {
    logger.debug(s"chooseEngineServerAlias: keyType = ${keyType.toSeq}, issuers = ${issuersToString(issuers)}, engine = $engine")

    keyManagers.iterator.find { (keyManager: X509KeyManager) =>
      try {
        keyManager match {
          case extendedKeyManager: X509ExtendedKeyManager =>
            val clientAlias = extendedKeyManager.chooseEngineServerAlias(keyType, issuers, engine)
            if (clientAlias != null) {
              logger.debug(s"chooseEngineServerAlias: using clientAlias $clientAlias with keyManager $extendedKeyManager")
              true
            } else false
          case _ => false // do nothing
        }
      } catch {
        case _: CertificateException => false
      }
    }.flatMap(keyManager => Option(keyManager.asInstanceOf[X509ExtendedKeyManager].chooseEngineServerAlias(keyType, issuers, engine))).orNull
  }

  def getServerAliases(keyType: String, issuers: Array[Principal]): Array[String] = {
    logger.debug(s"getServerAliases: keyType = $keyType, issuers = ${issuersToString(issuers)}")

    val serverAliases = ArrayBuffer[String]()
    withKeyManagers { keyManager =>
      val aliases = keyManager.getServerAliases(keyType, issuers)
      if (aliases != null) {
        serverAliases ++= aliases
      }
    }
    logger.debug(s"getServerAliases: serverAliases = $serverAliases")

    nullIfEmpty(serverAliases.toArray)
  }

  def chooseServerAlias(keyType: String, issuers: Array[Principal], socket: Socket): String = {
    logger.debug(s"chooseServerAlias: keyType = $keyType, issuers = ${issuersToString(issuers)}, socket = $socket")
    keyManagers.iterator.find { keyManager =>
      try {
        val serverAlias = keyManager.chooseServerAlias(keyType, issuers, socket)
        if (serverAlias != null) {
          logger.debug(s"chooseServerAlias: using serverAlias $serverAlias with keyManager $keyManager")
          true
        } else false
      } catch {
        case _: CertificateException => false
      }
    }.flatMap(keyManager => Option(keyManager.chooseServerAlias(keyType, issuers, socket))).orNull
  }

  def getCertificateChain(alias: String): Array[X509Certificate] = {
    logger.debug(s"getCertificateChain: alias = $alias")
    keyManagers.iterator.find { keyManager =>
      try {
        val chain = keyManager.getCertificateChain(alias)
        if (chain != null && chain.length > 0) {
          logger.debug(s"getCertificateChain: chain ${debugChain(chain)} with keyManager $keyManager")
          true
        } else false
      } catch {
        case _: CertificateException => false
      }
    }.flatMap(keyManager => Option(keyManager.getCertificateChain(alias))).orNull
  }

  def getPrivateKey(alias: String): PrivateKey = {
    logger.debug(s"getPrivateKey: alias = $alias")
    keyManagers.iterator.find { keyManager =>
      try {
        val privateKey = keyManager.getPrivateKey(alias)
        if (privateKey != null) {
          logger.debug(s"getPrivateKey: privateKey $privateKey with keyManager $keyManager")
          true
        } else false
      } catch {
        case _: CertificateException => false
      }
    }.flatMap(keyManager => Option(keyManager.getPrivateKey(alias))).orNull
  }

  private def withKeyManagers[T](block: (X509KeyManager => T)): Seq[CertificateException] = {
    val exceptionList = ArrayBuffer[CertificateException]()
    keyManagers.foreach { keyManager =>
      try {
        block(keyManager)
      } catch {
        case certEx: CertificateException =>
          exceptionList.append(certEx)
      }
    }
    exceptionList.toSeq
  }

  private def nullIfEmpty[T](array: Array[T]) = if (array.size == 0) null else array

  private def issuersToString(issuers: Array[Principal]) =
    if (issuers != null) issuers.mkString("[", ", ", "]") else null

  override def toString = {
    s"CompositeX509KeyManager(keyManagers = [$keyManagers])"
  }
}
