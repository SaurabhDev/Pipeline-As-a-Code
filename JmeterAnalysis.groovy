#!groovy
/**
 * Jenkins file for ROCC Pipeline
 * Author: arohilla@sapient.com
 */

/**
 * Import the package for using the class functions
 */
package main.com.rocc.stages.impl


/**
 * Function: File used for jmeter analysis
 */ 
void execJemeterAnalysis(def wspace) {

  try {
	    def result = sh(returnStdout: true, script: "cd \$wspace; docker build -t jenkins-siemenstest .;docker run -i -v /home/jenkins/workspace/Ci2-Siemens-Jmeter-Report/ext/:/home/jmeter/apache-jmeter-3.0/lib/ext jenkins-siemenstest -n -t /home/jmeter/siemens.jmx -l /home/jmeter/Siemens_Result.jtl;")
		println result
		def get_container_id = sh(returnStdout: true, script: "export CON_ID_siemens=\$(docker ps -a -q -f \"ancestor=jenkins-siemenstest\"| awk 'NR==1{print \$1}'); docker cp \$CON_ID_siemens:/home/jmeter/Siemens_Result.jtl .;docker rm \$CON_ID_siemens")
		println get_container_id
		
	   }
  catch (Exception groovyEx) {
    println "Error while Jemeter analysis :"+groovyEx.getMessage()
    println groovyEx.getCause()
  }
}