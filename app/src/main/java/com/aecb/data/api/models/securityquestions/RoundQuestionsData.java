package com.aecb.data.api.models.securityquestions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RoundQuestionsData {

	@SerializedName("applicationStatus")
	private int applicationStatus;

	@SerializedName("questionRound")
	private int questionRound;

	@SerializedName("questions")
	private List<QuestionsItem> questions;

	@SerializedName("portalUserId")
	private String portalUserId;

	@SerializedName("individualId")
	private String individualId;

	@SerializedName("applicationId")
	private String applicationId;

	public int getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(int applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public int getQuestionRound() {
		return questionRound;
	}

	public void setQuestionRound(int questionRound) {
		this.questionRound = questionRound;
	}

	public List<QuestionsItem> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionsItem> questions) {
		this.questions = questions;
	}

	public String getPortalUserId() {
		return portalUserId;
	}

	public void setPortalUserId(String portalUserId) {
		this.portalUserId = portalUserId;
	}

	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public String toString() {
		return
				"Data{" +
						"applicationStatus = '" + applicationStatus + '\'' +
						",questionRound = '" + questionRound + '\'' +
						",questions = '" + questions + '\'' +
						",portalUserId = '" + portalUserId + '\'' +
						",individualId = '" + individualId + '\'' +
						",applicationId = '" + applicationId + '\'' +
						"}";
	}
}