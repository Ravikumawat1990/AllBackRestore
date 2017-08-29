package backuprestore.udr.rk.allbackuprestore.sync;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon;

import static android.R.id.content;

import static android.content.ContentValues.TAG;

/**
 * Created by NetSupport on 09-02-2017.
 */

public class DownloadService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_DOWNLOAD = "backuprestore.udr.rk.allbackuprestore.ViewCloudAccount";
    //public static final String ACTION_BAZ = "com.example.dara.myapplication.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_URL = "com.example.dara.myapplication.extra.URL";
    public String EXTRA_MESSAGE = "";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    String message = "";

    public DownloadService() {
        super("DownloadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                //   final String url = intent.getStringExtra(EXTRA_URL);
                //Log.e("Service", url);
                DropBoxCommon dropBoxCommon = new DropBoxCommon();
                mDBApi = dropBoxCommon.getDropboxAPI(this);
                AndroidAuthSession session = mDBApi.getSession();
                if (session.authenticationSuccessful()) {
                    try {
                        session.finishAuthentication();
                        downloadImage();

                    } catch (IllegalStateException e) {
                        Log.i(TAG, "onHandleIntent:Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                    }
                }


            }
        }
    }

    private void downloadImage() {
        ArrayList<DropboxAPI.Entry> files = new ArrayList<>();
        try {

            DropboxAPI.Entry directory = mDBApi.metadata("/Backup/", 1000, null, true, null);

            if (directory.contents != null && directory.contents.size() > 0) {
                for (DropboxAPI.Entry e : directory.contents) {


                    if (!e.isDeleted && e.isDir) {


                        DropboxAPI.Entry directoryEntry = mDBApi.metadata(e.path, 1000, null, true, null);

                        for (DropboxAPI.Entry sub : directoryEntry.contents) {

                            if (!sub.isDir) {
                                switchCase(e.fileName(), sub);

                            }

                        }
                    }
                }
                message = "Sync Done";
            } else {

                message = getString(R.string.notDat);
            }


        } catch (DropboxException e) {
            e.printStackTrace();
            message = e.getMessage();
        }
        //Send the feedback message to the MainActivity
        Intent backIntent = new Intent(DownloadService.ACTION_DOWNLOAD);
        backIntent.putExtra("sync", message);
        sendBroadcast(backIntent);
    }

    private void copy(final DropboxAPI.Entry fileSelected, final File localFile) {
        //  final ProgressDialog pd = ProgressDialog.show(context, "Downloading...", "Please wait...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedInputStream br = null;
                BufferedOutputStream bw = null;
                DropboxAPI.DropboxInputStream fd;
                try {
                    fd = mDBApi.getFileStream(fileSelected.path,    ///SMS/smsbackup.xml
                            localFile.getPath());                         ///storage/emulated/0/Ringtones/SMS/smsbackup.xml
                    br = new BufferedInputStream(fd);
                    bw = new BufferedOutputStream(new FileOutputStream(localFile));
                    byte[] buffer = new byte[4096];
                    int read;
                    while (true) {
                        read = br.read(buffer);
                        if (read <= 0) {
                            break;
                        }
                        bw.write(buffer, 0, read);
                    }
                    //pd.dismiss();
                } catch (DropboxException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bw != null) {
                        try {
                            bw.close();
                            if (br != null) {
                                br.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public boolean switchCase(String name, DropboxAPI.Entry sub) {
        switch (name) {
            case "APKS":
                File newfile = new File(CONSTANTS.createAppFolderForApp(this).getAbsoluteFile(), sub.fileName());
                try {
                    if (!newfile.exists()) {
                        newfile.createNewFile();
                        copy(sub, newfile);
                    } else {
                        copy(sub, newfile);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                return true;
            case "SMS":
                File newfileSms = new File(CONSTANTS.createAppFolderForSMS(this).getAbsoluteFile(), sub.fileName());
                try {
                    if (!newfileSms.exists()) {
                        newfileSms.createNewFile();
                        copy(sub, newfileSms);
                    } else {
                        copy(sub, newfileSms);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                return true;
            case "CONTACTS":

                File newfileContact = new File(CONSTANTS.createAppFolderForContact(this).getAbsoluteFile(), sub.fileName());
                try {
                    if (!newfileContact.exists()) {
                        newfileContact.createNewFile();
                        copy(sub, newfileContact);
                    } else {
                        copy(sub, newfileContact);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return true;
            case "CALLLOGES":

                File newfileCallloges = new File(CONSTANTS.createAppFolderForCallog(this).getAbsoluteFile(), sub.fileName());
                try {
                    if (!newfileCallloges.exists()) {
                        newfileCallloges.createNewFile();
                        copy(sub, newfileCallloges);
                    } else {
                        copy(sub, newfileCallloges);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                return true;
            case "BOOKMARKS":
                File newfileCallBookmarks = new File(CONSTANTS.createAppFolderForBookMark(this).getAbsoluteFile(), sub.fileName());
                try {
                    if (!newfileCallBookmarks.exists()) {
                        newfileCallBookmarks.createNewFile();
                        copy(sub, newfileCallBookmarks);
                    } else {
                        copy(sub, newfileCallBookmarks);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                return true;
            case "CALENDRA":
                CONSTANTS.createAppFolderForCalendra(this);
                File newfileCallCalendra = new File(CONSTANTS.createAppFolderForBookMark(this).getAbsoluteFile(), sub.fileName());
                try {
                    if (!newfileCallCalendra.exists()) {
                        newfileCallCalendra.createNewFile();
                        copy(sub, newfileCallCalendra);
                    } else {
                        copy(sub, newfileCallCalendra);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                return true;
        }

        return false;
    }
}
