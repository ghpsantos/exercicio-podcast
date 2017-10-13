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
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

        protected static final String TAG = "FeedPreferenceFragment";
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;
        private Preference feedLinkPref;
        private JobScheduler jobScheduler;
        private ListPreference timeToLoadPref;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            jobScheduler = (JobScheduler) getContext().getSystemService(JOB_SCHEDULER_SERVICE);

            // carrega preferences de um recurso XML em /res/xml
            addPreferencesFromResource(R.xml.preferences);

            // pega o valor atual de FeedLink
            feedLinkPref = (Preference) getPreferenceManager().findPreference(FEED_LINK);
            timeToLoadPref = (ListPreference)getPreferenceManager().findPreference(TIME_TO_LOAD);

            //setting values
            timeToLoadPref.setEntries(R.array.time_names);
            timeToLoadPref.setEntryValues(R.array.time_values);

            // cria listener para atualizar summary ao modificar link do feed
            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    feedLinkPref.setSummary(sharedPreferences.getString(FEED_LINK, getActivity().getResources().getString(R.string.feed_link)));
                }
            };

            // pega objeto SharedPreferences gerenciado pelo PreferenceManager deste fragmento
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

            // registra o listener no objeto SharedPreferences
            prefs.registerOnSharedPreferenceChangeListener(mListener);

            // força chamada ao metodo de callback para exibir link atual
            mListener.onSharedPreferenceChanged(prefs, FEED_LINK);

            Preference button = getPreferenceManager().findPreference("scheduler_button");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                //                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    agendarJob();
                    return true;
                }
            });

            Preference cancel_button = getPreferenceManager().findPreference("cancel_scheduler_button");

            cancel_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    cancelarJobs();
                    Toast.makeText(getContext(), "Agendamento cancelado", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }

        private void agendarJob() {

            JobInfo.Builder b = new JobInfo.Builder(JOB_ID, new ComponentName(getContext(), DownloadAndPersistJob.class));
            PersistableBundle pb = new PersistableBundle();
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

            pb.putString(FEED_LINK, prefs.getString(FEED_LINK, ""));
            b.setExtras(pb);

            //criterio de rede
            b.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

            //define intervalo de periodicidade
            long timeInMillis = Long.parseLong(timeToLoadPref.getValue());
            b.setPeriodic(timeInMillis);

            //exige (ou nao) que esteja conectado ao carregador
            b.setRequiresCharging(false);

            //persiste (ou nao) job entre reboots
            //se colocar true, tem que solicitar permissao action_boot_completed
            b.setPersisted(false);

            //exige (ou nao) que dispositivo esteja idle
            b.setRequiresDeviceIdle(false);

            jobScheduler.schedule(b.build());

            //to setando 15 minutos mas por algum motivo ele tá executando instantaneamente(mas ainda fica registrado, verifiquei no debug), não consegui referencias pra resolver.
            Toast.makeText(getContext(), "Job Service Agendado", Toast.LENGTH_SHORT).show();
        }

        private void cancelarJobs() {
            jobScheduler.cancel(JOB_ID);
        }
    }


}