package br.ufpe.cin.if710.podcast.domain;

import java.io.Serializable;

//implements this interface to support passing a complete object between activities
public class ItemFeed implements Serializable{
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String downloadLink;
    private final String uri;

    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink, String uri) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return title;
    }
}