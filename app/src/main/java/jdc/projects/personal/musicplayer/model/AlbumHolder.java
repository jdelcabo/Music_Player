package jdc.projects.personal.musicplayer.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import jdc.projects.personal.musicplayer.R;
import jdc.projects.personal.musicplayer.enumeration.CardType;
import jdc.projects.personal.musicplayer.fragment.AlbumDetailsFragment;


public class AlbumHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // Attributes
    private Context applicationContext;
    private Album currentAlbum;
    private CardType cardType;
    // Possible views to modify depending on the card type
    private ImageView albumCover;
    private TextView cardAlbumTitle, cardAlbumArtist, cardAlbumPrice;
    // Attributes
    private Fragment assignedFragment;

    public AlbumHolder(LayoutInflater inflater, ViewGroup parent, Context applicationContext, int cardId, CardType cardType, Fragment fragment) {
        super(inflater.inflate(cardId, parent, false));

        // Initialize the attributes
        this.cardType = cardType;
        this.applicationContext = applicationContext;
        this.assignedFragment = fragment;

        // Set views according to the card type
        switch (cardType){
            case COMPLETE_ALBUM:
                cardAlbumTitle = itemView.findViewById(R.id.card_album_title);
                albumCover = itemView.findViewById(R.id.album_cover);
                cardAlbumArtist = itemView.findViewById(R.id.card_album_artist);
                cardAlbumPrice = itemView.findViewById(R.id.card_album_price);
                break;
            case  PARTIAL_ALBUM:
                cardAlbumTitle = itemView.findViewById(R.id.card_album_title);
                albumCover = itemView.findViewById(R.id.album_cover);
                cardAlbumArtist = itemView.findViewById(R.id.card_album_artist);
                cardAlbumPrice = itemView.findViewById(R.id.card_album_price);
        }

        // Set this holder as the click listener for the entire item view
        itemView.setOnClickListener(this);
    }

    public void bind(Album album) {
        currentAlbum = album;

        switch (cardType) {
            case COMPLETE_ALBUM:
                if (!album.getArtworkUrl100().isEmpty()) {
                    // Load image using Picasso with the URL from the game object
                    Picasso.get().load(album.getArtworkUrl100()).into(albumCover);
                    // Set the text for the title, artist and price
                    cardAlbumTitle.setText(album.getCollectionName());
                    cardAlbumArtist.setText(album.getArtistName());
                    String price = album.getCollectionPrice() + " €";
                    cardAlbumPrice.setText(price);
                }
                break;
            case PARTIAL_ALBUM:
                if (!album.getArtworkUrl100().isEmpty()) {
                    // Load image using Picasso with the URL from the game object
                    Picasso.get().load(album.getArtworkUrl100()).into(albumCover);
                    // Set the text for the title, artist and price
                    cardAlbumTitle.setText(album.getCollectionName());
                    cardAlbumArtist.setText(album.getArtistName());
                    String price = album.getCollectionPrice() + " €";
                    cardAlbumPrice.setText(price);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (cardType){
            case COMPLETE_ALBUM:
                if (!currentAlbum.getArtworkUrl100().isEmpty()) {
                    Fragment albumDetailsFragment = AlbumDetailsFragment.newInstance(currentAlbum.toString());
                    if (assignedFragment.isAdded()) {
                        FragmentTransaction transaction = assignedFragment.getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, albumDetailsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
                break;
            case PARTIAL_ALBUM:
                if (!currentAlbum.getArtworkUrl100().isEmpty()) {
                    Fragment albumDetailsFragment = AlbumDetailsFragment.newInstance(currentAlbum.toString());
                    if (assignedFragment.isAdded()) {
                        FragmentTransaction transaction = assignedFragment.getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, albumDetailsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
                break;
        }
    }
}