package com.aecb.data.api.models.securityquestions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SecurityQuestionsSubmitRequest {

	@SerializedName("questionRound")
	private int questionRound;

	@SerializedName("answers")
	private List<AnswersItem> answers;

	@SerializedName("portalUserId")
	private String portalUserId;

	@SerializedName("individualId")
	private String individualId;

	@SerializedName("applicationId")
	private String applicationId;

	public int getQuestionRound() {
		return questionRound;
	}

	public void setQuestionRound(int questionRound) {
		this.questionRound = questionRound;
	}

	public List<AnswersItem> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswersItem> answers) {
		this.answers = answers;
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
				"SecurityQuestionsSubmitRequest{" +
						"questionRound = '" + questionRound + '\'' +
						",answers = '" + answers + '\'' +
						",portalUserId = '" + portalUserId + '\'' +
						",individualId = '" + individualId + '\'' +
						",applicationId = '" + applicationId + '\'' +
						"}";
	}
}