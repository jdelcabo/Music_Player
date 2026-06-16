package jdc.projects.personal.musicplayer.dto;

import com.google.gson.annotations.SerializedName;

public class AlbumData {
    // Attributes
    @SerializedName("wrapperType")
    private String wrapperType;
    @SerializedName("collectionType")
    private String collectionType;
    @SerializedName("artistId")
    private long artistId;
    @SerializedName("collectionId")
    private long collectionId;
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("collectionName")
    private String collectionName;
    @SerializedName("collectionCensoredName")
    private String collectionCensoredName;
    @SerializedName("artistViewUrl")
    private String artistViewUrl;
    @SerializedName("collectionViewUrl")
    private String collectionViewUrl;
    @SerializedName("artworkUrl60")
    private String artworkUrl60;
    @SerializedName("artworkUrl100")
    private String artworkUrl100;
    @SerializedName("collectionPrice")
    private double collectionPrice;
    @SerializedName("collectionExplicitness")
    private String collectionExplicitness;
    @SerializedName("trackCount")
    private int trackCount;
    @SerializedName("copyright")
    private String copyright;
    @SerializedName("country")
    private String country;
    @SerializedName("currency")
    private String currency;
    @SerializedName("releaseDate")
    private String releaseDate;
    @SerializedName("primaryGenreName")
    private String primaryGenreName;

    // Constructor
    public AlbumData(String wrapperType, String collectionType, long artistId, long collectionId, String artistName, String collectionName, String collectionCensoredName, String artistViewUrl, String collectionViewUrl, String artworkUrl60, String artworkUrl100, double collectionPrice, String collectionExplicitness, int trackCount, String copyright, String country, String currency, String releaseDate, String primaryGenreName) {
        this.wrapperType = wrapperType;
        this.collectionType = collectionType;
        this.artistId = artistId;
        this.collectionId = collectionId;
        this.artistName = artistName;
        this.collectionName = collectionName;
        this.collectionCensoredName = collectionCensoredName;
        this.artistViewUrl = artistViewUrl;
        this.collectionViewUrl = collectionViewUrl;
        this.artworkUrl60 = artworkUrl60;
        this.artworkUrl100 = artworkUrl100;
        this.collectionPrice = collectionPrice;
        this.collectionExplicitness = collectionExplicitness;
        this.trackCount = trackCount;
        this.copyright = copyright;
        this.country = country;
        this.currency = currency;
        this.releaseDate = releaseDate;
        this.primaryGenreName = primaryGenreName;
    }

    // Getters

    public long getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getCountry() {
        return country;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public String getArtistViewUrl() {
        return artistViewUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public double getCollectionPrice() {
        return collectionPrice;
    }

    public long getCollectionId() {
        return collectionId;
    }
}
