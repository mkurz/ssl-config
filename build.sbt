import com.github.sbt.osgi.SbtOsgi
import com.github.sbt.osgi.SbtOsgi.autoImport._
import com.typesafe.tools.mima.core._
import Common.autoImport._

ThisBuild / scalaVersion       := Version.scala212
ThisBuild / crossScalaVersions := Seq(Version.scala213, Version.scala212, Version.scala3)

val disablePublishingSettings = Seq(
  publish / skip         := true,
  mimaReportBinaryIssues := false
)

lazy val sslConfigCore = project
  .in(file("ssl-config-core"))
  .settings(AutomaticModuleName.settings("ssl.config.core"))
  .settings(osgiSettings: _*)
  .settings(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    name                        := "ssl-config-core",
    mimaReportSignatureProblems := true,
    mimaPreviousArtifacts       := Set("com.typesafe" %% "ssl-config-core" % "0.6.1"),
    mimaBinaryIssueFilters ++= Seq(
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigParser.parseSSLParameters"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.disabledKeyAlgorithms"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.hostnameVerifierClass"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.sslParametersConfig"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.withHostnameVerifierClass"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.withSslParametersConfig"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.certpath"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.defaultctx"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.handshake"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.keygen"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.ocsp"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.pluggability"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.record"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.session"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.sessioncache"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withCertPath"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withDefaultContext"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withHandshake"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withKeygen"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withOcsp"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withPluggability"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withRecord"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withSession"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.withSessioncache"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.allowWeakCiphers"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.allowWeakProtocols"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.disableHostnameVerification"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.disableSNI"),
      ProblemFilters.exclude[DirectMissingMethodProblem](
        "com.typesafe.sslconfig.ssl.SSLLooseConfig.withDisableHostnameVerification"
      ),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.withDisableSNI"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.AlgorithmChecker"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.ClientAuth"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.ClientAuth$"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.ClientAuth$Default$"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.ClientAuth$Need$"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.ClientAuth$None$"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.ClientAuth$Want$"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.debug.ClassFinder"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.debug.DebugConfiguration"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.debug.FixCertpathDebugLogging"),
      ProblemFilters.exclude[MissingClassProblem](
        "com.typesafe.sslconfig.ssl.debug.FixCertpathDebugLogging$MonkeyPatchSunSecurityUtilDebugAction"
      ),
      ProblemFilters.exclude[MissingClassProblem](
        "com.typesafe.sslconfig.ssl.debug.FixCertpathDebugLogging$SunSecurityUtilDebugLogger"
      ),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.debug.FixInternalDebugLogging"),
      ProblemFilters.exclude[MissingClassProblem](
        "com.typesafe.sslconfig.ssl.debug.FixInternalDebugLogging$MonkeyPatchInternalSslDebugAction"
      ),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.debug.FixLoggingAction"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.DefaultHostnameVerifier"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.DisabledComplainingHostnameVerifier"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.JavaSecurityDebugBuilder"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.JavaxNetDebugBuilder"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.MonkeyPatcher"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.NoopHostnameVerifier"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.SSLDebugHandshakeOptions"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.SSLDebugHandshakeOptions$"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.SSLDebugRecordOptions"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.SSLDebugRecordOptions$"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.SSLParametersConfig"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.SSLParametersConfig$"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default$12"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default$13"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default$14"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$10"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$11"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$12"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$13"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$14"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$6"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$7"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$8"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$9"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.<init>$default$4"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.<init>$default$5"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.<init>$default$6"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.<init>$default$7"),
      ProblemFilters
        .exclude[IncompatibleResultTypeProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default$10"),
      ProblemFilters
        .exclude[IncompatibleResultTypeProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default$11"),
      ProblemFilters
        .exclude[IncompatibleResultTypeProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default$7"),
      ProblemFilters
        .exclude[IncompatibleResultTypeProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default$8"),
      ProblemFilters
        .exclude[IncompatibleResultTypeProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default$9"),
      ProblemFilters
        .exclude[IncompatibleResultTypeProblem]("com.typesafe.sslconfig.ssl.SSLDebugConfig.<init>$default$4"),
      ProblemFilters.exclude[MissingClassProblem]("com.typesafe.sslconfig.ssl.AlgorithmConstraint"),
    ),
    libraryDependencies ++= Dependencies.sslConfigCore,
    libraryDependencies ++= Dependencies.testDependencies,
    OsgiKeys.bundleSymbolicName := s"${organization.value}.sslconfig",
    OsgiKeys.exportPackage      := Seq(s"com.typesafe.sslconfig.*;version=${version.value}"),
    OsgiKeys.importPackage      := Seq("!sun.misc", "!sun.security.*", configImport(), "*"),
    OsgiKeys.requireCapability  := """osgi.ee;filter:="(&(osgi.ee=JavaSE)(version>=1.8))"""",
  )
  .enablePlugins(SbtOsgi)

val documentation = project
  .enablePlugins(ParadoxPlugin, ParadoxSitePlugin)
  .settings(
    name                     := "SSL Config",
    Paradox / siteSubdirName := "",
    paradoxTheme             := Some(builtinParadoxTheme("generic")),
    disablePublishingSettings,
  )

lazy val root = project
  .in(file("."))
  .aggregate(
    sslConfigCore,
    documentation
  )
  .settings(disablePublishingSettings: _*)

def configImport(packageName: String = "com.typesafe.config.*")        = versionedImport(packageName, "1.4.2", "1.5.0")
def versionedImport(packageName: String, lower: String, upper: String) = s"""$packageName;version="[$lower,$upper)""""

addCommandAlias("validateCode", "headerCheckAll ; scalafmtSbtCheck ; scalafmtCheckAll")

ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(
    List("validateCode", "test", "doc", "mimaReportBinaryIssues"),
    cond = Some(s"github.repository == '${githubOrg}/${githubRepo}'")
  ),
  WorkflowStep.Run(
    List("./scripts/validate-docs.sh"),
    cond = Some(s"matrix.java != 'temurin@8' && github.repository == '${githubOrg}/${githubRepo}'")
  ),
)

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishCond           := Some(s"github.repository == '${githubOrg}/${githubRepo}'")
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(
    RefPredicate.StartsWith(Ref.Tag("v")),
    RefPredicate.Equals(Ref.Branch("main"))
  )
ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish project"),
    env = Map(
      "PGP_PASSPHRASE"    -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET"        -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}",
    )
  )
)

ThisBuild / githubWorkflowOSes := Seq("ubuntu-latest", "macos-latest", "windows-latest")

ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec.temurin("8"),
  JavaSpec.temurin("11"),
  // JavaSpec.temurin("17"), // can't test currently because until we drop usage of sun.security.x509.*
  // JavaSpec.temurin("21"),
  // JavaSpec.temurin("25"),
)

ThisBuild / githubWorkflowBuildMatrixExclusions += MatrixExclude(Map("java" -> "temurin@8", "os" -> "macos-latest"))
