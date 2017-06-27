pipeline {

    agent any

    tools {
        maven   'MAVEN-3.0'
        jdk     'JDK8'
    }

    stages {

        stage ('prepare') {
            steps {
                checkout scm
            }
        }

        stage ('build') {
            steps {
                withEnv(['MAVEN_OPTS=-Xmx512m']) {
                    sh "mvn -Dmaven.test.failure.ignore clean install"
                }
            }
        }

        stage ('analysis') {
            steps {
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }

    }

    post {
        success {
            archiveArtifacts artifacts: "**/target/*.jar, **/target/*.zip"
        }
    }
}
