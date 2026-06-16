package jdc.projects.personal.musicplayer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.fragment.FavouritesFragment;
import jdc.projects.personal.musicplayer.fragment.GuessFragment;
import jdc.projects.personal.musicplayer.fragment.SearchFragment;
import jdc.projects.personal.musicplayer.model.AudioPlayer;

public class MainActivity extends AppCompatActivity {
    // Constants
    public static final String FAVOURITE_SONGS = "FAVOURITE_SONGS";
    public static final String FAVOURITE_ALBUMS = "FAVOURITE_ALBUMS";
    public static final int QUANTITY_OF_NEW_SONGS_ADDED = 15;
    public static final int QUANTITY_OF_NEW_ALBUMS_ADDED = 15;
    public static final int MAXIMUM_NUMBER_OF_LIVES = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this, PlayBackSongActivity.class));
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        loadFragment(new SearchFragment()); // Default screen
        bottomNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);


        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            if (item.getItemId() == R.id.nav_search) {
                selected = new SearchFragment();
            } else if (item.getItemId() == R.id.nav_guess) {
                selected = new GuessFragment();
            } else if (item.getItemId() == R.id.nav_favorites) {
                selected = new FavouritesFragment();

            }
            return loadFragment(selected);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            if (AudioPlayer.getInstance().isPlaying()){
                AudioPlayer.getInstance().switchPlayPauseState();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
