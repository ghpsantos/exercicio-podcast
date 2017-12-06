package br.ufpe.cin.if710.podcast;

/**
 * Created by Guilherme on 04/12/2017.
 */



import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.ui.MainActivity;
import br.ufpe.cin.if710.podcast.ui.SettingsActivity;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class IntentTest {
    @Rule
    public final IntentsTestRule<MainActivity> main = new IntentsTestRule<>(MainActivity.class, true);

    @Before
    public void waitForActivityLoad() throws InterruptedException {
        sleep(4000);
    }


    @Test
    public void itemDetailClickIntent() {
        onData(anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(14)
                .perform(click());

        intended(allOf(toPackage("br.ufpe.cin.if710.podcast"), hasExtra(any(String.class),any(ItemFeed.class))));
    }

//    @Test
//    public void itemDownloadClickIntent() throws InterruptedException {
//         onData(anything())
//                .inAdapterView(withId(R.id.items))
//                .atPosition(14)
//                .onChildView(withId(R.id.item_action))
//                .perform(click());
////                .check(matches(isDisplayed()));
//
//
//        intended(toPackage("br.ufpe.cin.if710.podcast"));
//    }

}