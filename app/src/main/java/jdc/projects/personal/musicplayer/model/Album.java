package jdc.projects.personal.musicplayer.model;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import jdc.projects.personal.musicplayer.dto.AlbumData;

public class Album {
    // Attributes
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("collectionName")
    private String collectionName;
    @SerializedName("country")
    private String country;
    @SerializedName("artworkUrl100")
    private String artworkUrl100;
    @SerializedName("artistViewUrl")
    private String artistViewUrl;
    @SerializedName("releaseDate")
    private String releaseDate;
    @SerializedName("primaryGenreName")
    private String primaryGenreName;
    @SerializedName("collectionPrice")
    private double collectionPrice;
    @SerializedName("collectionId")
    private long collectionId;

    // Constructor
    public Album(AlbumData albumData){
        this.artistName = albumData.getArtistName();
        this.collectionName = albumData.getCollectionName();
        this.country = albumData.getCountry();
        this.artworkUrl100 = albumData.getArtworkUrl100();
        this.artistViewUrl = albumData.getArtistViewUrl();
        this.releaseDate = albumData.getReleaseDate();
        this.primaryGenreName = albumData.getPrimaryGenreName();
        this.collectionPrice = albumData.getCollectionPrice();
        this.collectionId = albumData.getCollectionId();
    }

    // String reconstructor
    public Album(String album){
        Album albumParsed = new Gson().fromJson(album, Album.class);
        this.artistName = albumParsed.artistName;
        this.collectionName = albumParsed.collectionName;
        this.country = albumParsed.country;
        this.artworkUrl100 = albumParsed.artworkUrl100;
        this.artistViewUrl = albumParsed.artistViewUrl;
        this.releaseDate = albumParsed.releaseDate;
        this.primaryGenreName = albumParsed.primaryGenreName;
        this.collectionPrice = albumParsed.collectionPrice;
        this.collectionId = albumParsed.collectionId;
    }

    // Copy constructor
    public Album(Album album){
        this.artistName = album.artistName;
        this.collectionName = album.collectionName;
        this.country = album.country;
        this.artworkUrl100 = album.artworkUrl100;
        this.artistViewUrl = album.artistViewUrl;
        this.releaseDate = album.releaseDate;
        this.primaryGenreName = album.primaryGenreName;
        this.collectionPrice = album.collectionPrice;
        this.collectionId = album.collectionId;
    }

    // Empty constructor
    public Album(){
        this.artistName = "";
        this.collectionName = "";
        this.country = "";
        this.artworkUrl100 = "";
        this.artistViewUrl = "";
        this.releaseDate = "";
        this.primaryGenreName = "";
        this.collectionPrice = 0.0;
        this.collectionId = 0;
    }

    public void replaceContent(Album album){
        this.artistName = album.artistName;
        this.collectionName = album.collectionName;
        this.country = album.country;
        this.artworkUrl100 = album.artworkUrl100;
        this.artistViewUrl = album.artistViewUrl;
        this.releaseDate = album.releaseDate;
        this.primaryGenreName = album.primaryGenreName;
        this.collectionPrice = album.collectionPrice;
        this.collectionId = album.collectionId;
    }

    // Getters
    public String getArtistName() {
        return artistName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public double getCollectionPrice() {
        return collectionPrice;
    }

    public long getCollectionId() {
        return collectionId;
    }

    public String getFormattedDate(){
        String[] dateParts = this.releaseDate.split("T");
        String[] date = dateParts[0].split("-");
        return date[2] + "/" + date[1] + "/" + date[0];
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Album){
            Album album = (Album) obj;
            return this.artistName.equals(album.artistName) &&
                    this.collectionName.equals(album.collectionName) &&
                    this.country.equals(album.country) &&
                    this.artworkUrl100.equals(album.artworkUrl100) &&
                    this.artistViewUrl.equals(album.artistViewUrl) &&
                    this.releaseDate.equals(album.releaseDate) &&
                    this.primaryGenreName.equals(album.primaryGenreName) &&
                    this.collectionPrice == album.collectionPrice &&
                    this.collectionId == album.collectionId;
        }
        return false;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public boolean isEmpty(){
        return this.artistName.isEmpty() &&
                this.collectionName.isEmpty() &&
                this.country.isEmpty() &&
                this.artworkUrl100.isEmpty() &&
                this.artistViewUrl.isEmpty() &&
                this.releaseDate.isEmpty() &&
                this.primaryGenreName.isEmpty() &&
                this.collectionPrice == 0.0 &&
                this.collectionId == 0;
    }


}
