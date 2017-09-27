#!groovy
/************************************************************
***** Description :: This Package is used for NPM Build *****
***** Author      :: Ankush Rohila                         *****
***** Date        :: 06/06/2017                         *****
***** Revision    :: 2.0                                *****
************************************************************/

package main.com.rocc.stages.impl

String NPM_RUN

/*************************************
** Function to set the variables.
**************************************/
void setValue(String npm_run)
{
   this.NPM_RUN    =  npm_run
}

/********************************************
** Function to Build npm modules
*********************************************/
def npmBuild()
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	     println "\u001B[32m[INFO] Building NPM modules, please wait..."
		 if ( "${NPM_RUN}" == "null" ) {
		   sh "npm run build"
		 }
		 else {
		   sh """${NPM_RUN}"""
		 }
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to install NPM modules..."
         throw error
      }
   }
}
/*************************************
** Function to check Sequence File
**************************************/
def checkSEQFile()
{
   String SEQUENCE_FILE = "${env.WORKSPACE}/sequence.txt"
   
   try {
      if (fileExists("${SEQUENCE_FILE}"))
      {
         wrap([$class: 'AnsiColorBuildWrapper']) {
            println "\u001B[32m[INFO] reading modules from ${SEQUENCE_FILE}, please wait..."
         }
      }
      else
      {
         wrap([$class: 'AnsiColorBuildWrapper']) {
            error "\u001B[41m[ERROR] ${SEQUENCE_FILE} file does not exist..."
            currentBuild.result = 'FAILURE'
         }
      }
   }
   catch (Exception excp) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to read ${SEQUENCE_FILE}..." 
         throw excp
      }
   }
}
/********************************************
** Function to install npm modules
*********************************************/
def npmInstall()
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	     println "\u001B[32m[INFO] Installing NPM modules, please wait..."
		 sh '''#!/bin/bash
               SEQ=`cat sequence.txt`

               for i in $SEQ
               do
               cd $i/
               npm install && command || exit 19
               npm test && command || exit 19
               cd -
               done
            '''
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to install NPM modules..."
         throw error
      }
   }
}
/********************************************
** Function to run npm test cases
*********************************************/
def npmTests()
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	     println "\u001B[32m[INFO] Running test cases on NPM modules, please wait..."
		 sh '''#!/bin/bash
               SEQ=`cat sequence.txt`
			   export DISPLAY=:99

               for i in $SEQ
               do
               cd $i/
               npm install && command || exit 19
               npm test && command || exit 19
               cd -
               done
            '''
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to test NPM modules..."
         throw error
      }
   }
}
/********************************************
** Function to run npm command
*********************************************/
def npmRun()
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	     println "\u001B[32m[INFO] Running command on NPM modules, please wait..."
		 sh "npm run ${NPM_RUN} && command || exit 19"
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to run NPM command..."
         throw error
      }
   }
}#!groovy
/************************************************************
***** Description :: This Package is used for NPM Build *****
***** Author      :: Mukul Garg                         *****
***** Date        :: 04/24/2017                         *****
***** Revision    :: 2.0                                *****
************************************************************/

package com.sapient.devops.npm

String NPM_RUN

/*************************************
** Function to set the variables.
**************************************/
void setValue(String npm_run)
{
   this.NPM_RUN    =  npm_run
}

/********************************************
** Function to Build npm modules
*********************************************/
def npmBuild()
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	     println "\u001B[32m[INFO] Building NPM modules, please wait..."
		 if ( "${NPM_RUN}" == "null" ) {
		   sh "npm run build"
		 }
		 else {
		   sh """${NPM_RUN}"""
		 }
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to install NPM modules..."
         throw error
      }
   }
}
/*************************************
** Function to check Sequence File
**************************************/
def checkSEQFile()
{
   String SEQUENCE_FILE = "${env.WORKSPACE}/sequence.txt"
   
   try {
      if (fileExists("${SEQUENCE_FILE}"))
      {
         wrap([$class: 'AnsiColorBuildWrapper']) {
            println "\u001B[32m[INFO] reading modules from ${SEQUENCE_FILE}, please wait..."
         }
      }
      else
      {
         wrap([$class: 'AnsiColorBuildWrapper']) {
            error "\u001B[41m[ERROR] ${SEQUENCE_FILE} file does not exist..."
            currentBuild.result = 'FAILURE'
         }
      }
   }
   catch (Exception excp) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to read ${SEQUENCE_FILE}..." 
         throw excp
      }
   }
}
/********************************************
** Function to install npm modules
*********************************************/
def npmInstall()
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	     println "\u001B[32m[INFO] Installing NPM modules, please wait..."
		 sh '''#!/bin/bash
               SEQ=`cat sequence.txt`

               for i in $SEQ
               do
               cd $i/
               npm install && command || exit 19
               npm test && command || exit 19
               cd -
               done
            '''
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to install NPM modules..."
         throw error
      }
   }
}
/********************************************
** Function to run npm test cases
*********************************************/
def npmTests()
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	     println "\u001B[32m[INFO] Running test cases on NPM modules, please wait..."
		 sh '''#!/bin/bash
               SEQ=`cat sequence.txt`
			   export DISPLAY=:99

               for i in $SEQ
               do
               cd $i/
               npm install && command || exit 19
               npm test && command || exit 19
               cd -
               done
            '''
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to test NPM modules..."
         throw error
      }
   }
}
/********************************************
** Function to run npm command
*********************************************/
def npmRun()
{
   try {
      wrap([$class: 'AnsiColorBuildWrapper']) {
	     println "\u001B[32m[INFO] Running command on NPM modules, please wait..."
		 sh "npm run ${NPM_RUN} && command || exit 19"
	  }
   }
   catch (Exception error) {
      wrap([$class: 'AnsiColorBuildWrapper']) {
         println "\u001B[41m[ERROR] failed to run NPM command..."
         throw error
      }
   }
}