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
                    // 查找并停止旧的容器
                    sh '''
                    CONTAINERS=$(docker ps -q --filter "ancestor=${BACKEND_IMAGE}")
                    if [ -n "$CONTAINERS" ]; then
                        docker stop $CONTAINERS
                    fi
                    '''
            
                    // 删除停止的容器
                    sh '''
                    CONTAINERS=$(docker ps -a -q --filter "ancestor=${BACKEND_IMAGE}")
                    if [ -n "$CONTAINERS" ]; then
                        docker rm $CONTAINERS
                    fi
                    '''
                    sh '''
                    docker rmi -f ${BACKEND_IMAGE} || true
                    '''
                    // 构建 Docker 镜像
                    sh 'docker build -t ${BACKEND_IMAGE} ./backend'
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
                    sh 'kubectl delete -f k8s/backend-deployment.yaml'
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
        
        stage('Install Apifox CLI') {
            steps {
                sh 'npm install -g apifox-cli'
            }
        }
        stage('Running Test Scenario1') {
            steps {
                sh 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/456010/detail?token=xAlnGIMfDSJ5cNlMMPsFw0 -r html,cli'
            }
        }
        stage('Running Test Scenario2') {
            steps {
                sh 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/456033/detail?token=xBh34KczkGA7raLcfSZ-hR -r html,cli'
            }
        }
        stage('Running Test Scenario3') {
            steps {
                sh 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/455500/detail?token=xst_-7kP70toSLt_CssqOW -r html,cli'
            }
        }
        stage('Running Test Scenario') {
            steps {
                sh 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/456035/detail?token=xDr_qThi_-cbh9aJ6EMzNb -r html,cli'
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
