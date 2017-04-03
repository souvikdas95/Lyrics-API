package com.example.Sample;

import com.example.Lyrics_API.*;

public class Sample
{  
    public static void main(String[] args) throws Exception
    {
        API_Lyrics musixmatch = new API_Lyrics("api.musixmatch.com/ws/1.1/matcher.lyrics.get",
                                                "apikey",
                                                "q_track",
                                                "q_artist",
                                                new String[] {"message", "body", "lyrics", "lyrics_body"});
        String szKey = "<API Key>";
        musixmatch.setAPIkey(szKey);
        String url = musixmatch.getQURL("Demonic Resurrection", "A Tragedy Befallen");
        System.out.println(musixmatch.ReadLyrics(url));

    }
}
