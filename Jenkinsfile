pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    environment {
        DOCKER_IMAGE = 'shikhar68/scientific-calculator'
        DOCKER_TAG   = 'latest'
    }

    stages {

        stage('Checkout') {
            steps {
                script {
                    try {
                        git branch: 'main', url: 'https://github.com/shikhar-mutta/Scientific-Calculator-with-DevOps.git'
                    } catch (Exception e) {
                        currentBuild.description = 'Failed Stage: Checkout\nReason: ' + e.getMessage()
                        throw e
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    def exitCode = sh(script: 'mvn clean compile 2>&1 | tee /tmp/build.log', returnStatus: true)
                    if (exitCode != 0) {
                        def errors = sh(script: "grep '\\[ERROR\\]' /tmp/build.log | head -5", returnStdout: true).trim()
                        currentBuild.description = "Failed Stage: Build\nReason:\n${errors}"
                        error("Build failed")
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    def exitCode = sh(script: 'mvn test 2>&1 | tee /tmp/test.log', returnStatus: true)
                    if (exitCode != 0) {
                        def errors = sh(script: "grep -E '\\[ERROR\\]|Tests run:.*Failures: [1-9]' /tmp/test.log | head -5", returnStdout: true).trim()
                        currentBuild.description = "Failed Stage: Test\nReason:\n${errors}"
                        error("Tests failed")
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    def exitCode = sh(script: 'mvn package 2>&1 | tee /tmp/package.log', returnStatus: true)
                    if (exitCode != 0) {
                        def errors = sh(script: "grep '\\[ERROR\\]' /tmp/package.log | head -5", returnStdout: true).trim()
                        currentBuild.description = "Failed Stage: Package\nReason:\n${errors}"
                        error("Package failed")
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    def exitCode = sh(script: "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} . 2>&1 | tee /tmp/docker_build.log", returnStatus: true)
                    if (exitCode != 0) {
                        def errors = sh(script: "grep -i 'error\\|failed' /tmp/docker_build.log | head -5", returnStdout: true).trim()
                        currentBuild.description = "Failed Stage: Docker Build\nReason:\n${errors}"
                        error("Docker build failed")
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
                        currentBuild.description = 'Failed Stage: Docker Push\nReason: ' + e.getMessage()
                        throw e
                    }
                }
            }
        }

        stage('Deploy - Ansible') {
            steps {
                script {
                    def exitCode = sh(script: 'ansible-playbook -i inventory.ini deploy.yml 2>&1 | tee /tmp/deploy.log', returnStatus: true)
                    if (exitCode != 0) {
                        def errors = sh(script: "grep -i 'fatal\\|error\\|failed' /tmp/deploy.log | head -5", returnStdout: true).trim()
                        currentBuild.description = "Failed Stage: Deploy\nReason:\n${errors}"
                        error("Deployment failed")
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
                def info = currentBuild.description ?: 'Failed Stage: SCM Checkout (pre-pipeline)\nReason: Failed to fetch code from GitHub.'
                mail to: 'shikharmutta67@gmail.com',
                     subject: "FAILURE: Scientific Calculator Pipeline - Build #${env.BUILD_NUMBER}",
                     body: "The pipeline has failed.\n\nJob: ${env.JOB_NAME}\nBuild: #${env.BUILD_NUMBER}\n\n${info}\n\nConsole Output: ${env.BUILD_URL}console"
            }
        }
    }
}
