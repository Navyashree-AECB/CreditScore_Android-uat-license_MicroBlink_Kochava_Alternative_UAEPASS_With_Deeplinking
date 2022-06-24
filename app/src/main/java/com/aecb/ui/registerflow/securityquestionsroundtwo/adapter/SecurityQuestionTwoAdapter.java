package com.aecb.ui.registerflow.securityquestionsroundtwo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.data.api.models.securityquestions.QuestionsItem;
import com.aecb.databinding.QuestionsCustomLayoutBinding;
import com.aecb.ui.registerflow.securityquestionsroundtwo.view.SecurityQuestionsRoundTwoActivity;
import com.aecb.util.ValidationUtil;
import com.tumblr.remember.Remember;

import java.util.ArrayList;
import java.util.List;

import static com.aecb.AppConstants.QuestionTypes.A_MINUS;
import static com.aecb.AppConstants.QuestionTypes.A_ONLY;
import static com.aecb.AppConstants.QuestionTypes.A_PLUS;
import static com.aecb.AppConstants.QuestionTypes.B_MINUS;
import static com.aecb.AppConstants.QuestionTypes.B_ONLY;
import static com.aecb.AppConstants.QuestionTypes.B_PLUS;
import static com.aecb.data.preference.PrefHelperImpl.KEY_CURRENT_LANGUAGE;

public class SecurityQuestionTwoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<QuestionsItem> questionsList = new ArrayList<>();
    private Context context;

    public SecurityQuestionTwoAdapter(Context context, List<QuestionsItem> questionsList) {
        this.questionsList = questionsList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.questions_custom_layout, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final QuestionsItem questionsItem = questionsList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (questionsItem.getQuestionType().getFormattedValues().getDotQuestiontype().equals(A_PLUS) ||
                questionsItem.getQuestionType().getFormattedValues().getDotQuestiontype().equals(A_MINUS) ||
                questionsItem.getQuestionType().getFormattedValues().getDotQuestiontype().equals(A_ONLY)) {
            itemViewHolder.binding.edtAnswer.setVisibility(View.GONE);
            itemViewHolder.binding.rgQuestions.setVisibility(View.VISIBLE);
        } else if (questionsItem.getQuestionType().getFormattedValues().getDotQuestiontype().equals(B_PLUS) ||
                questionsItem.getQuestionType().getFormattedValues().getDotQuestiontype().equals(B_MINUS) ||
                questionsItem.getQuestionType().getFormattedValues().getDotQuestiontype().equals(B_ONLY)) {
            itemViewHolder.binding.edtAnswer.setVisibility(View.VISIBLE);
            itemViewHolder.binding.rgQuestions.setVisibility(View.GONE);
        }
        String mAppLang = Remember.getString(KEY_CURRENT_LANGUAGE, AppConstants.AppLanguage.ISO_CODE_ENG);
        if (!ValidationUtil.isNullOrEmpty(mAppLang)) {
            if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                itemViewHolder.binding.tvQuestion.setText(questionsItem.getQuestion());
            } else if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                itemViewHolder.binding.tvQuestion.setText(questionsItem.getQuestionAr());
            }
        } else {
            itemViewHolder.binding.tvQuestion.setText(questionsItem.getQuestion());
        }
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        QuestionsCustomLayoutBinding binding;


        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}