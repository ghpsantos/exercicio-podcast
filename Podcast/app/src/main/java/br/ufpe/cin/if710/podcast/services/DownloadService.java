package br.ufpe.cin.if710.podcast.services;

import android.app.IntentService;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by Guilherme on 01/10/2017.
 */

public class DownloadService extends IntentService {

    public static final String DOWNLOAD_COMPLETE = "br.ufpe.cin.if710.services.action.DOWNLOAD_COMPLETE";


    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onHandleIntent(Intent i) {
//        try {
            Uri uri = Uri.parse("http://www.artebahia.com/11465-thickbox_default/aplique-ancora-pequena.jpg");

            //checar se tem permissao... Android 6.0+
//            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            root.mkdirs();
//            File output = new File(root, uri.getLastPathSegment());
//            if (output.exists()) {
//                output.delete();
//            }
//
//            URL url = new URL(uri.toString());
////            URL url = new URL("http://www.artebahia.com/11465-thickbox_default/aplique-ancora-pequena.jpg");
//            HttpURLConnection c = (HttpURLConnection) url.openConnection();
//            FileOutputStream fos = new FileOutputStream(output.getPath());
//            BufferedOutputStream out = new BufferedOutputStream(fos);
//            try {
//                InputStream in = c.getInputStream();
//                byte[] buffer = new byte[8192];
//                int len = 0;
//                while ((len = in.read(buffer)) >= 0) {
//                    out.write(buffer, 0, len);
//                }
//                out.flush();
//            }
//            finally {
//                fos.getFD().sync();
//                out.close();
//                c.disconnect();
//            }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent downloadComplete = new Intent(DOWNLOAD_COMPLETE);
            downloadComplete.putExtra("uri",uri.toString());
            downloadComplete.putExtra("selectedItem", i.getIntExtra("selectedItem",0));
            LocalBroadcastManager.getInstance(this).sendBroadcast(downloadComplete);

//        } catch (IOException e2) {
//            Log.e(getClass().getName(), "Exception durante download", e2);
//        }
    }
}