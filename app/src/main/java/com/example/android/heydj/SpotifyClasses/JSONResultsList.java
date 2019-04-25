package com.example.android.heydj.SpotifyClasses;

public class JSONResultsList
{
    private static String song_title;
//    private static String artist_title;

    public static String getSong()
    {
        return song_title;
    }

//    public static String getArtist()
//    {
//        return artist_title;
//    }

    public JSONResultsList(String song_title, String artist_title)
    {
        this.song_title = song_title;
//        this.artist_title = artist_title;
    }

}
