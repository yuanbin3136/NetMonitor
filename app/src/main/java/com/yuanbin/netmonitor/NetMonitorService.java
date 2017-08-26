package com.yuanbin.netmonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.yuanbin.netmonitor.Utils.BitmapUtils;
import com.yuanbin.netmonitor.Utils.L;
import com.yuanbin.netmonitor.Utils.NetUtils;
import com.yuanbin.netmonitor.Utils.TrafficUtils;
import com.yuanbin.netmonitor.Utils.WeakHandler;

public class NetMonitorService extends Service {
    private final static int WHAT_GETTRAFFIC_REPEAT = 1234;
    private long interval_getTraffic = 1000;
    NotificationManager notificationManager;
    Notification notification;
    Notification.Builder builder;
    ScreenReceiver receiver;

    WeakHandler mhandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case WHAT_GETTRAFFIC_REPEAT:

                    String speedDownText = TrafficUtils.getSpeedDownText();
                    String speedUpText = TrafficUtils.getSpeedUpText();
                    refreshNotification(speedDownText,speedUpText);
                    mhandler.sendEmptyMessageDelayed(WHAT_GETTRAFFIC_REPEAT,interval_getTraffic);
                    break;
            }
            return false;
        }
    });
    public NetMonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 开始获取流量
        TrafficUtils.reStart();

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification(this);

        //开启屏幕广播接收，熄屏时关闭
        registScreen();

    }

    private void unregistScreen(){
        if (receiver != null)
        this.unregisterReceiver(receiver);
    }
    private void registScreen() {
        receiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        //添加action
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        //注册广播接收者
        this.registerReceiver(receiver,filter);
    }

    public class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.SCREEN_OFF")){
                System.out.println("屏幕锁屏了");
                mhandler.removeMessages(WHAT_GETTRAFFIC_REPEAT);
            }else if (action.equals("android.intent.action.SCREEN_ON")){
                System.out.println("屏幕解锁了");
                mhandler.sendEmptyMessage(WHAT_GETTRAFFIC_REPEAT);
            }
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mhandler.removeMessages(WHAT_GETTRAFFIC_REPEAT);
        mhandler.sendEmptyMessage(WHAT_GETTRAFFIC_REPEAT);

        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     */
    public void showNotification(Context context) {
        builder = new Notification.Builder(context);
        notification = builder
                /**设置通知左边的大图标**/
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.mipmap.ic_launcher)
                /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("通知来了")
                /**设置通知的标题**/
                .setContentTitle("正在获取网络状态")
                /**设置通知的内容**/
                .setContentText("这是一个通知的内容这是一个通知的内容")
                /**通知产生的时间，会在通知信息里显示**/
                .setWhen(0)//System.currentTimeMillis(),填0，不需要显示时间
                /**设置该通知优先级**/
//                .setPriority(Notification.PRIORITY_DEFAULT)
                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                .setAutoCancel(true)///不太懂
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                .setOngoing(true)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
//                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
        /**发起通知**/
        notificationManager.notify(0, notification);
    }

    String last_speedDownText;
    public void refreshNotification(String speedDownText,String speedUpText){
        getText(speedDownText, speedUpText);

        notification = builder.build();
        notificationManager.notify(0, notification);
    }

    private void getText(String speedDownText, String speedUpText) {
        String title = "正在获取网络状态";
        //是否有网络
        if (NetUtils.isConnected(this)){
            //如果是wifi
            if (NetUtils.isWiFi(this)){
                //获取ssid
                String ssid =  NetUtils.getWifiConnectionInfo(this).getSSID();
                title = "WiFi：" + ssid.replace("\"","");
            }else{
                String TypeName = NetUtils.getNetworkTypeName(this);
                title = "移动数据网络";// + TypeName; 不准确，暂不使用
            }
        }else {
            title = "无网络";
            speedDownText = "0B";
            speedUpText = "0B";
        }
        // 检查是否需要重新生成小icon
        if(!speedDownText.equals(last_speedDownText)){
            if (title.equals("无网络")){
                builder.setSmallIcon(Icon.createWithBitmap(BitmapUtils.createBitmapWithText("0KB")));
            }else {
                builder.setSmallIcon(Icon.createWithBitmap(BitmapUtils.createBitmapWithText(speedDownText)));
            }
        }

        builder.setContentTitle(title);
        builder.setContentText("当前下载速度：" + speedDownText + "/s      "
                + "当前上传速度：" + speedUpText + "/s");
    }

    @Override
    public void onDestroy() {
        mhandler.removeCallbacksAndMessages(null);
        notificationManager.cancelAll();
        unregistScreen();
        super.onDestroy();
    }
}
