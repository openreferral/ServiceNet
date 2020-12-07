---
name: Bug report
about: Create a report to help us improve
title: ''
labels: bug
assignees: ''

---

**Describe the bug**
A clear and concise description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**Expected behavior**
A clear and concise description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Desktop (please complete the following information):**
 - OS: [e.g. iOS]
 - Browser [e.g. chrome, safari]
 - Version [e.g. 22]

**Smartphone (please complete the following information):**
 - Device: [e.g. iPhone6]
 - OS: [e.g. iOS8.1]
 - Browser [e.g. stock browser, safari]
 - Version [e.g. 22]

**Additional context**
Add any other context about the problem here.

DoD:
- [ ] Dev: The functionality described in the story was implemented (or bug fixed) according to the provided acceptance criteria or reproduction steps.

- [ ] Dev: Automated unit tests were added for the core business features at the minimum.

- [ ] Review process: The code built clean (at the minimum it compiled, tests and static code analysis passed) on the TravisCI server.

- [ ] Review process: All code changes, additions, and removals went through code review, conducted by at least one person with technical expertise, who wasn’t directly involved in making the code changes; the code review feedback was incorporated or discussed with the reviewer.

- [ ] Review process: The code was audited with CodeQL to test and discover vulnerabilities across the  codebase.

- [ ] Review process: The Docker images for each service were built - code changes can’t break up the Docker images build process.

- [ ] Review process: The changes were deployed to the testing/UAT environment.

- [ ] QA: The changes were tested manually on a test environment that best replicates the production environment - the summary of the conducted tests is attached to the ticket (e.g. as a comment or as a link).

- [ ] QA: The changes did not introduce known, critical regressions.
