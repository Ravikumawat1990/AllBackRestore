package backuprestore.udr.rk.allbackuprestore.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

/**
 * Created by NetSupport on 03-02-2017.
 */

public class DropBoxCommon {

    public static DropboxAPI<AndroidAuthSession> mDBApi;
    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String ACCESS_KEY =
            "hbhmhurw0rel609";
    private final static String ACCESS_SECRET = "hz9e2vf06x82crw";
    private final static Session.AccessType ACCESS_TYPE = Session.AccessType.APP_FOLDER;
    public final static String DROPBOX_FILE_DIR = "/AndroidDropboxImplementationExample/";

    public DropboxAPI<AndroidAuthSession> initView(Context context) {
        if (!CM.getSp(context, "key", "").equals("") && !CM.getSp(context, "secret", "").equals("")) {
            mDBApi = getDropboxAPI(context);
        } else {
            AndroidAuthSession session = buildSession(context);
            mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        }
        return mDBApi;
    }

    private AndroidAuthSession buildSession(Context context) {
        AppKeyPair appKeyPair = new AppKeyPair(CONSTANTS.DROPBOX_APP_KEY,
                CONSTANTS.DROPBOX_APP_SECRET);
        AndroidAuthSession session;

        if (!CM.getSp(context, "key", "").equals("") && !CM.getSp(context, "secret", "").equals("")) {
            AccessTokenPair accessToken = new AccessTokenPair(CM.getSp(context, "key", "").toString(),
                    CM.getSp(context, "secret", "").toString());
            session = new AndroidAuthSession(appKeyPair, CONSTANTS.ACCESS_TYPE,
                    accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, CONSTANTS.ACCESS_TYPE);
        }

        return session;
    }

    private String[] getKeys(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
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

    public DropboxAPI<AndroidAuthSession> getDropboxAPI(Context context) {
        AppKeyPair appKeys = new AppKeyPair(CONSTANTS.DROPBOX_APP_KEY, CONSTANTS.DROPBOX_APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(appKeys, CONSTANTS.ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        AccessTokenPair access = null;
        if (CM.getSp(context, "key", "").toString() != null && CM.getSp(context, "secret", "").toString()!=null) {
            if (!CM.getSp(context, "key", "").equals("") && !CM.getSp(context, "secret", "").equals("")) {
                access = new AccessTokenPair(CM.getSp(context, "key", "").toString(), CM.getSp(context, "secret", "").toString());
                mDBApi.getSession().setAccessTokenPair(access);
            } else {
                mDBApi = new DropboxAPI<AndroidAuthSession>(session);
            }
        } else {
            mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        }

        return mDBApi;
    }


}
