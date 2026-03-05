# Scientific Calculator with DevOps Pipeline
**Mini Project Report**

---

## 1. Introduction
The objective of this mini-project is to construct a Scientific Calculator application utilizing a complete, automated DevOps toolchain. The application is a menu-driven command-line program built in Java that performs various mathematical operations including Addition, Subtraction, Multiplication, Division, Square Root, Factorial, Natural Logarithm, and Power.

Instead of manual builds and deployments, this project employs a robust Continuous Integration and Continuous Deployment (CI/CD) pipeline. Upon every push to the GitHub repository, the pipeline automatically checks out the code, compiles it, runs unit tests, builds a Docker container image, pushes that image to DockerHub, and utilizes Ansible to deploy the containerized application.

---

## 2. DevOps Toolchain & Architecture

The pipeline consists of the following automated steps, orchestrated seamlessly without manual intervention:
1. **Source Control Management:** Git & GitHub
2. **Build Tool:** Maven (compiles Java source code to `app.jar`)
3. **Testing Engine:** JUnit (executes automated unit tests)
4. **Continuous Integration Pipeline:** Jenkins (`Jenkinsfile` declarative pipeline)
5. **Containerization:** Docker (multi-stage `Dockerfile`)
6. **Container Registry:** DockerHub
7. **Configuration Management & Deployment:** Ansible (`deploy.yml` playbook and `inventory.ini`)

*(Insert Screenshot Here: High-level architectural diagram of the CI/CD pipeline)*

---

## 3. Jenkins CI/CD Pipeline Explanation

The backbone of this project's automation is the `Jenkinsfile`. It defines a Declarative Pipeline that systematically processes the application from raw code to a deployed container. Below is an in-depth stage-by-stage explanation of the pipeline:

### 3.1. Environment Setup
```groovy
environment {
    DOCKER_IMAGE    = 'shikhar68/scientific-calculator'
    DOCKER_TAG      = 'latest'
}
```
The pipeline initializes essential variables directly in the environment block. This prevents hardcoding the Docker image string throughout the script, ensuring that updates to the repository or image tags only need to be changed in one location.

### 3.2. Stage: Checkout SCM
```groovy
stage('Checkout') {
    steps {
        git branch: 'main', url: 'https://github.com/shikhar-mutta/Scientific-Calculator-with-DevOps.git'
    }
}
```
**Explanation:** The first stage of the pipeline pulls the latest source code from the remote `main` branch. This triggers the beginning of the build isolated inside the Jenkins workspace, guaranteeing that the pipeline is executing against the exact code that was just pushed.
*(Insert Screenshot Here: Jenkins showing the successful Checkout step log)*

### 3.3. Stage: Build
```groovy
stage('Build') {
    steps {
        sh 'mvn clean compile -q'
    }
}
```
**Explanation:** This stage initiates the Maven build process. The `clean` directive ensures the workspace is cleared of older class files, preventing cached artifacts from causing false positives. The `compile` directive translates the `.java` files into `.class` bytecode. The `-q` (quiet) flag is utilized to prevent log bloat in the Jenkins console, only outputting errors if they occur.

### 3.4. Stage: Test
```groovy
stage('Test') {
    steps {
        sh 'mvn test'
    }
}
```
**Explanation:** Once the code is compiled, the pipeline runs automated JUnit tests. If any tests fail (e.g., if a mathematical bug was introduced in the scientific calculator logic), the `mvn test` command returns a non-zero exit code, which immediately fails the Jenkins pipeline. This safety net prevents broken code from ever reaching the structural packaging or deployment phases.
*(Insert Screenshot Here: Jenkins Console Output highlighting the successful "mvn test" run)*

### 3.5. Stage: Package
```groovy
stage('Package') {
    steps {
        sh 'mvn package -DskipTests -q'
    }
}
```
**Explanation:** With tests successfully passed, Maven is instructed to package the `.class` files into a deployable, executable Java Archive (`.jar`). The `-DskipTests` flag is appended to prevent running the unit tests a second time, significantly speeding up the pipeline.

### 3.6. Stage: Docker Build
```groovy
stage('Docker Build') {
    options { timeout(time: 15, unit: 'MINUTES') }
    steps {
        sh 'docker info > /dev/null 2>&1'
        sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
    }
}
```
**Explanation:** The pipeline transitions from continuous integration (building a JAR) to containerization. Here, Jenkins interacts directly with the system daemon to build a Docker image using the project's multi-stage `Dockerfile`. 
*(Insert Screenshot Here: Jenkins executing the Docker Build stage, showing cached filesystem layers)*

