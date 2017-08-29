package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.TokenPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon;
import backuprestore.udr.rk.allbackuprestore.util.UploadFile;

import static backuprestore.udr.rk.allbackuprestore.util.CM.convertPixelsToDp;

public class ViewContact extends AppCompatActivity implements View.OnClickListener {

    private String vfile;
    private ProgressDialog mProgressDialog;
    private String status;
    LinearLayout backupLayout, backupWithContact, viewbackupLayout, restoreLayout, deleteBackup, sendToCloudLayout, deleteALLayout;
    private File contactFile;
    private boolean aBoolean = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact");
        setSupportActionBar(toolbar);
        vfile = "Contacts.vcf";
        contactFile = new File(CONSTANTS.createAppFolderForContact(ViewContact.this) + File.separator);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewContact.this);

            }
        });

        getActionBarTextView(toolbar);
        initView();
    }

    private void initView() {

        backupLayout = (LinearLayout) findViewById(R.id.backupLayout);
        backupWithContact = (LinearLayout) findViewById(R.id.backupWithContact);
        viewbackupLayout = (LinearLayout) findViewById(R.id.viewbackupLayout);
        restoreLayout = (LinearLayout) findViewById(R.id.restoreLayout);
        deleteBackup = (LinearLayout) findViewById(R.id.deleteBackup);
        sendToCloudLayout = (LinearLayout) findViewById(R.id.sendToCloudLayout);
        deleteALLayout = (LinearLayout) findViewById(R.id.deleteALLayout);


        sendToCloudLayout.setOnClickListener(this);
        deleteBackup.setOnClickListener(this);
        restoreLayout.setOnClickListener(this);
        viewbackupLayout.setOnClickListener(this);
        backupLayout.setOnClickListener(this);
        deleteALLayout.setOnClickListener(this);
    }

    public void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewContact.this);
        alert.setTitle(getString(R.string.app_name));
        alert.setMessage(getString(R.string.backuoMsgContact));


        final TextView textView = new TextView(ViewContact.this);
        textView.setText(contactFile.getAbsolutePath());
        textView.setPadding(40, 0, 0, 0);
        alert.setView(textView);

        LinearLayout linearLayout = new LinearLayout(ViewContact.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);

        final EditText input = new EditText(ViewContact.this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertPixelsToDp(50, this), 0, convertPixelsToDp(50, this), 0);
        input.setLayoutParams(params);
        input.setText(CONSTANTS.CONTACTS_FILE_NAME);
        linearLayout.addView(input);

        alert.setView(linearLayout);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(ViewContact.this, "Please Enter File Name with ext.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new TestAsyncs().execute(input.getText().toString());

                                }
                            }, 1000);
                        }
                    });

                }


            } // End of onClick(DialogInterface dialog, int whichButton)


        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton


        final AlertDialog alertDialog = alert.create();
        alertDialog.show();


    }


    public void getVCF(String fileName) {
        try {
            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            phones.moveToFirst();
            for (int i = 0; i < phones.getCount(); i++) {
                String lookupKey = phones.getString(phones
                        .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_VCARD_URI,
                        lookupKey);
                AssetFileDescriptor fd;
                try {
                    fd = getContentResolver().openAssetFileDescriptor(uri, "r");
                    FileInputStream fis = fd.createInputStream();
                    byte[] buf = new byte[(int) fd.getDeclaredLength()];
                    fis.read(buf);
                    String VCard = new String(buf);

                    File file = new File(contactFile.getAbsolutePath(), fileName);
                    FileOutputStream mFileOutputStream = new FileOutputStream(file.getAbsolutePath(), true);
                    mFileOutputStream.write(VCard.toString().getBytes());
                    phones.moveToNext();
                    Log.d("Vcard", VCard);
                    status = "success";
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    status = "fail";
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.getMessage();
            status = "fail";

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backupLayout:
                showDialog();
                break;
            case R.id.viewbackupLayout:

                File directory4 = new File(CONSTANTS.createAppFolderForContact(ViewContact.this).getAbsolutePath());

                if (directory4.exists()) {
                    File[] files = directory4.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "2");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;
            case R.id.restoreLayout:

                File directory = new File(CONSTANTS.createAppFolderForContact(ViewContact.this).getAbsolutePath());

                if (directory.exists()) {
                    File[] files = directory.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "2");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;
            case R.id.deleteBackup:

                File directory1 = new File(CONSTANTS.createAppFolderForContact(ViewContact.this).getAbsolutePath());

                if (directory1.exists()) {
                    File[] files = directory1.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "5");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;
            case R.id.sendToCloudLayout:


                File directory3 = new File(CONSTANTS.createAppFolderForContact(ViewContact.this).getAbsolutePath());

                if (directory3.exists()) {
                    File[] files = directory3.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "4");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;
            case R.id.deleteALLayout:
                break;


        }
    }

    class TestAsyncs extends AsyncTask<Object, Object, String> {
        String TAG = getClass().getSimpleName();
        String fileName = "";

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");
            mProgressDialog = new ProgressDialog(ViewContact.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected String doInBackground(Object... params) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            fileName = params[0].toString();
            getVCF(params[0].toString());
            return status;
        }

        protected void onProgressUpdate(Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {

            if (result != null) {
                if (result.equals("success")) {
                    CM.showToast(ViewContact.this, "CONTACT BACKUP DONE");
                    if (CM.getSp(ViewContact.this, "cloud", "").equals("1")) {
                        ShowDialog(fileName);
                    }
                } else {
                    CM.showToast(ViewContact.this, "CONTACT BACKUP NOT DONE");

                }
            } else {
                CM.showToast(ViewContact.this, "CONTACT BACKUP FAIL");
            }
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(ViewContact.this);
    }

    public void showPopup(Context context, final String item) {
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to delete file ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        File file = new File(contactFile, item);
                        if (file.exists()) {
                            if (file.exists()) {
                                file.delete();
                                CM.showToast(ViewContact.this, "FILE DELETED");
                            }
                        } else {
                            CM.showToast(ViewContact.this, "FILE NOT FOUND");
                        }


                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setIcon(R.drawable.delete).show();
    }

    public void ShowDialog(final String fileName) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(getString(R.string.app_name));
        adb.setMessage(getString(R.string.backupDone));
        adb.setCancelable(false);
        String yesButtonText = "SEND TO DROPBOX";
        String noButtonText = "CANCEL";
        adb.setPositiveButton(yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DropBoxCommon dropBoxCommon = new DropBoxCommon();
                DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewContact.this);
                AndroidAuthSession session = mDBApi.getSession();
                if (session.authenticationSuccessful()) {
                    session.finishAuthentication();

                    TokenPair tokens = session.getAccessTokenPair();

                    CM.setSp(ViewContact.this, "key", tokens.key.toString());
                    CM.setSp(ViewContact.this, "secret", tokens.secret.toString());
                    File file = new File(contactFile.getAbsolutePath(), fileName);
                    if (file.exists()) {
                        UploadFile upload = new UploadFile(ViewContact.this, mDBApi, CONSTANTS.DIRCONTACT, file);
                        upload.execute();
                    } else {
                        CM.showToast(ViewContact.this, "FILE NOT FOUND");
                    }
                } else {
                    mDBApi.getSession().startAuthentication(ViewContact.this);
                }


            }
        });

        adb.setNegativeButton(noButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    void showListDialog(File[] files, final String code) {

        //String path = Environment.getExternalStorageDirectory().toString() + "/AyalaPDF";
        //  Log.d("Files", "Path: " + path);

        Log.d("Files", "Size: " + files.length);
        ArrayList<String> dialog_item = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            dialog_item.add(files[i].getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewContact.this);
        builder.setTitle("Select Files");

        //list of items
        final String[] items = dialog_item.toArray(new String[dialog_item.size()]);
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        String name = items[which].toString();
                        CM.showToast(ViewContact.this, name);

                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();


                        if (code.equals("2")) {
                            //  CM.startActivity(ViewContact.this, ViewContactBackUp.class);

                            Intent intent = new Intent(ViewContact.this, ViewContactBackUp.class);
                            intent.putExtra("fileName", items[selectedPosition]);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_in_from_left,
                                    R.anim.push_out_to_right);

                        } else if (code.equals("2")) {
                            if (contactFile.exists()) {
                                File file = new File(contactFile, items[selectedPosition]);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "text/x-vcard"); //storage path is path of your vcf file and vFile is name of that file.
                                startActivity(intent);
                            } else {
                                CM.showToast(ViewContact.this, "NO FILE FOUND");
                            }

                        } else if (code.equals("4")) {

                            if (CM.isInternetAvailable(ViewContact.this)) {

                                DropBoxCommon dropBoxCommon = new DropBoxCommon();
                                DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewContact.this);
                                AndroidAuthSession session = mDBApi.getSession();
                                if (session.authenticationSuccessful()) {
                                    session.finishAuthentication();
                                    TokenPair tokens = session.getAccessTokenPair();

                                    CM.setSp(ViewContact.this, "key", tokens.key.toString());
                                    CM.setSp(ViewContact.this, "secret", tokens.secret.toString());


                                    File file = new File(contactFile, items[selectedPosition]);
                                    if (file.exists()) {
                                        UploadFile upload = new UploadFile(ViewContact.this, mDBApi, CONSTANTS.DIRCONTACT, file);
                                        upload.execute();
                                    } else {
                                        CM.showToast(ViewContact.this, "FILE NOT FOUND");
                                    }
                                } else {
                                    CM.showToast(ViewContact.this, getString(R.string.sessionExp));
                                    mDBApi.getSession().startAuthentication(ViewContact.this);

                                }
                            } else {
                                CM.showToast(ViewContact.this, getString(R.string.msg_internet_unavailable_msg));
                            }
                        } else if (code.equals("5")) {
                            showPopup(ViewContact.this, items[selectedPosition]);
                        } else if (code.equals("6")) {

                        }


                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Typeface khandBold = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            titleTextView.setTypeface(khandBold);
           // titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewContact.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
