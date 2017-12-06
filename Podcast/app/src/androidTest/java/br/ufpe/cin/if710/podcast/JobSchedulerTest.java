package br.ufpe.cin.if710.podcast;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import br.ufpe.cin.if710.podcast.ui.MainActivity;
import br.ufpe.cin.if710.podcast.ui.SettingsActivity;

import static android.os.SystemClock.sleep;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

/**
 * Created by Guilherme on 06/12/2017.
 */


@RunWith(AndroidJUnit4.class)
public class JobSchedulerTest {

    @Rule
    public final IntentsTestRule<SettingsActivity> main = new IntentsTestRule<>(SettingsActivity.class, true, true);

    private JobScheduler jobScheduler;

    //job_id for download service
    private static final int JOB_ID = 710;

    @Before
    public void waitForActivityLoad() throws InterruptedException {
        jobScheduler = (JobScheduler) main.getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        sleep(4000);
    }


    @Test
    public void isJobScheduled() {
        onData(withKey("scheduler_button")).perform(click());
        JobInfo pendingJob = jobScheduler.getPendingJob(JOB_ID);
        assertNotNull(pendingJob);
    }

    @Test
    public void isJobCancelled() {
        //scheduling job
        onData(withKey("scheduler_button")).perform(click());
        //cancel job scheduled
        onData(withKey("cancel_scheduler_button")).perform(click());
        JobInfo pendingJob = jobScheduler.getPendingJob(JOB_ID);
        assertNull(pendingJob);
    }


}
