package com.yuanbin.netmonitor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.yuanbin.netmonitor.Utils.L;
import com.yuanbin.netmonitor.Utils.NetUtils;
import com.yuanbin.netmonitor.Utils.PermissionUtils;
import com.yuanbin.netmonitor.Utils.SPUtils;
import com.yuanbin.netmonitor.Utils.ServiceUtils;
import com.yuanbin.netmonitor.Utils.WeakHandler;

public class MainActivity extends AppCompatActivity {

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    L.o("handleMessage " + 1111);
                    mHandler.sendEmptyMessageDelayed(1111,5000);
                    break;
            }
            return false;
        }
    });

    Switch sw_speed_noti,sw_boot;
    Button btn_getpermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.o("MainActivity onCreate");

        initView();
        initData();
        initLis();

        PermissionUtils.getLocatePermission(this,REQUESTCODE_GPS);
    }
    public final static int REQUESTCODE_GPS = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.i(this.getClass().toString(),requestCode + " " + resultCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        L.o(1114);
        switch (requestCode) {
            case REQUESTCODE_GPS: {
                // If request is cancelled, the result arrays are empty.
                L.o(grantResults.length);
                L.o(grantResults.length > 0 ? grantResults[0] : -1);
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                } else {
                    // permission denied
                    L.showT("获取WiFi名称需要定位权限");
                    btn_getpermission.setVisibility(View.VISIBLE);
                    btn_getpermission.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionUtils.getLocatePermission(MainActivity.this,REQUESTCODE_GPS);
                        }
                    });
                }
                return;
            }
        }
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
        btn_getpermission = (Button) findViewById(R.id.btn_getpermission);
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
        L.o("MainActivity onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        L.o("MainActivity onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        L.o("MainActivity onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        L.o("MainActivity onPause");
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.o("MainActivity onDestroy");
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
