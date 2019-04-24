package com.example.android.heydj;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class DjProfile
{
    String djName;
    String googleUserName;

    public DjProfile(String djName, String googleUserName)
    {
        this.djName = djName;
        this.googleUserName = googleUserName;
    }
    public DjProfile(){}

    public void setdjName(String djName)
    {
        this.djName = djName;
    }

    public String getdjName()
    {
        return djName;
    }

    public void setgoogleUserName(String googleUserName)
    {
        this.googleUserName = googleUserName;
    }
    public String getgoogleUserName()
    {
        return googleUserName;
    }
}