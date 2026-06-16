package jdc.projects.personal.musicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

import jdc.projects.personal.musicplayer.MainActivity;
import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.callback.SearchCallback;
import jdc.projects.personal.musicplayer.enumeration.CardType;
import jdc.projects.personal.musicplayer.enumeration.Range;
import jdc.projects.personal.musicplayer.model.Album;
import jdc.projects.personal.musicplayer.model.Persistence;
import jdc.projects.personal.musicplayer.model.Song;
import jdc.projects.personal.musicplayer.model.SongAdapter;

public class AllSongsFragment extends Fragment implements SearchCallback {
    // Constants
    private static final String ARG_ALL_SONGS_INFO = "ALL_SONGS_INFO";
    private static final String ARG_RANGE = "RANGE";
    // Attributes
    private SongAdapter songAdapter;
    private ArrayList<Song> songs;
    private Persistence persistence;
    private Range range;
    private SearchCallback fragment;
    // Views
    private RecyclerView songsRecyclerView;
    private ImageButton backButton;
    private Button orderAlphabeticallyButton, orderByReleaseDateButton, orderByTimeButton;
    private boolean isFirstLoad = true;


    public AllSongsFragment(){
        // Required empty public constructor
    }

    public static AllSongsFragment newInstance(ArrayList<String> songsInfo, Range range) {
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_ALL_SONGS_INFO, songsInfo);
        args.putSerializable(ARG_RANGE, range);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);

        // Initialize the RecyclerView for songs
        LinearLayoutManager songsLayoutManager = new LinearLayoutManager(getActivity());
        songsRecyclerView = view.findViewById(R.id.TracksRecyclerView);
        songsRecyclerView.setLayoutManager(songsLayoutManager);
        songsRecyclerView.setAdapter(songAdapter);

        // Retrieving the data passed in order to display the different songs
        if (getArguments() != null && isFirstLoad) {
            isFirstLoad = false;
            ArrayList<String> loadedSongs = getArguments().getStringArrayList(ARG_ALL_SONGS_INFO);
            for (String songInfo : loadedSongs) {
                Song song = new Song(songInfo);
                songs.add(song);
                songAdapter.notifyItemInserted(songs.size() - 1);
            }
        }

        // Retrieve the data passed in order to keep showing more songs or not
        if (getArguments() != null) {
            range = (Range) getArguments().getSerializable(ARG_RANGE);

            // This means the user has not searched for anything and should keep displaying randoms songs
            if (range == Range.FIFTEEN) {
                songsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                        // Only check when scrolling down
                        if (layoutManager != null && dy > 0) {
                            int visibleItemCount = layoutManager.getChildCount();
                            int totalItemCount = layoutManager.getItemCount();
                            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                // We have reached the end of the list
                                persistence.getRandomSetOfSongs(getActivity(), fragment);
                            }
                        }
                    }
                });

            } else if (range == Range.ALL) {
                // nothing to do, it means the user searched for something and all the songs are already loaded
            }
        }

        //Initialize back button and its listener
        backButton = view.findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        // Initialize order buttons
        orderAlphabeticallyButton = view.findViewById(R.id.alphabet_button);
        orderByReleaseDateButton = view.findViewById(R.id.oldest_button);
        orderByTimeButton = view.findViewById(R.id.shortest_button);


        // Set listeners for order buttons
        orderAlphabeticallyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderAlphabetically();
            }
        });

        orderByReleaseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderByReleaseDate();
            }
        });

        orderByTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderByTime();
            }
        });
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
        // Keep the fragment reference for listener purposes
        fragment = this;
    }

    @Override
    public void receiveSongs(ArrayList<Song> songs, Range range) {
        int newSongsIndex = 0;

        if (range == Range.FIFTEEN) {
            // remove extra songs in case API returns more than required, bug found in their services where they return a different number of songs
            ArrayList<Song> filteredSongs = new ArrayList<>();
            for (int i = 0; i < MainActivity.QUANTITY_OF_NEW_SONGS_ADDED; i++){
                filteredSongs.add(songs.get(i));
            }
            songs = filteredSongs;


            // Update the content retrieved
            for (Song song : songs){
                this.songs.add(song);
                songAdapter.notifyItemInserted(newSongsIndex);
            }

        }
    }

    @Override
    public void receiveAlbums(ArrayList<Album> albums, Range range) {

    }

    private void orderAlphabetically() {
        // Sort the songs alphabetically by track name
        songs.sort((song1, song2) -> song1.getTrackName().compareToIgnoreCase(song2.getTrackName()));
        songAdapter.notifyDataSetChanged();
    }
    private void orderByReleaseDate() {
        // Sort the songs by release date
        songs.sort(Comparator.comparing(Song::getReleaseDate));
        songAdapter.notifyDataSetChanged();
    }

    private void orderByTime() {
        // Sort the songs by shortest track time
        songs.sort(Comparator.comparing(Song::getTrackTimeMillis));
        songAdapter.notifyDataSetChanged();
    }
}
