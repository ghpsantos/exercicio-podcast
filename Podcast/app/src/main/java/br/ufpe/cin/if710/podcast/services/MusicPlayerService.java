package br.ufpe.cin.if710.podcast.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

import static br.ufpe.cin.if710.podcast.R.id.items;

public class MusicPlayerService extends Service {
    private final String TAG = "MusicPlayerNoBindingService";
    public static final String MUSIC_PAUSED = "br.ufpe.cin.if710.services.action.MUSIC_PAUSED";

    private MediaPlayer mPlayer;
    private int mStartID;

    @Override
    public void onCreate() {
        super.onCreate();

//         configurar media player
        mPlayer = new MediaPlayer();

        //nao deixa entrar em loop
        mPlayer.setLooping(false);

        // encerrar o service quando terminar a musica
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // encerra se foi iniciado com o mesmo ID
                stopSelf(mStartID);
            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null != mPlayer) {
            // ID para o comando de start especifico
            mStartID = startId;

            /**/
            //se ja esta tocando...
            if (mPlayer.isPlaying()) {
                int currentPosition = mPlayer.getCurrentPosition();
                mPlayer.reset();
                Intent musicPaused = new Intent(MUSIC_PAUSED);
                musicPaused.putExtra("selectedPosition", intent.getIntExtra("selectedPosition",0));
                musicPaused.putExtra("currentPosition", currentPosition);
                LocalBroadcastManager.getInstance(this).sendBroadcast(musicPaused);
            } else {
                try {
                    mPlayer.setDataSource(this, intent.getData());
                    mPlayer.prepare();
                    int currentPosition = intent.getIntExtra("currentPosition",0);
                    mPlayer.seekTo(currentPosition);
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // inicia musica

            }
        }
        // nao reinicia service automaticamente se for eliminado
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
        }

    }

    //nao eh possivel fazer binding com este service
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }
}