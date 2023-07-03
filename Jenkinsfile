pipeline {
    agent any

    options {
        buildDiscarder logRotator(
            artifactDaysToKeepStr: '30'
        )
    }

    stages {
        stage('Analyze') {
            steps {
                withCredentials([file(credentialsId: '05734911-26ea-4d2d-b31d-b988f17a2934', variable: 'google_services')]) {
                   writeFile file: 'app/google-services.json', text: readFile(google_services)
                }
                sh 'chmod +x -R ${WORKSPACE}'
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
                branch 'master'
            }
            steps {
                sh './gradlew testRelease'
            }
        }
        stage('Build debug') {
            steps {
                sh './gradlew assembleDebug'
                archiveArtifacts artifacts: 'app/build/outputs/apk/debug/app-debug.apk'
            }
        }
        stage('Build release') {
            when {
                branch 'master'
            }
            steps {
                sh './gradlew assembleRelease'
                archiveArtifacts artifacts: 'app/build/outputs/apk/release/app-release.apk, app/build/outputs/bundle/release/app-release.aab, app/build/outputs/mapping/release/mapping.txt'
            }
        }
    }
}
