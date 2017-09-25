#!groovy
/**
  * Test file for ROCC Pipeline
  * Author: nsharma43@sapient.com, ssinghal127@sapient.com
  */

package test.com  
  
import groovy.lang.Closure;
import java.nio.file.*
import main.com.rocc.stages.impl.* 


void testExecuteSonar() {
	def obj = new SonarAnalysis()
	obj.executeSonar()
}