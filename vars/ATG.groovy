#!/usr/bin/groovy
/**
  * Function: Read configurations from Jenkins file and implements the DSL elements and hence you can extend the pipeline.
  * Author  : Amit Patil, Saurabh Singhal and Neeraj Sharama
  * Date    : 21 Apr'2017
  * Params  : 
  **/
  
import main.com.rocc.stages.impl.*
String WORKSPACE = pwd()

def call(body) {
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
  try {
    timestamps {
      stage('\u2776 Initialize') {
        try {
          def wspace     = "${WORKSPACE}"
          def JAVA_HOME  = "/app/ci/jdk/jdk1.7.0_80"
          def MAVEN_HOME = "/app/ci/apache_maven/apache-maven-3.2.5"
          def ANT_HOME   = "/app/ci/ant/apache-ant-1.9.6"
          env.JAVA_HOME  = "${JAVA_HOME}"
          env.MAVEN_HOME = "${MAVEN_HOME}"
          env.ANT_HOME   = "${ANT_HOME}"
          env.wspace     = "${wspace}"
          env.PATH       = "${env.JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.ANT_HOME}:${env.PATH}"
          echo "\u001B[35m JAVA_HOME  => ${env.JAVA_HOME}"
          echo "\u001B[35m MAVEN_HOME => ${env.MAVEN_HOME}"
          echo "\u001B[35m WSPACE     => ${wspace}"
          echo "\u001B[35m PATH       => ${env.ANT_HOME}"
        }
        catch (Exception groovyEx) {
          echo groovyEx.getMessage()
          echo groovyEx.getCause()
          throw groovyEx
        }
      }
      stage('\u2777 GetLatest') {
        if ("${config.EXEC_GITCHECKOUT}" == "true" ) {
          def co_obj = new GITCheckout()
          co_obj.checkout( "${WORKSPACE}", "${config.deployBranch}")
        } else {
          println "Checkout skipped"  
        }
      }
      stage('\u2778 DownloadLib') {
        def dl_obj = new ATGAntBuild()
        dl_obj.copy( "${WORKSPACE}")
      }
      stage('\u2779 Compile Code') {
        println "Compliation is turned "+"${config.EXEC_COMPILE}"
        if ("${config.EXEC_COMPILE}" == "true" ) {
          def dl_obj = new ATGAntBuild()
          dl_obj.execAntBuild( "${WORKSPACE}")
        } else {
          println "Compilation skipped"  
        }
      }
      stage('\u277A UnitTesting') {
        if ("${config.EXEC_UNIT_TESTS}" == "true" ) {
          def ut_obj = new UnitTesting()
          ut_obj.execUnitTestCases( "${WORKSPACE}")
          ut_obj.publishReport( "${WORKSPACE}")
        } else {
          println "Unit Testing skipped"  
        }
      }
      stage('\u277B Sonar Analysis') {
        if ("${config.EXEC_SONAR}" == "true" ) {
          def sonar_obj = new SonarAnalysis()
          sonar_obj.execSonarAnalysis("${WORKSPACE}")
        } else {
          println "Sonar Analysis skipped"  
        }
      }
      stage('\u277C Check Quality Gates') {
        if ("${config.EXEC_GITCHECKOUT}" == "true" ) {
          echo "Quality Gates Passed coming soon"
        }
      }
      stage('\u277D Publish Artifacts') {
        if ("${config.EXEC_GITCHECKOUT}" == "true" ) {
          echo "Publish Artifacts coming soon"
        }
      }
    }
  }
  catch (Exception groovyEx) {
    echo groovyEx.getMessage()
    echo groovyEx.getCause()
    throw groovyEx
  }
}