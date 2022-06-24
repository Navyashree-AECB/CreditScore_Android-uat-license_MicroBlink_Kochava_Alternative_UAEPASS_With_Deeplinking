package com.aecb.data.api.models.securityquestions;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class QuestionType {

	@SerializedName("__formattedValues")
	private FormattedValues formattedValues;

	@SerializedName("dot_questionid")
	private String dotQuestionid;

	@SerializedName("dot_questiontype")
	private int dotQuestiontype;

	public FormattedValues getFormattedValues() {
		return formattedValues;
	}

	public void setFormattedValues(FormattedValues formattedValues) {
		this.formattedValues = formattedValues;
	}

	public String getDotQuestionid() {
		return dotQuestionid;
	}

	public void setDotQuestionid(String dotQuestionid) {
		this.dotQuestionid = dotQuestionid;
	}

	public int getDotQuestiontype() {
		return dotQuestiontype;
	}

	public void setDotQuestiontype(int dotQuestiontype) {
		this.dotQuestiontype = dotQuestiontype;
	}

	@Override
	public String toString() {
		return
				"QuestionType{" +
						"__formattedValues = '" + formattedValues + '\'' +
						",dot_questionid = '" + dotQuestionid + '\'' +
						",dot_questiontype = '" + dotQuestiontype + '\'' +
						"}";
	}
}