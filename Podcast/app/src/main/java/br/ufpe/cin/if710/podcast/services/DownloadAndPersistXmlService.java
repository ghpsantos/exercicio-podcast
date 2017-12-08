package br.ufpe.cin.if710.podcast.services;

/**
 * Created by Guilherme on 06/10/2017.
 */

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;


import org.xmlpull.v1.XmlPullParserException;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ufpe.cin.if710.podcast.db.AppDatabase;
import br.ufpe.cin.if710.podcast.db.ItemFeedDao;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;

public class DownloadAndPersistXmlService extends IntentService {

    public static final String DOWNLOAD_AND_PERSIST_XML_COMPLETE = "br.ufpe.cin.if710.services.action.DOWNLOAD_AND_PERSIST_XML_COMPLETE";


    public DownloadAndPersistXmlService() {
        super("DownloadAndPersistXmlService");
    }

    @Override
    public void onHandleIntent(Intent i) {

        List<ItemFeed> itemList = new ArrayList<>();
        try {
            itemList = XmlFeedParser.parse(getRssFeed(i.getStringExtra("rss")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        //inserting new data trough iterator
        Iterator<ItemFeed> ifIterator = itemList.iterator();

        ItemFeedDao itemFeedDao = AppDatabase.getDatabase(getApplicationContext()).podcastDao();
        while (ifIterator.hasNext()) {
            ItemFeed itemFeed = ifIterator.next();
            itemFeedDao.insert(itemFeed);
        }

        Intent downloadAndPersistComplete = new Intent(DOWNLOAD_AND_PERSIST_XML_COMPLETE);
       LocalBroadcastManager.getInstance(this).sendBroadcast(downloadAndPersistComplete);
    }


    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}