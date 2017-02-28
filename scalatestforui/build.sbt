name := "scalatestforui"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "internal-repository" at "http://nexus.sb.karmalab.net/nexus/content/groups/public/"

libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.53.0" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies += "com.expedia.www.commons" % "commons-test" % "latest.integration" % "test"
libraryDependencies += "com.expedia.www.commons" % "commons-ui-test" % "latest.integration" % "test"
libraryDependencies += "com.expedia.www.commons" % "commons-ui-expweb" % "latest.integration" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test->*" excludeAll (
ExclusionRule(organization="org.junit", name="junit")
)

initialize~= { _ =>
  System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver")
  System.setProperty("browser", "chrome")
}
parallelExecution in ThisBuild := true
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")