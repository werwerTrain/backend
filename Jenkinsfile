pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'bxr_new', url: 'https://github.com/werwerTrain/backend.git'
            }
        }
        
        stage('delete old image in k8s'){
            steps{
                 bat '''
                kubectl delete -f k8s/backend-deployment.yaml
                kubectl delete -f k8s/backend-service.yaml
                '''
            }
        }
        stage('Build Backend') {
            steps {
                script {
                    // 清理原有镜像，构建后端 Docker 镜像
                    // 查找并停止旧的容器
                    powershell '''
                    $containers = docker ps -q --filter "ancestor=bxr0820/backend:latest"
                    foreach ($container in $containers) {
                        Write-Output "Stopping container $container"
                        docker stop $container
                    }
    
                    $allContainers = docker ps -a -q --filter "ancestor=bxr0820/backend:latest"
                    foreach ($container in $allContainers) {
                        Write-Output "Removing container $container"
                        docker rm $container
                    }
                    '''
                    bat 'docker rmi -f bxr0820/backend:latest'
                    bat '''
                    docker build -t bxr0820/backend ./backend
                    '''
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                        bat '''
                        echo buxinran123| docker login -u bxr0820 --password-stdin
                        docker push bxr0820/backend:latest
                        '''
                }
            }
        }


        stage('deploy to k8s'){
            steps{
                bat '''
                kubectl apply -f k8s/backend-deployment.yaml
                kubectl apply -f k8s/backend-service.yaml
                '''
                echo '部署成功'
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
