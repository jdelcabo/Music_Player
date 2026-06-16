package jdc.projects.personal.musicplayer.callback;

import java.util.ArrayList;

import jdc.projects.personal.musicplayer.enumeration.Range;
import jdc.projects.personal.musicplayer.model.Album;
import jdc.projects.personal.musicplayer.model.Song;

public interface SearchCallback {
    void receiveSongs(ArrayList<Song> songs, Range range);
    void receiveAlbums(ArrayList<Album> albums, Range range);
}
