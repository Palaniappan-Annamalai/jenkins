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
            post{
                success{
                    emailext subject: "Build Success with ID: ${env.BUILD_ID}",
                          body: "The build was successful. Congratulations!",
                          to: "iyyappana1998@gmail.com",
                          mimeType: 'text/html'
                }
                failure{
                    emailext subject: "Build Failure with ID : ${env.BUILD_ID}",
                          body: "The build failed. Please investigate.",
                          to: "iyyappana1998@gmail.com"
                          mimeType: 'text/html'
                }
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
                                              -Dsonar.projectKey=Jenkins \
                                              -Dsonar.projectName=Jenkins \
                                              -Dsonar.projectVersion=1.0 \
                                              -Dsonar.sources=src \
                                              -Dsonar.java.binaries=target/classes \
                                              -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml
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
            environment{
                VERSION  = "${env.BUILD_ID}-SNAPSHOT"
            }
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: '192.168.29.186:8081',
                    groupId: 'com.cs',
                    version: VERSION,
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