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

    stage('Build and Test') {
        sh "gradle build"
    }

    def loginToRegistry = { String url = defaultProjectDockerRegistry,
                            String user = defaultProjectDockerRegistryUsername,
                            String pass = defaultProjectDockerRegistryPassword ->
        sh "docker login ${url} -u ${user} -p ${pass}"
    }

    def buildDockerImage = { String environment, String registry ->
        stage('Build docker image') {
            sh "gradle -x test buildDockerWithLatestTag -Penvironment=$environment -PdockerRegistryUrl=${registry}"
            //sh "gradle -x test buildDockerWithReleaseTag -Penvironment=$environment -PdockerRegistryUrl=${registry} -PreleaseVersion=${env.BUILD_NUMBER}"
            sh 'echo "*************** cleanup docker images ***************"'
            // translation ms
            sh "docker rmi ${registry}/translation-ms-$environment:latest"
            //sh "docker rmi ${registry}/translation-ms-$environment:${env.BUILD_NUMBER}"
        }
    }

    def executeSonarAnalysis = {
        stage('SonarQube analysis') {
            sh "gradle --info sonarqube -Dsonar.login=${env.SONAR_LOGIN} -Dsonar.host.url=${env.SONAR_HOST_URL}"
        }
    }

    def callDeploymentJob = { String projectName, String projectEnv ->
        def projectDockerRegistry = "registry.sflpro.com"
        def projectJobName = "deployment-local"
        build job: projectJobName, wait: false, parameters: [
                string(name: 'APP_NAME', value: projectName),
                string(name: 'ENVIRONMENT', value: projectEnv),
                string(name: 'DOCKER_REGISTRY', value: projectDockerRegistry),
                string(name: 'RELEASE_VERSION', value: env.BUILD_NUMBER)
        ] executeSonarAnalysis
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
            executeSonarAnalysis()
            loginToRegistry(defaultProjectDockerRegistry, defaultProjectDockerRegistryUsername, defaultProjectDockerRegistryPassword)
            buildDockerImage(projectEnv, defaultProjectDockerRegistry)
            callDeploymentJob("translation-ms", projectEnv)
            break
        case "acceptance":
            def projectEnv = "acceptance"
            //executeSonarAnalysis()
            loginToRegistry(defaultProjectDockerRegistry, defaultProjectDockerRegistryUsername, defaultProjectDockerRegistryPassword)
            buildDockerImage(projectEnv, defaultProjectDockerRegistry)
            //callDeploymentJob("translation-ms", projectEnv)
            break
        case "master":
            def projectEnv = "production"
            //executeSonarAnalysis()
            loginToRegistry(defaultProjectDockerRegistry, defaultProjectDockerRegistryUsername, defaultProjectDockerRegistryPassword)
            buildDockerImage(projectEnv, defaultProjectDockerRegistry)
            //callDeploymentJob("translation-ms", projectEnv)
            break
    }
    stage('Slack Notification') {
        notifySlack("Finalized build translation-ms${env.BUILD_NUMBER} for project Translation MS.", "#v4-builds")
    }
}
