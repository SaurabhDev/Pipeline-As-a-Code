#!groovy
/**
  * Test file for ROCC Pipeline
  * Author: nsharma43@sapient.com, ssinghal127@sapient.com
  */
package test.com
  
import main.com.rocc.stages.impl.*

void testInitialize() {
	def obj = new GITCheckout()
		obj.initialize()
}

void testcheckout(def wspace, def branch) { 
	  def obj = new GITCheckout()
		  obj.checkout(wspace,branch)	  
			  
}


