# Scientific Calculator with Complete CI/CD & DevOps Pipeline

A CLI-based scientific calculator application programmed in Java, fully automated using an end-to-end CI/CD pipeline integrated with Jenkins, Docker, Ansible, and Maven.

---

## 🚀 Key Features
- **Java 17 CLI Application:** Performs addition, subtraction, multiplication, division, power, square root, logarithm, and factorial.
- **Automated CI/CD:** Integrated Jenkins pipeline (`Jenkinsfile`) manages checkout, build, testing, packaging, and deployment entirely automatically upon GitHub push.
- **Ansible Deployment:** Ansible pulls the new image and spins up container changes automatically using `deploy.yml`. 
- **Dockerized & Highly Optimized:** Containerized using a multi-stage Dockerfile leveraging `jlink` on an Alpine environment.

---

## 🏆 Current Improvements & Architecture Optimizations

### 1. Massive Docker Image Size Reduction (74% Smaller)
Originally, a standard `eclipse-temurin:17-jre-alpine` image was 256MB. We optimized the build utilizing a **3-stage Dockerfile with `jlink`**:
1. **Maven Build Stage:** Caches dependencies and builds the project artifact.
2. **Custom JRE Stage:** Uses `eclipse-temurin:17-jdk-alpine` (musl-native) to run `jlink`. We generated a tailored, minimal Java Runtime Environment that strictly contains the required modules (`java.base`, `java.logging`).
3. **Runtime Stage:** Employs a barebones `alpine:3.19` image and copies the custom JRE over securely as a non-root user (`appuser`).

**Compression Results:**
- **Local Size:** 256 MB ➡️ **66.3 MB**
- **DockerHub (Compressed):** ~65 MB ➡️ **~22.2 MB** 

*Note: Initial attempts with jlink used glibc binaries copied to Alpine (musl), causing an `exec format error / exit 255` crash loop. This pipeline fixes this architectural mismatch by solely relying on musl-native base images.*

### 2. Jenkins Pipeline Enhancements
- Docker Builder deprecation warnings were resolved by letting Docker utilize its internal BuildKit seamlessly (`DOCKER_BUILDKIT`).
- Clean up dangling Docker images automated as a Post-Build action to preserve node disk space.
- The pipeline now executes in **under 45 seconds** with full Maven tests (37/37 passing).

---

## 🛠️ Tech Stack
- **Languages:** Java 17
- **Build Tool:** Maven 3.9
- **CI/CD:** Jenkins
- **Containerization:** Docker
- **Configuration Management:** Ansible
- **Code Repository:** GitHub

---

## 💻 Running the App Locally

Since it's an interactive CLI app, it must be run with the `-it` flag for interactive terminal support:

```bash
docker pull shikhar68/scientific-calculator:latest
docker run -it --name scientific-calculator shikhar68/scientific-calculator:latest
```

When started, it provides an interactive menu:
```text
=============================================
       SCIENTIFIC CALCULATOR
       SPE Mini Project
=============================================

--- Menu ---
1. Add
2. Subtract
3. Multiply
4. Divide
5. Power
6. Square Root
7. Logarithm
8. Factorial
9. Exit
Choose an option (1-9): 
```
