package br.ufpe.cin.if710.podcast.domain.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.AppDatabase;
import br.ufpe.cin.if710.podcast.db.ItemFeedDao;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import java.util.UUID;

import static android.os.SystemClock.sleep;

/**
 * Created by Guilherme on 10/12/2017.
 */

public class ItemFeedViewModel extends AndroidViewModel {
    private LiveData<List<ItemFeed>> itemsFeeds;

    private ItemFeedDao itemFeedDao;

    public ItemFeedViewModel(@NonNull Application application) {
        super(application);

        itemFeedDao = AppDatabase.getDatabase(application).podcastDao();
        itemsFeeds = itemFeedDao.getAll();
    }

    public LiveData<List<ItemFeed>> getAllItemsFeeds(){
        return itemsFeeds;
    }


    public void updateItemFeed(ItemFeed itemFeed){
        new UpdateItemFeedTask().execute(itemFeed);
    }

    private class UpdateItemFeedTask extends AsyncTask<ItemFeed, Void, ItemFeed> {

        @Override
        protected ItemFeed doInBackground(ItemFeed... itemFeeds) {
            itemFeedDao.update(itemFeeds[0]);
            return itemFeeds[0];
        }

    }


//    public void update3rd(){
//       new Remove3RD().execute();
//    }
//
//    private class Remove3RD extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            while(true) {
//                sleep(10000);
//                itemFeedDao.update3rd(UUID.randomUUID().toString().substring(0,7));
//            }
//        }
//
//    }


}

