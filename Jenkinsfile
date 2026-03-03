pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE = 'shikhar68/scientific-calculator'
        DOCKER_TAG   = 'latest'
    }

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/shikhar-mutta/Scientific-Calculator-with-DevOps.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh 'docker logout'
                }
            }
        }

        stage('Deploy - Ansible') {
            steps {
                sh 'ansible-playbook -i inventory.ini deploy.yml'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! Calculator is deployed.'
        }
        failure {
            echo 'Pipeline failed. Check the logs above for errors.'
        }
    }
}
