#!groovy
/**
  * Jenkins file for ROCC Pipeline
  * Author: nsharma43@sapient.com,ssi257
  */
 
/**
  * Import the package for using the class functions
  */
package main.com.rocc.stages.impl

/**
  * Function: Checkout GIT repository code at workspace
  * @Params: workspace and branch name mandatory
  */
void checkout( def wspace, def branch) {
	try {
	  echo "Running job at workspace " + wspace
    str = wspace[-1..-1]
    echo str+'strrrrrrrrrrrrrr'
    if (str.isNumber()) {
      ws = wspace[0..-3]
      echo ws+'          checkouttttttttt'+str
      file_path = ws+"@libs/DevOps/resources/config/account.properties"
    } else {
      file_path = wspace+"@libs/DevOps/resources/config/account.properties"
    }
	  echo "$file_path"
	  Properties props = new Properties()
	  File propsFile = new File("$file_path")
	  props.load(propsFile.newDataInputStream())
	  def git_url = props.getProperty('url')
    def git_pass = props.getProperty('password')
	  echo 'Starting code checkout at workspace ..'
	  checkout(
	  [$class: 'GitSCM',
		branches: [[name: branch]],
		doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'WipeWorkspace'], [$class: 'CloneOption', depth: 0, noTags: true, reference: '', shallow: true, timeout: 20]],
		submoduleCfg: [],
		userRemoteConfigs: [
		  [credentialsId: git_pass,
			url: git_url]
		  ]
	  ]
	)} catch (Exception groovyEx) {
		  println groovyEx.getMessage()
		  println groovyEx.getCause()
      throw groovyEx
	  }
}

/**
  * Function: get GIT repository URL from workspace
  * @Params: workspace and branch name mandatory
  */
def getRepo( def workspace) {
  try {
    gitRepoURL = sh(returnStdout: true, script: "cd ${workspace};git config --get remote.origin.url").trim()
    return gitRepoURL
  }
  catch (Exception error) {
    echo "Failed to get the Git repository URL..."
	  throw error
  }
}



