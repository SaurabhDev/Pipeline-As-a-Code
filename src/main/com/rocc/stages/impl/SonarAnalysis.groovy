#!groovy
/**
 * Jenkins file for ROCC Pipeline
 * Author: nsharma43@sapient.com,ssi257
 */

/**
 * Import the package for using the class functions
 */
package main.com.rocc.stages.impl
import main.com.rocc.stages.impl.ATGAntBuild

/**
 * Function: File used for sonar analysis
 * @Params: workspace,pom and branch name mandatory
 */
void executeSonar( def wspace) {
	try {
		def maven_root_pom = "/app/ci/jenkins_home/workspace/test2/WebCommerce/rocc/modules/commerce/core"
			   sh "export JAVA_HOME=/app/ci/jdk/jdk1.8.0_131; cd /app/ci/jenkins_home/workspace/test2/WebCommerce/rocc/modules/commerce/core;mvn sonar:sonar"
		}
		 catch (Exception error) {
			   println " failed to run sonar analysis using ..."
			   throw error
			 }
}

/**
 * Function: File used for sonar analysis
 * @Params: workspace,pom and branch name mandatory
 */
void execSonarAnalysis( def wspace) {
  try {
    def build_file = wspace+'/WebCommerce/rocc/build_scripts/build_weblogic.xml'
    echo "Ant file path for sonar analysis: "+build_file
    def result = sh(returnStdout: true, script: "export JAVA_HOME=/app/ci/jdk/jdk1.8.0_131; /app/ci/ant/apache-ant-1.9.6/bin/ant -f $build_file sonar -DdisableSonar=false -Dsonar.language=java -Dsonar.issuesReport.html.enable=true -Dsonar.issuesReport.console.enable=true -Dsonar.issuesReport.lightModeOnly=true -Dsonar.scm.disabled=true")
    println result
  }
  catch (Exception groovyEx) {
    println "Error while sonar analysis :"+groovyEx.getMessage()
    println groovyEx.getCause()
  }
}