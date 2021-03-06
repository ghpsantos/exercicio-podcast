package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

public class EpisodeDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        Intent i = getIntent();
        ItemFeed itemFeed = (ItemFeed)i.getSerializableExtra("clickedItem");

        //setting titles
        TextView title_tv = findViewById(R.id.title_value);
        TextView link_tv = findViewById(R.id.link_value);
        TextView pubDate_tv = findViewById(R.id.pubDate_value);
        TextView description_tv = findViewById(R.id.description_value);
        TextView downloadLink_tv = findViewById(R.id.downloadLink_value);
        TextView uri_tv = findViewById(R.id.uri_value);

        //setting values
        title_tv.setText(itemFeed.getTitle());
        link_tv.setText(itemFeed.getLink());
        pubDate_tv.setText(itemFeed.getPubDate());
        description_tv.setText(itemFeed.getDescription());
        downloadLink_tv.setText(itemFeed.getDownloadLink());
        uri_tv.setText(itemFeed.getUri());
    }
}
