package jdc.projects.personal.musicplayer.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.MainActivity;
import jdc.projects.personal.musicplayer.callback.SearchCallback;
import jdc.projects.personal.musicplayer.enumeration.CardType;
import jdc.projects.personal.musicplayer.enumeration.Range;
import jdc.projects.personal.musicplayer.model.Album;
import jdc.projects.personal.musicplayer.model.AudioPlayer;
import jdc.projects.personal.musicplayer.model.Persistence;
import jdc.projects.personal.musicplayer.model.Song;
import jdc.projects.personal.musicplayer.model.SongAdapter;

public class AlbumDetailsFragment extends Fragment implements SearchCallback {
    // Constants
    private static final String ARG_ALBUM_INFO = "ALBUM_INFO";
    // Views
    private ImageView albumCover;
    private RecyclerView songsRecyclerView;
    private TextView albumTitle, albumArtist;
    private ImageButton favouriteButton;
    private ImageButton backButton;

    // Attributes
    private SongAdapter songAdapter;
    private ArrayList<Song> songs;
    private Persistence persistence;
    private Album currentAlbum;
    private AudioPlayer audioPlayer;

    public AlbumDetailsFragment() {
        // Required empty public constructor
    }

    public static AlbumDetailsFragment newInstance(String songInfo) {
        AlbumDetailsFragment fragment = new AlbumDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ALBUM_INFO, songInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_details, container, false);

        // Initialize the audio player
        audioPlayer = AudioPlayer.getInstanceAndAssignUIElements(
                view.findViewById(R.id.seekBar),
                view.findViewById(R.id.textStartTime),
                view.findViewById(R.id.textEndTime),
                view.findViewById(R.id.playPauseButton),
                view.findViewById(R.id.replayButton),
                view.findViewById(R.id.musicPlayerBar)
        );

        // Initialize the RecyclerView for songs
        LinearLayoutManager songsLayoutManager = new LinearLayoutManager(getActivity());
        songsRecyclerView = view.findViewById(R.id.TracksRecyclerView);
        songsRecyclerView.setLayoutManager(songsLayoutManager);
        songsRecyclerView.setAdapter(songAdapter);

        // Initialize the views
        albumTitle = view.findViewById(R.id.album_details_title);
        albumArtist = view.findViewById(R.id.album_details_artist);
        albumCover = view.findViewById(R.id.album_details_cover);
        favouriteButton = view.findViewById(R.id.album_details_favourite_btn);
        backButton = view.findViewById(R.id.back_button);

        // Retrieving the data passed in order to display the details of the song
        if (getArguments() != null) {
            currentAlbum = new Album(getArguments().getString(ARG_ALBUM_INFO));
            Picasso.get().load(currentAlbum.getArtworkUrl100()).into(albumCover);
        }

        // Set the title and artist of the album
        if (currentAlbum != null) {
            albumTitle.setText(currentAlbum.getCollectionName());
            albumArtist.setText(currentAlbum.getArtistName());
        }

        // Set favourite button listener
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeFavoriteAlbumInPreferences();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlayer.isPlaying()) {
                    audioPlayer.switchPlayPauseState();
                }
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        persistence.getSetOfSongsByAlbumId(getActivity(), this, currentAlbum.getCollectionId());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize list of songs
        songs = new ArrayList<>();

        // Initialize the persistence
        persistence = Persistence.getInstance();

        // Prepare the adapter
        songAdapter = new SongAdapter(songs, getActivity(), R.layout.partial_song_skeleton, CardType.PARTIAL_SONG, this);
    }


    private void storeFavoriteAlbumInPreferences() {
        boolean albumExists;

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.FAVOURITE_ALBUMS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Firstly retrieve the list of songs from SharedPreferences
        String json = sharedPreferences.getString(MainActivity.FAVOURITE_ALBUMS, null);

        ArrayList<Album> albumList = new ArrayList<>();

        // Depending on having previous songs stored or being the first one, create the list, simply add the new item or remove it
        if (json != null) {
            // Convert JSON String to ArrayList of Song objects
            albumList = new Gson().fromJson(json, new TypeToken<ArrayList<Album>>() {}.getType());

            // Check if the song is already in the list
            albumExists = false;
            for (Album album : albumList) {
                if (album.getCollectionName().equals(currentAlbum.getCollectionName())) {
                    albumExists = true;
                    break;
                }
            }
            if (!albumExists) {
                // Add the current song to the list
                albumList.add(currentAlbum);

                Toast.makeText(getActivity(), "Album added to favourites", Toast.LENGTH_SHORT).show();

            } else {
                // Remove the current song from the list
                albumList.remove(currentAlbum);
                Toast.makeText(getActivity(), "Album removed from favourites", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If no songs are stored, create a new list
            albumList.add(currentAlbum);
            Toast.makeText(getActivity(), "Album added to favourites", Toast.LENGTH_SHORT).show();
        }

        // Once retrieved and updated the list of songs, we need to store it again in SharedPreferences

        // Convert the list of songs to JSON String
        json = new Gson().toJson(albumList);

        // Save JSON string and apply changes
        editor.putString(MainActivity.FAVOURITE_ALBUMS, json);
        editor.apply();
    }

    @Override
    public void receiveSongs(ArrayList<Song> songs, Range range) {
        for (Song song : songs) {
            if (song != null) {
                if (song.getTrackName() != null) {
                    if (!song.getTrackName().isEmpty()) {
                        this.songs.add(song);
                        songAdapter.notifyItemInserted(this.songs.size() - 1);
                    }
                }
            }
        }
    }

    @Override
    public void receiveAlbums(ArrayList<Album> albums, Range range) {
        // Not required for this screen
    }
}