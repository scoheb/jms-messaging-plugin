#!/usr/bin/env groovy

node('docker') {
    /* clean out the workspace just to be safe */
    deleteDir()

    /* Grab our source for this build */
    checkout scm


    String containerArgs = '-v /var/run/docker.sock:/var/run/docker.sock -v $HOME/.m2:/var/maven/.m2'
    stage('Build') {
        /* Performing some clever trickery to map our ~/.m2 into the container */

        /* Make sure our directory is there, if Docker creates it, it gets owned by 'root' */
        sh 'mkdir -p $HOME/.m2'

        docker.image('maven:3.3-jdk-7').inside(containerArgs) {
            timestamps {
                sh 'mvn install:install-file -DgroupId=org.zeromq -DartifactId=jeromq-jms ' +
                        ' -Dversion=1.0 -Dpackaging=jar -Dfile=lib/jeromq-jms-1.0.jar'
                sh 'mvn -B -U -e -Dmaven.test.failure.ignore=true -Duser.home=/var/maven clean install -DskipTests'
            }
        }
        def uid = sh returnStdout: true, script: "id -u | tr '\n' ' '"
        def gid = sh returnStdout: true, script: "id -g | tr '\n' ' '"

        def buildArgs = "--build-arg=uid=${uid} --build-arg=gid=${gid} src/test/resources/ath-container"
        docker.build('jenkins/ath', buildArgs)

    }

    stage('Test') {
        docker.image('jenkins/ath').inside(containerArgs) {
            sh '''
                eval $(./vnc.sh 2> /dev/null)
                mvn test -Dmaven.test.failure.ignore=true -Duser.home=/var/maven -DforkCount=1 -B
            '''
        }
    }

    stage('Archive') {
        junit 'target/surefire-reports/**/*.xml'
        archiveArtifacts artifacts: 'target/**/*.jar', fingerprint: true
    }
}
