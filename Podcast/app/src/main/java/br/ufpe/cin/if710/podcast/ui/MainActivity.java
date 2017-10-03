package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

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
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.services.DownloadService;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    //TODO teste com outros links de podcast

    private ListView items;
    private List<ItemFeed> feed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = (ListView) findViewById(R.id.items);
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
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if hasConnection download and save in the database
         * if not retrieve feed from database
         */
        //TODO ajustar Download e Database retrieve task. ( funcionamento assincrono )
//        if(isConnected(getApplicationContext())){
            new DownloadXmlAndSaveInDatabaseTask().execute(RSS_FEED);
//        }else {
//            new DatabaseRetrieveDataTask().execute();
//        }
    }

    //auxiliary method to verify connectivity
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        adapter.clear();
    }

    private class DownloadXmlAndSaveInDatabaseTask extends AsyncTask<String, Void, List<ItemFeed>> {
        @Override
        protected void onPreExecute() {
//            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemFeed> doInBackground(String... params) {
            List<ItemFeed> itemList = new ArrayList<>();
            try {
                itemList = XmlFeedParser.parse(getRssFeed(params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<ItemFeed> feed) {
//            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();


//            XmlFeedAdapter adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, feed);

            //atualizar o list view
//            items.setAdapter(adapter);
//            items.setTextFilterEnabled(true);
//
//            items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    XmlFeedAdapter adapter = (XmlFeedAdapter) parent.getAdapter();
//                    ItemFeed item = adapter.getItem(position);
//                    //passing an intent with the clicked item to EpisodeDetail Activity
//                    Intent i = new Intent(getApplicationContext(),EpisodeDetailActivity.class);
//                    i.putExtra("clickedItem", item);
//                    startActivity(i);
//                }
//            });



        //TODO comportamento estranho na primeira execução...
            //inserting data in database trough iterator
            Iterator<ItemFeed> ifIterator = feed.iterator();
            ContentResolver cr;
            ContentValues cv = new ContentValues();

            while(ifIterator.hasNext()){
                ItemFeed itemFeed = ifIterator.next();

                //if not exists in database, create
                ContentResolver crExists = getContentResolver();
                String selection = PodcastProviderContract.DESCRIPTION + " =? AND "+ PodcastProviderContract.DATE +" =?";
                String[] selectionArgs = new String[]{itemFeed.getDescription(), itemFeed.getPubDate()};
                Cursor existsItem = crExists.query(PodcastProviderContract.EPISODE_LIST_URI,
                        PodcastProviderContract.ALL_COLUMNS,
                        selection,
                        selectionArgs,
                        null);
                int s = existsItem.getCount();
                if(s==0) {
                    //data
                    cr = getContentResolver();
                    cv.put(PodcastDBHelper.EPISODE_TITLE, itemFeed.getTitle());
                    cv.put(PodcastDBHelper.EPISODE_LINK, itemFeed.getLink());
                    cv.put(PodcastDBHelper.EPISODE_DATE, itemFeed.getPubDate());
                    cv.put(PodcastDBHelper.EPISODE_DESC, itemFeed.getDescription());
                    cv.put(PodcastDBHelper.EPISODE_DOWNLOAD_LINK, itemFeed.getDownloadLink());

                    cr.insert(PodcastProviderContract.EPISODE_LIST_URI, cv);
                }


                // retrieving from database and setting view
                cr = getContentResolver();
                Cursor c = cr.query(PodcastProviderContract.EPISODE_LIST_URI,null,null,null,null);

                ArrayList<ItemFeed> itemFeeds = new ArrayList<>();
                while(c.moveToNext()){
                    String title = c.getString(c.getColumnIndex(PodcastProviderContract.TITLE));
                    String link = c.getString(c.getColumnIndex(PodcastProviderContract.EPISODE_LINK));
                    String pubDate = c.getString(c.getColumnIndex(PodcastProviderContract.DATE));
                    String description = c.getString(c.getColumnIndex(PodcastProviderContract.DESCRIPTION));
                    String downloadLink = c.getString(c.getColumnIndex(PodcastProviderContract.DOWNLOAD_LINK));
                    String uri = c.getString(c.getColumnIndex(PodcastProviderContract.EPISODE_FILE_URI));

                    itemFeeds.add(new ItemFeed(title,link,pubDate,description,downloadLink,uri));
                }


                (MainActivity.this).feed = itemFeeds;
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
                        Intent i = new Intent(getApplicationContext(),EpisodeDetailActivity.class);
                        i.putExtra("clickedItem", item);
                        startActivity(i);
                    }
                });
            }
        }
    }


    private class DatabaseRetrieveDataTask extends AsyncTask<String, Void, List<ItemFeed>> {
        @Override
        protected void onPreExecute() {
//            Toast.makeText(getApplicationContext(), "iniciando offline...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemFeed> doInBackground(String... params) {
            ContentResolver cr = getContentResolver();
            Cursor c = cr.query(PodcastProviderContract.EPISODE_LIST_URI,null,null,null,null);

            ArrayList<ItemFeed> itemFeeds = new ArrayList<>();
            while(c.moveToNext()){
                String title = c.getString(c.getColumnIndex(PodcastProviderContract.TITLE));
                String link = c.getString(c.getColumnIndex(PodcastProviderContract.EPISODE_LINK));
                String pubDate = c.getString(c.getColumnIndex(PodcastProviderContract.DATE));
                String description = c.getString(c.getColumnIndex(PodcastProviderContract.DESCRIPTION));
                String downloadLink = c.getString(c.getColumnIndex(PodcastProviderContract.DOWNLOAD_LINK));
                String uri = c.getString(c.getColumnIndex(PodcastProviderContract.EPISODE_FILE_URI));

                itemFeeds.add(new ItemFeed(title,link,pubDate,description,downloadLink,uri));
            }




            return itemFeeds;
        }

        @Override
        protected void onPostExecute(List<ItemFeed> feed) {
//            Toast.makeText(getApplicationContext(), "terminando offline...", Toast.LENGTH_SHORT).show();

            //Adapter Personalizado
            XmlFeedAdapter adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, feed);
            (MainActivity.this).feed = feed;
            //atualizar o list view
            items.setAdapter(adapter);
            items.setTextFilterEnabled(true);
            items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    XmlFeedAdapter adapter = (XmlFeedAdapter) parent.getAdapter();
                    ItemFeed item = adapter.getItem(position);
                    //passing an intent with the clicked item to EpisodeDetail Activity
                    Intent i = new Intent(getApplicationContext(),EpisodeDetailActivity.class);
                    i.putExtra("clickedItem", item);
                    startActivity(i);
                }
            });

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


    //OnButton Click methods listeners for download Button

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter f = new IntentFilter(DownloadService.DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDownloadCompleteEvent, f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDownloadCompleteEvent);
    }

    private BroadcastReceiver onDownloadCompleteEvent=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
            int selectedItem = i.getIntExtra("selectedItem",0);
            Button selectedButton = (Button)items.getChildAt(selectedItem).findViewById(R.id.item_action);
            selectedButton.setEnabled(true);

            String uri = i.getStringExtra("uri");

            ItemFeed itemFeed = (ItemFeed)items.getItemAtPosition(selectedItem);

            ContentResolver cr = getContentResolver();
            ContentValues cv = new ContentValues();

            cv.put(PodcastDBHelper.EPISODE_FILE_URI, uri);

            String selection = PodcastProviderContract.DESCRIPTION + " =? AND "+ PodcastProviderContract.DATE +" =?";
            String[] selectionArgs = new String[]{itemFeed.getDescription(), itemFeed.getPubDate()};
            int s = cr.update(PodcastProviderContract.EPISODE_LIST_URI,
                    cv,
                    selection,
                    selectionArgs);

            itemFeed.setUri(uri);
            feed.set(selectedItem, itemFeed);
            ((XmlFeedAdapter)items.getAdapter()).notifyDataSetChanged();

            selectedButton.setText("Ouvir");
        }
    };


}