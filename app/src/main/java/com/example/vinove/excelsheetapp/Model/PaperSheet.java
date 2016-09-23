package com.example.vinove.excelsheetapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class PaperSheet implements Serializable{


    private String questions;
    private String responseTypes;
    private String validResponses;
    private ArrayList<String> validRespList;


    public PaperSheet(){
        validRespList=new ArrayList<>();
    }
    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getResponseTypes() {
        return responseTypes;
    }

    public void setResponseTypes(String responseTypes) {
        this.responseTypes = responseTypes;
    }

    public String getValidResponses() {
        return validResponses;
    }

    public void setValidResponses(String validResponses) {
        this.validResponses = validResponses;
    }

    public ArrayList<String> getValidRespList() {
        return validRespList;
    }

    public void setValidRespList(String validResp) {
        if(!validResp.equals("")||validResp.equals(" "))
            this.validRespList.add(validResp);
    }

    public String toString(){

        return "questions: "+getQuestions()+" responseTypes: "+
                getResponseTypes()+" validResponses: "+getValidRespList();
    }
}
