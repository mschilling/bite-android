package com.move4mobile.apps.bite;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by casvd on 29-11-2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                sendNotification(remoteMessage);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) throws ExecutionException, InterruptedException {
        Log.d(TAG, "Send Notification method");
        Map<String, String> data = remoteMessage.getData();
        Log.d(TAG, data.get("type") + "");
        int type = Integer.parseInt(data.get("type"));
        switch (type) {
            case 0:
                Log.d(TAG, "TYPE: " + type);
                String title = data.get("title");
                Log.d(TAG, "title: " + title);
                String message = data.get("message");
                Log.d(TAG, "message: " + message);
                String image = data.get("image_url");
                Log.d(TAG, "image: " + image);
                String bite = data.get("bite");
                Log.d(TAG, "bite: " + bite);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_statbar)
                        .setColor(getResources().getColor(R.color.colorAccent_Light, getTheme()))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setLargeIcon(getCircleBitmap(Glide.with(this).load(image).asBitmap().into(250,250).get()))
                        .setLights(Color.RED, 1000, 1000)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVibrate(new long[0])
                        .setAutoCancel(true);

                Intent resultIntent = new Intent(this, RestaurantActivity.class);
                resultIntent.putExtra("key", bite);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack
                stackBuilder.addParentStack(RestaurantActivity.class);
                // Adds the Intent to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                // Gets a PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(type, mBuilder.build());

                break;
            case 1:
                Log.d(TAG, "TYPE: " + type);
                break;
            default:
                Log.d(TAG, "TYPE: " + type);
                break;
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //bitmap.recycle();

        return output;
    }
}
