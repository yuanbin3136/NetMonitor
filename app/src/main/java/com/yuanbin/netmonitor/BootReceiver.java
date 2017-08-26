package com.yuanbin.netmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yuanbin.netmonitor.Utils.L;
import com.yuanbin.netmonitor.Utils.SPUtils;
import com.yuanbin.netmonitor.Utils.ServiceUtils;

public class BootReceiver extends BroadcastReceiver {
    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (ACTION_BOOT.equals(intent.getAction())){
//            L.out();
            boolean monitoring = (boolean) SPUtils.get(context,SPUtils.SP_MONITOR_MONITORING,false);
            boolean boot = (boolean) SPUtils.get(context,SPUtils.SP_MONITOR_BOOT,false);

            if (monitoring && boot){
                ServiceUtils.startMonitorService(context);
            }
        }
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
