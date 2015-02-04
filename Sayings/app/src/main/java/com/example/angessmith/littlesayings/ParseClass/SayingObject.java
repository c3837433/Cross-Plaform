package com.example.angessmith.littlesayings.ParseClass;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

 // Created by AngeSSmith on 2/2/15.


@ParseClassName("Saying")
public class SayingObject  extends ParseObject{

    // each saying will have a child name, date, age, and saying

    public void setDate(Date date) {
        put("SayingDate", date);
    }

    public Date getSayingDate() {
        return getDate("SayingDate");
    }

    public void setSaying(String saying){
        put("ChildSaying", saying);
    }
    public String getSaying () {
        return getString("ChildSaying");
    }

    public void setChild (String childName){
        put("Name", childName);
    }

    public String getChild () {
        return getString("Name");
    }

    public void setAge (Integer childName){
        put("Age", childName);
    }

    public Integer getAge () {
        return getInt("Age");
    }

    // Run query for objects
    public static ParseQuery<SayingObject> getQuery() {
        return ParseQuery.getQuery(SayingObject.class);
    }



}
