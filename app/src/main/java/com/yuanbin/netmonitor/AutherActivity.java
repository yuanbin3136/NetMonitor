package com.yuanbin.netmonitor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.yuanbin.netmonitor.Utils.VersionUtils;

public class AutherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auther);
        setTitle();


        findViewById(R.id.tv_auther).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.app_auther_weibo)));
                startActivity(intent);
            }
        });
    }

    private void setTitle() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(getString(R.string.app_name) + " " + VersionUtils.getAppVersionName(this)
        + "\n" + VersionUtils.getAppVersionCode(this));

        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
