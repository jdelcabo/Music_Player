package jdc.projects.personal.musicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class AllAlbumsFragment extends Fragment implements SearchCallback {
    // Constants
    private static final String ARG_ALL_ALBUMS_INFO = "ALL_ALBUMS_INFO";
    private static final String ARG_RANGE = "RANGE";
    // Views
    private ImageButton backButton;
    private RecyclerView albumsListRecyclerView;
    // Attributes
    private ArrayList<Album> albums;
    private Range range;
    private SearchCallback fragment;
    private AlbumAdapter albumAdapter;
    private Persistence persistence;
    private boolean isFirstLoad = true;


    public AllAlbumsFragment() {
        // Required empty public constructor
    }

    public static AllAlbumsFragment newInstance(ArrayList<String> albumsInfo, Range range) {
        AllAlbumsFragment fragment = new AllAlbumsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_ALL_ALBUMS_INFO, albumsInfo);
        args.putSerializable(ARG_RANGE, range);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_albums, container, false);

        // Initialize the RecyclerView for albums
        LinearLayoutManager albumsLayoutManager = new LinearLayoutManager(getActivity());
        albumsListRecyclerView = view.findViewById(R.id.AlbumsRecyclerView);
        albumsListRecyclerView.setLayoutManager(albumsLayoutManager);
        albumsListRecyclerView.setAdapter(albumAdapter);

        // Retrieving the data passed in order to display the different albums
        if (getArguments() != null && isFirstLoad) {
            isFirstLoad = false;
            ArrayList<String> loadedAlbums = getArguments().getStringArrayList(ARG_ALL_ALBUMS_INFO);
            for (String albumInfo : loadedAlbums) {
                Album album = new Album(albumInfo);
                albums.add(album);
                albumAdapter.notifyItemInserted(albums.size() - 1);
            }
        }

        // Retrieve the data passed in order to keep showing more albums or not
        if (getArguments() != null) {
            range = (Range) getArguments().getSerializable(ARG_RANGE);

            // This means the user has not searched for anything and should keep displaying randoms songs
            if (range == Range.FIFTEEN) {
                // Add scroll listener to load more albums when reaching the end of the list
                albumsListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                persistence.getRandomSetOfAlbums(getActivity(), fragment);
                            }
                        }
                    }
                });

            } else if (range == Range.ALL) {
                // nothing to do, it means the user searched for something and all the songs are already loaded
            }

            //Initialize back button and its listener
            backButton = view.findViewById(R.id.back_button);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            });
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize list of albums and songs
        albums = new ArrayList<>();

        // Initialize the persistence
        persistence = Persistence.getInstance();

        // Prepare the adapter
        albumAdapter = new AlbumAdapter(albums, getActivity(), R.layout.partial_album_skeleton, CardType.PARTIAL_ALBUM, this);

        // Keep the fragment reference for listener purposes
        fragment = this;
    }

    @Override
    public void receiveSongs(ArrayList<Song> songs, Range range) {
        // Unused method
    }

    @Override
    public void receiveAlbums(ArrayList<Album> albums, Range range) {
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
            for (Album album : albums){
                this.albums.add(album);
                albumAdapter.notifyItemInserted(albums.size() - 1);
            }
        } else if (range == Range.ALL) {
            // nothing to do, it means the user searched for something and all the albums are already loaded
        }
    }
}
