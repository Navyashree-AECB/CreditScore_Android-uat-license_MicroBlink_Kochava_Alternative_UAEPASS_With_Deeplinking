package com.aecb.data.api.models.securityquestions;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class QuestionsItem {

    @SerializedName("question")
    private String question;

    @SerializedName("questionAr")
    private String questionAr;

    @SerializedName("round")
    private int round;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("templateId")
    private String templateId;

    @SerializedName("questionStatus")
    private int questionStatus;

    @SerializedName("questionType")
    private QuestionType questionType;

    @SerializedName("questionNumber")
    private int questionNumber;

    @SerializedName("parentQuestionId")
    private Object parentQuestionId;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(int questionStatus) {
        this.questionStatus = questionStatus;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Object getParentQuestionId() {
        return parentQuestionId;
    }

    public void setParentQuestionId(Object parentQuestionId) {
        this.parentQuestionId = parentQuestionId;
    }

    public String getQuestionAr() {
        return questionAr;
    }

    public void setQuestionAr(String questionAr) {
        this.questionAr = questionAr;
    }

    @Override
    public String toString() {
        return
                "QuestionsItem{" +
                        "question = '" + question + '\'' +
                        ",round = '" + round + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",templateId = '" + templateId + '\'' +
                        ",questionStatus = '" + questionStatus + '\'' +
                        ",questionType = '" + questionType + '\'' +
                        ",questionNumber = '" + questionNumber + '\'' +
                        ",parentQuestionId = '" + parentQuestionId + '\'' +
                        "}";
    }
}