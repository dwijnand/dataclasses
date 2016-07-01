import scala.language.implicitConversions

import scala.collection.immutable

import sbt._
import sbt.Keys._

sealed class Licence(val name: String, val url: URL)

final case object Apache2 extends Licence("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))

object MiscPlugin extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements

  object autoImport {
    val licences = settingKey[Seq[Licence]]("Project (typed) licences, UK spelling")
    val scala211 = settingKey[String]("")

    def scalaPartV = scalaVersion(CrossVersion.partialVersion)

    val noDocs    = Def.settings(sources in (Compile, doc) := Nil, publishArtifact in (Compile, packageDoc) := false)
    val noPackage = Def.settings(Keys.`package` := file(""), packageBin := file(""), packagedArtifacts := Map())
    val noPublish = Def.settings(
      publishArtifact := false,
      publish         := {},
      publishLocal    := {},
      publishM2       := {},
      publishTo       := Some(Resolver.file("devnull", file("/dev/null")))
    )
    val noArtifacts = Def.settings(noPackage, noPublish)

    // Remove with sbt 0.13.12+
    implicit def appendOption[T]: Append.Sequence[Seq[T], Option[T], Option[T]] =
      new Append.Sequence[Seq[T], Option[T], Option[T]] {
        def appendValue(a: Seq[T], b: Option[T]): Seq[T] = b.fold(a)(a :+ _)
        def appendValues(a: Seq[T], b: Option[T]): Seq[T] = b.fold(a)(a :+ _)
      }

    implicit def removeOption[T]: Remove.Value[Seq[T], Option[T]] with Remove.Values[Seq[T], Option[T]] =
      new Remove.Value[Seq[T], Option[T]] with Remove.Values[Seq[T], Option[T]] {
        def removeValue(a: Seq[T], b: Option[T]): Seq[T] = b.fold(a)(a filterNot _.==)
        def removeValues(a: Seq[T], b: Option[T]): Seq[T] = b.fold(a)(a filterNot _.==)
      }

    def wordSeq(s: String): immutable.Seq[String] = (s split "\\s+" filterNot (_ == "")).to[immutable.Seq]

    implicit def appendWords: Append.Values[Seq[String], String] = new Append.Values[Seq[String], String] {
      def appendValues(a: Seq[String], b: String): Seq[String] = a ++ wordSeq(b)
    }

    implicit def removeWords: Remove.Values[Seq[String], String] = new Remove.Values[Seq[String], String] {
      def removeValues(a: Seq[String], b: String): Seq[String] = a filterNot wordSeq(b).contains
    }

    implicit final class AnyWithForScalaVersion[A](val _o: A) {
      def ifScala(p: Int => Boolean) = scalaPartV(_ collect { case (2, y) if p(y) => _o })
      def ifScalaLte(v: Int) = ifScala(_ <= v)
      def ifScalaMag(v: Int) = ifScala(_ == v)
      def ifScalaGte(v: Int) = ifScala(_ >= v)
      def for212Plus(alt: => A) = ifScalaLte(11)(_ getOrElse alt)
    }

    private val crossVersionFull = CrossVersion.full // workaround sbt/librarymanagement#48
    implicit final class ModuleIDWithCompilerPlugin(val _m: ModuleID) extends AnyVal {
      def compilerPlugin(): ModuleID = sbt.compilerPlugin(_m cross crossVersionFull)
    }
  }
  import autoImport._

  override def buildSettings = Seq(
    watchSources ++= (baseDirectory.value * "*.sbt").get,
    watchSources ++= (baseDirectory.value / "project" * "*.scala").get
  )

  override def globalSettings = Seq(
    licenses := Nil,
    Def derive (licenses := licences.value map (l => l.name -> l.url)),
    Def derive (homepage := scmInfo.value map (_.browseUrl)),
    Def derive (pomExtra := pomExtra.value ++ {
      <developers>
        { developers.value.map { developer =>
          <developer>
            <id>{ developer.id }</id>
            <name>{ developer.name }</name>
            <email>{ developer.email }</email>
            <url>{ developer.url }</url>
          </developer>
        }}
      </developers>
    })
  )
}
