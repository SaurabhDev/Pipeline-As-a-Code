#!groovy
/**
 * Jenkinsfile for Jenkins2 Pipeline
 * author: ssi257,nsharma43@sapient.com
 */
package com.rocc.stages.impl

import groovy.lang.Closure;
import java.nio.file.*

/**
 * Method used to execute sonar
 * @return
 */
def executeSonar() {
	try {
	echo 'Execution of sonar'
	def proc='sh /u02/app/ci/jenkins_home/workspace/Checkout@libs/roccjenkinspipelineascode/resources/scripts/Sonar.sh'.execute()
	} catch(Exception groovyEx) {
		println groovyEx.getMessage()
		println groovyEx.getCause()
	}
  
}