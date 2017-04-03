package com.example.Lyrics_API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class API_Lyrics
{
    private final String endpoint;
    private final String apikey_q;
    private final String track_q;
    private final String artist_q;
    private final String[] jsonpath;
    private String szAPIkey;
    public API_Lyrics(String endpoint, String apikey_q, String track_q, String artist_q, String[] jsonpath)
    {
        this.endpoint = endpoint;
        this.apikey_q = apikey_q;
        this.track_q = track_q;
        this.artist_q = artist_q;
        this.jsonpath = jsonpath;
        szAPIkey = null;
    }
    public String getEndpoint()  {return endpoint;}
    public String getQAPIKey()  {return apikey_q;}
    public String getQTrack()  {return track_q;}
    public String getQArtist()  {return artist_q;}
    public void setAPIkey(String szAPIkey)   {this.szAPIkey = szAPIkey;}
    public String getQURL(String artist, String track) throws UnsupportedEncodingException
    {
        return "http://" + this.getEndpoint() + "?" +
               (szAPIkey != null ? (this.getQAPIKey() + "=" + this.szAPIkey + "&") : "") +
               this.getQArtist() + "=" + URLEncoder.encode(artist, "UTF-8") + "&" +
               this.getQTrack() + "=" + URLEncoder.encode(track, "UTF-8");
    }
    public String getQURL(String track) throws UnsupportedEncodingException
    {
        return "http://" + this.getEndpoint() + "?" +
               (szAPIkey != null ? (this.getQAPIKey() + "=" + this.szAPIkey + "&") : "") +
               this.getQTrack() + "=" + URLEncoder.encode(track, "UTF-8");
    }
    public String ReadLyrics(String szURL) throws MalformedURLException, IOException
    {
        BufferedReader reader = null;
        try
        {
            URL url = new URL(szURL);
            reader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            String szJSON = buffer.toString();
            JsonElement y = new JsonParser().parse(szJSON).getAsJsonObject();
            int cur = 0;
            String ret = null;
            while(cur < jsonpath.length)
            {
                if(!((JsonObject) y).has(jsonpath[cur]))
                    break;
                y = ((JsonObject) y).get(jsonpath[cur]);
                if(y.isJsonNull())
                    break;
                if(y.isJsonArray())
                    y = ((JsonArray) y).get(0);
                else if(y.isJsonPrimitive())
                {
                    if(cur != jsonpath.length - 1)
                        break;
                    ret = y.toString().replaceAll("\\\\n", "\n").replaceAll("^\"|\"$", "");
                }
                System.out.println(jsonpath[cur]);
                cur++;
            }
            return ret;
        }
        catch(JsonSyntaxException | IOException e)
        {
            return null;
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
    }
}
