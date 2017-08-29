package backuprestore.udr.rk.allbackuprestore.util;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ravi kumawat on 2017-02-02.
 */

public class CallLogUtility {
    public static void AddNumToCallLog(ContentResolver resolver, String strNum, int type, String timeInMiliSecond, String numberLabel, String callduration) {
        while (strNum.contains("-")) {
            strNum = strNum.substring(0, strNum.indexOf('-')) + strNum.substring(strNum.indexOf('-') + 1, strNum.length());
        }
        try {
//            SimpleDateFormat parseFormat =
//                    new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
//            timeInMiliSecond = timeInMiliSecond.replace(" IST ", " GMT+0530 ");
//            Date date = parseFormat.parse(timeInMiliSecond);

            //Date date = parseFormat.parse(timeInMiliSecond);
            // long timeInMilliseconds = date.getTime();
            ContentValues values = new ContentValues();
            values.put(CallLog.Calls.NUMBER, strNum);
            values.put(CallLog.Calls.DATE, Long.parseLong(timeInMiliSecond));
            values.put(CallLog.Calls.DURATION, callduration);
            values.put(CallLog.Calls.TYPE, type);
            values.put(CallLog.Calls.NEW, 1);
            values.put(CallLog.Calls.CACHED_NAME, "");
            values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
            values.put(CallLog.Calls.CACHED_NUMBER_LABEL, numberLabel);
            Log.d("AddToCallLog", "Inserting call log placeholder for " + strNum);

            resolver.insert(CallLog.Calls.CONTENT_URI, values);
        } catch (Exception e) {

            e.getMessage();
        }


        //getContentResolver().delete(url, where, selectionArgs)
    }

    public void DeleteNumFromCallLog(ContentResolver resolver, String strNum) {
        try {
            String strUriCalls = "content://call_log/calls";
            Uri UriCalls = Uri.parse(strUriCalls);
            //Cursor c = res.query(UriCalls, null, null, null, null);
            if (null != resolver) {
                resolver.delete(UriCalls, CallLog.Calls.NUMBER + "=?", new String[]{strNum});
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


}
