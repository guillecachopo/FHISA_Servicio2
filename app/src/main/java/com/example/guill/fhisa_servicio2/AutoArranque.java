package com.example.guill.fhisa_servicio2;

/**
 * Created by guill on 19/09/2017.
 */

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class AutoArranque extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ComponentName cp = new ComponentName(context.getPackageName(), JobSchedulerService.class.getName());
            JobInfo jb = new JobInfo.Builder(1, cp)
                    .setBackoffCriteria(4000, JobInfo.BACKOFF_POLICY_LINEAR)
                    .setPersisted(true)
                    .setPeriodic(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setRequiresCharging(false)
                    .setRequiresDeviceIdle(false)
                    .build();
        }
    }
}