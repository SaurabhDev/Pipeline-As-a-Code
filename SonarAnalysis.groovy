#!groovy
/**
 * Jenkins file for ROCC Pipeline
 * Author: nsharma43@sapient.com
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
void execSonarAnalysis( def wspace, def ant_path, def java_path) {
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
    def res = sh(returnStdout: true, script: "export JAVA_HOME=${java_path}; ${ant_path}/bin/ant -f $build_file sonar -DdisableSonar=false -Dsonar.language=java -Dsonar.issuesReport.html.enable=true -Dsonar.issuesReport.console.enable=true -Dsonar.issuesReport.lightModeOnly=true -Dsonar.scm.disabled=true")
    println res
    /*def proc = "".execute()
    def b = new StringBuffer()
    proc.consumeProcessErrorStream(b)
    echo proc.text
    echo b.toString()*/
  }
  catch (Exception groovyEx) {
    println "Error while sonar analysis :"+groovyEx.getMessage()
    println groovyEx.getCause()
  }
}