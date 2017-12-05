package br.ufpe.cin.if710.podcast;

import android.content.Context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Teste de métodos estáticos do parser utilizando o PowerMockito
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(XmlFeedParser.class)
public class XmlParserTest {

    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";

    List<ItemFeed> mockedItemsFeeds = new ArrayList<ItemFeed>() {
        {
            add(new ItemFeed("Aliens","somesite.com","05/09/1995","lorem ipsum","somesite.com","someuri.com",1));
            add(new ItemFeed("Homeopatia","somesite.com","05/09/1995","lorem ipsum","somesite.com","someuri.com",1));
            add(new ItemFeed("Rick and Morty","somesite.com","05/09/1995","lorem ipsum","somesite.com","someuri.com",1));
        }
    };

    @Test
    public void isParserWorking() throws IOException, XmlPullParserException {
        mockStatic(XmlFeedParser.class);
        when(XmlFeedParser.parse(anyString())).thenReturn(mockedItemsFeeds);
        List<ItemFeed> parse = XmlFeedParser.parse(RSS_FEED);
        assertNotNull(parse);
    }
}