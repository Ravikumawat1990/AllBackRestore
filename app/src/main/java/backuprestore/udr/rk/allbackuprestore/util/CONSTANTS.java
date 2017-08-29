package backuprestore.udr.rk.allbackuprestore.util;

import android.content.Context;
import android.os.Environment;

import com.dropbox.client2.session.Session;

import java.io.File;

/**
 * Created by NetSupport on 30-01-2017.
 */

public class CONSTANTS {


    public static final String OVERRIDEMSG = "File name with this name already exists.Do you want to replace this file?";
    final static public String DROPBOX_APP_KEY = "hwztz2bmnhh6g0b";
    final static public String DROPBOX_APP_SECRET = "twtziasci3hbd57";

    final static public String DIR = "/";

    final static public String DIRAPPS = "/BACKUP/APKS/";
    final static public String DIRSMS = "/BACKUP/SMS/";
    final static public String DIRCONTACT = "/BACKUP/CONTACTS/";
    final static public String DIRCALLLOGES = "/BACKUP/CALLLOGES/";

    final static public String DIRBOOKMARK = "/BACKUP/BOOKMARKS/";

    final static public String DIRCALENDRA = "/BACKUP/CALENDRA/";


    public static boolean mLoggedIn = false;

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;

    final static public String ACCOUNT_PREFS_NAME = "prefs";
    final static public String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static public String ACCESS_SECRET_NAME = "ACCESS_SECRET";


    public static final String SMS_FILE_NAME = "smsbackup.xml";
    public static final String BOOK_MARK_FILE_NAME = "bookmark.xml";
    public static final String CONTACTS_FILE_NAME = "contacts.vcf";
    public static final String CALLLOGES_FILE_NAME = "callLoges.xml";
    public static final String CALENDRA_FILE_NAME = "calendra.xml";
    public static final String DEMO_FILE_NAME = "demo.xml";

    public static String root = Environment.getExternalStorageDirectory().toString();

    public static void CreateDir() {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "BACKUP");
        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
    }


    public static File createAppFolderForApp(Context context) {
        File FilePathDir;
        if (!CM.getSp(context, "path", "").toString().equals("")) {
            FilePathDir = new File(CM.getSp(context, "path", "").toString(), "/BACKUP/APPS");
        } else {
            FilePathDir = new File(root, "BACKUP/APPS");

        }
        if (!FilePathDir.exists()) {
            FilePathDir.mkdir();
        }
        return FilePathDir;
    }

    public static File createAppFolderForSMS(Context context) {
        File FilePathDir;
        if (!CM.getSp(context, "path", "").toString().equals("")) {
            FilePathDir = new File(CM.getSp(context, "path", "").toString(), "/BACKUP/SMS");
        } else {
            FilePathDir = new File(root, "BACKUP/SMS");

        }
        if (!FilePathDir.exists()) {
            FilePathDir.mkdirs();
        }
        return FilePathDir;
    }

    public static File createAppFolderForContact(Context context) {
        File FilePathDir;
        if (!CM.getSp(context, "path", "").toString().equals("")) {
            FilePathDir = new File(CM.getSp(context, "path", "").toString(), "/Contacts");
        } else {
            FilePathDir = new File(root, "BACKUP/Contacts");

        }
        if (!FilePathDir.exists()) {
            FilePathDir.mkdir();
        }
        return FilePathDir;
    }

    public static File createAppFolderForCallog(Context context) {
        File FilePathDir;
        if (!CM.getSp(context, "path", "").toString().equals("")) {
            FilePathDir = new File(CM.getSp(context, "path", "").toString(), "/BACKUP/CALLLOG");
        } else {
            FilePathDir = new File(root, "BACKUP/CALLLOG");

        }

        if (!FilePathDir.exists()) {
            FilePathDir.mkdir();
        }
        return FilePathDir;
    }

    public static File createAppFolderForCalendra(Context context) {
        File FilePathDir;
        if (!CM.getSp(context, "path", "").toString().equals("")) {
            FilePathDir = new File(CM.getSp(context, "path", "").toString(), "/BACKUP/CALENDRA");
        } else {
            FilePathDir = new File(root, "BACKUP/CALENDRA");

        }

        if (!FilePathDir.exists()) {
            FilePathDir.mkdir();
        }
        return FilePathDir;
    }

    public static File createAppFolderForBookMark(Context context) {
        File FilePathDir;
        if (!CM.getSp(context, "path", "").toString().equals("")) {
            FilePathDir = new File(CM.getSp(context, "path", "").toString(), "/BACKUP/BOOKMARK");
        } else {
            FilePathDir = new File(root, "BACKUP/BOOKMARK");

        }

        if (!FilePathDir.exists()) {
            FilePathDir.mkdir();
        }
        return FilePathDir;
    }
}
