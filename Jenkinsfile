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
                sh 'mvn package -DskipTests'
            }
        }
        stage('UNIT TEST'){
            steps{
                sh 'mvn test'
            }
        }

        stage('CHECKSTYLE REPORT'){
            steps{
              sh 'mvn checkstyle:checkstyle'
            }
        }

        stage('UPLOAD REPORTS TO SONARQUBE'){
            environment{
              sonarScanner = tool 'sonar'
            }
            steps{
              withSonarQubeEnv('SonarServer') {
                                      sh """
                                               ${sonarScanner}/bin/sonar-scanner \
                                              -Dsonar.junit.reportPaths=target/surefire-reports/*.xml \
                                              -Dsonar.coverage.jacoco.xmlReportPaths=target/jacoco-reports/jacoco.xml \
                                              -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml \
                                              -Dsonar.projectKey=Jenkins \
                                              -Dsonar.projectName=Jenkins \
                                              -Dsonar.projectVersion=1.0 \
                                              -Dsonar.sources=src \
                                              -Dsonar.java.binaries=target/classes
                                      """
                                 }

            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 15, unit: 'MINUTES') {
                  waitForQualityGate abortPipeline: true
              }
            }  
        }

        stage('Publish to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: '192.168.29.186:8081',
                    groupId: 'com.cs',
                    version: "${env.BUILD_ID}-SNAPSHOT",
                    repository: 'Jenkins-Repo',
                    credentialsId: 'nexus-id',
                    artifacts: [
                        [artifactId: 'jenkins-demo', 
                         file: 'target/jenkins-demo-0.0.1-SNAPSHOT.jar', 
                         type: 'jar']
                    ]
                )
            }
        }

    }
}