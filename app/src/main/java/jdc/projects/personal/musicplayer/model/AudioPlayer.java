package jdc.projects.personal.musicplayer.model;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import jdc.projects.personal.musicplayer.R;

public class AudioPlayer {
    // Singleton instance
    private static AudioPlayer instance;
    // Attributes
    private MediaPlayer mediaPlayer;
    private boolean isLooping;
    private Handler handler;
    // UI elements to interact with
    private SeekBar seekBar;
    private TextView startTimeTextView, endTimeTextView;
    private ImageButton playPauseButton, loopButton;
    private View musicPlayerBar;

    // Constructor
    private AudioPlayer() {
        // Empty media player by default
        mediaPlayer = new MediaPlayer();
        // No looping by default
        isLooping = false;
        // Initialize the handler
        handler = new Handler();
    }

    // Method to get the singleton instance
    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

    // Method to link UI elements to the logic
    public static void assignUIElements(SeekBar seekBar, TextView startTimeTextView, TextView endTimeTextView, ImageButton playPauseButton, ImageButton loopButton, View musicPlayerBar) {
        // Invoke to make sure that the instance is not null
        getInstance();

        // Link UI components to the audio player
        instance.seekBar = seekBar;
        instance.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    instance.mediaPlayer.seekTo(progress);
                    instance.startTimeTextView.setText(instance.formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // mediaPlayer.start();
                instance.updateSeekBar();
            }
        });

        instance.startTimeTextView = startTimeTextView;
        instance.endTimeTextView = endTimeTextView;
        instance.playPauseButton = playPauseButton;
        instance.loopButton = loopButton;
        instance.musicPlayerBar = musicPlayerBar;

        instance.playPauseButton.setOnClickListener(v -> {
            instance.switchPlayPauseState();
        });

        instance.loopButton.setOnClickListener(v -> {
            instance.switchLoopingState();
        });
    }

    // Method to get the singleton instance and assign UI elements at the same time
    public static AudioPlayer getInstanceAndAssignUIElements(SeekBar seekBar, TextView startTimeTextView, TextView endTimeTextView, ImageButton playPauseButton, ImageButton loopButton, View musicPlayerBar) {
        assignUIElements(seekBar, startTimeTextView, endTimeTextView, playPauseButton, loopButton, musicPlayerBar);
        return getInstance();
    }

    public void switchLoopingState(){
        isLooping = !isLooping;
        mediaPlayer.setLooping(isLooping);
        instance.loopButton.setImageResource(R.drawable.autoplay_button);
    }

    public void switchPlayPauseState() {
        if (instance.mediaPlayer.isPlaying()) {
            instance.mediaPlayer.pause();
            instance.playPauseButton.setImageResource(R.drawable.play_button);
        } else {
            instance.mediaPlayer.start();
            instance.playPauseButton.setImageResource(R.drawable.pause_button);
            instance.musicPlayerBar.setVisibility(View.VISIBLE);
            instance.updateSeekBar();
        }
    }

    private String formatTime(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    public void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
         startTimeTextView.setText(formatTime(mediaPlayer.getCurrentPosition()));

        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(this::updateSeekBar, 1000);
        }
    }

    public void loadNewAudio(String filepath) throws IOException {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }

        File file = new File(filepath);
        if (!file.exists() || file.length() == 0) {
            Log.e("AudioPlayer", "File does not exist or is empty: " + filepath);
            return;
        }
//        Log.d("AudioPlayer", "Loading audio from: " + filepath);


        mediaPlayer.setDataSource(filepath);
        mediaPlayer.prepare();

        endTimeTextView.setText(formatTime(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    startTimeTextView.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // mediaPlayer.start();
                updateSeekBar();
            }
        });

        musicPlayerBar.setVisibility(View.VISIBLE);

        updateSeekBar();
        playPauseButton.setImageResource(R.drawable.play_button);
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}
