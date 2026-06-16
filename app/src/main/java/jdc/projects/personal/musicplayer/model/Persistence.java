package jdc.projects.personal.musicplayer.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import jdc.projects.personal.musicplayer.callback.SearchCallback;
import jdc.projects.personal.musicplayer.dto.AlbumData;
import jdc.projects.personal.musicplayer.dto.SongData;
import jdc.projects.personal.musicplayer.enumeration.Range;

public class Persistence {
    // Attributes
    private static Persistence instance;
    // Constants
    // Useful pieces of URL:
    private final static String BASIS = "https://itunes.apple.com/search?term=";
    private final static String NUMBER_OF_SONGS = "&limit=15";
    private final static String GET_SONGS = "&media=music&entity=song" + NUMBER_OF_SONGS;
    private final static String GET_ALBUMS = "&media=music&entity=album" + NUMBER_OF_SONGS;
    private final static String LOOKUP_BASIS = "https://itunes.apple.com/lookup?id=";
    private final static String SONG_SPECIFICATION = "&entity=song";

    // Constants
    private static final String[] RANDOM_KEYWORDS_FOR_SEARCH = {
            "twenty one pilots", "imagine dragons", "the weeknd", "ariana grande", "melendi", "Taylor Swift", "robin schulz", "sting", "bruno mars", "ed sheeran", "maroon 5",
            "the script", "coldplay", "kings of leon", "the killers", "linkin park", "marshmello", "david guetta", "calvin harris", "avicii", "zedd", "major lazer",
            "clean bandit", "sia", "dua lipa", "halsey", "charlie puth", "shawn mendes", "justin bieber", "selena gomez", "khalid", "post malone", "lauv",
            "james arthur", "ed sheeran", "sam smith", "bebe rexha", "miley cyrus", "camila cabello", "billie eilish", "lizzo", "doja cat", "olivia rodrigo",
            "tove lo", "zara larsson", "jason derulo", "fifth harmony", "little mix", "one direction", "5 seconds of summer", "the vamps", "all time low", "paramore",
            "metallica", "green day", "blink-182", "foo fighters", "avenged sevenfold", "linkin park", "my chemical romance", "panic! at the disco", "fall out",
            "kiss", "the who", "the rolling stones", "led zeppelin", "queen", "pink floyd", "the doors", "jimi hendrix", "bob marley", "elton john",
    };
    public final static int MAXIMUM_NUMBER_OF_SONGS_API_CAN_RETURN = 200;
    public final static int MAXIMUM_NUMBER_OF_ALBUMS_API_CAN_RETURN = 200;

    // Constructor
    private Persistence() {
        // Private constructor to prevent instantiation
    }

    // Method to get the singleton instance
    public static Persistence getInstance() {
        if (instance == null) {
            instance = new Persistence();
        }
        return instance;
    }

