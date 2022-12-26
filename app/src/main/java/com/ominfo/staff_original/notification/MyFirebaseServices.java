package com.ominfo.staff_original.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ominfo.staff_original.R;
import com.ominfo.staff_original.interfaces.SharedPrefKey;
import com.ominfo.staff_original.ui.dashboard.DashbooardActivity;
import com.ominfo.staff_original.util.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MyFirebaseServices extends FirebaseMessagingService {

    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID ="101" ;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference, mRef;
    int mAppCommunityMinutes;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    //save App community minutes to firebase db
    private void setAppCommunityMinutes(){
        Date mDate = new Date();
        String mCurrentDate = new SimpleDateFormat("yyyy-MM").format(mDate);
        // mCurrentDate = "2021-03-19";
        mRef = mDatabase.getReference("community-minutes");
        mDatabaseReference = mDatabase.getReference("community-minutes").child(mCurrentDate);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    //if date entry exits then create new entry
                    long value = snapshot.getValue(Long.class);
                    mAppCommunityMinutes = (int) value;
                    if(mAppCommunityMinutes==0){
                        //if no session added then init with 1 min for completed session
                        mDatabaseReference.setValue(1);
                    }
                    else {
                        //add 1 minutes for every complete session
                        mDatabaseReference.setValue(mAppCommunityMinutes +1);
                    }
                }
                catch (NullPointerException e){
                    //if date entry does not exits then create new entry
                    e.printStackTrace();
                    mDatabaseReference = mRef.child(mCurrentDate);
                    mDatabaseReference.setValue(1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static final String NOTIFICATION_REPLY = "NotificationReply";
    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.getDefault()).format(now));
        return id;
    }
    private void showNotification(String title,String message){
        int valButton = 1;//( int ) System.currentTimeMillis () ;
        Intent intent = new Intent(this, DashbooardActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //SharedPref.getInstance(this).write(SharedPrefKey.IS_NOTIFY, true);
        //String iSNotify = SharedPref.getInstance(getApplicationContext()).read(SharedPrefKey.IS_NOTIFY_COUNT, "0");
        //setAppCommunityMinutes();
        //SharedPref.getInstance(this).write(SharedPrefKey.IS_NOTIFY_COUNT, String.valueOf(Integer.parseInt(iSNotify)+1));
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent;
        //= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE/* or PendingIntent.FLAG_UPDATE_CURRENT*/);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        }else {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
//Create an Intent for the BroadcastReceiver
        Intent buttonIntent = new Intent(this, ButtonReceiver.class);
        buttonIntent.putExtra("notificationId",valButton);
        //Create the PendingIntent
        PendingIntent btPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btPendingIntent =  PendingIntent.getBroadcast(this,
                    valButton, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        }else {
            btPendingIntent =  PendingIntent.getBroadcast(this,
                    valButton, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_logo_main)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                //.setCategory(NotificationCompat.CATEGORY_MESSAGE)
                //.setColor(Color.BLUE)
                //.setAutoCancel(true)
                //.setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_close_whitee, "Dismiss", btPendingIntent)
                .setOngoing(true)
                //.setDeleteIntent(getDeleteIntent()) ;
                .setAutoCancel(false)
                .setWhen(0);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());//NotificationManager.cancel(_notificationId);
    }

    protected PendingIntent getDeleteIntent () {
        Intent intent = new Intent( this,
                ButtonReceiver. class ) ;
        intent.setAction( "notification_cancelled" ) ;
        return PendingIntent.getBroadcast ( this, 0 , intent , PendingIntent. FLAG_CANCEL_CURRENT ) ;
    }
}
