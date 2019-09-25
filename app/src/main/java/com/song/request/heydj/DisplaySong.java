package com.song.request.heydj;

public class DisplaySong
{
    String songName;
    int songCount;



    public DisplaySong(String songName, int songCount){
        this.songName = songName;
        this.songCount = songCount;
    }

    public DisplaySong(){}

    public void setSong(String songName)
    {
        this.songName = songName;
    }

    public String getSong()
    {
        return songName;
    }

    public void setSongCount(int songCount)
    {
        this.songCount = songCount;
    }

    public int getSongCount()
    {
        return songCount;
    }

}
