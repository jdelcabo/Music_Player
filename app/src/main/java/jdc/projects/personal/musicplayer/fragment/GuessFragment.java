package jdc.projects.personal.musicplayer.fragment;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import jdc.projects.personal.musicplayer.MainActivity;
import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.callback.SearchCallback;
import jdc.projects.personal.musicplayer.enumeration.Range;
import jdc.projects.personal.musicplayer.model.Album;
import jdc.projects.personal.musicplayer.model.AudioPlayer;
import jdc.projects.personal.musicplayer.model.Persistence;
import jdc.projects.personal.musicplayer.model.Song;

public class GuessFragment extends Fragment implements SearchCallback {
    private Persistence persistence;  //Instance of the DAO to retrieve from the database
    private int lives;
    private int correctAnswers;
    private ImageButton sendButton;
    private EditText inputText;
    private ImageView feedbackImage;
    private TextView feedbackText;
    private ProgressBar progressBar;
    private TextView livesText;
    private ArrayList<Song> songs;  //The array of songs for the game
    private int currentSongIndex;    //The index of the songs array corresponding to the current song
    private AudioPlayer audioPlayer;

    public GuessFragment() {
        // Required empty public constructor
    }

    // This displays directly the layout of the fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_guess, container, false);

        // Initialize the audio player
        audioPlayer = AudioPlayer.getInstanceAndAssignUIElements(
                v.findViewById(R.id.seekBar),
                v.findViewById(R.id.textStartTime),
                v.findViewById(R.id.textEndTime),
                v.findViewById(R.id.playPauseButton),
                v.findViewById(R.id.replayButton),
                v.findViewById(R.id.musicPlayerBar)
        );

        // Initialize the views
        sendButton = v.findViewById(R.id.sendButton);
        livesText = v.findViewById(R.id.num_count);
        inputText = v.findViewById(R.id.songInput);
        feedbackImage = v.findViewById(R.id.feedbackImage);
        feedbackText = v.findViewById(R.id.feedbackText);
        progressBar = v.findViewById(R.id.progress_bar);

        // Set feedback to invisible
        feedbackImage.setVisibility(INVISIBLE);
        feedbackText.setVisibility(INVISIBLE);

        // Update lives and progress
        updateRemainingLives();
        progressBar.setProgress(0);
        progressBar.setMax(MainActivity.QUANTITY_OF_NEW_SONGS_ADDED);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Enable feedback (before first input its off)
                feedbackImage.setVisibility(VISIBLE);
                feedbackText.setVisibility(VISIBLE);

                String userInput = inputText.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    //Check if the guess is correct and handle it
                    checkAnswer(userInput);
                    if (progressBar.getProgress() == MainActivity.QUANTITY_OF_NEW_SONGS_ADDED) progressBar.setProgress(0);

                    updateRemainingLives();

                    inputText.setText("");
                    if (currentSongIndex == songs.size()-1){
                        feedbackImage.setVisibility(VISIBLE);
                        feedbackText.setVisibility(VISIBLE);
                        String msg = getString(R.string.finished_message, correctAnswers, songs.size());
                        feedbackText.setText(msg);
                        feedbackImage.setImageResource(R.drawable.reload);

                    }
                } else {
                    if (currentSongIndex == songs.size()-1){
                        feedbackText.setText("Press the reload button to play again.");
                    } else {
                        feedbackText.setText("Please enter a guess.");
                    }
                }
            }
        });

        feedbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If game has finished, the view can be pressed to reload the game
                if (currentSongIndex == songs.size()-1){
                    resetGame();
                }
            }
        });
        return v;
    }

    // Creation of the fragment before displaying it (use as our constructor)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the attributes
        persistence = Persistence.getInstance();

        resetGame();
    }

    private void resetGame(){
        if (feedbackImage != null && feedbackText != null) {
            feedbackImage.setVisibility(INVISIBLE);
            feedbackText.setVisibility(INVISIBLE);
        }
        lives = MainActivity.MAXIMUM_NUMBER_OF_LIVES;
        correctAnswers = 0;
        currentSongIndex = 0;
        songs = new ArrayList<>();
        persistence.getRandomSetOfSongs(requireContext(), this);
        if (progressBar != null) {
            progressBar.setProgress(0);
        }
    }

    private void checkAnswer(String answer){
        if (answer.equalsIgnoreCase(songs.get(currentSongIndex).getTrackName())){
            feedbackText.setText(R.string.correct_guess);
            feedbackImage.setImageResource(R.drawable.check);
            handleCorrectGuess();
        } else {
            feedbackText.setText(R.string.incorrect_guess);
            feedbackImage.setImageResource(R.drawable.cross);
            handleWrongGuess();
        }
    }

    private void handleCorrectGuess(){
        correctAnswers++;

        moveToNextSong(true);
    }

    private void handleWrongGuess(){
        lives--;
        if (lives == 0){
            moveToNextSong(false);
        }
    }

    private void moveToNextSong(boolean correctAnswer){
        currentSongIndex++;
        lives = MainActivity.MAXIMUM_NUMBER_OF_LIVES;
        updateRemainingLives();
        progressBar.incrementProgressBy(1);

        // Set the feedback invisible
        feedbackImage.setVisibility(INVISIBLE);
        feedbackText.setVisibility(INVISIBLE);

        if (correctAnswer){
            Toast.makeText(getContext(), "Right answer, next question!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Wrong answer, next question!", Toast.LENGTH_SHORT).show();
        }
        persistence.downloadAudioFile(getContext(), songs.get(currentSongIndex).getPreviewUrl(), songs.get(currentSongIndex).getTrackName(), false);

    }

    @Override
    public void receiveSongs(ArrayList<Song> songs, Range range) {
        this.songs.clear();
        while(songs.size() > 15) songs.remove(0);
        this.songs.addAll(songs);

        progressBar.setMax(songs.size());

        persistence.downloadAudioFile(getContext(), songs.get(currentSongIndex).getPreviewUrl(), songs.get(currentSongIndex).getTrackName(), false);
    }

    @Override
    public void receiveAlbums(ArrayList<Album> albums, Range range) {
        //Unused
    }

    private void updateRemainingLives(){
        String remainingLives = Integer.toString(this.lives);
        livesText.setText(remainingLives);
    }
}
