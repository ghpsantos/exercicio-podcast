package br.ufpe.cin.if710.podcast.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by Guilherme on 07/12/2017.
 */
@Database(entities = {ItemFeed.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ItemFeedDao podcastDao();

    public static AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "podcasts").build();
        }
        return instance;
    }
}