#!groovy
import groovy.json.JsonOutput

node {
    def defaultProjectDockerRegistry = "registry.sflpro.com"
    def defaultProjectDockerRegistryUsername = "jenkins"
    def defaultProjectDockerRegistryPassword = "J3nk1ns"
    def gradleHome = tool 'GRADLE_5.0'
    def scannerHome = tool 'SONAR_SCANNER_3.0.3.778'
    env.JAVA_HOME = tool 'JDK_u162'
    env.PATH = "${gradleHome}/bin:${scannerHome}/bin:${env.PATH}"

    stage('Checkout project') {
        checkout scm
    }

    stage('Build') {
        sh "gradle -x test build"
    }

    def loginToRegistry = { String url = defaultProjectDockerRegistry,
                            String user = defaultProjectDockerRegistryUsername,
                            String pass = defaultProjectDockerRegistryPassword ->
        sh "docker login ${url} -u ${user} -p ${pass}"
    }

    def buildDockerImage = { String environment, String registry ->
        stage('Build docker image') {
            sh "gradle -x test buildDockerWithLatestTag -Penvironment=$environment -PdockerRegistryUrl=${registry}"
            sh "docker rmi ${registry}/translation-ms-$environment:latest"
        }
    }

    def executeSonarAnalysis = {
        stage('SonarQube analysis') {
            sh "gradle --info sonarqube -Dsonar.login=${env.SONAR_LOGIN} -Dsonar.host.url=${env.SONAR_HOST_URL}"
        }
    }

    def callDeploymentJob = { String projectJobName, String projectName ->
        stage('Call to Deployer') {
            build job: projectJobName, wait: false, parameters: [
                    string(name: 'QUP_APP_NAME', value: projectName)
            ]
        }
    }

    // Add whichever params you think you'd most want to have
    // replace the slackURL below with the hook url provided by
    // slack when you configure the webhook
    def notifySlack = { String text, String channel ->
        def slackURL = 'https://hooks.slack.com/services/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'
        def payload = JsonOutput.toJson([text      : text,
                                         channel   : channel,
                                         username  : "vasil",
                                         icon_emoji: ":ghost:"])
        sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"
    }

    switch (BRANCH_NAME) {
        case "develop":
            def projectEnv = "test"
            //executeSonarAnalysis()
            loginToRegistry(defaultProjectDockerRegistry, defaultProjectDockerRegistryUsername, defaultProjectDockerRegistryPassword)
            buildDockerImage(projectEnv, defaultProjectDockerRegistry)
            callDeploymentJob("development-deployer", "translation-ms")
            break
        case "master":
            def projectEnv = "production"
            //executeSonarAnalysis()
            loginToRegistry(defaultProjectDockerRegistry, defaultProjectDockerRegistryUsername, defaultProjectDockerRegistryPassword)
            buildDockerImage(projectEnv, defaultProjectDockerRegistry)
            //callDeploymentJob("translation-ms", projectEnv)
            break
    }
//    stage('Slack Notification') {
//        notifySlack("Finalized build translation-ms${env.BUILD_NUMBER} for project Translation MS.", "#v4-builds")
//    }
}
