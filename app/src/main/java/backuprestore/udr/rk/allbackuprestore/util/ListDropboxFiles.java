package backuprestore.udr.rk.allbackuprestore.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by NetSupport on 30-01-2017.
 */

public class ListDropboxFiles extends AsyncTask<Void, Void, ArrayList<String>> {

    private final ProgressDialog mDialog;
    private DropboxAPI<?> dropbox;
    private String path;
    private Handler handler;
    Context context;

    public ListDropboxFiles(DropboxAPI<?> dropbox, String path, Context context) {
        this.dropbox = dropbox;
        this.path = path;
        this.context = context;
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("Plaease Wait...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setProgress(0);
        mDialog.setCancelable(false);
        mDialog.show();

    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> files = new ArrayList<String>();
        try {
            DropboxAPI.Entry directory = dropbox.metadata(path, 1000, null, true, null);
            for (DropboxAPI.Entry e : directory.contents) {
                if (!e.isDeleted && !e.isDir) {

                    File newfile = new File(CONSTANTS.createAppFolderForSMS(context).getAbsoluteFile(), e.fileName());
                    try {
                        if (!newfile.exists()) {
                            newfile.createNewFile();
                            copy(e, newfile);
                        } else {
                            copy(e, newfile);
                        }

                        files.add(e.fileName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                }
            }


        } catch (DropboxException e) {
            e.printStackTrace();
        }

        return files;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {


        mDialog.dismiss();
        //Message msgObj = handler.obtainMessage();
        // Bundle b = new Bundle();
        //  b.putStringArrayList("data", result);
        //  msgObj.setData(b);
        //  handler.sendMessage(msgObj);

    }

//    public static final void copyDirectory(File dropboxfile, File newfile) throws IOException {
//
//        newfile.mkdirs();
//        File[] files = dropboxfile.listFiles();
//
//        for (File file : files) {
//            if (file.isDirectory()) {
//                copyDirectory(file, new File(newfile, file.getName()));
//
//            } else {
//                copyFile(file, new File(newfile, file.getName()));
//            }
//        }
//    }


    private void copy(final DropboxAPI.Entry fileSelected, final File localFile) {
        //  final ProgressDialog pd = ProgressDialog.show(context, "Downloading...", "Please wait...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedInputStream br = null;
                BufferedOutputStream bw = null;
                DropboxAPI.DropboxInputStream fd;
                try {
                    fd = dropbox.getFileStream(fileSelected.path,    ///SMS/smsbackup.xml
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

    private void showFileExitsDialog(final DropboxAPI.Entry fileSelected,
                                     final File localFile, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(CONSTANTS.OVERRIDEMSG);
        alertBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        copy(fileSelected, localFile);
                    }
                });
        alertBuilder.setNegativeButton("Cancel", null);
        alertBuilder.create().show();

    }
}
