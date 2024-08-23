pipeline {
    agent any

    tools { nodejs "nodejs" }

    environment {
        BACKEND_IMAGE = "3181577132/backend:latestApifox"
        KUBECONFIG = credentials('kubectl_id')
    }

    stages {
        stage('Build Backend') {
            steps {
                script {
                    bat "docker build -t ${BACKEND_IMAGE} ./backend"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    bat "kubectl apply -f k8s/backend-deployment.yaml"
                    bat "kubectl apply -f k8s/backend-service.yaml"
                }
            }
        }

        stage('Install Apifox CLI') {
            steps {
                script {
                    bat 'npm install -g apifox-cli'
                }
            }
        }
        
        stage('Running Test Scenario') {
            steps {
                script {
                    bat 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/454752/detail?token=xAjJfgLp7PZnYRurGHvTOv -r html,cli'
                }
            }
        }
    }

    post {
        always {
            node {
                bat "docker system prune -f"
            }
        }
        success {
            echo 'Build and deployment succeeded!'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}
