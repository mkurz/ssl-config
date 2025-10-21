/*
 * Copyright (C) 2015 - 2025 Lightbend Inc. <https://www.lightbend.com>
 */

package com.typesafe.sslconfig.ssl

import java.math.BigInteger
import java.security._
import java.security.cert._
import java.util.Date

import org.joda.time.Instant
import sun.security.x509._

import scala.concurrent.duration.{ FiniteDuration, _ }

/**
 * Used for testing only.  This relies on internal sun.security packages, so cannot be used in OpenJDK.
 */
@deprecated(
  "Uses internal sun.security.x509 classes. " +
  "Works in Java 17 only with the `--add-exports=java.base/sun.security.x509=ALL-UNNAMED` flag. " +
  "Does not work at all anymore with Java 21 and newer. " +
  "To create certificates from code, use alternatives like Bouncy Castle instead.", "0.7.0"
)
object CertificateGenerator {

  // http://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
  // http://www.keylength.com/en/4/

  /**
   * Generates a certificate using RSA (which is available in 1.6).
   */
  @deprecated("Uses internal sun.security.x509 classes. Java 17 requires add-exports flags; Java 21 fails.", "0.7.0")
  def generateRSAWithSHA256(keySize: Int = 2048, from: Instant = Instant.now, duration: FiniteDuration = 365.days): X509Certificate = {
    val dn = "CN=localhost, OU=Unit Testing, O=Mavericks, L=Moon Base 1, ST=Cyberspace, C=CY"
    val to = from.plus(duration.toMillis)

    val keyGen = KeyPairGenerator.getInstance("RSA")
    keyGen.initialize(keySize, new SecureRandom())
    val pair = keyGen.generateKeyPair()
    generateCertificate(dn, pair, from.toDate, to.toDate, "SHA256withRSA")
  }

  @deprecated("Uses internal sun.security.x509 classes. Java 17 requires add-exports flags; Java 21 fails.", "0.7.0")
  def generateRSAWithSHA1(keySize: Int = 2048, from: Instant = Instant.now, duration: FiniteDuration = 365.days): X509Certificate = {
    val dn = "CN=localhost, OU=Unit Testing, O=Mavericks, L=Moon Base 1, ST=Cyberspace, C=CY"
    val to = from.plus(duration.toMillis)

    val keyGen = KeyPairGenerator.getInstance("RSA")
    keyGen.initialize(keySize, new SecureRandom())
    val pair = keyGen.generateKeyPair()
    generateCertificate(dn, pair, from.toDate, to.toDate, "SHA1withRSA")
  }

  @deprecated("Uses internal sun.security.x509 classes. Java 17 requires add-exports flags; Java 21 fails.", "0.7.0")
  def toPEM(certificate: X509Certificate) = {
    val encoder = java.util.Base64.getMimeEncoder()
    val certBegin = "-----BEGIN CERTIFICATE-----\n"
    val certEnd = "-----END CERTIFICATE-----"

    val derCert = certificate.getEncoded()
    val pemCertPre = encoder.encodeToString(derCert)
    val pemCert = certBegin + pemCertPre + certEnd
    pemCert
  }

  @deprecated("Uses internal sun.security.x509 classes. Java 17 requires add-exports flags; Java 21 fails.", "0.7.0")
  def generateRSAWithMD5(keySize: Int = 2048, from: Instant = Instant.now, duration: FiniteDuration = 365.days): X509Certificate = {
    val dn = "CN=localhost, OU=Unit Testing, O=Mavericks, L=Moon Base 1, ST=Cyberspace, C=CY"
    val to = from.plus(duration.toMillis)

    val keyGen = KeyPairGenerator.getInstance("RSA")
    keyGen.initialize(keySize, new SecureRandom())
    val pair = keyGen.generateKeyPair()
    generateCertificate(dn, pair, from.toDate, to.toDate, "MD5WithRSA")
  }

  @deprecated("Uses internal sun.security.x509 classes. Java 17 requires add-exports flags; Java 21 fails.", "0.7.0")
  private[sslconfig] def generateCertificate(dn: String, pair: KeyPair, from: Date, to: Date, algorithm: String): X509Certificate = {

    val info: X509CertInfo = new X509CertInfo
    val interval: CertificateValidity = new CertificateValidity(from, to)
    // I have no idea why 64 bits specifically are used for the certificate serial number.
    val sn: BigInteger = new BigInteger(64, new SecureRandom)
    val owner: X500Name = new X500Name(dn)

    info.set(X509CertInfo.VALIDITY, interval)
    info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn))
    info.set(X509CertInfo.SUBJECT, owner)
    info.set(X509CertInfo.ISSUER, owner)
    info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic))
    info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3))

    info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(AlgorithmId.get(algorithm)))
    var cert: X509CertImpl = new X509CertImpl(info)
    val privkey: PrivateKey = pair.getPrivate
    cert.sign(privkey, algorithm)
    val algos = cert.get(X509CertImpl.SIG_ALG).asInstanceOf[AlgorithmId]
    info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algos)
    cert = new X509CertImpl(info)
    cert.sign(privkey, algorithm)
    cert
  }
}
