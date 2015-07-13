import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtGit._

object GitVersion {
  lazy val settings: Seq[Def.Setting[_]] = Seq(
    version in ThisBuild := {
      val snapshotVersion = """v?([0-9\.]+)-(\d+)-([0-9a-z]+)""".r
      val releaseVersion = """v?([0-9\.]+)""".r
      val isPullRequest = sys.env.getOrElse("TRAVIS_PULL_REQUEST", "false") != "false"
      git.gitDescribedVersion.value getOrElse "0.1-SNAPSHOT" match {
        case v if (isPullRequest) => s"0.0.0-PULLREQUEST"
        case snapshotVersion(v, n, h) => s"${v}-${"%02d".format(n.toInt)}-${h}-SNAPSHOT"
        case releaseVersion(v) => v
        case v => v
      }
    }
  )
}
