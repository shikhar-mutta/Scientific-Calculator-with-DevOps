pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    environment {
        DOCKER_IMAGE    = 'shikhar68/scientific-calculator'
        DOCKER_TAG      = 'latest'

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
                    try {
                        sh 'mvn clean compile -q'
                    } catch (Exception e) {
                        currentBuild.description = 'Failed Stage: Build\nReason: Maven compilation failed. Check for syntax errors in Java code.'
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
                        currentBuild.description = 'Failed Stage: Test\nReason: One or more JUnit tests failed. Check test results for details.'
                        throw e
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    try {
                        sh 'mvn package -DskipTests -q'
                    } catch (Exception e) {
                        currentBuild.description = 'Failed Stage: Package\nReason: Failed to package the JAR file.'
                        throw e
                    }
                }
            }
        }

        stage('Docker Build') {
            options {
                timeout(time: 15, unit: 'MINUTES')  // Kill the stage if it hangs
            }
            steps {
                script {
                    try {
                        // Verify Docker daemon is reachable before building
                        sh 'docker info > /dev/null 2>&1'
                        sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    } catch (Exception e) {
                        currentBuild.description = 'Failed Stage: Docker Build\nReason: Docker image build failed. Ensure Docker daemon is running and Jenkins has socket access.'
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
                            sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                            sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                            sh 'docker logout'
                        }
                    } catch (Exception e) {
                        currentBuild.description = 'Failed Stage: Docker Push\nReason: Failed to push image to Docker Hub. Check credentials and permissions.'
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
                        currentBuild.description = 'Failed Stage: Deploy\nReason: Ansible deployment failed. Check deploy.yml and container status.'
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
                def info = currentBuild.description ?: 'Failed Stage: SCM Checkout (pre-pipeline)\nReason: Failed to fetch code from GitHub. The repository may be unreachable or GitHub may be down.'
                mail to: 'shikharmutta67@gmail.com',
                     subject: "FAILURE: Scientific Calculator Pipeline - Build #${env.BUILD_NUMBER}",
                     body: "The pipeline has failed.\n\nJob: ${env.JOB_NAME}\nBuild: #${env.BUILD_NUMBER}\n\n${info}\n\nConsole Output: ${env.BUILD_URL}console"
            }
        }
        always {
            // Clean up dangling images to save disk space
            sh 'docker image prune -f || true'
        }
    }
}
