import docx
from docx.shared import Pt, Inches, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH

doc = docx.Document()

# --- Title ---
title = doc.add_heading('CS 816 - Software Production Engineering', 0)
title.alignment = WD_ALIGN_PARAGRAPH.CENTER

subtitle = doc.add_paragraph('Mini Project - Scientific Calculator with DevOps\nBy: [Your Roll Number]')
subtitle.alignment = WD_ALIGN_PARAGRAPH.CENTER

doc.add_page_break()

# --- Section 1 ---
doc.add_heading('1. What and Why of DevOps?', level=1)
p = doc.add_paragraph()
p.add_run('What is DevOps?\n').bold = True
p.add_run('DevOps is a set of cultural philosophies, practices, and tools that combines software development (Dev) and IT operations (Ops). It aims to shorten the systems development life cycle while delivering features, fixes, and updates frequently in close alignment with business objectives.\n\n')

p.add_run('Why DevOps?\n').bold = True
p.add_run('In the context of this scientific calculator project, manual compilation, testing, and deployment of the Java application are prone to human error and are highly time-consuming. Adopting DevOps methodologies automates these processes. A Continuous Integration and Continuous Deployment (CI/CD) pipeline ensures that every time a developer commits code to the repository, it is instantly built, rigorously tested against edge cases, smoothly containerized, and deployed to production servers without manual downtime.')

# --- Section 2 ---
doc.add_heading('2. Tools Used', level=1)
tools = doc.add_paragraph()
tools.add_run('- Source Control Management: ').bold = True
tools.add_run('GitHub\n')
tools.add_run('- Build Tool: ').bold = True
tools.add_run('Maven\n')
tools.add_run('- Testing Framework: ').bold = True
tools.add_run('JUnit\n')
tools.add_run('- Continuous Integration: ').bold = True
tools.add_run('Jenkins\n')
tools.add_run('- Containerization: ').bold = True
tools.add_run('Docker\n')
tools.add_run('- Configuration Management: ').bold = True
tools.add_run('Ansible\n')

doc.add_paragraph('\n[INSERT SCREENSHOT: High-level architectural diagram of the CI/CD pipeline or tools combined]').alignment = WD_ALIGN_PARAGRAPH.CENTER

doc.add_page_break()

# --- Section 3 ---
doc.add_heading('3. Jenkinsfile Pipeline Stages Explanation', level=1)
doc.add_paragraph('The automation backbone of this project is the Jenkins declarative pipeline (`Jenkinsfile`). Below is a stage-by-stage explanation of the pipeline operations orchestrating the build from source code to container deployment.')

stages = [
    {
        "title": "Stage 1: Checkout SCM",
        "description": "This initializes the pipeline by pulling the latest committed source code from the remote `main` branch on GitHub into the isolated Jenkins workspace. It guarantees the pipeline runs against the absolute latest code push.",
        "placeholder": "[INSERT SCREENSHOT: GitHub Push Webhook Triggering Jenkins / Jenkins Checkout Details]"
    },
    {
        "title": "Stage 2: Maven Build",
        "description": "Executes `mvn clean compile -q`. The `clean` command removes old `.class` files to guarantee a fresh build, and `compile` safely translates the `.java` files into `.class` bytecode. The quiet flag suppresses terminal bloat.",
        "placeholder": "[INSERT SCREENSHOT: Jenkins Console Output showing successful Maven Compilation]"
    },
    {
        "title": "Stage 3: JUnit Test",
        "description": "Executes `mvn test` to run 37 automated JUnit cases against the scientific arithmetic functions (Factorial, Logarithm, Power, etc.). The pipeline enforces strict integration; if even one test fails, the build halts immediately, protecting the deployment.",
        "placeholder": "[INSERT SCREENSHOT: Jenkins Console Output showing 'Tests run: 37, Failures: 0']"
    },
    {
        "title": "Stage 4: Package",
        "description": "After passing the tests, the pipeline executes `mvn package -DskipTests` to compress and package the verified `.class` files into a standalone, deployable Java Archive (`app.jar`).",
        "placeholder": "[INSERT SCREENSHOT: Jenkins packaging the JAR file successfully]"
    },
    {
        "title": "Stage 5: Docker Build",
        "description": "Jenkins connects to the Docker daemon to build a container image directly from the `Dockerfile`. To maintain extremely small image sizes, we employ a multi-stage Alpine build using `jlink` to strip unnecessary Java Runtime Environment (JRE) modules.",
        "placeholder": "[INSERT SCREENSHOT: Jenkins Terminal executing Docker Build stage and caching layers]"
    },
    {
        "title": "Stage 6: Docker Push (DockerHub)",
        "description": "Jenkins retrieves securely managed credentials to authenticate with DockerHub natively. It executes a `docker push` of the optimized image tagged `latest`, making the newly compiled application globally accessible. Afterwards, a logout maintains session security.",
        "placeholder": "[INSERT SCREENSHOT: DockerHub Repository page displaying the new pushed tag and size]"
    },
    {
        "title": "Stage 7: Ansible Deploy",
        "description": "Ansible handles Configuration Management. Jenkins executes `ansible-playbook -i inventory.ini deploy.yml`. Ansible dynamically connects to the target machine, shuts down legacy instances of the calculator container, and pulls the newest image to run securely.",
        "placeholder": "[INSERT SCREENSHOT: Ansible terminal playback reading 'ok' and 'changed' tasks]"
    }
]

