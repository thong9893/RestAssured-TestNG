package test;

import api_flow.IssueFlow;
import org.testng.annotations.Test;

public class JiraIssueCRUD extends BaseTest {
    @Test
    public void testE2EFlow() {
        IssueFlow issueFlow = new IssueFlow(request,baseUri,projectKey,"task");
        issueFlow.createIssue();
        issueFlow.verifyIssueDetails();
        issueFlow.updateIssue("Done");
//        issueFlow.verifyIssueDetails();
        issueFlow.deleteIssue();
    }
    @Test
    public void createIssue() {
        IssueFlow issueFlow = new IssueFlow(request,baseUri,projectKey,"task");
        issueFlow.createIssue();
        issueFlow.verifyIssueDetails();
    }
    @Test
    public void updateIssue() {
        IssueFlow issueFlow = new IssueFlow(request,baseUri,projectKey,"task");
        issueFlow.createIssue();
        issueFlow.updateIssue("Done");
        issueFlow.verifyIssueDetails();
    }
    @Test
    public void deleteIssue() {
        IssueFlow issueFlow = new IssueFlow(request,baseUri,projectKey,"task");
        issueFlow.createIssue();
        issueFlow.deleteIssue();
    }

    }
