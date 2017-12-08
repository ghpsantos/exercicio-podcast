package br.ufpe.cin.if710.podcast.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.io.Serializable;

//implements this interface to support passing a complete object between activities
@Entity(tableName = "episodes")
public class ItemFeed implements Serializable{

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String title;
    private String link;
    private String pubDate;
    private String description;
    private String downloadLink;
    private String uri;
    private Integer currentPosition;

    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink, String uri, Integer currentPosition) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.uri = uri;
        this.currentPosition = currentPosition;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Override
    public String toString() {
        return title;
    }
}