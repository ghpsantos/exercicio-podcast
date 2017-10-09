package br.ufpe.cin.if710.podcast.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.DownloadAndPersistXmlService;
import br.ufpe.cin.if710.podcast.services.EpisodeDownloadService;
import br.ufpe.cin.if710.podcast.services.MusicPlayerService;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    //TODO teste com outros links de podcast

    private ListView items;
//    private List<ItemFeed> feedToOnCompleteDownload;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = (ListView) findViewById(R.id.items);

        IntentFilter f_d = new IntentFilter(DownloadAndPersistXmlService.DOWNLOAD_AND_PERSIST_XML_COMPLETE);
        IntentFilter f_m = new IntentFilter(MusicPlayerService.MUSIC_PAUSED);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDownloadAndPersistCompleteEvent, f_d);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onMusicPaused, f_m);

        if (!podeEscrever()) {
            requestPermissions(STORAGE_PERMISSIONS, WRITE_EXTERNAL_STORAGE_REQUEST);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent downloadAndPersistXmlService = new Intent(this, DownloadAndPersistXmlService.class);
        downloadAndPersistXmlService.putExtra("rss", RSS_FEED);
        startService(downloadAndPersistXmlService);
    }

    @Override
    protected void onStop() {
        super.onStop();
        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        if (adapter != null) {
            adapter.clear();
        }
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


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter f = new IntentFilter(EpisodeDownloadService.DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDownloadCompleteEvent, f);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDownloadAndPersistCompleteEvent);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onMusicPaused);
    }

    //requests
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 710;

    private static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST:
                if (!podeEscrever()) {
                    Toast.makeText(this, "Sem permissão para escrita", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    public boolean podeEscrever() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private BroadcastReceiver onDownloadCompleteEvent = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
            int selectedItem = i.getIntExtra("selectedItem", 0);
            String uri = i.getStringExtra("uri");

            ItemFeed itemFeed = (ItemFeed) items.getItemAtPosition(selectedItem);

            ContentResolver cr = getContentResolver();
            ContentValues cv = new ContentValues();

            cv.put(PodcastDBHelper.EPISODE_FILE_URI, uri);

            String selection = PodcastProviderContract.DESCRIPTION + " = ? AND " + PodcastProviderContract.DATE + " = ?";
            String[] selectionArgs = new String[]{itemFeed.getDescription(), itemFeed.getPubDate()};
            cr.update(PodcastProviderContract.EPISODE_LIST_URI,
                    cv,
                    selection,
                    selectionArgs);
            //seta a tela
            itemFeed.setUri(uri);
            ((XmlFeedAdapter) items.getAdapter()).notifyDataSetChanged();

        }
    };

    private BroadcastReceiver onDownloadAndPersistCompleteEvent = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //activity running
            if (MainActivity.this.getWindow().getDecorView().getRootView().isShown()) {
                // retrieving from database and setting view
                ContentResolver cr = getContentResolver();
                Cursor c = cr.query(PodcastProviderContract.EPISODE_LIST_URI, null, null, null, null);

                ArrayList<ItemFeed> itemFeeds = new ArrayList<>();
                while (c.moveToNext()) {
                    String title = c.getString(c.getColumnIndex(PodcastProviderContract.TITLE));
                    String link = c.getString(c.getColumnIndex(PodcastProviderContract.EPISODE_LINK));
                    String pubDate = c.getString(c.getColumnIndex(PodcastProviderContract.DATE));
                    String description = c.getString(c.getColumnIndex(PodcastProviderContract.DESCRIPTION));
                    String downloadLink = c.getString(c.getColumnIndex(PodcastProviderContract.DOWNLOAD_LINK));
                    String uri = c.getString(c.getColumnIndex(PodcastProviderContract.EPISODE_FILE_URI));
                    Integer currentPosition = c.getInt(c.getColumnIndex(PodcastProviderContract.CURRENT_POSITION));
                    itemFeeds.add(new ItemFeed(title, link, pubDate, description, downloadLink, uri, currentPosition));
                }

//            ((MainActivity.this).feedToOnCompleteDownload) = itemFeeds;
                //Adapter Personalizado
                XmlFeedAdapter adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, itemFeeds);

                //atualizar o list view
                items.setAdapter(adapter);
                items.setTextFilterEnabled(true);
                items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        XmlFeedAdapter adapter = (XmlFeedAdapter) parent.getAdapter();
                        ItemFeed item = adapter.getItem(position);
                        //passing an intent with the clicked item to EpisodeDetail Activity
                        Intent i = new Intent(getApplicationContext(), EpisodeDetailActivity.class);
                        i.putExtra("clickedItem", item);
                        startActivity(i);
                    }
                });
            } else {
                //foreground
                final Intent notificationIntent = new Intent(context, MainActivity.class);
                final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                final Notification notification = new Notification.Builder(
                        getApplicationContext())
                        .setSmallIcon(android.R.drawable.alert_dark_frame)
                        .setAutoCancel(true)
                        .setOngoing(true).setContentTitle("Lista de Podcasts Atualizados")
                        .setContentText("Clique para acessar o aplicativo de podcast")
                        .setContentIntent(pendingIntent)
                        .build();

                final int NOTIFICATION_ID = 2;
                NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
//
        }
    };

    private BroadcastReceiver onMusicPaused = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int currentPosition = intent.getIntExtra("currentPosition", 0);
            int selectedPosition = intent.getIntExtra("selectedPosition",0);
            ItemFeed itemFeed = (ItemFeed)items.getItemAtPosition(selectedPosition);

            ContentResolver cr = getContentResolver();
            ContentValues cv = new ContentValues();

            cv.put(PodcastDBHelper.CURRENT_POSITION, currentPosition);

            String selection = PodcastProviderContract.DESCRIPTION + " = ? AND " + PodcastProviderContract.DATE + " = ?";
            String[] selectionArgs = new String[]{itemFeed.getDescription(), itemFeed.getPubDate()};
            cr.update(PodcastProviderContract.EPISODE_LIST_URI,
                    cv,
                    selection,
                    selectionArgs);
            //seta a tela
            itemFeed.setCurrentPosition(currentPosition);

            ((XmlFeedAdapter)items.getAdapter()).notifyDataSetChanged();
        }
    };
}




