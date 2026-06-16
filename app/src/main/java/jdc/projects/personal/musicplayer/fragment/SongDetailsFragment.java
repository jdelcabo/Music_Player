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
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import jdc.projects.personal.musicplayer.MainActivity;
import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.model.AudioPlayer;
import jdc.projects.personal.musicplayer.model.Persistence;
import jdc.projects.personal.musicplayer.model.Song;

public class SongDetailsFragment extends Fragment {
    // Constants
    private static final String ARG_SONG_INFO = "SONG_INFO";
    // Views
    private ImageView songCover, favouriteButton;
    private TextView songTitle, releaseDate, songArtist, songAlbum, songGenre, songPrice;
    // Attributes
    private Song currentSong;
    private AudioPlayer audioPlayer;
    private Persistence persistence;
    private ImageButton backButton;


    public SongDetailsFragment() {
        // Required empty public constructor
    }

    public static SongDetailsFragment newInstance(String songInfo) {
        SongDetailsFragment fragment = new SongDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SONG_INFO, songInfo);
        fragment.setArguments(args);
        return fragment;
    }

    // This displays directly the layout of the fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_details, container, false);

        // Initialize the views
        songCover = view.findViewById(R.id.song_cover);
        favouriteButton = view.findViewById(R.id.favourite_button);
        songTitle = view.findViewById(R.id.song_title);
        releaseDate = view.findViewById(R.id.song_release_date);
        songArtist = view.findViewById(R.id.song_artist);
        songAlbum = view.findViewById(R.id.song_album);
        songGenre = view.findViewById(R.id.song_genres);
        songPrice = view.findViewById(R.id.song_price);
        backButton = view.findViewById(R.id.back_button);

        // Retrieving the data passed in order to display the details of the song
        if (getArguments() != null) {
            currentSong = new Song(getArguments().getString(ARG_SONG_INFO));
            // Set the values of the views
            songTitle.setText(currentSong.getTrackName());
            releaseDate.setText(currentSong.getFormattedDate());
            songArtist.setText(currentSong.getArtistName());
            songAlbum.setText(currentSong.getCollectionName());
            songGenre.setText(currentSong.getPrimaryGenreName());
            String price = currentSong.getTrackPrice() + " €";
            songPrice.setText(price);
            // Load image using Picasso with the URL from the game object
            Picasso.get().load(currentSong.getArtworkUrl100()).into(songCover);
            // Set the listener for the favourite button
            favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storeFavoriteSongInPreferences();
                }
            });
        }

        // Initialize the audio player
        audioPlayer = AudioPlayer.getInstanceAndAssignUIElements(
                view.findViewById(R.id.seekBar),
                view.findViewById(R.id.textStartTime),
                view.findViewById(R.id.textEndTime),
                view.findViewById(R.id.playPauseButton),
                view.findViewById(R.id.replayButton),
                view.findViewById(R.id.musicPlayerBar)
        );

        //Initialize back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlayer.isPlaying()) {
                    audioPlayer.switchPlayPauseState();
                }
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        // Initialize the persistence
        persistence = Persistence.getInstance();

        // Download the audio file
        persistence.downloadAudioFile(getActivity(), currentSong.getPreviewUrl(), currentSong.getTrackName(), false);

        return view;

    }

    // Creation of the fragment before displaying it (use as our constructor)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void storeFavoriteSongInPreferences() {
        boolean songExists;

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.FAVOURITE_SONGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Firstly retrieve the list of songs from SharedPreferences
        String json = sharedPreferences.getString(MainActivity.FAVOURITE_SONGS, null);

        ArrayList<Song> songList = new ArrayList<>();

        // Depending on having previous songs stored or being the first one, create the list, simply add the new item or remove it
        if (json != null) {
            // Convert JSON String to ArrayList of Song objects
            songList = new Gson().fromJson(json, new TypeToken<ArrayList<Song>>() {}.getType());

            // Check if the song is already in the list
            songExists = false;
            for (Song song : songList) {
                if (song.getTrackName().equals(currentSong.getTrackName())) {
                    songExists = true;
                    break;
                }
            }
            if (!songExists) {
                // Add the current song to the list
                songList.add(currentSong);

                Toast.makeText(getActivity(), "Song added to favourites", Toast.LENGTH_SHORT).show();

            } else {
                // Remove the current song from the list
                songList.remove(currentSong);
                Toast.makeText(getActivity(), "Song removed from favourites", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If no songs are stored, create a new list
            songList.add(currentSong);
            Toast.makeText(getActivity(), "Song added to favourites", Toast.LENGTH_SHORT).show();
        }

        // Once retrieved and updated the list of songs, we need to store it again in SharedPreferences

        // Convert the list of songs to JSON String
        json = new Gson().toJson(songList);

        // Save JSON string and apply changes
        editor.putString(MainActivity.FAVOURITE_SONGS, json);
        editor.apply();
    }
}