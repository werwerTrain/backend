pipeline {
    agent any

    tools { 
        nodejs "nodejs" 
    }

    environment {
        // 设置 Docker 镜像的标签
        BACKEND_IMAGE = "luluplum/backend:latest"
        DOCKER_CREDENTIALS_ID = '9b671c50-14d3-407d-9fe7-de0463e569d2'
        DOCKER_PASSWORD = 'luluplum'
        DOCKER_USERNAME = 'woaixuexi0326'
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
                    sh 'docker run -d ${BACKEND_IMAGE}'
                }
            }
        }


        stage('Push Docker Image') {
            steps {
                script {
                    // 使用凭证登录 Docker 镜像仓库
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push ${BACKEND_IMAGE}
                        '''
                    }
                }
            }
        }
    

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh 'kubectl delete -f k8s/backend-service.yaml'
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

        //stage('Running Test Scenario') {
        //    steps {
        //        sh 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/454814/detail?token=xMdFQ9lwtW8wtYkrsTsMO5 -r html,cli'
        //    }
        //}


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