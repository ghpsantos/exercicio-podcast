package br.ufpe.cin.if710.podcast.managers.jobscheduler;

/**
 * Created by Guilherme on 09/10/2017.
 */

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;

import br.ufpe.cin.if710.podcast.services.DownloadAndPersistXmlService;
import br.ufpe.cin.if710.podcast.ui.SettingsActivity;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DownloadAndPersistJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        PersistableBundle pb = params.getExtras();
        String downloadLink = pb.getString(SettingsActivity.FEED_LINK);

        if (downloadLink != null) {
            Intent downloadService = new Intent(getApplicationContext(), DownloadAndPersistXmlService.class);
            downloadService.putExtra("rss", downloadLink);
            getApplicationContext().startService(downloadService);
            return true;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
//        Intent downloadService = new Intent (getApplicationContext(),DownloadAndPersistXmlService.class);
//        getApplicationContext().stopService(downloadService);
        return true;
    }
}