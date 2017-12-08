package br.ufpe.cin.if710.podcast;

import android.content.ContentValues;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.ufpe.cin.if710.podcast.db.AppDatabase;
import br.ufpe.cin.if710.podcast.db.ItemFeedDao;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Guilherme on 05/12/2017.
 */

public class DatabaseUnitTest {

    @Test
    public void isInsertingAItemProperly() throws Exception {
        ItemFeedDao itemFeedDao = mock(ItemFeedDao.class);
        ItemFeed itemFeed = new ItemFeed("Aliens","somesitelink.com","05/09/1995","lorem ipsum","somedownloadsite.com","someuri.com",1);
        //mocking methods and classes
        when(itemFeedDao.insert(any(ItemFeed.class))).thenReturn(1L);

        long insertResult = itemFeedDao.insert(itemFeed);
        assertEquals(insertResult,1L);
    }

    @Test
    public void isUpdatingAItemProperly() throws Exception {
        ItemFeedDao itemFeedDao = mock(ItemFeedDao.class);
        ItemFeed itemFeed = new ItemFeed("Aliens","somesitelink.com","05/09/1995","lorem ipsum","somedownloadsite.com","someuri.com",1);
        //mocking methods and classes
        when(itemFeedDao.update(any(ItemFeed.class))).thenReturn(1);

        int insertResult = itemFeedDao.update(itemFeed);
        assertEquals(insertResult,1);

    }
}
