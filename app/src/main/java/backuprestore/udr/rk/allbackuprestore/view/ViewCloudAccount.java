package backuprestore.udr.rk.allbackuprestore.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;

import java.lang.reflect.Field;

import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;
import backuprestore.udr.rk.allbackuprestore.sync.DownloadService;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon;

public class ViewCloudAccount extends AppCompatActivity implements View.OnClickListener {

    private final String DIR = "/";
    private boolean isUserLoggedIn;
    private MtplTextView txtDropBox;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private boolean mLoggedIn, onResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cloud_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cloud account");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewCloudAccount.this);

            }
        });


        getActionBarTextView(toolbar);
        initView();


        DropBoxCommon dropBoxCommon = new DropBoxCommon();
        if (!CM.getSp(ViewCloudAccount.this, "key", "").equals("") && !CM.getSp(ViewCloudAccount.this, "secret", "").equals("")) {
            mDBApi = dropBoxCommon.getDropboxAPI(ViewCloudAccount.this);
            setLoggedIn(true);
            onResume = true;
        } else {
            AndroidAuthSession session = buildSession();
            mDBApi = new DropboxAPI<AndroidAuthSession>(session);
            setLoggedIn(false);
            onResume = false;
        }

    }

    @Override
    protected void onResume() {
        registerReceiver(receiver, filter);
        AndroidAuthSession session = mDBApi.getSession();

        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                CM.setSp(ViewCloudAccount.this, "key", tokens.key.toString());
                CM.setSp(ViewCloudAccount.this, "secret", tokens.secret.toString());


                setLoggedIn(onResume);
            } catch (IllegalStateException e) {
                CM.showToast(ViewCloudAccount.this, "Couldn't authenticate with Dropbox:"
                        + e.getLocalizedMessage());
            }
        }
        super.onResume();

    }

    private void storeKeys(String key, String secret) {
        SharedPreferences prefs = getSharedPreferences(
                CONSTANTS.ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(CONSTANTS.ACCESS_KEY_NAME, key);
        edit.putString(CONSTANTS.ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

    private void initView() {
        txtDropBox = (MtplTextView) findViewById(R.id.txtdropBox);
        Button btnsyncAllData = (Button) findViewById(R.id.btnsyncAllData);
        btnsyncAllData.setOnClickListener(this);
        txtDropBox.setOnClickListener(this);


    }

    public void loggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
        txtDropBox.setText(userLoggedIn ? "Logout" : "Log in");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(this);
    }

    public void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
        isUserLoggedIn = loggedIn;
        txtDropBox.setText(mLoggedIn ? "Logout" : "Log in");
        if (loggedIn) {
            onResume = false;

        }
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(CONSTANTS.DROPBOX_APP_KEY,
                CONSTANTS.DROPBOX_APP_SECRET);
        AndroidAuthSession session;
        // String[] stored = getKeys();

        if (!CM.getSp(ViewCloudAccount.this, "key", "").equals("") && !CM.getSp(ViewCloudAccount.this, "secret", "").equals("")) {
            AccessTokenPair accessToken = new AccessTokenPair(CM.getSp(ViewCloudAccount.this, "key", "").toString(),
                    CM.getSp(ViewCloudAccount.this, "secret", "").toString());
            session = new AndroidAuthSession(appKeyPair, CONSTANTS.ACCESS_TYPE,
                    accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, CONSTANTS.ACCESS_TYPE);
        }


        return session;
    }

    private String[] getKeys() {
        SharedPreferences prefs = getSharedPreferences(
                CONSTANTS.ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(CONSTANTS.ACCESS_KEY_NAME, null);
        String secret = prefs.getString(CONSTANTS.ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
            String[] ret = new String[2];
            ret[0] = key;
            ret[1] = secret;
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtdropBox:

                if (CM.isInternetAvailable(ViewCloudAccount.this)) {
                    if (isUserLoggedIn) {
                        CM.setSp(ViewCloudAccount.this, "key", "");
                        CM.setSp(ViewCloudAccount.this, "secret", "");
                        mDBApi.getSession().unlink();
                        loggedIn(false);
                    } else {
                        mDBApi.getSession().startAuthentication(ViewCloudAccount.this);
                        onResume = true;
                    }
                } else {
                    CM.showToast(this, getString(R.string.msg_internet_unavailable_msg));
                }
                break;
            case R.id.btnsyncAllData:


                if (CM.isInternetAvailable(ViewCloudAccount.this)) {

                    DropBoxCommon dropBoxCommon = new DropBoxCommon();
                    DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewCloudAccount.this);
                    AndroidAuthSession session = mDBApi.getSession();
                    if (session.authenticationSuccessful()) {
                        session.finishAuthentication();
                        TokenPair tokens = session.getAccessTokenPair();

                        CM.setSp(ViewCloudAccount.this, "key", tokens.key.toString());
                        CM.setSp(ViewCloudAccount.this, "secret", tokens.secret.toString());
                        Intent newIntent = new Intent(ViewCloudAccount.this, DownloadService.class);
                        newIntent.setAction(DownloadService.ACTION_DOWNLOAD);
                        newIntent.putExtra(DownloadService.EXTRA_URL, "");
                        startService(newIntent);
                    } else {
                        CM.showToast(ViewCloudAccount.this, getString(R.string.sessionExp));
                        mDBApi.getSession().startAuthentication(ViewCloudAccount.this);
                    }
                } else {
                    CM.showToast(this, getString(R.string.msg_internet_unavailable_msg));
                }
                break;
        }

    }

    IntentFilter filter = new IntentFilter("backuprestore.udr.rk.allbackuprestore.ViewCloudAccount");
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras().getString("sync") != null) {
                CM.showToast(ViewCloudAccount.this, intent.getExtras().getString("sync"));

            }
        }
    };

    private TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Typeface khandBold = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            titleTextView.setTypeface(khandBold);
          //  titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewCloudAccount.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
