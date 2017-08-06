package com.yuanbin.netmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuanbin.netmonitor.Utils.L;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        L.out("Main2Activity onCreate");
    }

    @Override
    protected void onStart() {
        L.out("Main2Activity onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        L.out("Main2Activity onResume");
        super.onResume();
    }
}
