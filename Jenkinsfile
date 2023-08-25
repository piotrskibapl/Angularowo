def webhookUrl = 'https://discord.com/api/webhooks/1081761276797128754/0yXcIoxXm_aui4VhomSpuUXfOFNO1V12NGJQSGP0DsX-LGUuxXba9mLOj15F0iYdDWcY'
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
                    discordSend title: title, description: description, result: currentBuild.currentResult, link: env.JOB_DISPLAY_URL, webhookURL: webhookUrl
                }
                checkout scm
                sh 'chmod +x -R ${WORKSPACE}'
                sh './gradlew clean'
            }
        }
        stage('Analyze') {
            steps {
                withCredentials([file(credentialsId: '05734911-26ea-4d2d-b31d-b988f17a2934', variable: 'google_services')]) {
                   writeFile file: 'app/google-services.json', text: readFile(google_services)
                }
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
                sh './gradlew assembleDebug'
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
                ]) {
                    sh './gradlew assembleRelease -PsigningKeyAlias=\'key0\' -PsigningKeyPass=${key_pass} -PsigningStoreFilePath=${key_store} -PsigningStorePass=${store_pass}'
                    sh './gradlew bundleRelease -PsigningKeyAlias=\'key0\' -PsigningKeyPass=${key_pass} -PsigningStoreFilePath=${key_store} -PsigningStorePass=${store_pass}'
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
                discordSend title: title, description: description, result: currentBuild.currentResult, link: env.JOB_DISPLAY_URL, webhookURL: webhookUrl
            }
        }
        success {
            archiveArtifacts artifacts: 'app/build/outputs/apk/*/app-*.apk, app/build/outputs/bundle/release/app-release.aab, app/build/outputs/mapping/release/mapping.txt'
        }
    }
}
