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
def execUnitTestCases( def wspace, def antpath) {
  try {
    echo "chopping the string"
    def str = wspace[-1..-1]
    if (str.isNumber()) {
      ws = wspace[0..-3]
      file_path = ws+"@libs/DevOps/resources/config/prod_dev_weblogic.properties"
    } else {
      file_path = wspace+"@libs/DevOps/resources/config/prod_dev_weblogic.properties"
    }
    Properties props = new Properties()
    File propsFile = new File("$file_path")
    props.load(propsFile.newDataInputStream())
    def app_path = props.getProperty('module.workspace')
  
    def build_file = wspace+'/'+"${app_path}"+'/build_scripts/build_weblogic.xml'
    echo "Ant file path for unit test execution: "+build_file
    
    def proc = sh(returnStdout: true, script: "${antpath}/bin/ant -f $build_file -DdisableUnitTest=false build")
    println proc
    /*def b = new StringBuffer()
    proc.consumeProcessErrorStream(b)
    println proc.text
    println b.toString()*/
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
    throw groovyEx
  }
}

/**
 * Function: Method used to publish sonar report
 * @param wspace
 * @return
 */
def publishReport( def wspace) {
  try {
    echo "chopping the string"
    def str = wspace[-1..-1]
    if (str.isNumber()) {
      ws = wspace[0..-3]
      file_path = ws+"@libs/DevOps/resources/config/prod_dev_weblogic.properties"
    } else {
      file_path = wspace+"@libs/DevOps/resources/config/prod_dev_weblogic.properties"
    }
    Properties props = new Properties()
    File propsFile = new File("$file_path")
    props.load(propsFile.newDataInputStream())
    def app_path = props.getProperty('module.workspace')
    junit allowEmptyResults: true, testResults: wspace+'/'+"${app_path}"+'/reports/junit/xml/*.xml'
    //publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: wspace+'/WebCommerce/rocc/reports/html', reportFiles: 'index.html', reportName: 'HTML Report'])
    //publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: '/u02/app/ci/jenkins/jobs/Release1.2-track-WebCommerce-PollSCM/workspace/kairos/reports/html', reportFiles: 'index.html', reportName: 'Report'])
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
    throw groovyEx
  }
}