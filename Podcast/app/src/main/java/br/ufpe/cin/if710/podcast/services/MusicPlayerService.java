package br.ufpe.cin.if710.podcast.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

public class MusicPlayerService extends Service {
    private final String TAG = "MusicPlayerNoBindingService";
    public static final String MUSIC_PAUSED = "br.ufpe.cin.if710.services.action.MUSIC_PAUSED";
    public static final String MUSIC_ENDED = "br.ufpe.cin.if710.services.action.MUSIC_ENDED";

    private MediaPlayer mPlayer;
    private int itemPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        //init player
        mPlayer = new MediaPlayer();

        //nao deixa entrar em loop
        mPlayer.setLooping(false);

        // when music end, send broadcast music_ended and reset player
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
            //if music is playing, reset player(pause), get current position and send broadcast
            if (mPlayer.isPlaying()) {
                int currentPosition = mPlayer.getCurrentPosition();
                mPlayer.reset();
                Intent musicPaused = new Intent(MUSIC_PAUSED);
                musicPaused.putExtra("selectedPosition", intent.getIntExtra("selectedPosition", 0));
                musicPaused.putExtra("currentPosition", currentPosition);
                LocalBroadcastManager.getInstance(this).sendBroadcast(musicPaused);
            } else {
                try {
                    //set player with current position and start playing.
                    mPlayer.setDataSource(this, intent.getData());
                    mPlayer.prepare();
                    int currentPosition = intent.getIntExtra("currentPosition", 0);
                    itemPlaying = intent.getIntExtra("selectedPosition", 0);
                    mPlayer.seekTo(currentPosition);
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}