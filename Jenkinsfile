pipeline {
    agent any

    tools {nodejs "nodejs"}
    environment {
        // 设置 Docker 镜像的标签
        BACKEND_IMAGE = "bush/backend:latest"
    }

    stages {

        stage('Build Backend') {
            steps {
                script {
                    // 构建后端 Docker 镜像
                    echo 'Starting Docker build...'
                    bat 'docker build -t %BACKEND_IMAGE% .\\backend'
                    echo 'Docker build completed.'
                }
            }
        }

        // stage('Push Backend Image') {
        //     steps {
        //         script {
        //             // 推送后端 Docker 镜像到 Docker Registry
        //             bat 'docker push %BACKEND_IMAGE%'
        //         }
        //     }
        // }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    bat 'kubectl apply -f k8s\\backend-deployment.yaml'
                }
            }
        }

        stage('Service to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    bat 'kubectl apply -f k8s\\backend-service.yaml'
                }
            }
        }

        stage('Install Apifox CLI') {
            steps {
                script{
                    bat 'npm install -g apifox-cli'
                }
            }
        }

        stage('Running Test Scenario') {
            steps {
                script{
                    bat 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/454758/detail?token=xo8xYK5XoS7t7qWjDR6Wih -r html,cli'
                }
            }
        }
    }

    post {
        always {
            // 这里可以添加一些清理步骤，例如清理工作目录或通知
            bat 'docker system prune -f'
        }
        success {
            echo 'Build and deployment succeeded!'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}
