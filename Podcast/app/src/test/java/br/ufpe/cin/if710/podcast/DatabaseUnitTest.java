package br.ufpe.cin.if710.podcast;

import android.content.ContentValues;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Guilherme on 05/12/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Uri.class})
public class DatabaseUnitTest {


    @Test
    public void isInsertingAItemProperly() throws Exception {
        //mocking methods and classes
        PodcastProvider podcastProvider = mock(PodcastProvider.class);
        ContentValues contentValues = mock(ContentValues.class);
        PowerMockito.mockStatic(Uri.class);
        Uri succesUri = mock(Uri.class);
        Uri episodeUri = mock(Uri.class);

        when(podcastProvider.insert(episodeUri, contentValues)).thenReturn(succesUri);
        doNothing().when(contentValues).put(anyString(),anyString());
        doNothing().when(contentValues).put(anyString(),anyInt());

        //test start
        contentValues.put(PodcastProviderContract.DESCRIPTION,"Aliens");
        contentValues.put(PodcastProviderContract.EPISODE_LINK,"somesite.com");
        contentValues.put(PodcastProviderContract.DATE,"05/09/1995");
        contentValues.put(PodcastProviderContract.DESCRIPTION,"lorem ipsum");
        contentValues.put(PodcastProviderContract.EPISODE_FILE_URI,"someuri.com");
        contentValues.put(PodcastProviderContract.CURRENT_POSITION,1);

        Uri insertResult = podcastProvider.insert(episodeUri, contentValues);

        assertEquals(insertResult,succesUri);
    }

    String [] selectionArgs = new String[]{
        "arg1", "arg2"
    };
    @Test
    public void isUpdatingAItemProperly() throws Exception {
        //mocking methods and classes
        PodcastProvider podcastProvider = mock(PodcastProvider.class);
        ContentValues contentValues = mock(ContentValues.class);
        PowerMockito.mockStatic(Uri.class);
        Uri episodeUri = mock(Uri.class);

        when(podcastProvider.update(episodeUri,contentValues,"", selectionArgs)).thenReturn(1);
        doNothing().when(contentValues).put(anyString(),anyString());
        doNothing().when(contentValues).put(anyString(),anyInt());

        //setting values to update
        contentValues.put(PodcastProviderContract.DESCRIPTION,"Aliens");
        contentValues.put(PodcastProviderContract.EPISODE_LINK,"somesite.com");
        contentValues.put(PodcastProviderContract.DATE,"05/09/1995");
        contentValues.put(PodcastProviderContract.DESCRIPTION,"lorem ipsum");
        contentValues.put(PodcastProviderContract.EPISODE_FILE_URI,"someuri.com");
        contentValues.put(PodcastProviderContract.CURRENT_POSITION,1);

        int updateResult = podcastProvider.update(episodeUri, contentValues, "", selectionArgs);

        assertEquals(updateResult,1);

    }
}
