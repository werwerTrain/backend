pipeline {
    agent any

    environment {
        // 设置 Docker 镜像的标签
        FRONTEND_IMAGE = "luluplum/frontend:latest"
        BACKEND_IMAGE = "luluplum/backend:latest"
        DOCKER_REGISTRY = "your-docker-registry-url" // 如果你使用的是私有 Docker Registry，请修改此处
    }

    stages {
        stage('Build Frontend') {
            steps {
                script {
                    // 构建前端 Docker 镜像
                    sh 'docker build -t ${FRONTEND_IMAGE} ./frontend'
                }
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    // 构建后端 Docker 镜像
                    sh 'docker build -t ${BACKEND_IMAGE} ./backend'
                }
            }
        }

        stage('Push Frontend Image') {
            steps {
                script {
                    // 推送前端 Docker 镜像到 Docker Registry
                    sh 'docker push ${FRONTEND_IMAGE}'
                }
            }
        }

        stage('Push Backend Image') {
            steps {
                script {
                    // 推送后端 Docker 镜像到 Docker Registry
                    sh 'docker push ${BACKEND_IMAGE}'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    sh 'kubectl apply -f k8s/frontend-deployment.yaml'
                    sh 'kubectl apply -f k8s/backend-deployment.yaml'
                }
            }
        }

        stage('Service to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    sh 'kubectl apply -f k8s/frontend-service.yaml'
                    sh 'kubectl apply -f k8s/backend-service.yaml'
                }
            }
        }

        stage('Integration Test') {
            steps {
                echo 'tested!'
                // 等待应用启动
                //sleep(time: 30, unit: 'SECONDS')
                
                // 使用测试工具进行集成测试
                
                // 使用 Postman Collection 进行测试
                //sh 'newman run collection.json'  // 如果使用 Newman 运行 Postman 测试
                
            }
        }
    }

    post {
        always {
            // 这里可以添加一些清理步骤，例如清理工作目录或通知
            sh 'docker system prune -f'
        }
        success {
            echo 'Build and deployment succeeded!'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}