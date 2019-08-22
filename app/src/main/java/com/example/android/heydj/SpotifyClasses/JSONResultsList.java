package com.example.android.heydj.SpotifyClasses;

import java.net.URL;

public class JSONResultsList
{
    private static String song_title;

    public static String getSong()
    {
        return song_title;
    }

    public JSONResultsList(String song_title)
    {
        this.song_title = song_title;
    }

}
