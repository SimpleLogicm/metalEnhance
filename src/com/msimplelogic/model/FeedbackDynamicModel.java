package com.msimplelogic.model;

import java.util.List;

public class FeedbackDynamicModel {
    public String question_id;
    public String all_english_tv_channels;
    public String question_type;
    List<String> priceList;

    public FeedbackDynamicModel(String question_id,String all_english_tv_channels,String question_type, List<String> priceList) {
        this.question_id=question_id;
        this.all_english_tv_channels=all_english_tv_channels;
        this.question_type=question_type;
        this.priceList=priceList;
    }

    public String getAll_english_tv_channels() {
        return all_english_tv_channels;
    }

    public void setAll_english_tv_channels(String all_english_tv_channels) {
        this.all_english_tv_channels = all_english_tv_channels;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public List<String> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<String> priceList) {
        this.priceList = priceList;
    }



}
