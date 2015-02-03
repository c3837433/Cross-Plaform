package com.example.angessmith.littlesayings.ParseClass;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;


 // Created by AngeSSmith on 2/2/15.

@ParseClassName("UserChild")
public class UserChild extends ParseObject {

    // each child will have 2 properties: name, birthday, and their parent
    public void setChildName(String name) {
        put("ChildName", name);
    }

    public String getChildName() {
        return getString("ChildName");
    }

    public void setChildBirthday(Date childBirthday) {
        put("ChildBirthday", childBirthday);
    }

    public Date getChildBirthday() {
        return getDate("ChildBirthday");
    }

    public void setParent(ParseUser user){
        put("Parent", user);
    }


}
