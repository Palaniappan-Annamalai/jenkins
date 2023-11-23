pipeline{
    agent any
    tools{
        jdk 'JAVA11'
        maven 'MAVEN3'
    }
    environment{
         DOCKER_CREDENTIALS = 'docker-hub'
         IMAGE_NAME = 'iyyappan24/my-jenkins-image'
         IMAGE_TAG = 'latest'
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
                    emailext subject: "Build Successfull - Build ID: ${env.BUILD_ID}",
                          body: 'The build was successful. Congratulations!',
                          to: 'root@iyyappan',
                          mimeType: 'text/html'
                }
                failure{
                    emailext subject: "Build Failure - Build ID: ${env.BUILD_ID}" ,
                          body: 'The build failed. Please investigate.' ,
                          to: 'root@iyyappan' ,
                          mimeType: 'text/html'
                    echo 'Pipeline failed. Aborting further steps.'
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
            post{
                success{
                    emailext subject: "Sonar Analysis Successfull - Build ID: ${env.BUILD_ID}",
                          body: 'Sonar Analysis was successful. Ready to publish into NEXUS',
                          to: 'root@iyyappan',
                          mimeType: 'text/html'
                }
                failure{
                    emailext subject: "Sonar Analysis Failed - Build ID: ${env.BUILD_ID}" ,
                          body: 'Sonar Analysis failed. Please investigate the issues.' ,
                          to: 'root@iyyappan' ,
                          mimeType: 'text/html'
                    echo 'Pipeline failed. Aborting further steps.'
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

        stage('Build Docker Image'){
            steps{
                script{
                   sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -f Dockerfile .'
                   //docker.build("${IMAGE_NAME}:${IMAGE_TAG}", "--file Dockerfile .")
                }
            }
        }

        stage('Publish Image'){
            steps{
                withDockerRegistry([credentialsId: DOCKER_CREDENTIALS, url: 'https://index.docker.io/v1/']) {
                    sh "docker push ${IMAGE_NAME}"
                }
            }
            post{
                always{
                    sh 'docker rmi ${IMAGE_NAME}'
                    sh 'docker logout'
                }
            }
        }

        stage('Deploy DEV'){
            steps{
                script {
                    def userInput = input(
                        id: 'userInput', message: 'Do you want to Deploy?', parameters: [
                            choice(name : 'Proceed', choices: ['Yes', 'No']),
                        ]
                    )

                    if (userInput == 'Yes') {
                        echo 'Proceeding with deployment...'
                    } else {
                        error 'Deployment aborted by user.'
                    }
                }
            }
        }

        stage('Deploy QA'){
            steps{
                script {
                    def userInput = input(
                        id: 'userInput', message: 'Do you want to Deploy?', parameters: [
                            choice(name : 'Proceed', choices: ['Yes', 'No']),
                        ]
                    )

                    if (userInput == 'Yes') {
                        echo 'Proceeding with deployment...'
                    } else {
                        error 'Deployment aborted by user.'
                    }
                }
            }
        }

        stage('Deploy UAT'){
            steps{
                script {
                    def userInput = input(
                        id: 'userInput', message: 'Do you want to Deploy?', parameters: [
                            choice(name : 'Proceed', choices: ['Yes', 'No']),
                        ]
                    )

                    if (userInput == 'Yes') {
                        echo 'Proceeding with deployment...'
                    } else {
                        error 'Deployment aborted by user.'
                    }
                }
            }
        }

        stage('Deploy Production'){
            steps{
                script {
                    def userInput = input(
                        id: 'userInput', message: 'Do you want to Deploy?', parameters: [
                            //choice(name : 'Proceed', choices: ['Yes', 'No']),
                            choice(name : 'Select and Proceed', choices: 'Yes')
                        ]
                    )

                    if (userInput == 'Yes') {
                        echo 'Proceeding with deployment...'
                    } else {
                        error 'Deployment aborted by user.'
                    }
                }
            }
        }
    }
}