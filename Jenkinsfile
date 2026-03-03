pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    environment {
        DOCKER_IMAGE = 'shikhar68/scientific-calculator'
        DOCKER_TAG   = 'latest'
        FAILURE_STAGE = ''
        FAILURE_REASON = ''
    }

    stages {

        stage('Checkout') {
            steps {
                script {
                    try {
                        git branch: 'main', url: 'https://github.com/shikhar-mutta/Scientific-Calculator-with-DevOps.git'
                    } catch (Exception e) {
                        env.FAILURE_STAGE = 'Checkout'
                        env.FAILURE_REASON = e.getMessage()
                        throw e
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    try {
                        sh 'mvn clean compile'
                    } catch (Exception e) {
                        env.FAILURE_STAGE = 'Build'
                        env.FAILURE_REASON = 'Maven compilation failed. Check for syntax errors in Java code.'
                        throw e
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    try {
                        sh 'mvn test'
                    } catch (Exception e) {
                        env.FAILURE_STAGE = 'Test'
                        env.FAILURE_REASON = 'One or more JUnit tests failed. Check test results for details.'
                        throw e
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    try {
                        sh 'mvn package'
                    } catch (Exception e) {
                        env.FAILURE_STAGE = 'Package'
                        env.FAILURE_REASON = 'Failed to package the JAR file.'
                        throw e
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    try {
                        sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    } catch (Exception e) {
                        env.FAILURE_STAGE = 'Docker Build'
                        env.FAILURE_REASON = 'Docker image build failed. Check Dockerfile for errors.'
                        throw e
                    }
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    try {
                        withCredentials([usernamePassword(
                            credentialsId: 'docker-hub-credentials',
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )]) {
                            sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                            sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                            sh 'docker logout'
                        }
                    } catch (Exception e) {
                        env.FAILURE_STAGE = 'Docker Push'
                        env.FAILURE_REASON = 'Failed to push image to Docker Hub. Check credentials and permissions.'
                        throw e
                    }
                }
            }
        }

        stage('Deploy - Ansible') {
            steps {
                script {
                    try {
                        sh 'ansible-playbook -i inventory.ini deploy.yml'
                    } catch (Exception e) {
                        env.FAILURE_STAGE = 'Deploy - Ansible'
                        env.FAILURE_REASON = 'Ansible deployment failed. Check deploy.yml and container status.'
                        throw e
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! Calculator is deployed.'
            mail to: 'shikharmutta67@gmail.com',
                 subject: "SUCCESS: Scientific Calculator Pipeline - Build #${env.BUILD_NUMBER}",
                 body: "The pipeline completed successfully.\n\nJob: ${env.JOB_NAME}\nBuild: #${env.BUILD_NUMBER}\nURL: ${env.BUILD_URL}"
        }
        failure {
            echo 'Pipeline failed. Check the logs above for errors.'
            script {
                def stage = env.FAILURE_STAGE ?: 'SCM Checkout (pre-pipeline)'
                def reason = env.FAILURE_REASON ?: 'Failed to fetch code from GitHub. The repository may be unreachable or GitHub may be down.'
                mail to: 'shikharmutta67@gmail.com',
                     subject: "FAILURE: Scientific Calculator Pipeline - Build #${env.BUILD_NUMBER}",
                     body: "The pipeline has failed.\n\nJob: ${env.JOB_NAME}\nBuild: #${env.BUILD_NUMBER}\n\nFailed Stage: ${stage}\nReason: ${reason}\n\nConsole Output: ${env.BUILD_URL}console"
            }
        }
    }
}
