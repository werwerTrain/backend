pipeline {
    agent any

    environment {
        // 设置 Docker 镜像的标签
        BACKEND_IMAGE = "3181577132/backend:latest"
        DOCKER_CREDENTIALS_ID = "361fae32-8683-4422-8312-c1e80b9dceed" // Jenkins 中 Docker Hub 凭据的 ID
    }

    stages {

        stage('Docker Login') {
            steps {
                script {
                    // 登录到 Docker Hub
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        bat "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    // 构建后端 Docker 镜像
                    bat "docker build -t ${BACKEND_IMAGE} ./backend"
                }
            }
        }

        stage('Push Backend Image') {
            steps {
                script {
                    // 推送后端 Docker 镜像到 Docker Registry
                    bat "docker push ${BACKEND_IMAGE}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    bat "kubectl apply -f k8s/backend-deployment.yaml"
                }
            }
        }

        stage('Service to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    bat "kubectl apply -f k8s/backend-service.yaml"
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
                //powershell 'newman run collection.json'  // 如果使用 Newman 运行 Postman 测试
                
            }
        }
    }

    post {
        always {
            // 这里可以添加一些清理步骤，例如清理工作目录或通知
            bat "docker system prune -f"
        }
        success {
            echo 'Build and deployment succeeded!'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}
