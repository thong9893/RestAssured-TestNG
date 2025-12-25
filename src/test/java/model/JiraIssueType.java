package model;

import model.RequestCapability;
import utils.ProjectInfo;

public class JiraIssueType implements RequestCapability {
    public static void main(String[] args) {
        String baseURI = "https://thongluminh.atlassian.net/";
        String projectKey = "RES";

        ProjectInfo projectInfo = new ProjectInfo(baseURI,projectKey);
        System.out.println(projectInfo.getIssueTypeId("Task"));

    }
}
