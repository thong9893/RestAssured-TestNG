# REST-assured Jira API Tests

This project is an automated API test suite (Java + Maven) using `rest-assured` and `TestNG` to perform CRUD tests for Jira issues on Jira Cloud.

Reference link : https://developer.atlassian.com/cloud/jira/platform/rest/v3/api-group-issues/#api-rest-api-3-issue-issueidorkey-get

Used API for this demo project : 
+ Get project &nbsp; -  &nbsp;   GET   /rest/api/3/project/{projectIdOrKey} 
+ Get issue &nbsp; -  &nbsp;    GET   /rest/api/3/issue/{issueIdOrKey}   
+ Create issue &nbsp; -  &nbsp; POST  /rest/api/3/issue                    
+ Update issue &nbsp; -  &nbsp;  POST  /rest/api/3/issue/{issueIdOrKey}/transitions  
+ Delete issue &nbsp; -  &nbsp;  DEL    /rest/api/3/issue/{issueIdOrKey}  
    

## Prerequisites
- Java 17 (JDK) — check with `java --version`.
- Maven (>= 3.6) — check with `mvn -v`.

## Key Structure
- `pom.xml` — Maven configuration and dependencies.
- `src/test/java/api_flow` — business flows (e.g. `IssueFlow`).
- `src/test/java/builder` — builders that construct JSON request payloads.
- `src/test/java/model` — models and configuration (e.g. `ENV`).
- `src/test/java/test` — test classes (`JiraIssueCRUD`, `BaseTest`).
- `src/test/java/utils` — helper utilities (e.g. `AuthenticationHandler`).
- `allure-results/` — Allure results directory (generated after tests if Allure is configured).

## Credentials / Configuration
Edit the credentials in `src/test/java/model/ENV.java` and set `email` and `token` (Jira Cloud API token). The `AuthenticationHandler` uses these values to create the Base64-encoded authentication header.

## Run Tests (local)
Open a terminal in the project folder  and run:

```bash
mvn -v
mvn test
```

TestNG reports will be available under `target/surefire-reports/`. If Allure is used, results are generated in `allure-results/`.

## Generate and View Allure Report (optional)
1) Install Allure CLI following the official instructions.
2) After `mvn test` produces `allure-results/`, run:

```bash
allure serve allure-results


