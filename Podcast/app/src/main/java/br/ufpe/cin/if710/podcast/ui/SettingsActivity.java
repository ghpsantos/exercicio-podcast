package br.ufpe.cin.if710.podcast.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.managers.jobscheduler.DownloadAndPersistJob;
//import br.ufpe.cin.if710.podcast.managers.jobscheduler.DownloadAndPersistJob;

public class SettingsActivity extends Activity {
    public static final String FEED_LINK = "feedlink";
    public static final String TIME_TO_LOAD = "timeToLoad";
    public static final int JOB_ID = 710;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class FeedPreferenceFragment extends PreferenceFragment {

        private Preference timeToLoadPref;
//        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            jobScheduler = (JobScheduler) getContext().getSystemService(JOB_SCHEDULER_SERVICE);

            // carrega preferences de um recurso XML em /res/xml
            addPreferencesFromResource(R.xml.preferences);

            // pega o valor atual de FeedLink
            feedLinkPref = (Preference) getPreferenceManager().findPreference(FEED_LINK);
            timeToLoadPref = (Preference) getPreferenceManager().findPreference(TIME_TO_LOAD);

            // cria listener para atualizar summary ao modificar link do feed
            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    feedLinkPref.setSummary(sharedPreferences.getString(FEED_LINK, getActivity().getResources().getString(R.string.feed_link)));
                    timeToLoadPref.setSummary(sharedPreferences.getString(TIME_TO_LOAD,"200"));
                }
            };

            // pega objeto SharedPreferences gerenciado pelo PreferenceManager deste fragmento
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

            // registra o listener no objeto SharedPreferences
            prefs.registerOnSharedPreferenceChangeListener(mListener);

            // força chamada ao metodo de callback para exibir link atual
            mListener.onSharedPreferenceChanged(prefs, FEED_LINK);
            mListener.onSharedPreferenceChanged(prefs, TIME_TO_LOAD);

            Preference button = getPreferenceManager().findPreference("scheduler_button");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    agendarJob();
                    Toast.makeText(getContext(), "Works", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
        protected static final String TAG = "FeedPreferenceFragment";
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;
        private Preference feedLinkPref;


        static protected JobScheduler jobScheduler;

//        @RequiresApi(api = Build.VERSION_CODES.N)
        private void agendarJob() {

            JobInfo.Builder b = new JobInfo.Builder(JOB_ID, new ComponentName(getContext(), DownloadAndPersistJob.class));
            PersistableBundle pb=new PersistableBundle();
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

            pb.putString(FEED_LINK,prefs.getString(FEED_LINK,""));
            b.setExtras(pb);

            //criterio de rede
            b.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            //b.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);

            //define intervalo de periodicidade
//            Toast.makeText(getContext(), prefs.getString(TIME_TO_LOAD,"3000") , Toast.LENGTH_SHORT).show();

//            b.setPeriodic(Long.parseLong(prefs.getString(TIME_TO_LOAD, "3000")),3000);

            //exige (ou nao) que esteja conectado ao carregador
            b.setRequiresCharging(false);

            //persiste (ou nao) job entre reboots
            //se colocar true, tem que solicitar permissao action_boot_completed
            b.setPersisted(false);

            //exige (ou nao) que dispositivo esteja idle
            b.setRequiresDeviceIdle(false);

            //backoff criteria (linear ou exponencial)
            //b.setBackoffCriteria(1500, JobInfo.BACKOFF_POLICY_EXPONENTIAL);

            //periodo de tempo minimo pra rodar
            //so pode ser chamado se nao definir setPeriodic...
            b.setMinimumLatency(Integer.parseInt(prefs.getString(TIME_TO_LOAD,"3000")));

            //mesmo que criterios nao sejam atingidos, define um limite de tempo
            //so pode ser chamado se nao definir setPeriodic...
//            b.setOverrideDeadline(Integer.parseInt(prefs.getString(TIME_TO_LOAD,"3000")));

             jobScheduler.schedule(b.build());
//            if (ret == JobScheduler.RESULT_SUCCESS) {
//                Log.e(TAG, "Job scheduled successfully");
//            } else {
//                Log.e(TAG, "Job scheduling failed");
//            }
            Toast.makeText(getContext(), "Job Service Agendado", Toast.LENGTH_SHORT).show();
        }
    }


}