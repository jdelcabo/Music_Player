
package jdc.projects.personal.musicplayer.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import jdc.projects.personal.musicplayer.MainActivity;
import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.enumeration.CardType;
import jdc.projects.personal.musicplayer.model.Album;
import jdc.projects.personal.musicplayer.model.AlbumAdapter;
import jdc.projects.personal.musicplayer.model.AudioPlayer;
import jdc.projects.personal.musicplayer.model.Persistence;
import jdc.projects.personal.musicplayer.model.Song;
import jdc.projects.personal.musicplayer.model.SongAdapter;

public class FavouritesFragment extends Fragment {
    // Attributes
    private ArrayList<Song> songs;
    private ArrayList<Album> albums;
    private RecyclerView songsListRecyclerView, albumsListRecyclerView;
    private SongAdapter songAdapter;
    private AlbumAdapter albumAdapter;
    private AudioPlayer audioPlayer;
    private Persistence persistence;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    // This displays directly the layout of the fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favorites, container, false);

        // Initialize the RecyclerView for songs
        LinearLayoutManager songsLayoutManager = new LinearLayoutManager(getActivity());
        songsListRecyclerView = view.findViewById(R.id.SongsContainerBackground);
        songsListRecyclerView.setLayoutManager(songsLayoutManager);
        songsListRecyclerView.setAdapter(songAdapter);

        // Initialize the RecyclerView for albums
        LinearLayoutManager albumsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        albumsListRecyclerView = view.findViewById(R.id.favourite_albums_list);
        albumsListRecyclerView.setLayoutManager(albumsLayoutManager);
        albumsListRecyclerView.setAdapter(albumAdapter);

        // Initialize the audio player
        audioPlayer = AudioPlayer.getInstanceAndAssignUIElements(
                view.findViewById(R.id.seekBar),
                view.findViewById(R.id.textStartTime),
                view.findViewById(R.id.textEndTime),
                view.findViewById(R.id.playPauseButton),
                view.findViewById(R.id.replayButton),
                view.findViewById(R.id.musicPlayerBar)
        );

        return view;
    }

    // Creation of the fragment before displaying it (use as our constructor)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize lists of songs and albums
        songs = new ArrayList<>();
        albums = new ArrayList<>();

        // Initialize the persistence
        persistence = Persistence.getInstance();

        // Initialization of the adapters
        songAdapter = new SongAdapter(songs, getActivity(), R.layout.partial_song_skeleton, CardType.PARTIAL_SONG, this);
        albumAdapter = new AlbumAdapter(albums, getActivity(), R.layout.complete_album_placeholder, CardType.COMPLETE_ALBUM, this);

        // Get stored favourite songs
        retrieveFavouriteSongs();

        // Get stored favourite albums
        retrieveFavouriteAlbums();
    }

    private void retrieveFavouriteSongs(){
        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.FAVOURITE_SONGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Firstly retrieve the list of songs from SharedPreferences
        String json = sharedPreferences.getString(MainActivity.FAVOURITE_SONGS, null);

        ArrayList<Song> songList = new ArrayList<>();

        // Depending on having previous songs stored or being the first one, create the list or simply add the new item
        if (json != null) {
            // Convert JSON String to ArrayList of Song objects
            songList = new Gson().fromJson(json, new TypeToken<ArrayList<Song>>() {}.getType());
        }

        for (Song song : songList) {
            // Add the current song to the list
            songs.add(song);
            // Notify the adapter about the new song
            songAdapter.notifyItemInserted(songs.size() - 1);
        }
    }

    private void retrieveFavouriteAlbums(){
        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.FAVOURITE_ALBUMS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Firstly retrieve the list of songs from SharedPreferences
        String json = sharedPreferences.getString(MainActivity.FAVOURITE_ALBUMS, null);

        ArrayList<Album> albumList = new ArrayList<>();

        // Depending on having previous songs stored or being the first one, create the list or simply add the new item
        if (json != null) {
            // Convert JSON String to ArrayList of Song objects
            albumList = new Gson().fromJson(json, new TypeToken<ArrayList<Album>>() {}.getType());
        }

        for (Album album : albumList) {
            // Add the current song to the list
            albums.add(album);
            // Notify the adapter about the new song
            songAdapter.notifyItemInserted(albums.size() - 1);
        }
    }
}

