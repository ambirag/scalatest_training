# Scala Test for UI Automation

# Class 1
1. Date: 27th Sep 2016

# Setup

 - [Install SBT] (http://www.scala-sbt.org/download.html) or
 - browser install sbt
 - [ScalaTest with IntelliJ] (http://www.scalatest.org/user_guide/using_scalatest_with_intellij)

# Importing sbt project into Intellij
- close IntelliJ
- backup whole project folder
- delete .idea folder
- delete target folder
- delete project/target folder
- reopen IntelliJ and import as a sbt project

# Introducing Different UI Automation Frameworks
#### How to choose a framework?

1. [Scala Test Syntax Cheesheets](http://docs.scala-lang.org/cheatsheets/)
2. Specs
3. Tags
4. Template
  * Land on a page
  * Wait for page to load
  * Check for element before interacting
  * Act on element
  * Check if the actiion happened
  * Wait for the expected next event
