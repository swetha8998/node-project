final STACKNAME= 'hello-world-node'
final ACCOUNT= '993745358053'
def version = currentBuild.number

pipeline{
  agent any
  stages{
    stage('git checkout'){
      steps{
        git branch: 'master', url: 'https://github.com/swetha8998/node-project'     
  }
} 
 }
}
