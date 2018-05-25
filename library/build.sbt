name := "cilib-library"

description := "A library of composable components enabling simpler Computational Intelligence"

parallelExecution in Test := false

libraryDependencies ++= Seq(
    "com.google.guava" % "guava" % "13.0.1",
    "org.parboiled" % "parboiled-core" % "1.1.4",
    "org.parboiled" % "parboiled-java" % "1.1.4",
    "org.functionaljava" % "functionaljava" % "3.1",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
    "com.novocode" % "junit-interface" % "0.10" % "test",
    "org.apache.commons" % "commons-math3" % "3.3",
    "nz.ac.waikato.cms.weka" % "weka-stable" % "3.8.2",
    "gov.nist.math" % "jama" % "1.0.3"
)

crossPaths := false

//autoScalaLibrary := false // Prevent the scala-library automatically being added to the pom

