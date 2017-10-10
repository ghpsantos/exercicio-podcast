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

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DownloadAndPersistJob extends JobService {
    public static final String downloadLink = "http://s2.glbimg.com/ylmOBAxzbEu_x1usqf2IwfdAfds=/206x116/top/smart/filters:strip_icc()/s2.glbimg.com/vTzgkIRvttN5dxztM0ecQqi_0g0=/0x0:699x392/267x150/i.glbimg.com/og/ig/infoglobo1/f/original/2017/07/27/garotinho.jpg";

    @Override
    public boolean onStartJob(JobParameters params) {
        PersistableBundle pb=params.getExtras();
//        if (pb.getBoolean(JobSchedulerActivity.KEY_DOWNLOAD, false)) {
//            Intent downloadService = new Intent (getApplicationContext(),DownloadAndPersistXmlService.class);
//            downloadService.setData(Uri.parse(downloadLink));
//
//            getApplicationContext().startService(downloadService);
//            return true;
//        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
//        Intent downloadService = new Intent (getApplicationContext(),DownloadAndPersistXmlService.class);
//        getApplicationContext().stopService(downloadService);
        return true;
    }
}