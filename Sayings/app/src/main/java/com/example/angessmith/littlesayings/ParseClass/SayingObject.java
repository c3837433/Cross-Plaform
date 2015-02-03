package com.example.angessmith.littlesayings.ParseClass;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

 // Created by AngeSSmith on 2/2/15.


@ParseClassName("Saying")
public class SayingObject  extends ParseObject{

    // each saying will have a child, date, and saying
    public void setChild(UserChild child) {
        put("Child", child);
    }

    public UserChild getChildName() {
        return (UserChild) get("Child");
    }

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
}
