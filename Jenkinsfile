pipeline {
    agent any

    tools { 
        nodejs "nodejs" 
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'sxq', url: 'https://github.com/werwerTrain/backend.git'
            }
        }
        
        stage('delete old image in k8s'){
            steps{
                 bat '''
                kubectl delete -f k8s/backend-deployment.yaml --ignore-not-found
                kubectl delete -f k8s/backend-service.yaml --ignore-not-found
                '''
            }
        }
        stage('Build Backend') {
            steps {
                script {
                    // 清理原有镜像，构建后端 Docker 镜像
                    // 查找并停止旧的容器
                    powershell '''
                    $containers = docker ps -q --filter "ancestor=qiuer0121/backend:latest"
                    foreach ($container in $containers) {
                        Write-Output "Stopping container $container"
                        docker stop $container
                    }
    
                    $allContainers = docker ps -a -q --filter "ancestor=qiuer0121/backend:latest"
                    foreach ($container in $allContainers) {
                        Write-Output "Removing container $container"
                        docker rm $container
                    }
                    '''
                    bat 'docker rmi -f qiuer0121/backend:latest'
                    bat '''
                    docker build -t qiuer0121/backend ./backend
                    '''
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                        bat '''
                        echo 20050121Rabbit| docker login -u qiuer0121 --password-stdin
                        docker push qiuer0121/backend:latest
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
        
        stage('Install Apifox CLI') {
            steps {
                sh 'npm install -g apifox-cli'
            }
        }
        
        stage('Running Test Scenario') {
            steps {
                sh 'apifox run https://api.apifox.com/api/v1/projects/4458630/api-test/ci-config/455500/detail?token=xst_-7kP70toSLt_CssqOW -r html,cli'
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
