package com.rnirest.rnirestapp.namesearch.config;

import com.github.openjson.JSONObject;

public class CustomDocScoreBuilder {

    JSONObject jsonObject;
    
    public CustomDocScoreBuilder(String name, String dob, String nameWeight, String dobWeight){
        JSONObject object = new JSONObject();
        JSONObject fieldsObject = new JSONObject();
        JSONObject nameObjectInternal = new JSONObject();
        JSONObject dobObjectInternal = new JSONObject();

        nameObjectInternal.put("query_value", name);
        nameObjectInternal.put("weight", nameWeight);

        dobObjectInternal.put("query_value", name);
        dobObjectInternal.put("weight", nameWeight);

        fieldsObject.put("rni_name", nameObjectInternal);
        fieldsObject.put("rni_dob", dobObjectInternal);

        object.put("fields", fieldsObject);
        this.jsonObject = object;
    }

    public JSONObject getObject(){
        return this.jsonObject;
    }

    public String toString(){
        return this.jsonObject.toString();
    }
    
}
