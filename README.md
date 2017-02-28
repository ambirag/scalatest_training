# Scala Test for UI Automation

# Class 1
1. Date: 27th Sep 2016

# Setup

 - [Install SBT] (http://www.scala-sbt.org/download.html) or
 - browser install sbt
 - [ScalaTest with IntelliJ] (http://www.scalatest.org/user_guide/using_scalatest_with_intellij)
 - git clone https://ewegithub.sb.karmalab.net/rambighananthan/scalatest_training.git
 - sbt clean compile
 - sbt test (you should see a browser open and run a test)
 - running one particular test sbt test-only helloworld.spec.Lesson5 -n l5 (this is the tag name)

# Importing sbt project into Intellij
- close IntelliJ
- backup whole project folder
- delete .idea folder
- delete target folder
- delete project/target folder
- reopen IntelliJ and import as a sbt project

# Class I - Jan 31st 2017 - Tuesday
## Introducing Different UI Automation Frameworks
1. How to choose a framework?
2. Scala CheetSheet
* [Scala Syntax Cheesheets](http://docs.scala-lang.org/cheatsheets/)
* [Scala Test Syntax Cheesheets](http://www.scalatest.org/at_a_glance/FlatSpec)
3. Specs

# Class II - Feb 7th 2017 - Tuesday
1. Tags
2. Querying other elements (Checkbox, Texifield and findElement)
3. Before / After hooks
4. Quit vs Close
5. Ignore scenario / Ignore feature
6. Regular asserts vs CheckPoints with AssertResults
7. Assume
8. implicit vs Explicit keyword

# Class III - Feb 21st 2017 - Tuesday
1. Timeouts / Waiting / Implicit and Explicit waiting
2. Page Object Model
3. Fixtures / withFixture
4. Handling transient(interstitial, typeahead) elements
5. Reliable Test 
6. Parallel / Sequence Execution
 
# Class IV - Oct 18th 2016 - Tuesday
1. EWE ScalaTest Library and how to use?
  - http://ewegithub.sb.karmalab.net/EWE/commons-test
  - http://ewegithub.sb.karmalab.net/EWE/commons-ui-test
  - http://ewegithub.sb.karmalab.net/EWE/commons-ui-expweb
2. Look at MultiItemCKO example trunk/checkout.ui/acceptanceTest/com/expedia/checkout/micko/mickofh
3. Creating neat APIs using Implicit / Currying
4. Creating automaiton using Scala Worksheet
3. How to set AB Buckets
2. How to contribute to EWE ScalaTest library (Working Group meeting : Every Thursday 5pm GMT(9AM PST / 10:30PM IST)
  

# Class V - Nov 1 2016 - Tuesday
1. Discussing classic UI automaiton issues
  - CKO Login module
  - Interstitial
  - Urgency Messages
  - Scratchpad popup
2. Understand Waiting in UI automation
3. 
3. Automation Best Practice Template
  - Land on a page
  - Verify landed page is completely loaded
  - use isErrorPage to check if page has errors 
  - Verify if it is the expected page
  - Get the POM of the element to be interacted
    - Check if it is null, poll until its available
    - Interact with the element and assert or act
  - Log every step of automation 
4. Handling SSO login

# Class VI - Nov 7th 2016 - Monday
  - Scala.js https://www.scala-js.org/ - https://www.scala-js.org/tutorial/basic/
  -- `Scala.js is a compiler that compiles Scala source code to equivalent Javascript code. That lets you write Scala code that you can run in a web browser, or other environments (Chrome plugins, Node.js, etc.) where Javascript is supported.`
  - Tables to test scenarios with multiple inputs (POS, different inputs,etc)
  - Guillaume to guest lecture
