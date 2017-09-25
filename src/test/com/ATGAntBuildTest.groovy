#!groovy
/**
 * Jenkinsfile for Jenkins2 Pipeline
 * author: nsharma43@sapient.com, ssinghal127@sapient.com
 */
package test.com

import groovy.lang.Closure;
import java.nio.file.*
import groovy.util.AntBuilder.*
import main.com.rocc.stages.impl.* 

def getProperty(def wspace, def prop) {
	try {
	def workspace = wspace+"@libs/DevOps/resources/config/account.properties"
	echo workspace
	Properties props = new Properties()
	File propsFile = new File(workspace)
	props.load(propsFile.newDataInputStream())
	return props
	} catch(Exception groovyEx) {
		println groovyEx.getMessage()
		println groovyEx.getCause()
	}
}

/**
 * Method use to clean the workspace
 * @param wspace
 * @return
 */
def testCleanWorkspace(def wspace) {
	def obj = new ATGAntBuild()
	obj.cleanWorkspace(wspace)
}

/**
 * Method used to copy the src/classes directory
 * @param wspace
 * @return
 */
def testCopy(def wspace) {
	def obj = new ATGAntBuild()
	obj.copy(wspace) 
}

/**
 * Method used to run the junit
 */
void junit() {
	try {
	junit(haltonfailure:'no') {
	classpath(refid:'test.path')
	classpath(location:'${web.classes.dir}')
	formatter(type:'xml')
			batchtest(fork:'yes', todir:'${report.dir}') {
				fileset(dir:'${test.dir}')
					include(name:'**/*Test*.java')
					}
			}
		}
	catch(Exception groovyEx) {
	println groovyEx.getMessage()
	println groovyEx.getCause()
	}
}
	
/**
 * Method used to execute the ant build	
 */
def testExecAntBuild(def wspace) {
	def obj = new ATGAntBuild()
	obj.execAntBuild(wspace)
}
