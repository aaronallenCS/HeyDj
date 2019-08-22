package com.example.android.heydj;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class DjProfile
{
    String djName;


    public DjProfile(String djName)
    {
        this.djName = djName;
    }

    public void setDjName(String djName)
    {
        this.djName = djName;
    }

    public String getdjName()
    {
        return djName;
    }
}