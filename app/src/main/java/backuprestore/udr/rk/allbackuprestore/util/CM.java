package backuprestore.udr.rk.allbackuprestore.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import backuprestore.udr.rk.allbackuprestore.BaseApp;
import backuprestore.udr.rk.allbackuprestore.R;

import static backuprestore.udr.rk.allbackuprestore.BaseApp.sqLiteDatabase;


public class CM {


    private static final String LOG_TAG = "Internet";

    /**
     * Getting Typeface Passing attributes(Font name)
     *
     * @param ctx
     * @param attrs
     * @return
     */
    public static Typeface getTypeFace(Context ctx, AttributeSet attrs) {
        Typeface myTypeface = null;
        if (attrs != null) {
            TypedArray a = ctx.obtainStyledAttributes(attrs,
                    R.styleable.mtplviews);
            String fontName = a.getString(R.styleable.mtplviews_TypeFace);


            if (fontName != null) {
                if (fontName.equalsIgnoreCase(ctx.getString(R.string.FontFace_DroidSerif_Bold))) {
                    myTypeface = ((BaseApp) ctx.getApplicationContext()).mTypeFaceRobotoBlack;
                } else if (fontName.equalsIgnoreCase(ctx.getString(R.string.FontFace_DroidSerif_Regular))) {
                    myTypeface = ((BaseApp) ctx.getApplicationContext()).mTypeFaceRobotoLight;
                }
            } else {
                myTypeface = ((BaseApp) ctx.getApplicationContext()).mTypeFaceRobotoLight;
            }

            a.recycle();
        }
        return myTypeface;
    }

    /**
     * Checking Internet is available or not
     *
     * @param context
     * @return
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void shareApp(Context mContext) {
        String shareBody = "Check out the new Online Food Ordering Android App " + mContext.getString(R.string.app_name);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite Using");
        String link = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody + " " + link);
        mContext.startActivity(Intent.createChooser(sharingIntent,
                "Share Using..."));
    }

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("http://play.google.com/store/apps/details?id="
                            + context.getPackageName())));
        }
    }

    public static void setSp(Context activity, String key, Object value) {
        SharedPreferences prefs = activity.getSharedPreferences(
                activity.getPackageName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }

        editor.commit();
        editor = null;
        prefs = null;

    }

    ///*** hide keyboard **///

    public static void hideKeyboard(Activity currentActivity) {
        InputMethodManager inputManager = (InputMethodManager) currentActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null && (currentActivity.getCurrentFocus() != null)) {
            inputManager.hideSoftInputFromWindow(currentActivity
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String getUDID(final Context activity) {
        String android_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    /**
     * returns object of specific type from SharedPrefs
     *
     * @param activity
     * @param key          name of data in SP
     * @param defaultValue used if no value available for this key
     * @return
     */
    public static Object getSp(Context activity, String key, Object defaultValue) {
        SharedPreferences prefs = activity.getSharedPreferences(
                activity.getPackageName(), Activity.MODE_PRIVATE);
        if (defaultValue instanceof String) {
            return prefs.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return prefs.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return prefs.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            return prefs.getLong(key, (Long) defaultValue);
        } else {
            return prefs.getFloat(key, (Float) defaultValue);
        }

    }

    // call this method when you don't have any data via intent
    public static void startActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_from_left,
                R.anim.push_out_to_right);

    }

    // call this method when you have to pass data in intent
    public static void startActivity(Intent intent, Activity activity) {

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_from_left,
                R.anim.push_out_to_right);

    }

    // call this method when you have to pass data in intent
    public static void startActivityForResult(Intent intent, int resultcode, Activity activity) {

        activity.startActivityForResult(intent, resultcode);
        activity.overridePendingTransition(R.anim.push_in_from_left,
                R.anim.push_out_to_right);

    }

