package jdc.projects.personal.musicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jdc.projects.personal.musicplayer.MainActivity;
import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.callback.SearchCallback;
import jdc.projects.personal.musicplayer.enumeration.CardType;
import jdc.projects.personal.musicplayer.enumeration.Range;
import jdc.projects.personal.musicplayer.model.Album;
import jdc.projects.personal.musicplayer.model.AlbumAdapter;
import jdc.projects.personal.musicplayer.model.Persistence;
import jdc.projects.personal.musicplayer.model.Song;
import jdc.projects.personal.musicplayer.model.SongAdapter;

public class SearchFragment extends Fragment implements SearchCallback {
    // Attributes
    private Persistence persistence;
    private ArrayList<Song> songs;
    private ArrayList<Album> albums;
    private RecyclerView songsListRecyclerView, albumsListRecyclerView;
    private SongAdapter songAdapter;
    private AlbumAdapter albumAdapter;
    private Button allSongsButton;
    private Button allAlbumsButton;
    private boolean newSongsLoading;
    private boolean newAlbumsLoading;
    private ImageButton searchButton;
    private EditText searchEditText;
    private Fragment assignedFragment;


    // Fragment constructor
    public SearchFragment() {
        // Required empty public constructor
    }

    // This displays directly the layout of the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        // Initialization of variables
        newSongsLoading = false;
        newAlbumsLoading = false;

        // Initialize the RecyclerView for songs
        LinearLayoutManager songsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        songsListRecyclerView = view.findViewById(R.id.SongsContainer);
        songsListRecyclerView.setLayoutManager(songsLayoutManager);
        songsListRecyclerView.setAdapter(songAdapter);

