pipeline{
    agent any
    tools{
        jdk 'JAVA11'
        maven 'MAVEN3'
    }
    environment{
        GIT_BRANCH = 'main'
    }
    stages{
        stage('FETCH CODE'){
            steps{
                git branch: GIT_BRANCH , url: 'https://github.com/Palaniappan-Annamalai/jenkins.git'
            }
        }
        stage('BUILD'){
            steps{
                sh 'mvn install -DskipTests'
            }
        }
        stage('UNIT TEST'){
            steps{
                sh 'mvn test'
            }
        }
    }
}