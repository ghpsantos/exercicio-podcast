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

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

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
        ContentResolver cr;
        ContentValues cv = new ContentValues();

        while (ifIterator.hasNext()) {
            ItemFeed itemFeed = ifIterator.next();

            //if not exists in database, create
            ContentResolver crExists = getContentResolver();
            String selection = PodcastProviderContract.DESCRIPTION + " =? AND " + PodcastProviderContract.DATE + " =?";
            String[] selectionArgs = new String[]{itemFeed.getDescription(), itemFeed.getPubDate()};
            Cursor existsItem = crExists.query(PodcastProviderContract.EPISODE_LIST_URI,
                    PodcastProviderContract.ALL_COLUMNS,
                    selection,
                    selectionArgs,
                    null);

            if (existsItem.getCount() == 0) {
                //data
                cr = getContentResolver();
                cv.put(PodcastDBHelper.EPISODE_TITLE, itemFeed.getTitle());
                cv.put(PodcastDBHelper.EPISODE_LINK, itemFeed.getLink());
                cv.put(PodcastDBHelper.EPISODE_DATE, itemFeed.getPubDate());
                cv.put(PodcastDBHelper.EPISODE_DESC, itemFeed.getDescription());
                cv.put(PodcastDBHelper.EPISODE_DOWNLOAD_LINK, itemFeed.getDownloadLink());

                cr.insert(PodcastProviderContract.EPISODE_LIST_URI, cv);
            }
        }

        Intent downloadAndPersistComplete = new Intent(DOWNLOAD_AND_PERSIST_XML_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(downloadAndPersistComplete);
    }


    //TODO Opcional - pesquise outros meios de obter arquivos da internet
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
//
//


//
//    final Intent notificationIntent = new Intent(getApplicationContext(), DownloadAndPersistXmlService.class);
//    final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//    final Notification notification = new Notification.Builder(
//            getApplicationContext())
//            .setSmallIcon(android.R.drawable.ic_media_play)
//            .setOngoing(true).setContentTitle("Music Service rodando")
//            .setContentText("Clique para acessar o player!")
//            .setContentIntent(pendingIntent).build();
//
//    // inicia em estado foreground, para ter prioridade na memoria
//    // evita que seja facilmente eliminado pelo sistema
//    startForeground(NOTIFICATION_ID, notification);