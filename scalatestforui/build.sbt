name := "scalatestforui"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "3.0.0-beta3" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test->*" excludeAll (
ExclusionRule(organization="org.junit", name="junit")
)
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")