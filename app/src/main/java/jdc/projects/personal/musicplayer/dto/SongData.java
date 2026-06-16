package jdc.projects.personal.musicplayer.dto;

import com.google.gson.annotations.SerializedName;

public class SongData {
    // Attributes
    @SerializedName("wrapperType")
    private String wrapperType;
    @SerializedName("kind")
    private String kind;
    @SerializedName("artistId")
    private long artistId;
    @SerializedName("collectionId")
    private long collectionId;
    @SerializedName("trackId")
    private long trackId;
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("collectionName")
    private String collectionName;
    @SerializedName("trackName")
    private String trackName;
    @SerializedName("collectionCensoredName")
    private String collectionCensoredName;
    @SerializedName("trackCensoredName")
    private String trackCensoredName;
    @SerializedName("collectionArtistId")
    private long collectionArtistId;
    @SerializedName("collectionArtistName")
    private String collectionArtistName;
    @SerializedName("artistViewUrl")
    private String artistViewUrl;
    @SerializedName("collectionViewUrl")
    private String collectionViewUrl;
    @SerializedName("trackViewUrl")
    private String trackViewUrl;
    @SerializedName("previewUrl")
    private String previewUrl;
    @SerializedName("artworkUrl30")
    private String artworkUrl30;
    @SerializedName("artworkUrl60")
    private String artworkUrl60;
    @SerializedName("artworkUrl100")
    private String artworkUrl100;
    @SerializedName("collectionPrice")
    private double collectionPrice;
    @SerializedName("trackPrice")
    private double trackPrice;
    @SerializedName("releaseDate")
    private String releaseDate;
    @SerializedName("collectionExplicitness")
    private String collectionExplicitness;
    @SerializedName("trackExplicitness")
    private String trackExplicitness;
    @SerializedName("discCount")
    private int discCount;
    @SerializedName("discNumber")
    private int discNumber;
    @SerializedName("trackCount")
    private int trackCount;
    @SerializedName("trackNumber")
    private int trackNumber;
    @SerializedName("trackTimeMillis")
    private long trackTimeMillis;
    @SerializedName("country")
    private String country;
    @SerializedName("currency")
    private String currency;
    @SerializedName("primaryGenreName")
    private String primaryGenreName;
    @SerializedName("contentAdvisoryRating")
    private String contentAdvisoryRating;
    @SerializedName("isStreamable")
    private boolean isStreamable;

    // Constructor
    public SongData(boolean isStreamable, String contentAdvisoryRating, String primaryGenreName, String currency, String country, long trackTimeMillis, int trackNumber, int trackCount, int discNumber, int discCount, String trackExplicitness, String collectionExplicitness, String releaseDate, double trackPrice, double collectionPrice, String artworkUrl100, String artworkUrl60, String artworkUrl30, String previewUrl, String trackViewUrl, String collectionViewUrl, String artistViewUrl, String collectionArtistName, long collectionArtistId, String trackCensoredName, String collectionCensoredName, String trackName, String collectionName, String artistName, long trackId, long collectionId, long artistId, String kind, String wrapperType) {
        this.isStreamable = isStreamable;
        this.contentAdvisoryRating = contentAdvisoryRating;
        this.primaryGenreName = primaryGenreName;
        this.currency = currency;
        this.country = country;
        this.trackTimeMillis = trackTimeMillis;
        this.trackNumber = trackNumber;
        this.trackCount = trackCount;
        this.discNumber = discNumber;
        this.discCount = discCount;
        this.trackExplicitness = trackExplicitness;
        this.collectionExplicitness = collectionExplicitness;
        this.releaseDate = releaseDate;
        this.trackPrice = trackPrice;
        this.collectionPrice = collectionPrice;
        this.artworkUrl100 = artworkUrl100;
        this.artworkUrl60 = artworkUrl60;
        this.artworkUrl30 = artworkUrl30;
        this.previewUrl = previewUrl;
        this.trackViewUrl = trackViewUrl;
        this.collectionViewUrl = collectionViewUrl;
        this.artistViewUrl = artistViewUrl;
        this.collectionArtistName = collectionArtistName;
        this.collectionArtistId = collectionArtistId;
        this.trackCensoredName = trackCensoredName;
        this.collectionCensoredName = collectionCensoredName;
        this.trackName = trackName;
        this.collectionName = collectionName;
        this.artistName = artistName;
        this.trackId = trackId;
        this.collectionId = collectionId;
        this.artistId = artistId;
        this.kind = kind;
        this.wrapperType = wrapperType;
    }

    // Getters
    public String getArtistName() {
        return artistName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public double getTrackPrice() {
        return trackPrice;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getCountry() {
        return country;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public boolean isStreamable() {
        return isStreamable;
    }

    public String getKind() {
        return kind;
    }

    public long getArtistId() {
        return artistId;
    }

    public long getTrackTimeMillis() {
        return trackTimeMillis;
    }
}
