package com.example.android.heydj.SpotifyClasses;

import java.net.URL;

public class JSONResultsList
{
    private static String song_title;
    private static URL imageURL;

    public static String getSong()
    {
        return song_title;
    }

    public URL getImageURL()
    {
        return imageURL;
    }

    public JSONResultsList(String song_title, URL imageURL)
    {
        this.song_title = song_title;
        this.imageURL = imageURL;
    }

}
