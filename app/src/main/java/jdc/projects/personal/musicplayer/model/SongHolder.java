package jdc.projects.personal.musicplayer.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jdc.projects.personal.musicplayer.MainActivity;
import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.enumeration.CardType;
import jdc.projects.personal.musicplayer.fragment.SongDetailsFragment;

public class SongHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // Attributes
    private Context applicationContext;
    private Song currentSong;
    private CardType cardType;
    // Possible views to modify depending on the card type
    private ImageView songCover;
    private TextView cardSongTitle, cardSongArtist, cardSongDuration;
    private ImageButton playButton, addToFavouritesButton;
    private Fragment assignedFragment;

    public SongHolder(LayoutInflater inflater, ViewGroup parent, Context applicationContext, int cardId, CardType cardType, Fragment fragment) {
        super(inflater.inflate(cardId , parent, false));

        // Initialize the attributes
        this.cardType = cardType;
        this.applicationContext = applicationContext;
        this.assignedFragment = fragment;

        // Set views according to the card type
        switch (cardType){
            case COMPLETE_SONG:
                songCover = itemView.findViewById(R.id.song_cover);
                cardSongTitle = itemView.findViewById(R.id.card_song_title);
                cardSongArtist = itemView.findViewById(R.id.card_song_artist);
                break;
            case PARTIAL_SONG:
                cardSongDuration = itemView.findViewById(R.id.duration_song);
                cardSongTitle = itemView.findViewById(R.id.title_song);
                playButton = itemView.findViewById(R.id.playButton);
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Persistence.getInstance().downloadAudioFile(fragment.getActivity(), currentSong.getPreviewUrl(), currentSong.getTrackName(), true);


                    }
                });
                addToFavouritesButton = itemView.findViewById(R.id.favourite);
                addToFavouritesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        storeFavoriteSongsInPreferences();
                    }
                });
                break;
        }

        // Set this holder as the click listener for the entire item view
        itemView.setOnClickListener(this);
    }

    public void bind(Song song) {
        currentSong = song;

        switch (cardType){
            case COMPLETE_SONG:
                if (!song.getArtworkUrl100().isEmpty()) {
                    // Load image using Picasso with the URL from the game object
                    Picasso.get().load(song.getArtworkUrl100()).into(songCover);
                    // Set the text for the title and artist
                    cardSongTitle.setText(song.getTrackName());
                    cardSongArtist.setText(song.getArtistName());
                }
                break;
            case PARTIAL_SONG:
                cardSongTitle.setText(song.getTrackName());

                // Create a variable of type String called duration that takes the milliseconds from the song object and converts it to a String with format MM:SS
                String durationInSeconds = String.format("%d:%02d", (song.getTrackTimeMillis() / 1000) / 60, (song.getTrackTimeMillis() / 1000) % 60);


                cardSongDuration.setText(durationInSeconds);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (cardType == CardType.COMPLETE_SONG || cardType == CardType.PARTIAL_SONG) {
            if (!currentSong.getArtworkUrl100().isEmpty()) {
                Fragment songDetailsFragment = SongDetailsFragment.newInstance(currentSong.toString());
                if (assignedFragment.isAdded()) {
                    FragmentTransaction transaction = assignedFragment.getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, songDetailsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        }
    }

    private void storeFavoriteSongsInPreferences() {
        boolean songExists;

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(MainActivity.FAVOURITE_SONGS, MODE_PRIVATE);
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

                Toast.makeText(applicationContext, "Song added to favourites", Toast.LENGTH_SHORT).show();

            } else {
                // Remove the current song from the list
                songList.remove(currentSong);
                Toast.makeText(applicationContext, "Song removed from favourites", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If no songs are stored, create a new list
            songList.add(currentSong);
            Toast.makeText(applicationContext, "Song added to favourites", Toast.LENGTH_SHORT).show();
        }

        // Once retrieved and updated the list of songs, we need to store it again in SharedPreferences

        // Convert the list of songs to JSON String
        json = new Gson().toJson(songList);

        // Save JSON string and apply changes
        editor.putString(MainActivity.FAVOURITE_SONGS, json);
        editor.apply();
    }

}