        // Add scroll listener to load more songs when reaching the end of the list
        songsListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!searchEditText.getText().toString().isEmpty()){
                    return;
                }

                int visibleItemCount = songsLayoutManager.getChildCount();
                int totalItemCount = songsLayoutManager.getItemCount();
                int firstVisibleItemPosition = songsLayoutManager.findFirstVisibleItemPosition();

                // Check if the end has been reached
                if (!newSongsLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    newSongsLoading = true;

                    // Load more songs
                    receiveNewRandomSongs();

                    // Prevent multiple triggers (reset after data load) (the delay can be adjusted)
                    songsListRecyclerView.postDelayed(() -> newSongsLoading = false, 1500);
                }
            }


        });

        // Initialize the RecyclerView for albums
        LinearLayoutManager albumsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        albumsListRecyclerView = view.findViewById(R.id.AlbumsContainer);
        albumsListRecyclerView.setLayoutManager(albumsLayoutManager);
        albumsListRecyclerView.setAdapter(albumAdapter);

        // Add scroll listener to load more albums when reaching the end of the list
        albumsListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!searchEditText.getText().toString().isEmpty()){
                    return;
                }
                int visibleItemCount = albumsLayoutManager.getChildCount();
                int totalItemCount = albumsLayoutManager.getItemCount();
                int firstVisibleItemPosition = albumsLayoutManager.findFirstVisibleItemPosition();

                // Check if the end has been reached
                if (!newAlbumsLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    newAlbumsLoading = true;

                    // Load more albums
                    receiveNewRandomAlbums();

                    // Prevent multiple triggers (reset after data load) (the delay can be adjusted)
                    albumsListRecyclerView.postDelayed(() -> newAlbumsLoading = false, 1500);
                }
            }
        });

        // Initialize buttons
        allAlbumsButton = view.findViewById(R.id.seeAllAlbumsButton);
        allSongsButton = view.findViewById(R.id.seeAllSongsButton);
        searchButton = view.findViewById(R.id.search_button);

        // Initialize search edit text
        searchEditText = view.findViewById(R.id.search_input);

        allSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Convert songs into jsons
                ArrayList<String> songsInfo = new ArrayList<>();
                for (Song song : songs) {
                    songsInfo.add(song.toString());
                }

                // Create the fragment depending on the range (has the user looked for a keyword?)
                Fragment allSongsFragment;
                if (searchEditText.getText().toString().isEmpty()){
                    allSongsFragment = AllSongsFragment.newInstance(songsInfo, Range.FIFTEEN);
                } else {
                    allSongsFragment = AllSongsFragment.newInstance(songsInfo, Range.ALL);
                }

                if (assignedFragment.isAdded()) {
                    FragmentTransaction transaction = assignedFragment.getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, allSongsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        allAlbumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Convert songs into jsons
                ArrayList<String> albumsInfo = new ArrayList<>();
                for (Album album : albums) {
                    albumsInfo.add(album.toString());
                }

                // Create the fragment depending on the range (has the user looked for a keyword?)
                Fragment allAlbumssFragment;
                if (searchEditText.getText().toString().isEmpty()){
                    allAlbumssFragment = AllAlbumsFragment.newInstance(albumsInfo, Range.FIFTEEN);
                } else {
                    allAlbumssFragment = AllAlbumsFragment.newInstance(albumsInfo, Range.ALL);
                }

                if (assignedFragment.isAdded()) {
                    FragmentTransaction transaction = assignedFragment.getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, allAlbumssFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchEditText.getText().toString();
                if (search.isEmpty()){
                    cleanRecyclerViews();
                    receiveNewRandomSongs();
                    receiveNewRandomAlbums();
                } else {
                    cleanRecyclerViews();
                    receiveNewSongsBySearch(search);
                    receiveNewAlbumsBySearch(search);
                }
            }
        });

        return view;
    }

    // Creation of the fragment before displaying it (use as our constructor)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize attributes
        persistence = Persistence.getInstance();
        songs = new ArrayList<>();
        albums = new ArrayList<>();
        assignedFragment = this;

        // Initialization of the adapters
        songAdapter = new SongAdapter(songs, getActivity(), R.layout.complete_song_placeholder, CardType.COMPLETE_SONG, this);
        albumAdapter = new AlbumAdapter(albums, getActivity(), R.layout.complete_album_placeholder, CardType.COMPLETE_ALBUM, this);

        receiveNewRandomSongs();
        receiveNewRandomAlbums();
    }

    @Override
    public void receiveSongs(ArrayList<Song> songs, Range range) {
        int newSongsIndex = 0;

        if (range == Range.FIFTEEN) {
            // remove extra songs in case API returns more than required, bug found in their services where they return a different number of songs
            ArrayList<Song> filteredSongs = new ArrayList<>();
            for (int i = 0; i < songs.size(); i++){
                if (i >= MainActivity.QUANTITY_OF_NEW_SONGS_ADDED){
                    break;
                }
                filteredSongs.add(songs.get(i));
            }
            songs = filteredSongs;

            // Update the content retrieved
            for (int i = this.songs.size() - songs.size(); i < this.songs.size(); i++) {
                this.songs.get(i).replaceContent(songs.get(newSongsIndex));
                songAdapter.notifyItemChanged(i);
                newSongsIndex++;
            }

            // Remove extra cards
            for (int i = 0; i < this.songs.size(); i++) {
                if (this.songs.get(i).isEmpty()) {
                    this.songs.remove(i);
                    songAdapter.notifyItemRemoved(i);
                    i--;
                }
            }

        } else if (range == Range.ALL){
            // Update the content retrieved
            for (int i = this.songs.size() - Persistence.MAXIMUM_NUMBER_OF_SONGS_API_CAN_RETURN; i < songs.size(); i++) {
                this.songs.get(i).replaceContent(songs.get(newSongsIndex));
                songAdapter.notifyItemChanged(i);
                newSongsIndex++;
            }

            // Remove any extra song that was added
            for (int i = this.songs.size() - 1; i >= songs.size(); i--) {
                this.songs.remove(i);
                songAdapter.notifyItemRemoved(i);
            }

        }
    }

    @Override
    public void receiveAlbums(ArrayList<Album> albums, Range range) {
        int newAlbumsIndex = 0;

        if (range == Range.FIFTEEN) {

            // remove extra songs in case API returns more than required, bug found in their services where they return a different number of songs
            ArrayList<Album> filteredAlbums = new ArrayList<>();
            for (int i = 0; i < albums.size(); i++){
                if (i >= MainActivity.QUANTITY_OF_NEW_ALBUMS_ADDED){
                    break;
                }
                filteredAlbums.add(albums.get(i));
            }
            albums = filteredAlbums;

            // Update the content retrieved
            for (int i = this.albums.size() - albums.size(); i < this.albums.size(); i++) {
                this.albums.get(i).replaceContent(albums.get(newAlbumsIndex));
                albumAdapter.notifyItemChanged(i);
                newAlbumsIndex++;
            }

            // Remove extra cards
            for (int i = 0; i < this.albums.size(); i++) {
                if (this.albums.get(i).isEmpty()) {
                    this.albums.remove(i);
                    albumAdapter.notifyItemRemoved(i);
                    i--;
                }
            }

        } else if (range == Range.ALL) {

            // Update the content retrieved
            for (int i = this.albums.size() - Persistence.MAXIMUM_NUMBER_OF_ALBUMS_API_CAN_RETURN; i < albums.size(); i++) {
                this.albums.get(i).replaceContent(albums.get(newAlbumsIndex));
                albumAdapter.notifyItemChanged(i);
                newAlbumsIndex++;
            }

            // Remove any extra album that was added
            for (int i = this.albums.size() - 1; i >= albums.size(); i--) {
                this.albums.remove(i);
                albumAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void receiveNewRandomSongs(){
        for (int i = 0; i < MainActivity.QUANTITY_OF_NEW_SONGS_ADDED; i++) {
            songs.add(new Song());
            songAdapter.notifyItemInserted(songs.size() - 1);

        }
        persistence.getRandomSetOfSongs(getActivity(), this);
    }

    private void receiveNewRandomAlbums(){
        for (int i = 0; i < MainActivity.QUANTITY_OF_NEW_ALBUMS_ADDED; i++) {
            albums.add(new Album());
            albumAdapter.notifyItemInserted(albums.size() - 1);
        }
        persistence.getRandomSetOfAlbums(getActivity(), this);
    }

    private void receiveNewSongsBySearch(String search){
        for (int i = songs.size()-1; i >= 0; i--) {
            songs.remove(i);
            songAdapter.notifyItemRemoved(i);
        }

        for (int i = 0; i < Persistence.MAXIMUM_NUMBER_OF_SONGS_API_CAN_RETURN; i++) {
            songs.add(new Song());
            songAdapter.notifyItemInserted(songs.size() - 1);
        }

        persistence.getSongsBySearch(getActivity(), this, search);
    }

    private void receiveNewAlbumsBySearch(String search){
        for (int i = albums.size()-1; i >= 0; i--) {
            albums.remove(i);
            albumAdapter.notifyItemRemoved(i);
        }

        for (int i = 0; i < Persistence.MAXIMUM_NUMBER_OF_ALBUMS_API_CAN_RETURN; i++) {
            albums.add(new Album());
            albumAdapter.notifyItemInserted(albums.size() - 1);
        }

        persistence.getAlbumsBySearch(getActivity(), this, search);
    }

    private void cleanRecyclerViews(){
        for (int i = songs.size()-1; i >= 0; i--) {
            songs.remove(i);
            songAdapter.notifyItemRemoved(i);
        }

        for (int i = albums.size()-1; i >= 0; i--) {
            albums.remove(i);
            albumAdapter.notifyItemRemoved(i);
        }
    }
}
