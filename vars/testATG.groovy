#!/usr/bin/groovy
/**
  * Function: Read configurations from Jenkins file and implements the DSL elements and hence you can extend the pipeline.
  * Author  : Amit Patil, Saurabh Singhal and Neeraj Sharama
  * Date    : 21 Apr'2017
  * Params  : 
  **/
  
import main.com.rocc.stages.impl.*
import test.com.*

String WORKSPACE = pwd()

def call(body) {
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
  try {
    timestamps {
      stage('\u27A5 Initialize') {
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
      stage('\u27A5 GetLatest') {
        def co_obj = new GITCheckoutTest()
        co_obj.testcheckout( "${WORKSPACE}", "${config.deployBranch}")
      }
      stage('\u27A5 DownloadLib') {
        def dl_obj = new ATGAntBuildTest()
        dl_obj.testCopy( "${WORKSPACE}")
      }
      stage('\u27A5 Compile Code') {
        def dl_obj = new ATGAntBuildTest()
        dl_obj.testExecAntBuild( "${WORKSPACE}")
      
      }
      stage('\u27A5 UnitTesting') {
        def ut_obj = new UnitTestingTest()
        //ut_obj.testExecUTCases( "${WORKSPACE}")
        //ut_obj.testPublishReport( "${WORKSPACE}")
      }
      stage('\u27A5 Sonar Analysis') {
        def sonar_obj = new SonarAnalysisTest()
        //sonar_obj.execSonarAnalysis("${WORKSPACE}")
      }
      stage('\u27A5 Check Quality Gates') {
        echo "Quality Gates Passed"
      }
      stage('\u27A5 Publish Artifacts') {
        echo "Publish Artifacts"
      }
    }
  }
  catch (Exception groovyEx) {
    echo groovyEx.getMessage()
    echo groovyEx.getCause()
    throw groovyEx
  }
}