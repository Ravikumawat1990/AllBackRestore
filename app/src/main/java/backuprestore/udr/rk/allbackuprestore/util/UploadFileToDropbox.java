package backuprestore.udr.rk.allbackuprestore.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import backuprestore.udr.rk.allbackuprestore.view.ViewCloudAccount;

/**
 * Created by NetSupport on 30-01-2017.
 */

public class UploadFileToDropbox extends AsyncTask<Void, Void, Boolean> {

    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private DropboxAPI.UploadRequest mRequest;

    public UploadFileToDropbox(Context context, DropboxAPI<?> dropbox,
                               String path) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;
        try {

           // dropbox = DropBoxCommon.initView(context);

            File tmpFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), CONSTANTS.CALLLOGES_FILE_NAME);
            tmpFile.exists();
            FileInputStream fis = new FileInputStream(tmpFile);

            try {
                DropboxAPI.Entry response = dropbox.putFileOverwrite(CONSTANTS.CALLLOGES_FILE_NAME, fis, tmpFile.length(), null);
                Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
            } catch (DropboxUnlinkedException e) {
                Log.e("DbExampleLog", "User has unlinked.");


            } catch (DropboxException e) {
                Log.e("DbExampleLog", "Something went wrong while uploading.");
            }


            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, "File Uploaded Sucesfully!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Failed to upload file", Toast.LENGTH_LONG)
                    .show();
        }
    }
}