#!groovy
/**
 * Jenkinsfile for Jenkins2 Pipeline
 * author: nsharma43@sapient.com
 */
package test.com

import groovy.lang.Closure;
import java.nio.file.*
import groovy.util.AntBuilder.*
import main.com.rocc.stages.impl.* 

/**
 * Method used to execute junit	
 * @param wspace
 * @return
 */
def testExecUTCases(def wspace) {
  def obj = new UnitTesting()
  obj.execUnitTestCases(wspace)
}

/**
 * Method used to publis sonar report
 * @param wspace
 * @return
 */
def testPublishReport(def wspace) {
	def obj = new UnitTesting()
    obj.publishReport(wspace)
}