### 3.7. Stage: Docker Push
```groovy
stage('Docker Push') {
    steps {
        withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', ...)]) {
            sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
            sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
            sh 'docker logout'
        }
    }
}
```
**Explanation:** The freshly built image currently resides only on the Jenkins local disk. To deploy it globally, it must live in a container registry. Jenkins securely accesses DockerHub credentials and pushes the image. A logout is subsequently executed to maintain shell security and avert dangling credential sessions.
*(Insert Screenshot Here: DockerHub Repository page portraying the newly pushed 'latest' tag image size and upload time)*

### 3.8. Stage: Deploy - Ansible
```groovy
stage('Deploy - Ansible') {
    steps {
        sh 'ansible-playbook -i inventory.ini deploy.yml'
    }
}
```
**Explanation:** Continuous Deployment is governed by Ansible. Rather than executing raw imperative shell commands, Jenkins invokes the `deploy.yml` playbook. Ansible connects to the designated hosts, pulls the updated image from DockerHub, removes obsolete containers proactively, and boots up the fresh application container cleanly.

### 3.9. Post Actions
```groovy
post {
    always {
        sh 'docker image prune -f || true'
    }
}
```
**Explanation:** Following every pipeline cycle (Success or Failure), dangling Docker images (orphaned layers) are wiped automatically. This intelligent pipeline design averts disk space starvation on the Jenkins node over extended periods of CI/CD usage.

---

## 4. Test Results and Observations
The application logic features a comprehensive JUnit test suite ensuring robust mathematical operations.

**Test Results Summary:**
* **Total Tests Run:** 37
* **Failures / Errors:** 0
* **Time Elapsed:** ~0.23 seconds

**Observations on Testing:** 
The tests effectively evaluate edge cases that are common in scientific operations, such as ensuring factorials do not return inaccurate memory overflows at reasonable bounds and ensuring negative logarithms handle exception protocols. Because the entire test suite runs continuously under `mvn test` in Jenkins, logic regressions are immediately caught and flagged as structural failed builds.

*(Insert Screenshot Here: Snippet of Maven Surefire Report showing "Tests run: 37, Failures: 0")*

---

## 5. Containerization Optimization (Dockerfile Strategy)

A massive success parameter in this DevOps iteration was optimizing the Docker image footprint utilizing a highly advanced **Multi-stage `jlink` Build**. 

While a standard Java application utilizing `eclipse-temurin:17-jre-alpine` hovered at **256 MB**, our implemented CI/CD pipeline produced an uncompressed image layout of merely **66 MB** (~22 MB highly compressed on DockerHub).

**How it Works:**
1. **Builder Stage:** Uses Maven to gather dependencies efficiently, cache them heavily, and compile the JAR.
2. **Minimal JRE Stage:** Instead of packaging the entire massive JRE, the pipeline uses the `jlink` tool against `eclipse-temurin:17-jdk-alpine`. This pulls out only `java.base` and `java.logging` binaries specific strictly to `musl` libc architecture, heavily reducing bulk.
3. **Runtime Stage:** A blank, barebones `alpine:3.19` image imports the freshly stripped JRE and the `app.jar`.

This translates to extraordinarily fast pipeline pulling procedures and tiny microservice profiles perfect for highly scalable Kubernetes clusters.

*(Insert Screenshot Here: `docker images` terminal output contrasting image sizes)*

---

## 6. Configuration Management with Ansible

Automated Deployment leverages Ansible to keep state definitions declarative, meaning if the playbook is run 10 times, the outcome is successfully identical without throwing false errors.

**Breakdown of the Playbook Logic (`deploy.yml`):**
1. **Pull Latest Image:** Identifies and securely pulls the exact updated tag (`latest`) out of DockerHub automatically.
2. **Remove Existing Container:** Intelligently checks if an instance named `scientific-calculator` already exists running or stopped. If so, it kills it to free up ports and allocation.
3. **Deploy the Update:** Triggers `docker run` detached.

*(Insert Screenshot Here: Ansible playbook terminal execution showing OK / CHANGED status per task)*

---

## 7. Application Overview & Usage

Because the scientific calculator operates as an interactive command-line application inside the container, a user instantiates it by hooking into Docker's interactive TTY (`-it`):
```bash
docker run -it --name calc shikhar68/scientific-calculator:latest
```

The user is met with a clean terminal UI offering all programmed functionalities iteratively until the user explicitly decides to "Exit" the program flow safely.

*(Insert Screenshot Here: Calculator App running in terminal— showing menu prompt and a successful calculation)*

---
**Links:**
* **GitHub Repository:** *(Insert GitHub Repo Link Here)*
* **DockerHub Repository:** *(Insert DockerHub link Here)*
