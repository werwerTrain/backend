pipeline {
    agent any

    tools { 
        nodejs "nodejs" 
    }

    environment {
        // 设置 Docker 镜像的标签
        BACKEND_IMAGE = "luluplum/backend:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'luluplum', url: 'https://github.com/werwerTrain/backend.git'
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
    

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    sh 'kubectl apply -f k8s/backend-deployment.yaml'
                }
            }
        }

        stage('Service to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    sh 'kubectl apply -f k8s/backend-service.yaml'
                }
            }
        }

        stage('Running Test Scenario') {
            steps {
                sh 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/454814/detail?token=xMdFQ9lwtW8wtYkrsTsMO5 -r html,cli'
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