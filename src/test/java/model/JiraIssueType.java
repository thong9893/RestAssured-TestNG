package model;

import model.RequestCapability;
import utils.ProjectInfo;

public class JiraIssueType implements RequestCapability {
    public static void main(String[] args) {
        String baseURI = "";
        String projectKey = "";

        ProjectInfo projectInfo = new ProjectInfo(baseURI,projectKey);
        System.out.println(projectInfo.getIssueTypeId("Task"));

    }
}
