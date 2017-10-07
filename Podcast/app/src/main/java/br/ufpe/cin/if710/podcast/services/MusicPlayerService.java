package br.ufpe.cin.if710.podcast.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

import br.ufpe.cin.if710.podcast.R;

public class MusicPlayerService extends Service {
    private final String TAG = "MusicPlayerNoBindingService";

    private MediaPlayer mPlayer;
    private int mStartID;

    @Override
    public void onCreate() {
        super.onCreate();

//         configurar media player
//        Nine Inch Nails Ghosts I-IV is licensed under a Creative Commons Attribution Non-Commercial Share Alike license.
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
                mPlayer.reset();
            } else {
                try {
                    mPlayer.setDataSource(this, intent.getData());
                    mPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // inicia musica
                mPlayer.start();
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