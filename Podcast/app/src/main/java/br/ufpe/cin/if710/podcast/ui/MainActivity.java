package br.ufpe.cin.if710.podcast.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.AppDatabase;
import br.ufpe.cin.if710.podcast.db.ItemFeedDao;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.viewmodel.ItemFeedViewModel;
import br.ufpe.cin.if710.podcast.services.DownloadAndPersistXmlService;
import br.ufpe.cin.if710.podcast.services.EpisodeDownloadService;
import br.ufpe.cin.if710.podcast.services.MusicPlayerService;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class MainActivity extends AppCompatActivity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
//    private ItemFeedDao itemFeedDao;

    private ListView items;

    private ItemFeedViewModel itemFeedViewModel;

    private XmlFeedAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = (ListView) findViewById(R.id.items);
        //forçando o adapter a não ser nulo
        adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, new ArrayList<ItemFeed>());
        items.setAdapter(adapter);

        IntentFilter f_d = new IntentFilter(DownloadAndPersistXmlService.DOWNLOAD_AND_PERSIST_XML_COMPLETE);
        IntentFilter f_m = new IntentFilter(MusicPlayerService.MUSIC_PAUSED);
        IntentFilter f_e = new IntentFilter(MusicPlayerService.MUSIC_ENDED);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDownloadAndPersistCompleteEvent, f_d);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onMusicPaused, f_m);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onMusicEnded, f_e);

        itemFeedViewModel = ViewModelProviders.of(this).get(ItemFeedViewModel.class);

        itemFeedViewModel.getAllItemsFeeds().observe(MainActivity.this, new Observer<List<ItemFeed>>() {
            @Override
            public void onChanged(@Nullable List<ItemFeed> itemFeeds) {
                if(itemFeeds != null){
                    adapter.addAll(itemFeeds);
                }
            }
        });


        //forcing permission
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
//        itemFeedDao = AppDatabase.getDatabase(getApplicationContext()).podcastDao();
        //when start execute DownloadAndPersist service
        Intent downloadAndPersistXmlService = new Intent(this, DownloadAndPersistXmlService.class);
        downloadAndPersistXmlService.putExtra("rss", RSS_FEED);
        startService(downloadAndPersistXmlService);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter = (XmlFeedAdapter) items.getAdapter();
        if (adapter != null) {
            adapter.clear();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDownloadAndPersistCompleteEvent);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onMusicPaused);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onMusicEnded);
    }

    //requests
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 710;

    private static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
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


    //when finish download Episode, update the database and view
    private BroadcastReceiver onDownloadCompleteEvent = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
            int selectedItem = i.getIntExtra("selectedItem", 0);
            String uri = i.getStringExtra("uri");
            ItemFeed itemFeed = (ItemFeed) items.getItemAtPosition(selectedItem);
            itemFeed.setUri(uri);
            itemFeedViewModel.updateItemFeed(itemFeed);
//            new UpdateItemFeedTask().execute(itemFeed);
        }
    };


    private BroadcastReceiver onDownloadAndPersistCompleteEvent = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //verify if MainActivity is running
            if (MainActivity.this.getWindow().getDecorView().getRootView().isShown()) {
                new SetUiOnDownloadEndTask().execute();

            } else {
                //if MainActivity is in foreground creates a notification
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
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
//
        }
    };


    private class SetUiOnDownloadEndTask extends AsyncTask<Void, Void, List<ItemFeed>> {
        @Override
        protected List<ItemFeed> doInBackground(Void... params) {
            return itemFeedViewModel.getAllItemsFeeds().getValue();
        }

        @Override
        protected void onPostExecute(List<ItemFeed> itemFeeds) {
            super.onPostExecute(itemFeeds);

            //Adapter Personalizado
             adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, itemFeeds);

            //atualizar o list view
            items.setAdapter(adapter);
            items.setTextFilterEnabled(true);
            //when clicked goes to EpisodeDetailActivity
            items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter = (XmlFeedAdapter) parent.getAdapter();
                    ItemFeed item = adapter.getItem(position);
                    //passing an intent with the clicked item to EpisodeDetail Activity
                    Intent i = new Intent(getApplicationContext(), EpisodeDetailActivity.class);
                    i.putExtra("clickedItem", item);
                    startActivity(i);
                }
            });

        }
    }


    //when music paused, update the database with the currentTime and set view
    private BroadcastReceiver onMusicPaused = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int currentPosition = intent.getIntExtra("currentPosition", 0);
            int selectedPosition = intent.getIntExtra("selectedPosition", 0);
            ItemFeed itemFeed = (ItemFeed) items.getItemAtPosition(selectedPosition);

            itemFeed.setCurrentPosition(currentPosition);
            itemFeedViewModel.updateItemFeed(itemFeed);
//            new UpdateItemFeedTask().execute(itemFeed);
        }
    };

    //when music end, delete episode and update database
    private BroadcastReceiver onMusicEnded = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int endedItemPosition = intent.getIntExtra("itemPlaying", 0);

            ItemFeed endedItem = (ItemFeed) items.getItemAtPosition(endedItemPosition);
            File file = new File(endedItem.getUri());

            boolean deleted = file.delete();
            if (deleted) {
                endedItem.setUri(null);
                itemFeedViewModel.updateItemFeed(endedItem);
//                new UpdateItemFeedTask().execute(endedItem);
            } else {
                Toast.makeText(context, "Arquivo não deletado " + intent.getIntExtra("itemPlaying", 0), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
