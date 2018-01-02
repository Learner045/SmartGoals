package com.example.shreya.smartgoals.Services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;

import com.example.shreya.smartgoals.ActivityMain;
import com.example.shreya.smartgoals.R;
import com.example.shreya.smartgoals.beans.Goal;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        Realm realm=null;
        try{
            realm=Realm.getDefaultInstance();
            RealmResults<Goal> results=realm.where(Goal.class).equalTo("completed",false).findAll();//find async not reqd because service works in background anyway

            for(Goal current:results){
                if(isNotificationNeeded(current.getAdded(),current.getWhen())){
                    fireNotification(current);
                }
            }

        }finally{
            if(realm!=null)
                realm.close();
        }
    }

    private void fireNotification(Goal current) {

        String message= getString(R.string.notif_messg)+ "\""+current.getWhat()+"\"";
        PugNotification.with(this) //service extends from context indirectly
                .load()
                .title(R.string.notif_title)
                .message(message)
                .bigTextStyle(R.string.notif_long_messg)
                .smallIcon(R.drawable.pugnotification_ic_launcher)
                .largeIcon(R.drawable.pugnotification_ic_launcher)
                .flags(Notification.DEFAULT_ALL)
                .autoCancel(true)
                .click(ActivityMain.class)
                .simple()
                .build();
    }

    private boolean isNotificationNeeded(long added, long when){

        long now=System.currentTimeMillis();
        if(now>when){
            return false; //goal already expired
        }else{
            long diff90= (long) (0.9*(when-added));//90% of time has passed or elapsed
            return now > (added+diff90) ? true : false; //90% of time has elapsed

        }

    }

}
