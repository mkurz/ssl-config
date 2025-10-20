import com.typesafe.sbt.osgi.SbtOsgi
import com.typesafe.sbt.osgi.SbtOsgi.autoImport._
import com.typesafe.tools.mima.core._

ThisBuild / scalaVersion := Version.scala212
ThisBuild / crossScalaVersions := Seq(Version.scala213, Version.scala212, Version.scala3)

val disablePublishingSettings = Seq(
  // https://github.com/sbt/sbt/pull/3380
  publish / skip := true,
  publishArtifact := false,
  mimaReportBinaryIssues := false
 )

lazy val sslConfigCore = project.in(file("ssl-config-core"))
  .settings(AutomaticModuleName.settings("ssl.config.core"))
  .settings(osgiSettings: _*)
  .settings(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    name := "ssl-config-core",
    mimaReportSignatureProblems := true,
    mimaPreviousArtifacts := (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _)) =>
        Set("0.5.0")
      case _ =>
        Set()
    }).map("com.typesafe" %% "ssl-config-core" % _),
    mimaBinaryIssueFilters ++= Seq(
      // private[sslconfig]
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLLooseConfig.this"),
      // private[sslconfig]
      ProblemFilters.exclude[DirectMissingMethodProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.this"),
      // synthetic on private[sslconfig]
      ProblemFilters.exclude[IncompatibleResultTypeProblem]("com.typesafe.sslconfig.ssl.SSLConfigSettings.<init>$default*")
    ),
    libraryDependencies ++= Dependencies.sslConfigCore,
    libraryDependencies ++= Dependencies.testDependencies,
    OsgiKeys.bundleSymbolicName := s"${organization.value}.sslconfig",
    OsgiKeys.exportPackage := Seq(s"com.typesafe.sslconfig.*;version=${version.value}"),
    OsgiKeys.importPackage := Seq("!sun.misc", "!sun.security.*", configImport(), "*"),
    OsgiKeys.requireCapability := """osgi.ee;filter:="(&(osgi.ee=JavaSE)(version>=1.8))"""",
).enablePlugins(SbtOsgi)

val documentation = project.enablePlugins(ParadoxPlugin, ParadoxSitePlugin).settings(
  name := "SSL Config",
  Paradox / siteSubdirName := "",
  paradoxTheme := Some(builtinParadoxTheme("generic")),
  disablePublishingSettings,
)

lazy val root = project.in(file("."))
  .aggregate(
    sslConfigCore,
    documentation
  )
  .settings(disablePublishingSettings: _*)

def configImport(packageName: String = "com.typesafe.config.*") = versionedImport(packageName, "1.4.2", "1.5.0")
def versionedImport(packageName: String, lower: String, upper: String) = s"""$packageName;version="[$lower,$upper)""""

lazy val checkCodeFormat = taskKey[Unit]("Check that code format is following Scalariform rules")

checkCodeFormat := {
  import scala.sys.process._
  val exitCode = ("git diff --exit-code" !)
  if (exitCode != 0) {
    sys.error(
      """
        |ERROR: Scalariform check failed, see differences above.
        |To fix, format your sources using sbt scalariformFormat test:scalariformFormat before submitting a pull request.
        |Additionally, please squash your commits (eg, use git commit --amend) if you're going to update this pull request.
      """.stripMargin)
  }
}

addCommandAlias("validateCode", ";scalariformFormat;test:scalariformFormat;headerCheck;test:headerCheck;checkCodeFormat")

ThisBuild / githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("validateCode", "test", "doc", "mimaReportBinaryIssues")))

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
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
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}",
    )
  )
)

ThisBuild / githubWorkflowOSes := Seq("ubuntu-latest", "macos-latest", "windows-latest")

ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec.temurin("8"),
  JavaSpec.temurin("11"),
  //JavaSpec.temurin("17"), // can't test currently because until we drop usage of sun.security.x509.*
  //JavaSpec.temurin("21"),
  //JavaSpec.temurin("25"),
)

ThisBuild / githubWorkflowBuildMatrixExclusions += MatrixExclude(Map("java" -> "temurin@8", "os" -> "macos-latest"))
