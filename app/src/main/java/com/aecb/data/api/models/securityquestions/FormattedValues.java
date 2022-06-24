package com.aecb.data.api.models.securityquestions;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FormattedValues {

	@SerializedName("dot_questiontype")
	private String dotQuestiontype;

	public String getDotQuestiontype() {
		return dotQuestiontype;
	}

	public void setDotQuestiontype(String dotQuestiontype) {
		this.dotQuestiontype = dotQuestiontype;
	}

	@Override
	public String toString() {
		return
				"FormattedValues{" +
						"dot_questiontype = '" + dotQuestiontype + '\'' +
						"}";
	}
}