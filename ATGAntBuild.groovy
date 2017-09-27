#!groovy
/**
  * Function: ATG ANT Builder which will compiles the code, execute unit tests and create packages
  * Author: nsharma43@sapient.com
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
    throw groovyEx
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
    throw groovyEx
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
    def str = wspace[-1..-1]
    echo str+'strrrrrrrrrrrrrr'
    if (str.isNumber()) {
      ws = wspace[0..-3]
      echo ws+'          neweeeeeeeeeeeeeee'+str
      def sourceFolder = ws+"@libs/DevOps/resources/"
      echo "source path : $sourceFolder"
      file_path = ws+"@libs/DevOps/resources/config/prod_dev_weblogic.properties"
      Properties props = new Properties()
      File propsFile = new File("$file_path")
      props.load(propsFile.newDataInputStream())
      def app_path = props.getProperty('module.workspace')
      
      def destFolder = wspace+'/'+"${app_path}"+'/build_scripts'
      echo "destination folder: $destFolder"
      def script_path=ws+'@libs/DevOps/resources/scripts/download_lib.sh'
      def proc="sh $script_path $sourceFolder $destFolder".execute().text
    } else {
      def sourceFolder1 = wspace+"@libs/DevOps/resources/"
      echo "source path : $sourceFolder1"
      file_path1 = wspace+"@libs/DevOps/resources/config/prod_dev_weblogic.properties"
      Properties props1 = new Properties()
      File propsFile1 = new File("$file_path1")
      props1.load(propsFile1.newDataInputStream())
      def app_path1 = props1.getProperty('module.workspace')
      def destFolder1 = wspace+'/'+"${app_path1}"+'/build_scripts'
      echo "destination folder: $destFolder1"
      def script_path1=wspace+'@libs/DevOps/resources/scripts/download_lib.sh'
      def proc1="sh $script_path1 $sourceFolder1 $destFolder1".execute().text
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
    throw groovyEx
	}
}

/**
  * Function: Method used to execute the ant build
  * @params: workspace, ant home
  */
def execAntBuild( def wspace, def antpath) {
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
    echo "Buildfile path : ${build_file}"
    def result = sh(returnStdout: true, script: "${antpath}/bin/ant -f $build_file -DdisableUnitTest=true build")
    println result
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
    throw groovyEx
  }
}

/**
  * Function: Method used to package the build artifacts
  * @params: workspace, instance name, branch name, environment name
  */
def AssembleEAR ( def instance, def env, def wspace, def Instrumented, def branch_name, def ant_path) {
  try {
    echo "chopping the string"
    def str = wspace[-1..-1]
    if (str.isNumber()) {
      ws = wspace[0..-3]
      file_path = ws+"@libs/DevOps/resources/config/${instance}_dev_weblogic.properties"
	  println file_path
	  } else {
      file_path = wspace+"@libs/DevOps/resources/config/${instance}_dev_weblogic.properties"
    }
    Properties props = new Properties()
    File propsFile = new File("$file_path")
    props.load(propsFile.newDataInputStream())
    def app_path = props.getProperty('module.workspace')
    def build_file = wspace+'/'+"${app_path}"+'/build_scripts/build_weblogic.xml'
    echo "Buildfile path : ${build_file}"
    echo "JVM: ${instance}"
    def result = sh(returnStdout: true, script: "cd $wspace; ${ant_path}/bin/ant -f $build_file -DInstrumented=${Instrumented} -Denv=${env} -Dbranch_name=${branch_name} -Dservername=${instance} -Dear.home=${wspace} deploy-ear-to-weblogic")
    println result
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
    throw groovyEx
  }
}

/**
  * Function: Method used to deploy package the build artifacts repository
  * @params: workspace, instance name, branch name, environment name
  */
def NexusDeploy( def mvn_home, def wspace, def instance,def buildNumber) {
  
  try {
    echo "chopping the string"
	echo "Build Number  ==  ${buildNumber}"
    def str = wspace[-1..-1]
    if (str.isNumber()) {
      ws = wspace[0..-3]
      file_path = ws+"@libs/DevOps/resources/config/username.properties"
      get_nexus_path = ws+"@libs/DevOps/resources/config/account.properties"
    } else {
      file_path = wspace+"@libs/DevOps/resources/config/username.properties"
      get_nexus_path = wspace+"@libs/DevOps/resources/config/account.properties"
    }
    Properties props = new Properties()
    File propsFile = new File("$file_path")
    props.load(propsFile.newDataInputStream())
    def app_path = props.getProperty('ant.project.name')
    
    Properties props1 = new Properties()
    File propsFile1 = new File("$get_nexus_path")
    props1.load(propsFile1.newDataInputStream())
    def nexus_url = props1.getProperty('nexus')    
    
    def result = sh(returnStdout: true, script: "cd ${wspace}/application/${app_path}_${instance}; tar -zcvf ${app_path}_${instance}.ear.tar.gz ${app_path}_${instance}.ear; ${mvn_home}/bin/mvn deploy:deploy-file -DgroupId=${app_path} -DartifactId=${app_path} -Dversion=${buildNumber} -DgeneratePom=true -Dpackaging=tar.gz -DrepositoryId=nexus -Durl=${nexus_url} -Dfile=${app_path}_${instance}.ear.tar.gz")    
    println result
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
    throw groovyEx
  }
}


/**
  * Function: Method used to deploy package the build artifacts repository
  * @params: workspace, instance name, branch name, environment name
  */
def deployment ( def wspace ) {
  //def weblogic_home, def ear_name, def  wspace, def Instrumented, def branch_name
  // yet to be integrated.
  try {
    def build_file = wspace+'/WebCommerce/rocc/build_scripts/build_weblogic.xml'
    echo "Stopping weblogic server "
    sh(returnStdout: false, script: "sleep 20")  
    echo "Starting weblogic server "
    def result = sh(returnStdout: true, script: "sleep 20")
    echo "Weblogic server restarted"
    println result
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
    throw groovyEx
  }
}

/**
  * Function: Method used to deploy package the build artifacts repository
  * @params: workspace, instance name, branch name, environment name
  */
def ZapSecurity( def wspace , def ZAP_path) {
  //def weblogic_home, def ear_name, def  wspace, def Instrumented, def branch_name
  try {
    def build_file = wspace+'/WebCommerce/rocc/build_scripts/build_weblogic.xml'
    def ran = sh(returnStdout: true, script: "head -200 /dev/urandom | cksum | cut -f1 -d ' '")
    println ran
    def result = sh(returnStdout: true, script: "sh ${ZAP_path}/ZAP_2.6.0/zap.sh -daemon -newsession ${ran} -cmd -quickurl http://del2vmplidoweb01:8081/nexus/content/repositories/rocc  -quickprogress -quickout report.xml")    
    println result
  }
  catch (Exception groovyEx) {
    println groovyEx.getMessage()
    println groovyEx.getCause()
    throw groovyEx
  }
}