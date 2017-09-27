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
          def MAVEN_HOME = "${config.MAVEN_HOME}"
          def ANT_HOME   = "${config.ANT_HOME}"
          env.MAVEN_HOME = "${MAVEN_HOME}"
          env.ANT_HOME   = "${ANT_HOME}"
          env.wspace     = "${wspace}"
          env.PATH       = "${MAVEN_HOME}/bin:${env.ANT_HOME}:${env.PATH}"
		  def buildNumber = "${env.BUILD_NUMBER}"
          echo "\u001B[35m MAVEN_HOME => ${env.MAVEN_HOME}"
          echo "\u001B[35m WSPACE     => ${wspace}"
          echo "\u001B[35m PATH       => ${env.ANT_HOME}"
		  echo "\u001B[35m BUILD_NUMBER =>${buildNumber}"
		}
        catch (Exception groovyEx) {
          echo groovyEx.getMessage()
          echo groovyEx.getCause()
          throw groovyEx
        }
      }
      stage('\u2777 GetLatest') {
        def co_obj = new GITCheckout()
        co_obj.gitCheckout( "${WORKSPACE}")
        def dl_obj = new ATGAntBuild()
        dl_obj.copy( "${WORKSPACE}")
      }
      stage('\u2778 Compile Code') {
        def dl_obj = new ATGAntBuild()
        dl_obj.execAntBuild( "${WORKSPACE}", "${config.ANT_HOME}")
      }
      stage('\u2779 UnitTesting') {
        if ("${config.EXEC_UNIT_TESTS}" == "true" ) {
          def ut_obj = new UnitTesting()
          ut_obj.execUnitTestCases( "${WORKSPACE}", "${config.ANT_HOME}")
          ut_obj.publishReport( "${WORKSPACE}")
        } else {
          println "Unit Testing skipped"  
        }
      }
      stage('\u277A Sonar Analysis') {
        if ("${config.EXEC_SONAR}" == "true" ) {
          def sonar_obj = new SonarAnalysis()
          sonar_obj.execSonarAnalysis("${WORKSPACE}", "${config.ANT_HOME}", "${config.JAVA_HOME}")
        } else {
          println "Sonar Analysis skipped"  
        }
      }
      stage('\u277B Assembling') {
        if ("${config.EXEC_DEPLOYMENT}" == "true" ) {
          script {
            echo "Start Packaging"
            parallel(
              phase1: {
                def dl_obj = new ATGAntBuild()
                dl_obj.AssembleEAR("prod", "${config.CURRENT_Env}", "${WORKSPACE}", "${config.EXEC_FUNC_TESTS}", "${config.deployBranch}", "${config.ANT_HOME}")
                echo "Deploying EAR on nexus repository"
                dl_obj.NexusDeploy("${config.MAVEN_HOME}", "${WORKSPACE}", "prod" ,"${env.BUILD_NUMBER}" )
              },
              phase2: {
                def dl_obj1 = new ATGAntBuild()
                //dl_obj1.AssembleEAR("pub", "${config.CURRENT_Env}", "${WORKSPACE}", "${config.EXEC_FUNC_TESTS}", "${config.deployBranch}", "${config.ANT_HOME}")
                //dl_obj1.NexusDeploy("${config.MAVEN_HOME}", "${WORKSPACE}", "pub" ,"${env.BUILD_NUMBER}" )
              },
              phase3: {
                def dl_obj2 = new ATGAntBuild()
                //dl_obj2.AssembleEAR("stg", "${config.CURRENT_Env}", "${WORKSPACE}", "${config.EXEC_FUNC_TESTS}", "${config.deployBranch}", "${config.ANT_HOME}")
                //dl_obj2.NexusDeploy("${config.MAVEN_HOME}", "${WORKSPACE}", "stg" ,"${env.BUILD_NUMBER}")
              }
            )
          }
        }
      }
      stage('\u277C Deployment') {
        if ("${config.EXEC_DEPLOYMENT}" == "true" ) {
          def dl_obj1 = new ATGAntBuild()
          dl_obj1.deployment( "${WORKSPACE}")
        }
      }
      stage('\u277D Integration Testing') {
        if ("${config.EXEC_DEPLOYMENT}" == "true" ) {
          if ("${config.RUN_FUNC_TESTS}" == "true" ) {
            build job: 'selenium_web'
          } else {
            println "Please run deployment before Functional Testing."
            println "Function Testing Skipped"
          }
        }
      }
      stage('\u277E Zap Security Testing') {
        if ("${config.EXEC_DEPLOYMENT}" == "true" ) {
          if ("${config.EXEC_ZAP}" == "true" ) {
            def dl_obj = new ATGAntBuild()
            dl_obj.ZapSecurity( "${WORKSPACE}", "${config.ZAP_HOME}")
          } else {
            println "Please run deployment before Security Testing."
            println "Security Testing Skipped"
          }
        }
      }
	  stage('\u2776 \u0030 Jmeter Testing') 
	  { 
			jmeter_workdir = ${WORKSPACE}+"@libs/DevOps/resources/config/username.properties"
			Properties props = new Properties()
			File propsFile = new File("$jmeter_workdir")
			props.load(propsFile.newDataInputStream())
			def jmeter_wspace_path = props.getProperty('jmeter_wspace_path')
			if ("${config.EXEC_JMET}" == "true" )
			{
				node ("Perf-Simens-Jmeter")
				{
			
			 
				def dl_obj = new JmeterAnalysis()
				dl_obj.execJemeterAnalysis("${jmeter_wspace_path}") 
				
				}
			}
			else
				{
					println "Please run deployment before Jmeter Testing."
					println "Jmeter Testing Skipped"
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