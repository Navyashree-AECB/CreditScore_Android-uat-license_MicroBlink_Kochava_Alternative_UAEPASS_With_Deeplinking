package com.aecb.data.api.models.securityquestions;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AnswersItem {

	@SerializedName("dot_authenticationquestionid")
	private String dotAuthenticationquestionid;

	@SerializedName("dot_answer")
	private String dotAnswer;

	public String getDotAuthenticationquestionid() {
		return dotAuthenticationquestionid;
	}

	public void setDotAuthenticationquestionid(String dotAuthenticationquestionid) {
		this.dotAuthenticationquestionid = dotAuthenticationquestionid;
	}

	public String getDotAnswer() {
		return dotAnswer;
	}

	public void setDotAnswer(String dotAnswer) {
		this.dotAnswer = dotAnswer;
	}

	@Override
	public String toString() {
		return
				"AnswersItem{" +
						"dot_authenticationquestionid = '" + dotAuthenticationquestionid + '\'' +
						",dot_answer = '" + dotAnswer + '\'' +
						"}";
	}
}