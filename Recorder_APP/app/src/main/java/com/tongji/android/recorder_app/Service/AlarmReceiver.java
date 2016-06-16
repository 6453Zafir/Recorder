package com.tongji.android.recorder_app.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

import com.tongji.android.recorder_app.Activity.LoginActivity;
import com.tongji.android.recorder_app.Activity.MainActivity;
import com.tongji.android.recorder_app.R;

/**
 * Created by 重书 on 2016/6/13.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        //Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

////        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        Ringtone r = RingtoneManager.getRingtone(context, notification);
//        r.play();

        NotificationManager mNManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent it = new Intent(context, MainActivity.class);
        PendingIntent pit = PendingIntent.getActivity(context, 0, it, 0);
        //设置图片,通知标题,发送时间,提示方式等属性
        Notification notify= new Notification.Builder(context)
                .setSmallIcon(R.mipmap.logo) // 设置状态栏中的小图片，尺寸一般建议在24×24， 这里也可以设置大图标
                .setContentTitle("Recorder notify")                        //标题
                .setContentText("It's time to check your habit!")      //内容
                .setSubText("GO")                    //内容下面的一小段文字
                .setTicker("Notification Message from Recorder")             //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())           //设置通知时间
                //.setSmallIcon(R.drawable.logo)            //设置小图标
                //   .setLargeIcon(LargeBitmap)                     //设置大图标
                //   .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                //   .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.biaobiao))  //设置自定义的提示音
                .setAutoCancel(true)                         //设置点击后取消Notification
                .setContentIntent(pit)
                .getNotification();


        mNManager.notify(1, notify);
        Vibrator vibrator = (Vibrator)context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }
}