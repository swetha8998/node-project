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
       //sh "aws ecr create-repository --repository-name ${STACKNAME}"
         sh "docker tag ${STACKNAME}:latest ${ECR_REGISTRY}/${STACKNAME}"
        sh "docker push ${ECR_REGISTRY}/${STACKNAME}"
         //sh "docker rmi ${ECR_REGISTRY} ${STACKNAME}"
        sh 'echo "image is pushed"'
         
       }
   }
 }
    stage('deployment'){
      steps{
        script{
          sh " echo 'in deployment stage' "
           try{
          sh "aws ecs create-cluster --cluster-name fargate-cluster"
      } catch(Exception e){
      println e
      } 
           sh "aws ecs register-task-definition --cli-input-json file://taskdef.json"
   sh "aws ecs list-task-definitions"
  
      sh "aws ecs list-services --cluster fargate-cluster"
  }
}
    }
    
    stage("parsing taskdefcount"){
    steps{
    script{
   taskdefcount=sh(script:"aws ecs list-task-definitions --family-prefix sample-fargate",returnStdout:true)
   //println taskdefcount
   
    taskdefcount= new groovy.json.JsonSlurperClassic().parseText(taskdefcount )
    taskdefcount=taskdefcount.taskDefinitionArns.size()
    //println "taskdefcount parsed:${taskdefcount}"
    sh " aws ecs create-service --cluster fargate-cluster --service-name fargate-service --task-definition sample-fargate:${taskdefcount} --desired-count 1 --launch-type \"FARGATE\" --network-configuration \"awsvpcConfiguration={subnets=[subnet-4c6fb07d],securityGroups=[sg-1579811d],assignPublicIp=ENABLED}\""
sluper =null
      //taskdefcount=null 
    }
    }
  }
     stage("creating service"){
    steps{
     // sh " aws ecs create-service --cluster fargate-cluster --service-name fargate-service --task-definition sample-fargate:${taskdefcount} --desired-count 1 --launch-type \"FARGATE\" --network-configuration \"awsvpcConfiguration={subnets=[subnet-4c6fb07d],securityGroups=[sg-1579811d],assignPublicIp=ENABLED}\""
      sh "aws ecs list-services --cluster fargate-cluster"
    }
     }
  } //stages
} //pipeline
