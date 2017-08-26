package com.yuanbin.netmonitor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.yuanbin.netmonitor.Utils.L;
import com.yuanbin.netmonitor.Utils.SPUtils;
import com.yuanbin.netmonitor.Utils.ServiceUtils;
import com.yuanbin.netmonitor.Utils.WeakHandler;

public class MainActivity extends AppCompatActivity {

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    L.out("handleMessage " + 1111);
                    mHandler.sendEmptyMessageDelayed(1111,5000);
                    break;
            }
            return false;
        }
    });

    Switch sw_speed_noti,sw_boot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.out("MainActivity onCreate");

        initView();
        initData();
        initLis();
    }

    private void initLis() {
        sw_speed_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //startService;
                    startMonitorService();
                }else{
                    //stopService
                    stopMonitorService();
                }
                SPUtils.put(MainActivity.this,SPUtils.SP_MONITOR_MONITORING,isChecked);
                sw_boot.setEnabled(isChecked);
            }
        });
        sw_boot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.put(MainActivity.this,SPUtils.SP_MONITOR_BOOT,isChecked);
            }
        });
    }

    private void initData() {
        //        L.out("NetMonitorService.class.getName() " + NetMonitorService.class.getName());
        boolean monitoring = (boolean) SPUtils.get(this,SPUtils.SP_MONITOR_MONITORING,false);
        boolean boot = (boolean) SPUtils.get(this,SPUtils.SP_MONITOR_BOOT,false);

        sw_boot.setChecked(boot);
        sw_speed_noti.setChecked(monitoring);
        if (!monitoring){
            sw_boot.setEnabled(false);
        }else {
            if(!ServiceUtils.isServiceRunning(this,NetMonitorService.class.getName())){
                startMonitorService();
            }
        }

    }

    private void initView() {
        sw_speed_noti  = (Switch) findViewById(R.id.sw_speed_noti);
        sw_boot  = (Switch) findViewById(R.id.sw_boot);
    }

    private void startMonitorService(){
        ServiceUtils.startMonitorService(this);
    }
    private void stopMonitorService(){
        ServiceUtils.stopMonitorService(this);
    }

    private void one2two() {
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        L.out("MainActivity onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        L.out("MainActivity onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        L.out("MainActivity onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        L.out("MainActivity onPause");
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.out("MainActivity onDestroy");
//        stopMyService();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //toAutherActivity
            Intent intent = new Intent(this,AutherActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