for stage in stages:
    doc.add_heading(stage["title"], level=2)
    doc.add_paragraph(stage["description"])
    ph = doc.add_paragraph(stage["placeholder"])
    ph.alignment = WD_ALIGN_PARAGRAPH.CENTER
    ph.runs[0].font.color.rgb = RGBColor(128, 128, 128)
    ph.runs[0].font.italic = True
    doc.add_paragraph()  # Spacing

doc.add_page_break()

# --- Section 4 ---
doc.add_heading('4. Test Results & Observations', level=1)
doc.add_paragraph('Testing is a crucial hurdle defined inside the Jenkins pipeline. Instead of manual verification, the system runs programmatic unit tests seamlessly.')
doc.add_paragraph('- Total Tests Executed: 37')
doc.add_paragraph('- Failed Tests: 0')
doc.add_paragraph('- Error Rate: 0%')
doc.add_paragraph('- Time Elapsed: ~0.23 seconds')

doc.add_paragraph('\nObservations:', style='List Bullet')
doc.add_paragraph('The automated JUnit suite rigorously evaluates arithmetic edge cases—such as fractional division, negative exponents, and memory bounding on large factorials. Because Jenkins tests this sequentially on every commit, developers are instantly alerted if their mathematical logic causes regressions, ensuring 100% stable releases.', style='List Bullet')

doc.add_paragraph('\n[INSERT SCREENSHOT: Detailed Output of the Test Cases or Surefire Reports]').alignment = WD_ALIGN_PARAGRAPH.CENTER

doc.add_page_break()

# --- Section 5 ---
doc.add_heading('5. System Architecture & Resource Pruning', level=1)
doc.add_paragraph('The pipeline is equipped with critical resource management safeguards.')
doc.add_paragraph('1. Storage Management: At the end of every build lifecycle, regardless of structural success or failure, Jenkins executes a post-build environment variable running `docker image prune -f`. This safely purges all hanging, untagged images drastically saving disk space.')
doc.add_paragraph('2. Pipeline Execution Speed: By omitting recursive test executions (`-DskipTests` on packaging) and caching Docker layers locally via BuildKit integration, the complete CI/CD duration was optimized to run strictly under 45 seconds.')

doc.add_paragraph('\n[INSERT SCREENSHOT: Docker system df outputs showing reclaimed space, or Jenkins run time durations]').alignment = WD_ALIGN_PARAGRAPH.CENTER

doc.add_page_break()

# --- Section 6 ---
doc.add_heading('6. Application Output', level=1)
doc.add_paragraph('Because this is a command-line interface application executed securely inside a microservice container, it is provisioned completely detached from the local machine\'s host OS vulnerabilities. End users access it via standard interactive TTY hooks.')

doc.add_paragraph('\n[INSERT SCREENSHOT: Initial execution of `docker run -it shikhar68/scientific-calculator:latest` showing the calculator menu UI]').alignment = WD_ALIGN_PARAGRAPH.CENTER

doc.add_paragraph('\n[INSERT SCREENSHOT: Example of Mathematical operations returning accurate answers inside the terminal (e.g., Square root of 16 = 4.0, or factorial routines)]').alignment = WD_ALIGN_PARAGRAPH.CENTER

# --- Section 7 ---
doc.add_heading('7. Repository Links', level=1)
doc.add_paragraph('- GitHub Source Code Repository: [Insert Link Here]')
doc.add_paragraph('- DockerHub Container Registry: [Insert Link Here]')

doc.save('/home/shikhar/Sem 2/SPE/Mini Project/Scientific-Calculator-with-DevOps/Scientific_Calculator_Mini_Project_Report.docx')
print("Detailed DOCX report generated successfully.")
