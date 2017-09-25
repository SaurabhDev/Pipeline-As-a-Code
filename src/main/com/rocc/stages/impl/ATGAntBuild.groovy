#!groovy
/**
  * Function: ATG ANT Builder which will compiles the code, execute unit tests and create packages
  * Author: nsharma43@sapient.com, ssinghal127@sapient.com
  * Date: 24 Apr'2017
  */
package main.com.rocc.stages.impl
import groovy.lang.Closure;
import java.nio.file.*
import groovy.util.AntBuilder.*


/**
  * @Function: Load a resource file
  * @params: Workspace and path of file
  */
def getProperty( def wspace, def prop) {
	try {
    def workspace = wspace+"@libs/DevOps/resources/config/account.properties"
    echo "Load the file workspace"
    Properties props = new Properties()
    File propsFile = new File(workspace)
    props.load(propsFile.newDataInputStream())
    return props
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
  }
}

/**
  * Funtion: Method use to clean the workspace
  * @param workspace
  */
def cleanWorkspace( def wspace) {
	try {
    echo 'Cleaning the workspace'
    echo wspace
    Properties props = getProperty()
    def build_dir = props.getProperty(wspace,"build_dir")
    new File(build_dir).deleteDir()
	}
  catch (Exception groovyEx) {
		println groovyEx.getMessage()
		println groovyEx.getCause()
	}
}

/**
  * Function: Method used to copy the src/classes directory
  * @param workspace
  */
def copy( def wspace) {
  try { 
    echo 'copy code base at workspace'
    echo "chopping the string"
    str = wspace[-1..-1]
    echo str+'strrrrrrrrrrrrrr'
    if (str.isNumber()) {
      ws = wspace[0..-3]
      echo ws+'          neweeeeeeeeeeeeeee'+str
      def sourceFolder = ws+"@libs/DevOps/resources/"
      echo "source path : $sourceFolder"
      def destFolder = wspace+"/WebCommerce/rocc/build_scripts"
      echo "destination folder: $destFolder"
      def script_path=ws+'@libs/DevOps/resources/scripts/download_lib.sh'
      def proc="sh $script_path $sourceFolder $destFolder".execute().text
    } else {
      def sourceFolder = wspace+"@libs/DevOps/resources/"
      echo "source path : $sourceFolder"
      def destFolder = wspace+"/WebCommerce/rocc/build_scripts"
      echo "destination folder: $destFolder"
      def script_path=wspace+'@libs/DevOps/resources/scripts/download_lib.sh'
      def proc="sh $script_path $sourceFolder $destFolder".execute().text
    }
    
    //sh(returnStdout: true, script: "")
    //def c = new StringBuffer()
    //proc.consumeProcessErrorStream(c)
    //echo proc.text
    //echo c.toString()
	}
  catch (Exception groovyEx) {
		println groovyEx.getMessage()
		println groovyEx.getCause()
	}
}

/**
  * Function: Method used to execute the ant build
  * @params: workspace
  */
def execAntBuild( def wspace) {
  try {
    def build_file = wspace+'/WebCommerce/rocc/build_scripts/build_weblogic.xml'
    def result = sh(returnStdout: true, script: "/app/ci/ant/apache-ant-1.9.6/bin/ant -f $build_file -Dprojectname=rocc -DdisableUnitTest=true build")
    println result
    /*def proc = "/app/ci/ant/apache-ant-1.9.6/bin/ant -f $build_file -Dprojectname=rocc -DdisableUnitTest=true build".execute()
    def b = new StringBuffer()
    proc.consumeProcessErrorStream(b)
    echo proc.text
    echo b.toString()*/
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
  }
}