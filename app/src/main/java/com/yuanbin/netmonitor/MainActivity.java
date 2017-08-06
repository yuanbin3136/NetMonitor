package com.yuanbin.netmonitor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.yuanbin.netmonitor.Utils.BitmapUtils;
import com.yuanbin.netmonitor.Utils.L;
import com.yuanbin.netmonitor.Utils.ServiceUtils;
import com.yuanbin.netmonitor.Utils.WeakHandler;

import java.util.Random;

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

    Switch sw_speed_noti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.out("MainActivity onCreate");

        String a ="http://img.jandan.net/news/ ";
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////                one2two();
//                super.run();
//            }
//        }.start();
        sw_speed_noti  = (Switch) findViewById(R.id.sw_speed_noti);


        L.out("MyService.class.getName() " + MyService.class.getName());
        if(ServiceUtils.isServiceRunning(this,MyService.class.getName())){
            sw_speed_noti.setChecked(true);
        }
        sw_speed_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //startService;
                    startMyService();
                }else{
                    //stopService
                    stopMyService();
                }
            }
        });
    }

    private void startMyService(){
        Intent intent = new Intent(MainActivity.this,MyService.class);
        MainActivity.this.startService(intent);
    }
    private void stopMyService(){
        Intent intent = new Intent(MainActivity.this,MyService.class);
        stopService(intent);
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
