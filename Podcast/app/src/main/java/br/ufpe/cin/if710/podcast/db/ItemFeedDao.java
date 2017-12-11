package br.ufpe.cin.if710.podcast.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.ClipData;

import java.util.List;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Guilherme on 07/12/2017.
 */

@Dao
public interface ItemFeedDao {
    @Query("SELECT * FROM episodes")
    LiveData<List<ItemFeed>> getAll();

    @Insert(onConflict = REPLACE)
    long insert(ItemFeed itemFeed);

    @Update
    int update(ItemFeed itemFeed);

    @Delete
    int delete(ItemFeed itemFeed);

//    "DELETE FROM episodes WHERE id in (SELECT id FROM episodes LIMIT 1 OFFSET 2)"


    @Query( "UPDATE episodes SET title = :str WHERE id in (SELECT id FROM episodes LIMIT 1 OFFSET 2)")
    void update3rd(String str);
}
