#!groovy
/**
 * Jenkinsfile for Jenkins2 Pipeline
 * author: nsharma43@sapient.com
 */
package main.com.rocc.stages.impl

import groovy.lang.Closure;
import java.nio.file.*
import groovy.util.AntBuilder.*

/**
  * Method used to execute junit
  * @param workspace
  */
def execUnitTestCases( def wspace) {
  try {
    def build_file = wspace+'/WebCommerce/rocc/build_scripts/build_weblogic.xml'
    echo "Ant file path : "+build_file
    
    def proc = "/app/ci/ant/apache-ant-1.9.6/bin/ant -f $build_file -Dprojectname=rocc -DdisableUnitTest=false build".execute()
    def b = new StringBuffer()
    proc.consumeProcessErrorStream(b)
    println proc.text
    println b.toString()
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
  }
}

/**
 * Function: Method used to publish sonar report
 * @param wspace
 * @return
 */
def publishReport( def wspace) {
  try {
    junit allowEmptyResults: true, testResults: wspace+'/WebCommerce/rocc/reports/junit/xml/*.xml'
    //publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: wspace+'/WebCommerce/rocc/reports/html', reportFiles: 'index.html', reportName: 'HTML Report'])
    //publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: '/u02/app/ci/jenkins/jobs/Release1.2-track-WebCommerce-PollSCM/workspace/kairos/reports/html', reportFiles: 'index.html', reportName: 'Report'])
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
  }
}