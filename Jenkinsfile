pipeline {
    agent any

    environment {
        MAVEN_HOME = '/path/to/maven' // Specify your Maven installation path
        JAVA_HOME = '/path/to/java'   // Specify your Java installation path
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the Git repository
                git 'https://github.com/your-repository/my-java-project.git'
            }
        }
        
        stage('Build') {
            steps {
                // Run Maven to build the project
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                // Run Maven test phase, which triggers JUnit tests
                sh 'mvn test'
            }
        }

        stage('Generate Code Coverage') {
            steps {
                // Run JaCoCo to generate the code coverage report
                sh 'mvn jacoco:report'
            }
        }

        stage('Publish Results') {
            steps {
                // Publish the test results and coverage report (JaCoCo)
                junit '**/target/test-*.xml' // Publish JUnit test results
                jacoco(execPattern: '**/target/*.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java')
            }
        }
    }

    post {
        always {
            // Archive the artifacts if needed
            archiveArtifacts '**/target/*.jar'
            // Clean up workspace
            cleanWs()
        }
    }
}
