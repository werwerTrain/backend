pipeline {
    agent any

    environment {
        // 设置 Docker 镜像的标签
        BACKEND_IMAGE = "bush/backend:latest"
    }

    stages {

        stage('Build Backend') {
            steps {
                script {
                    // 构建后端 Docker 镜像
                    bat 'docker build -t %BACKEND_IMAGE% .\\backend'
                }
            }
        }

        stage('Push Backend Image') {
            steps {
                script {
                    // 推送后端 Docker 镜像到 Docker Registry
                    bat 'docker push %BACKEND_IMAGE%'
                }
            }
        }

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

        stage('Integration Test') {
            steps {
                echo 'tested!'
                // 等待应用启动
                //bat 'timeout /T 30'
                
                // 使用测试工具进行集成测试
                
                // 使用 Postman Collection 进行测试
                //bat 'newman run collection.json'  // 如果使用 Newman 运行 Postman 测试
                
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
