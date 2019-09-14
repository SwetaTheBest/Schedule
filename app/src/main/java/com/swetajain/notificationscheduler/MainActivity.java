package com.swetajain.notificationscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private JobScheduler mJobScheduler;
    public static final int JOB_ID = 100;
    private Switch idleSwitch, chargingSwitch;
    private SeekBar mSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idleSwitch = findViewById(R.id.idle_switch);
        chargingSwitch = findViewById(R.id.charging_switch);
        mSeekBar = findViewById(R.id.seek_bar);
        final TextView seekBarProgress = findViewById(R.id.set_t_v);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0) {
                    seekBarProgress.setText(String.valueOf(progress));
                } else {
                    seekBarProgress.setText(getString(R.string.not_set));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void scheduleJob(View view) {
        int seekBarInteger = mSeekBar.getProgress();
        boolean isSeekBarSet = seekBarInteger > 0;

        int selectedNetworkId = JobInfo.NETWORK_TYPE_NONE;
        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        RadioGroup radioGroupNetworkType = findViewById(R.id.network_r_g);
        switch (radioGroupNetworkType.getCheckedRadioButtonId()) {
            case R.id.any_r_b:
                selectedNetworkId = JobInfo.NETWORK_TYPE_ANY;
                Toast.makeText(this,
                        getString(R.string.any),
                        Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.none_r_b:
                selectedNetworkId = JobInfo.NETWORK_TYPE_NONE;
                Toast.makeText(this,
                        getString(R.string.none),
                        Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.wifi_r_b:
                selectedNetworkId = JobInfo.NETWORK_TYPE_UNMETERED;
                Toast.makeText(this,
                        getString(R.string.wi_fi),
                        Toast.LENGTH_SHORT)
                        .show();
                break;
        }

        ComponentName componentName =
                new ComponentName(getPackageName(),
                        NotificationJobScheduler.class.getName());
        if (selectedNetworkId != JobInfo.NETWORK_TYPE_NONE
                || chargingSwitch.isChecked()
                || idleSwitch.isChecked()
                || isSeekBarSet) {
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
            builder.setRequiredNetworkType(selectedNetworkId);
            builder.setRequiresCharging(chargingSwitch.isChecked());
            builder.setRequiresDeviceIdle(idleSwitch.isChecked());
            if (isSeekBarSet) {
                builder.setOverrideDeadline(seekBarInteger * 1000);
            }
            mJobScheduler.schedule(builder.build());
            Toast.makeText(this,
                    "Job Scheduled, job will run when \nthe constraints are met.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,
                    "Please select some constraint other than none.",
                    Toast.LENGTH_SHORT).show();
        }


    }

    public void cancelJob(View view) {

        if (mJobScheduler != null) {
            mJobScheduler.cancelAll();
            mJobScheduler = null;
            Toast.makeText(this,
                    "All jobs are cancelled!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