    public static void finishActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(0, R.anim.push_out_to_left);
    }

    public static String converDateFormate(String oldpattern,
                                           String newPattern, String dateString) {
        // SimpleDateFormat sdf = new SimpleDateFormat(oldpattern);
        SimpleDateFormat sdf = new SimpleDateFormat(oldpattern,
                CV.LOCALE_USE_DATEFORMAT);
        Date testDate = null;
        try {
            if (dateString.contains("IST")) {
                dateString = dateString.replace(" IST ", " GMT ");
            }
            testDate = sdf.parse(dateString);
            SimpleDateFormat formatter = new SimpleDateFormat(newPattern,
                    CV.LOCALE_USE_DATEFORMAT);
            String newFormat = formatter.format(testDate);
            System.out.println("" + newFormat);
            return newFormat;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static int getDeviceWidth(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static void dialogBox(final Activity activity,
                                 final String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(R.string.app_name);
        alertDialogBuilder.setMessage(title);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });

//        alertDialogBuilder.setNegativeButton("cancel",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /**
     * Loading Image Progress
     */
    public static void loadImageProgess(ImageView imgProgress, Activity activity) {
        imgProgress.setVisibility(View.VISIBLE);
        Animation a = AnimationUtils.loadAnimation(activity, R.anim.scale);
        a.setDuration(1000);
        imgProgress.startAnimation(a);

        a.setInterpolator(new Interpolator() {
            private final int frameCount = 8;

            @Override
            public float getInterpolation(float input) {
                return (float) Math.floor(input * frameCount) / frameCount;
            }
        });
    }

    public static String Validation(String[] edittextName, TextView... edt) {
        String message = null;
        for (int i = 0; i < edt.length; i++) {
            if (edt[i].getText().length() > 0) {
                message = CV.Valid;
            } else {
                message = edittextName[i] + " is required.";
                break;
            }
        }
        return message;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }


    /// *** dialogue *** ////

    public static void ShowDialogue(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

//    public static <T> T JsonParse(T t, String response)
//            throws JsonSyntaxException, IOException, XmlPullParserException {
//        InputStream in = new ByteArrayInputStream(response.getBytes());
//        JsonReader reader;
//        reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
//        GsonBuilder b = new GsonBuilder();
//        b.registerTypeAdapter(boolean.class, new BooleanSerializer());
//        Gson gson = b.create();
//        t = (T) gson.fromJson(reader, t.getClass());
//        reader.close();
//        return t;
//    }

    /**
     * Json Response using key value
     *
     * @param Key
     * @param response
     * @return
     */
    public static String getValueFromJson(String Key, String response) {
        // if you use below code then it will throw null pointer exception when
        // key is not found in jsonResponse
        Gson gson = new Gson();
        ByteArrayInputStream io = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(io);
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        return jsonObject.get(Key).getAsString();
    }

    public static String getValueFromJsonArray(String Key, String response) {
        // if you use below code then it will throw null pointer exception when
        // key is not found in jsonResponse
        Gson gson = new Gson();
        ByteArrayInputStream io = new ByteArrayInputStream(
                response.getBytes());
        Reader reader = new InputStreamReader(io);
        JSONArray jsonObject = gson.fromJson(reader, JSONArray.class);
        return jsonObject.toString();
    }

    public static boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }


    // database Operation copy file asset to application database folder
    public static void copyDataBase(Context mActivity) throws IOException {

        InputStream myInput = new FileInputStream(new File("/data/data/"
                + mActivity.getPackageName() + "/databases/"
                + "backup.sqlite"));
        File files = new File(Environment.getExternalStorageDirectory()
                + "/FoodOrder/");
        files.mkdirs();
        String outFileName = Environment.getExternalStorageDirectory()
                + "/FoodOrder/backup.sqlite";
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int bufferLength;
        while ((bufferLength = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, bufferLength);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.d("tag", "Copy Database Done");
    }

    /**
     * @param intMonthAgo
     * @param dateFormat
     * @return
     */
    public static String getMonthAgoDate(int intMonthAgo, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat,
                CV.LOCALE_USE_DATEFORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, intMonthAgo);
        return formatter.format(calendar.getTime());
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        // DateFormat formatter = new SimpleDateFormat(dateFormat);
        DateFormat formatter = new SimpleDateFormat(dateFormat,
                CV.LOCALE_USE_DATEFORMAT);

        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getCurentDateAndTime() {
        // Create a DateFormatter object for displaying date in specified
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("hh:mm aaa", new Date()).toString();
        return date;
    }


    public static String getCurrentDate(String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        return getDate(calendar.getTimeInMillis(), dateFormat);
    }


    public static Date convertStringtodate(String strDate) {
        // SimpleDateFormat format = new SimpleDateFormat(
        // CommonVariable.DATABASE_DATE_FORMAT);

        SimpleDateFormat format = new SimpleDateFormat(
                CV.DATABASE_DATE_FORMAT);
        Date date = null;
        try {
            date = format.parse(strDate);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Http url parameter value divide in map value
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> splitUrlVariable(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    public static String getDoubleTwoDecimal(double value)
            throws NumberFormatException {

        DecimalFormat decimalFormat = new DecimalFormat(
                "#0.00");

        return decimalFormat.format(value);
    }

    public static String getFormatedData(String unformatedData) {
        if (unformatedData != null) {
            try {
                //unformatedData.replaceAll(",", "");
                Double result = Double.valueOf(unformatedData);
                DecimalFormat myFormatter = new DecimalFormat("#,##,##0.00");
                //DecimalFormat myFormatter = new DecimalFormat("#,###,###");
                //If you don't want to show .00 format
                return myFormatter.format(result);
            } catch (NumberFormatException e) {
                return unformatedData;
            }

        } else {
            return "0.00";
        }
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = BaseApp.sqLiteDatabase;
        String Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue + "'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public static boolean CheckIsDataAlreadyInDBorNotString(String TableName,
                                                            String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = sqLiteDatabase;
        String Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue + "'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public static void setClipboard(Context context, String text) {

        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                clipboard.setPrimaryClip(clip);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }


    public static void showSnackBar(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static String getTime(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        Date value = null;
        try {
            value = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // TimeZone tz = TimeZone.getTimeZone("America/Chicago");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        return dt;
    }

//    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");
//
//    public static ArrayList<String> arrayOfTime(String startTime, String endTime) {
//
//
//        final DateTime start = FORMAT.parseDateTime(startTime);
//        final DateTime end = FORMAT.parseDateTime(endTime);
//
//        ArrayList<String> strings = new ArrayList<>();
//        final Duration duration = Minutes.minutes(15).toStandardDuration();
//
//        final Set<DateTime> set = new LinkedHashSet<DateTime>();
//
//        DateTime d = new DateTime(start);
//
//        do {
//            set.add(d);
//            d = d.plus(duration);
//        } while (d.compareTo(end) <= 0);
//
//        for (final DateTime dt : set) {
//            strings.add(FORMAT.print(dt));
//        }
//        //    System.out.println(FORMAT.print(dt));
//        return strings;
//    }


    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public String compressImage(String imageUri, Context context) {

        String filePath = getRealPathFromURI(imageUri, context);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static Bitmap decodeFile(String pathName, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeFile(pathName, options);

        return unscaledBitmap;
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    public static enum ScalingLogic {
        CROP, FIT
    }

    public static Spanned getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";

        return Html.fromHtml(input);
    }

    private static boolean isWifiAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isConnected();
    }

    public static boolean isInternetAccessible(Context context) {
        if (isWifiAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Couldn't check internet connection", e);
            }
        } else {
            Log.d(LOG_TAG, "Internet not available!");
        }
        return false;
    }

    public static DisplayMetrics getDeviceMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }


    public static Double addTwoDecimal(String x) {
        Double aDouble = 0.00;
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            String dx = df.format(x);
            aDouble = Double.valueOf(dx);
        } catch (Exception e) {
            aDouble = 0.00;
        }
        return aDouble;
    }

    public static Date dateFormat(String date) {
        Date time = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
            if (CM.getCurentDateAndTime().contains("p.m.") || CM.getCurentDateAndTime().contains("a.m.")) {
                date = date.replace("PM", "p.m.");
                date = date.replace("AM", "a.m.");
            }
            time = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / (metrics.densityDpi / 160f));
        return dp;
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static Object chkNull(Object pData) {
        return (pData == null ? "" : pData);
    }
}

