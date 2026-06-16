package jdc.projects.personal.musicplayer.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jdc.projects.personal.musicplayer.enumeration.CardType;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {
    // Attributes
    private ArrayList<Album> albums;
    private Context applicationContext;
    private int cardId;
    private CardType cardType;
    private Fragment assignedFragment;


    // Constructor
    public AlbumAdapter(ArrayList<Album> albums, Context applicationContext, int cardId, CardType cardType, Fragment fragment) {
        this.albums = albums;
        this.applicationContext = applicationContext;
        this.cardId = cardId;
        this.cardType = cardType;
        this.assignedFragment = fragment;
    }

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(applicationContext);
        return new AlbumHolder(layoutInflater, parent, applicationContext, cardId, cardType, assignedFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
        holder.bind(albums.get(position));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

}