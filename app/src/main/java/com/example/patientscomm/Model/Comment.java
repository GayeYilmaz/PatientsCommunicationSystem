package com.example.patientscomm.Model;

public class Comment {
    private String id;
    private String userId;
    private String questionId;
    private String comment;

    public Comment() {
    }

    public Comment(String id, String userId,String questionId, String comment) {
        this.id = id;
        this.userId = userId;
        this.questionId=questionId;
        this.comment = comment;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
