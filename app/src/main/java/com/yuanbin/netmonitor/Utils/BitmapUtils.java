package com.yuanbin.netmonitor.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by John on 2017/8/5.
 */

public class BitmapUtils {
    public static Bitmap createBitmapWithText(String text) {

        String number = text.replaceAll("[^0-9]", "");
        String units = text.replaceAll("[^a-z^A-Z]", "");

        int length = 100;
        float size_number = length * 3 / 4;
        float size_kb = length * 5 / 12;
        Bitmap whiteBgBitmap = Bitmap.createBitmap(length, length,
                Bitmap.Config.ARGB_8888);

        //2、设置画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);//设置画笔颜色
        paint.setStrokeWidth(60.0f);// 设置画笔粗细

        //适配下三位数字的显示
        if(number.length() > 2){
            paint.setTextSize(size_number * 5 / 6);//设置文字大小
        }else {
            paint.setTextSize(size_number);//设置文字大小
        }
        paint.setTextAlign(Paint.Align.CENTER);
        Canvas canvas = new Canvas(whiteBgBitmap);
//        canvas.drawColor(Color.GRAY);
        //字儿，x坐标，y坐标,画笔 ,坐标点好像是指文字左下角
        canvas.drawText(number, length / 2 , length * 3 / 5, paint);
        paint.setTextSize(size_kb);//设置文字大小
        canvas.drawText(units + "/s", length / 2, length, paint);
//        canvas.drawLine(100, 300, 300, 300, paint);
//        canvas.drawCircle(200, 500, 100, paint);
//        canvas.drawBitmap(iv_test, 0, 0, null);
        return whiteBgBitmap;
    }
}
