package model;

public class IssueFields {
    private Fields fields;
    public IssueFields(Fields fields) {
        this.fields = fields;
    }
    public Fields getFields() {
        return fields;
    }
    public void setFields(Fields fields) {
        this.fields = fields;
    }
    public static class Fields{
        private Project project;
        private IssueType issuetype;
        private String summary;
        public Fields(Project project, IssueType issueType,String summary) {
            this.project = project;
            this.issuetype = issueType;
            this.summary = summary;
        }
        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }

        public IssueType getIssueType() {
            return issuetype;
        }

        public void setIssueType(IssueType issueType) {
            this.issuetype = issueType;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }

    public static class Project{
        private String key;
        public Project(String key) {
            this.key = key;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
    }
    public static class  IssueType{
        private String id;
        public IssueType(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
    }
}
