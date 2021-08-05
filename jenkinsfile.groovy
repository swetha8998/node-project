final STACKNAME= 'node-project'
final ACCOUNT= '993745358053'
def version = currentBuild.number
final ECR_REGISTRY='993745358053.dkr.ecr.us-east-1.amazonaws.com'
pipeline{
  agent any
  stages{
    stage('git checkout'){
      steps{
        git branch: 'master', url: 'https://github.com/swetha8998/node-project'     
  }
} 
    stage('Building'){
   steps{
       script{
           sh 'docker build . -f Dockerfile -t node-project'
           sh 'aws --version'
           sh 'aws configure set region us-east-1'
           sh " aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 993745358053.dkr.ecr.us-east-1.amazonaws.com "
       sh "aws ecr create-repository --repository-name ${STACKNAME}"
         sh "docker tag ${STACKNAME}:latest ${ECR_REGISTRY}/${STACKNAME}"
        sh "docker push ${ECR_REGISTRY}/${STACKNAME}"
        sh 'echo "image is pushed"'
       }
   }
 }
  }
}
