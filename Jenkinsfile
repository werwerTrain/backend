pipeline {
    agent any
    
    environment {
        // 设置 Docker 镜像的标签
        DOCKER_CREDENTIALS_ID = 'dockerhub'
        DOCKER_PASSWORD = '20050121Rabbit'
        DOCKER_USERNAME = 'qiuer0121'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'sxq', url: 'https://github.com/werwerTrain/backend.git'
            }
        }
        

        stage('Build Backend') {
            steps {
                script {
                    // 清理原有镜像，构建后端 Docker 镜像
                    bat '''
                    docker build -t backend ./backend
                    '''
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                        bat '''
                        echo "20050121Rabbit" | docker login -u qiuer0121 --password-stdin
                        docker push backend
                        '''
                }
            }
        }


        stage('Deploy to Kubernetes') {
            steps {
                script {
                    bat 'kubectl apply -f k8s/backend-deployment.yaml'
                }
            }
        }

        stage('Service to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    bat 'kubectl apply -f k8s/backend-service.yaml'
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
