package backuprestore.udr.rk.allbackuprestore.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.util.CM;


public class ViewHome extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ViewHome";
    private LinearLayout appLayout, rateUsLayout;
    private LinearLayout smsLayout, contactLayout, callLogLayout, calanderLayout, bookMarkLayout, callRecorderLayout;

    int PERMISSION_ALL = 1;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS, Manifest.permission.READ_LOGS, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALENDAR};
    private AdView mAdView;

    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (checkPermissions()) {
            Log.d(TAG, "onCreate: ALL PERMISSIONS  GRANTED");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        interstitialAd = new InterstitialAd(ViewHome.this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_full_screen));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        getActionBarTextView(toolbar);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Toast.makeText(getApplicationContext(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                mAdView.setVisibility(View.GONE);
                //  Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
                //   Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                mAdView.setVisibility(View.GONE);
                //  Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                mAdView.setVisibility(View.GONE);
                //    Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
            }
        });


        initView();

    }

    public void initView() {
        appLayout = (LinearLayout) findViewById(R.id.appLayout);
        smsLayout = (LinearLayout) findViewById(R.id.smsLayout);
        contactLayout = (LinearLayout) findViewById(R.id.contactLayout);
        callLogLayout = (LinearLayout) findViewById(R.id.callLogLayout);
        calanderLayout = (LinearLayout) findViewById(R.id.calanderLayout);
        bookMarkLayout = (LinearLayout) findViewById(R.id.bookMarkLayout);
        callRecorderLayout = (LinearLayout) findViewById(R.id.callRecorderLayout);
        rateUsLayout = (LinearLayout) findViewById(R.id.rateUsLayout);


        appLayout.setOnClickListener(this);
        smsLayout.setOnClickListener(this);
        contactLayout.setOnClickListener(this);
        callLogLayout.setOnClickListener(this);
        calanderLayout.setOnClickListener(this);
        bookMarkLayout.setOnClickListener(this);
        //callRecorderLayout.setOnClickListener(this);
        rateUsLayout.setOnClickListener(this);

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_ALL);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_dropbox:
                CM.startActivity(this, ViewCloudAccount.class);
                break;
            case R.id.action_settings:
                CM.startActivity(this, ViewSetting.class);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appLayout:
                CM.startActivity(this, ViewApps.class);
                break;
            case R.id.smsLayout:
                CM.startActivity(this, ViewSms.class);
                break;
            case R.id.contactLayout:
                CM.startActivity(this, ViewContact.class);
                break;
            case R.id.callLogLayout:
                CM.startActivity(this, ViewCallLog.class);
                break;
            case R.id.calanderLayout:
                CM.startActivity(this, ViewCalendar.class);
                break;
            case R.id.bookMarkLayout:
                CM.startActivity(this, ViewBookMarks.class);
                break;
            case R.id.callRecorderLayout:

                if (CM.isInternetAvailable(ViewHome.this)) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.appstar.callrecorder")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.appstar.callrecorder")));
                    }
                } else {
                    CM.showToast(ViewHome.this, getString(R.string.msg_internet_unavailable_msg));

                }
                break;

            case R.id.rateUsLayout:
                if (CM.isInternetAvailable(ViewHome.this)) {
                    CM.rateApp(this);
                } else {
                    CM.showToast(ViewHome.this, getString(R.string.msg_internet_unavailable_msg));

                }
                break;


        }

    }

    @Override
    public void onBackPressed() {
        showPopup(ViewHome.this);
    }

    public void showPopup(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (interstitialAd.isLoaded()) {
                            interstitialAd.show();
                        }

                        CM.finishActivity(ViewHome.this);
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setIcon(R.mipmap.applogo).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    Log.i(TAG, "onRequestPermissionsResult: PERMISSIONS GRANTED");
                } else {
                    // no permissions granted.
                    Log.i(TAG, "onRequestPermissionsResult: NO PERMISSIONS GRANTED.");
                }
                return;
            }
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    private TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Typeface khandBold = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            titleTextView.setTypeface(khandBold);
            //  titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewHome.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
