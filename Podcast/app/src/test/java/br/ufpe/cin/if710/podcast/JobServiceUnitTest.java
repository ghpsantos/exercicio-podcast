package br.ufpe.cin.if710.podcast;

import android.app.job.JobParameters;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import br.ufpe.cin.if710.podcast.managers.jobscheduler.DownloadAndPersistJob;

/**
 * Created by Guilherme on 05/12/2017.
 */

public class JobServiceUnitTest {

    @Test
    public void doesJobDownloadJobServiceWorked(){
        DownloadAndPersistJob downloadAndPersistJob = mock(DownloadAndPersistJob.class);
        JobParameters params = mock(JobParameters.class);
        when(downloadAndPersistJob.onStartJob(params)).thenReturn(true);

        boolean isJobScheduled = downloadAndPersistJob.onStartJob(params);
        assertTrue(isJobScheduled);
    }
}
