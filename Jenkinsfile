pipeline {
    options {
        buildDiscarder logRotator(numToKeepStr: '3')
    }
    agent any
    stages {
        stage("Build and Test") {
            agent {
                docker {
                    image "$DOCKER_REGISTRY/oracle/serverjre:8"
                    registryUrl "https://$DOCKER_REGISTRY/"
                    registryCredentialsId 'nexus'
                }
            }
            steps {
                sh './gradlew wrapper clean build'
            }
        }
        stage("Upload Maven") {
            when {
                anyOf {
                    branch 'master';
                    branch 'acceptance';
                    branch 'develop'
                }
            }
            steps {
                withCredentials(
                    [
                        usernamePassword(
                            credentialsId: 'nexus',
                            usernameVariable: 'SONATYPE_USERNAME',
                            passwordVariable: 'SONATYPE_PASSWORD'
                        )
                    ]
                ) {
                    sh "./gradlew wrapper :rest:client:uploadArchives"
                }
            }
        }
        stage("Push Docker") {
            steps {
                withCredentials(
                    [
                        usernamePassword(
                            credentialsId: 'nexus',
                            usernameVariable: 'DOCKER_REGISTRY_USERNAME',
                            passwordVariable: 'DOCKER_REGISTRY_PASSWORD'
                        )
                    ]
                ) {
                    sh "./gradlew wrapper --exclude-task test pushDockerTags --project-prop dockerRegistry=$DOCKER_REGISTRY --project-prop removeImage"
                }
            }
        }
        stage("Deploy") {
            when {
                anyOf {
                    branch 'acceptance';
                    branch 'develop'
                }
            }
            steps {
                script {
                    switch (env.BRANCH_NAME) {
                        case "develop":
                            environment = "development"
                            break
                        case "acceptance":
                            environment = "acceptance"
                            break
                    }
                }
                build(
                    job: 'deploy/master',
                    parameters: [
                        string(name: 'environment', value: environment),
                        booleanParam(name: "translation", value: true)
                    ],
                    propagate: 'true',
                    wait: 'false'
                )
            }
        }
    }
    post {
        always {
            script {
                switch (currentBuild.currentResult) {
                    case "SUCCESS":
                        color = 'good'
                        break
                    case "UNSTABLE":
                        color = 'warning'
                        break
                    case "FAILURE":
                        color = 'danger'
                        break
                    default:
                        color = '#439FE0'
                        break
                }
            }
            slackSend(
                color: "$color",
                message: "Build Finished with ${currentBuild.currentResult} - <${env.BUILD_URL}|${env.JOB_NAME} ${env.BUILD_NUMBER}>",
                channel: '#qup-jenkins'
            )
        }
    }
}