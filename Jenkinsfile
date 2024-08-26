pipeline {
    agent any

    environment {
        // 设置 Docker 镜像的标签
        BACKEND_IMAGE = "3181577132/backend:latest"
        KUBECONFIG = credentials('kubectl_id')
        DOCKER_CREDENTIALS_ID = "d5402ec3-f900-4767-94b2-bea019b24060" // Jenkins 中 Docker Hub 凭据的 ID
        // DOCKER_USERNAME = "3181577132"
        // DOCKER_PASSWORD = ""
    }

    stages {

        // stage('Docker Login') {
        //     steps {
        //         script {
        //             // 登录到 Docker Hub
        //             withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
        //                 bat "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
        //             }
        //         }
        //     }
        // }

        tage('Checkout') {
            steps {
                git branch: 'candy', url: 'https://github.com/werwerTrain/backend.git'
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    // 查找并停止旧的容器
                    bat "
                    CONTAINERS=$(docker ps -q --filter "ancestor=${BACKEND_IMAGE}")
                    if [ -n "$CONTAINERS" ]; then
                        docker stop $CONTAINERS
                    fi"

                    // 删除停止的容器
                    bat "
                    CONTAINERS=$(docker ps -a -q --filter "ancestor=${BACKEND_IMAGE}")
                    if [ -n "$CONTAINERS" ]; then
                        docker rm $CONTAINERS
                    fi
                    "

                    bat "
                    docker rmi -f ${BACKEND_IMAGE} || true
                    "
                    // 构建后端 Docker 镜像
                    bat "docker build -t ${BACKEND_IMAGE} ./backend"
                }
            }
        }

        stage('Push Backend Image') {
            steps {
                script {
                    // 推送后端 Docker 镜像到 Docker Registry
                    // 使用凭证登录 Docker 镜像仓库
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID)]) {
                        bat "
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push ${BACKEND_IMAGE}
                        "
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // 应用 Kubernetes 配置
                    bat "kubectl delete -f k8s/backend-deployment.yaml"
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
