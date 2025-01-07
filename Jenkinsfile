pipeline {
    agent any
    options {
        skipDefaultCheckout true
        buildDiscarder logRotator(
            artifactDaysToKeepStr: '30'
        )
    }
    stages {
        stage('Setup') {
            steps {
                script {
                    def title = "Build started: ${env.JOB_NAME.split('/')[0]}"
                    def description = """
                        Branch: ${env.BRANCH_NAME}
                        Build: ${env.BUILD_NUMBER}
                    """.stripIndent()
                    withCredentials([string(credentialsId: 'angularowoDiscordWebhookUrl', variable: 'discord_webhook_url')]) {
                        discordSend title: title, description: description, result: currentBuild.currentResult, link: env.JOB_DISPLAY_URL, webhookURL: discord_webhook_url
                    }
                }
                checkout scm
                sh 'chmod +x -R ${WORKSPACE}'
                sh './gradlew clean'
                withCredentials([file(credentialsId: 'angularowoGoogleServices', variable: 'google_services')]) {
                   writeFile file: 'app/google-services.json', text: readFile(google_services)
                }
            }
        }
        stage('Analyze') {
            steps {
                sh './gradlew ktlintCheck'
            }
        }
        stage('Test debug') {
            steps {
                sh './gradlew testDebug'
            }
        }
        stage('Test release') {
            when {
                anyOf { branch 'master'; tag 'v*.*.*' }
            }
            steps {
                sh './gradlew testRelease'
            }
        }
        stage('Build debug') {
            steps {
                withCredentials([string(credentialsId: 'angularowoDebugApiKey', variable: 'api_key')]) {
                    sh './gradlew assembleDebug -PapiKey=${api_key}'
                }
            }
        }
        stage('Build release') {
            when {
                anyOf { branch 'master'; tag 'v*.*.*' }
            }
            steps {
                withCredentials([
                    file(credentialsId: 'androidKeyStore', variable: 'key_store'),
                    string(credentialsId: 'androidKeyPass', variable: 'key_pass'),
                    string(credentialsId: 'androidStorePass', variable: 'store_pass'),
                    string(credentialsId: 'angularowoReleaseApiKey', variable: 'api_key'),
                ]) {
                    sh './gradlew assembleRelease -PsigningKeyAlias=\'key0\' -PsigningKeyPass=${key_pass} -PsigningStoreFilePath=${key_store} -PsigningStorePass=${store_pass} -PapiKey=${api_key}'
                    sh './gradlew bundleRelease -PsigningKeyAlias=\'key0\' -PsigningKeyPass=${key_pass} -PsigningStoreFilePath=${key_store} -PsigningStorePass=${store_pass} -PapiKey=${api_key}'
                }
            }
        }
    }
    post {
        always {
            script {
                def changelog = ""
                if (currentBuild.changeSets.size() == 0) {
                    changelog = "No changes."
                } else {
                    for (int i = 0; i < currentBuild.changeSets.size(); i++) {
                        for (int j = 0; j < currentBuild.changeSets[i].items.size(); j++) {
                            def entry = currentBuild.changeSets[i].items[j]
                            changelog = "${changelog}\n - ${entry.commitId[-6..-1]} ${entry.msg} - ${entry.author.toString()}"
                        }
                    }
                }
                def title = "${env.JOB_NAME.split('/')[0]}"
                def description = """
                    |Branch: ${env.BRANCH_NAME}
                    |Build: ${env.BUILD_NUMBER}
                    |Status: ${currentBuild.currentResult.toLowerCase().capitalize()}
                    |Changes: $changelog
                """.stripMargin()
                withCredentials([string(credentialsId: 'angularowoDiscordWebhookUrl', variable: 'discord_webhook_url')]) {
                    discordSend title: title, description: description, result: currentBuild.currentResult, link: env.JOB_DISPLAY_URL, webhookURL: discord_webhook_url
                }
            }
        }
        success {
            archiveArtifacts artifacts: 'app/build/outputs/apk/*/app-*.apk, app/build/outputs/bundle/release/app-release.aab, app/build/outputs/mapping/release/mapping.txt'
        }
    }
}
