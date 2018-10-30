package com.yuanbin.netmonitor.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.yuanbin.netmonitor.BaseApp;
import com.yuanbin.netmonitor.BuildConfig;


/**
 * Created by Shayne.xu on 2018/3/5.
 */

public class L {
    private static final int LOG_MAXLENGTH = 2000;
    static String TAG = "wind: ";
    static boolean isPrintLog = true;

    public static void o(Object o){
//        System.out.println(TAG + o);
        o(getVersion() + " debug: " + BuildConfig.DEBUG + " " +TAG
                ,getPrefix() + getTrace() + o);
    }

    public static void i(String tag,String msg){
        Log.i(tag,msg);
    }

    public static void o(String tagName, String msg) {
        if (isPrintLog) {
            if (tagName == null || tagName.length() == 0
                    || msg == null || msg.length() == 0)
                return;

            int segmentSize = LOG_MAXLENGTH;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                System.out.println(tagName + msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    System.out.println(tagName + logContent);
                }
                System.out.println(tagName + msg);// 打印剩余日志
            }
        }
    }
    private static Context context;
    public static void setContext(Context context) {
        L.context = context.getApplicationContext();
    }

    public static Toast toast;
    public static void showT(String s){
        if (context != null){
            if (toast == null){
                toast = Toast.makeText(context,s, Toast.LENGTH_SHORT);
            }else {
                toast.setText(s);
            }
            toast.show();
        }
    }
    private static String getPrefix() {
        Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        long threadId = currentThread.getId();
        String prefix = "thread: " + threadName + "(" + threadId + "):";
        return prefix;
    }

    private static String getTrace() {
        try {
            Throwable t = new Throwable();
            StackTraceElement trace = t.getStackTrace()[2];
            String method = trace.getClassName() + "." + trace.getMethodName()
                    + "[" + trace.getLineNumber() + "]line\n";
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String getVersion(){
        int versionCode = VersionUtils.getAppVersionCode(BaseApp.getContext());
        return versionCode + "";
    }
}