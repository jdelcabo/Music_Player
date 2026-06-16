package jdc.projects.personal.musicplayer.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import jdc.projects.personal.musicplayer.dto.SongData;

public class Song {
    // Attributes
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("artistId")
    private long artistId;
    @SerializedName("trackName")
    private String trackName;
    @SerializedName("collectionName")
    private String collectionName;
    @SerializedName("artworkUrl100")
    private String artworkUrl100;
    @SerializedName("previewUrl")
    private String previewUrl;
    @SerializedName("trackPrice")
    private double trackPrice;
    @SerializedName("releaseDate")
    private String releaseDate;
    @SerializedName("country")
    private String country;
    @SerializedName("isStreamable")
    private boolean isStreamable;
    @SerializedName("kind")
    private String kind;
    @SerializedName("primaryGenreName")
    private String primaryGenreName;
    @SerializedName("trackTimeMillis")
    private long trackTimeMillis;

    // Constructor
    public Song(SongData songData) {
        this.artistName = songData.getArtistName();
        this.trackName = songData.getTrackName();
        this.collectionName = songData.getCollectionName();
        this.artworkUrl100 = songData.getArtworkUrl100();
        this.previewUrl = songData.getPreviewUrl();
        this.trackPrice = songData.getTrackPrice();
        this.releaseDate = songData.getReleaseDate();
        this.country = songData.getCountry();
        this.isStreamable = songData.isStreamable();
        this.kind = songData.getKind();
        this.artistId = songData.getArtistId();
        this.primaryGenreName = songData.getPrimaryGenreName();
        this.trackTimeMillis = songData.getTrackTimeMillis();
    }

    // String reconstructor
    public Song(String song){
        Song songParsed = new Gson().fromJson(song, Song.class);
        this.artistName = songParsed.artistName;
        this.trackName = songParsed.trackName;
        this.collectionName = songParsed.collectionName;
        this.artworkUrl100 = songParsed.artworkUrl100;
        this.previewUrl = songParsed.previewUrl;
        this.trackPrice = songParsed.trackPrice;
        this.releaseDate = songParsed.releaseDate;
        this.country = songParsed.country;
        this.isStreamable = songParsed.isStreamable;
        this.kind = songParsed.kind;
        this.artistId = songParsed.artistId;
        this.primaryGenreName = songParsed.primaryGenreName;
        this.trackTimeMillis = songParsed.trackTimeMillis;
    }

    // Copy constructor
    public Song(Song song){
        this.artistName = song.artistName;
        this.trackName = song.trackName;
        this.collectionName = song.collectionName;
        this.artworkUrl100 = song.artworkUrl100;
        this.previewUrl = song.previewUrl;
        this.trackPrice = song.trackPrice;
        this.releaseDate = song.releaseDate;
        this.country = song.country;
        this.isStreamable = song.isStreamable;
        this.kind = song.kind;
        this.artistId = song.artistId;
        this.primaryGenreName = song.primaryGenreName;
        this.trackTimeMillis = song.trackTimeMillis;
    }

    // Empty constructor
    public Song() {
        this.artistName = "";
        this.trackName = "";
        this.collectionName = "";
        this.artworkUrl100 = "";
        this.previewUrl = "";
        this.trackPrice = 0;
        this.releaseDate = "";
        this.country = "";
        this.isStreamable = false;
        this.kind = "";
        this.artistId = 0;
        this.primaryGenreName = "";
        this.trackTimeMillis = 0;
    }

    public void replaceContent(Song song){
        this.artistName = song.artistName;
        this.trackName = song.trackName;
        this.collectionName = song.collectionName;
        this.artworkUrl100 = song.artworkUrl100;
        this.previewUrl = song.previewUrl;
        this.trackPrice = song.trackPrice;
        this.releaseDate = song.releaseDate;
        this.country = song.country;
        this.isStreamable = song.isStreamable;
        this.kind = song.kind;
        this.artistId = song.artistId;
        this.primaryGenreName = song.primaryGenreName;
        this.trackTimeMillis = song.trackTimeMillis;
    }

    // Getters
    public String getPreviewUrl(){
        return previewUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getTrackName(){
        return trackName;
    }

    public String getArtworkUrl100(){
        return artworkUrl100;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public double getTrackPrice() {
        return trackPrice;
    }

    public long getTrackTimeMillis() {
        return trackTimeMillis;
    }

    public String getFormattedDate(){
        String[] dateParts = this.releaseDate.split("T");
        String[] date = dateParts[0].split("-");
        return date[2] + "/" + date[1] + "/" + date[0];
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Song ) {
            Song song = (Song) obj;
            return this.artistName.equals(song.artistName) &&
                    this.trackName.equals(song.trackName) &&
                    this.collectionName.equals(song.collectionName) &&
                    this.artworkUrl100.equals(song.artworkUrl100) &&
                    this.previewUrl.equals(song.previewUrl) &&
                    this.trackPrice == song.trackPrice &&
                    this.releaseDate.equals(song.releaseDate) &&
                    this.country.equals(song.country) &&
                    this.isStreamable == song.isStreamable &&
                    this.kind.equals(song.kind) &&
                    this.artistId == song.artistId &&
                    this.primaryGenreName.equals(song.primaryGenreName) &&
                    this.trackTimeMillis == song.trackTimeMillis;
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public boolean isEmpty(){
        return this.artistName.isEmpty() &&
                this.trackName.isEmpty() &&
                this.collectionName.isEmpty() &&
                this.artworkUrl100.isEmpty() &&
                this.previewUrl.isEmpty() &&
                this.releaseDate.isEmpty() &&
                this.country.isEmpty() &&
                this.kind.isEmpty() &&
                this.primaryGenreName.isEmpty();
    }
}
