package backuprestore.udr.rk.allbackuprestore.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by NetSupport on 30-01-2017.
 */

public class DownloadFileToDropbox extends AsyncTask<Void, Void, Boolean> {

    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private DropboxAPI.UploadRequest mRequest;

    public DownloadFileToDropbox(Context context, DropboxAPI<?> dropbox,
                                 String path) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        File file = new File(CONSTANTS.createAppFolderForCallog(context).getAbsolutePath(), CONSTANTS.DEMO_FILE_NAME);
        OutputStream out = null;
        boolean result = false;
        try {
            file.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DropboxAPI.DropboxFileInfo info = dropbox.getFile(path + "" + CONSTANTS.BOOK_MARK_FILE_NAME, null, out, null);
            Log.i("DbExampleLog", "The file's rev is: " + info.getMetadata().rev);
            result = true;
        } catch (DropboxException e) {
            Log.e("DbExampleLog", "Something went wrong while downloading.");
            file.delete();
            result = false;
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