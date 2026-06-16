package jdc.projects.personal.musicplayer.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jdc.projects.personal.musicplayer.enumeration.CardType;

public class SongAdapter extends RecyclerView.Adapter<SongHolder> {
    // Attributes
    private ArrayList<Song> songs;
    private Context applicationContext;
    private int cardId;
    private CardType cardType;
    private Fragment assignedFragment;

    // Constructor
    public SongAdapter(ArrayList<Song> songs, Context applicationContext, int cardId, CardType cardType, Fragment fragment) {
        this.songs = songs;
        this.applicationContext = applicationContext;
        this.cardId = cardId;
        this.cardType = cardType;
        this.assignedFragment = fragment;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(applicationContext);
        return new SongHolder(layoutInflater, parent, applicationContext, cardId, cardType, assignedFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        holder.bind(songs.get(position));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

}