package test;

import model.RequestCapability;
import utils.ProjectInfo;

public class JiraIssueType implements RequestCapability {
    public static void main(String[] args) {
        String baseURI = "https://thongluminh.atlassian.net/";
        String projectKey = "RA";

        ProjectInfo projectInfo = new ProjectInfo(baseURI,projectKey);
        projectInfo.getIssueTypeId("Task");

    }
}