    public void getRandomSetOfSongs(Context context, SearchCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String songsUrl = BASIS + RANDOM_KEYWORDS_FOR_SEARCH[getRandomNumberInRange(0, RANDOM_KEYWORDS_FOR_SEARCH.length-1)] + GET_SONGS;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, songsUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray songsJsonList = response.getJSONArray("results");
                            ArrayList<SongData> retrievedSongs = new ArrayList<>();

                            for (int i = 0; i < songsJsonList.length(); i++) {
                                JSONObject songObject = songsJsonList.getJSONObject(i);
                                SongData songJsonDto = new Gson().fromJson(songObject.toString(), SongData.class);
                                retrievedSongs.add(songJsonDto);
                            }

                            callback.receiveSongs(convertDataToSongs(retrievedSongs), Range.FIFTEEN);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Songs could not be retrieved...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // if its because of the 404 error from the iTunnes API, try again with a different random keyword
                        getRandomSetOfSongs(context, callback);
                        //Toast.makeText(context, "Songs could not be retrieved...", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void getSongsBySearch(Context context, SearchCallback callback, String searchInput){
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, BASIS + searchInput + GET_SONGS, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray songsJsonList = response.getJSONArray("results");
                            ArrayList<SongData> retrievedSongs = new ArrayList<>();

                            for (int i = 0; i < songsJsonList.length(); i++) {
                                JSONObject songObject = songsJsonList.getJSONObject(i);
                                SongData songJsonDto = new Gson().fromJson(songObject.toString(), SongData.class);
                                retrievedSongs.add(songJsonDto);
                            }

                            callback.receiveSongs(convertDataToSongs(retrievedSongs), Range.ALL);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Songs could not be retrieved...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Songs could not be retrieved...", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void getSetOfSongsByAlbumId(Context context, SearchCallback callback, long albumId){
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, LOOKUP_BASIS + albumId + SONG_SPECIFICATION, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray songsJsonList = response.getJSONArray("results");
                            ArrayList<SongData> retrievedSongs = new ArrayList<>();

                            for (int i = 0; i < songsJsonList.length(); i++) {
                                JSONObject songObject = songsJsonList.getJSONObject(i);
                                SongData songJsonDto = new Gson().fromJson(songObject.toString(), SongData.class);
                                retrievedSongs.add(songJsonDto);
                            }

                            callback.receiveSongs(convertDataToSongs(retrievedSongs), Range.ALL);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Songs could not be retrieved...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Songs could not be retrieved...", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void getRandomSetOfAlbums(Context context, SearchCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String albumsUrl = BASIS + RANDOM_KEYWORDS_FOR_SEARCH[getRandomNumberInRange(0, RANDOM_KEYWORDS_FOR_SEARCH.length-1)] + GET_ALBUMS;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, albumsUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray albumsJsonList = response.getJSONArray("results");
                            ArrayList<AlbumData> retrievedAlbums = new ArrayList<>();

                            for (int i = 0; i < albumsJsonList.length(); i++) {
                                JSONObject albumObject = albumsJsonList.getJSONObject(i);
                                AlbumData albumJsonDto = new Gson().fromJson(albumObject.toString(), AlbumData.class);
                                retrievedAlbums.add(albumJsonDto);
                            }

                            callback.receiveAlbums(convertDataToAlbums(retrievedAlbums), Range.FIFTEEN);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Albums could not be retrieved...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // if its because of the 404 error from the iTunnes API, try again with a different random keyword
                        getRandomSetOfAlbums(context, callback);
                        // Toast.makeText(context, "Albums could not be retrieved...", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void getAlbumsBySearch(Context context, SearchCallback callback, String searchInput){
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, BASIS + searchInput + GET_ALBUMS, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray albumsJsonList = response.getJSONArray("results");
                            ArrayList<AlbumData> retrievedAlbums = new ArrayList<>();

                            for (int i = 0; i < albumsJsonList.length(); i++) {
                                JSONObject albumObject = albumsJsonList.getJSONObject(i);
                                AlbumData albumJsonDto = new Gson().fromJson(albumObject.toString(), AlbumData.class);
                                retrievedAlbums.add(albumJsonDto);
                            }

                            callback.receiveAlbums(convertDataToAlbums(retrievedAlbums), Range.ALL);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Albums could not be retrieved...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Albums could not be retrieved...", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    @NonNull
    private ArrayList<Song> convertDataToSongs(ArrayList<SongData> songs) {
        ArrayList<Song> convertedSongs = new ArrayList<>();
        for (SongData songData : songs) {
            convertedSongs.add(new Song(songData));
        }
        return convertedSongs;
    }

    @NonNull
    private ArrayList<Album> convertDataToAlbums(ArrayList<AlbumData> albums) {
        ArrayList<Album> convertedAlbums = new ArrayList<>();
        for (AlbumData albumData : albums) {
            convertedAlbums.add(new Album(albumData));
        }
        return convertedAlbums;
    }

    public void downloadAudioFile(Context context, String fileUrl, String fileName, boolean automaticallyPlayWhenReady) {
        new Thread(() -> {
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    /*Log.e("Download", "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());*/
                    return;
                }

                File storageDir = new File(context.getExternalFilesDir(null), "previews");
                if (!storageDir.exists()) storageDir.mkdirs();

                File outputFile = new File(storageDir, fileName + ".m4a");

                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(outputFile);

                byte[] data = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                //Log.d("Download", "File saved to " + outputFile.getAbsolutePath());

                // Now switch to main thread to play the audio
                new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        AudioPlayer.getInstance().loadNewAudio(outputFile.getAbsolutePath());
                        if (automaticallyPlayWhenReady) {
                            AudioPlayer.getInstance().switchPlayPauseState();
                        }
                    } catch (IOException e) {
                        //Log.e("AudioPlayer", "Failed to load audio", e);
                    }
                });

            } catch (Exception e) {
             //   Log.e("Download", "Error during download", e);
            }
        }).start();
    }

    private int getRandomNumberInRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
