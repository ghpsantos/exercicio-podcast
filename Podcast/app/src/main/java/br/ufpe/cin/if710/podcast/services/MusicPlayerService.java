package br.ufpe.cin.if710.podcast.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;


public class MusicPlayerService extends Service {
    private final String TAG = "MusicPlayerNoBindingService";
    public static final String MUSIC_PAUSED = "br.ufpe.cin.if710.services.action.MUSIC_PAUSED";
    public static final String MUSIC_ENDED = "br.ufpe.cin.if710.services.action.MUSIC_ENDED";

    private MediaPlayer mPlayer;
    private int itemPlaying;

    @Override
    public void onCreate() {
        super.onCreate();

//         configurar media player
        mPlayer = new MediaPlayer();

        //nao deixa entrar em loop
        mPlayer.setLooping(false);

        // executa o release do player quando terminar a musica
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent musicEnded = new Intent(MUSIC_ENDED);
                musicEnded.putExtra("itemPlaying", itemPlaying);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(musicEnded);
                //reseting media Player
                mPlayer = new MediaPlayer();
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null != mPlayer) {
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
//                    Podcast Position
                    int currentPosition = intent.getIntExtra("currentPosition",0);
//                    itemPlaying >-
                    itemPlaying =  intent.getIntExtra("selectedPosition",0);
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