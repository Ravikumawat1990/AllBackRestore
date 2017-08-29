package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Telephony;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import backuprestore.udr.rk.allbackuprestore.Model.Sms;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon;
import backuprestore.udr.rk.allbackuprestore.util.UploadFile;

import static backuprestore.udr.rk.allbackuprestore.util.CM.convertPixelsToDp;

public class ViewSms extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ViewSms";
    LinearLayout backupLayout, backupConvLayout, backupRestore, sendToLayout, deleteBackup, deleteMsgLayout, viewBackLayout;
    private ArrayList<Sms> smsbackup;
    private ProgressDialog mProgressDialog;
    String status = "";
    private File smsFile;
    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SMS");
        setSupportActionBar(toolbar);
        smsFile = new File(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewSms.this);

            }
        });

        getActionBarTextView(toolbar);

        initView();
    }

    public void initView() {
        backupLayout = (LinearLayout) findViewById(R.id.backupLayout);
        backupConvLayout = (LinearLayout) findViewById(R.id.backupConvLayout);
        backupRestore = (LinearLayout) findViewById(R.id.backupRestore);
        viewBackLayout = (LinearLayout) findViewById(R.id.layoutViewBackup);

        sendToLayout = (LinearLayout) findViewById(R.id.sendToLayout);
        deleteBackup = (LinearLayout) findViewById(R.id.deleteBackup);
        deleteMsgLayout = (LinearLayout) findViewById(R.id.deleteMsgLayout);


        backupLayout.setOnClickListener(this);
        backupConvLayout.setOnClickListener(this);
        backupRestore.setOnClickListener(this);
        sendToLayout.setOnClickListener(this);
        deleteBackup.setOnClickListener(this);
        deleteMsgLayout.setOnClickListener(this);
        viewBackLayout.setOnClickListener(this);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(ViewSms.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backupLayout:
                showDialog();
                break;
            case R.id.backupRestore:

                File directory = new File(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath());

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
            case R.id.layoutViewBackup:


                File directory1 = new File(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath());

                if (directory1.exists()) {
                    File[] files = directory1.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "3");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;
            case R.id.backupConvLayout:
                CM.startActivity(this, ViewSmsConvercation.class);
                break;
            case R.id.deleteBackup:

                File directory4 = new File(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath());

                if (directory4.exists()) {
                    File[] files = directory4.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "9");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }

                break;

            case R.id.sendToLayout:


                File directory3 = new File(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath());

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
            case R.id.deleteMsgLayout:
                break;


        }

    }


    public void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewSms.this);
        alert.setTitle(getString(R.string.app_name)); //Set Alert dialog title here
        alert.setMessage(getString(R.string.backuoMsg)); //Message here
        final TextView textView = new TextView(ViewSms.this);
        textView.setText(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath());
        textView.setPadding(40, 0, 0, 0);
        alert.setView(textView);

        LinearLayout linearLayout = new LinearLayout(ViewSms.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);


//        TableRow.LayoutParams params = new TableRow.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
//        params.setMargins(40, 0, right, bottom);


        final EditText input = new EditText(ViewSms.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertPixelsToDp(50, this), 0, convertPixelsToDp(50, this), 0);
        input.setLayoutParams(params);
        input.setText(CONSTANTS.SMS_FILE_NAME);
        input.setSelection(input.getText().length());
        linearLayout.addView(input);

        alert.setView(linearLayout);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(ViewSms.this, "Please Enter File Name with ext.", Toast.LENGTH_SHORT).show();
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
                                    new TestAsync().execute(input.getText().toString());

                                }
                            }, 1500);
                        }
                    });
                }
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    public List<Sms> getAllSms(String fileName) {

        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        startManagingCursor(c);
        int totalSMS = c.getCount();
        try {
            if (c.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {
                    objSms = new Sms();
                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objSms.setFolderName("inbox");
                        objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                        objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                        objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                        objSms.setReadState(c.getString(c.getColumnIndex("read")));
                        objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                    } else {
                        objSms.setFolderName("sent");
                    }

                    lstSms.add(objSms);
                    c.moveToNext();
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        if (lstSms.size() > 0) {
            createNewXml(lstSms, fileName);
        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CM.showToast(ViewSms.this, "NO DATA FOUND");
                }
            });

        }


        return lstSms;
    }


    private void parseXML(String fileName) {
        try {
            ArrayList<Sms> smses = new ArrayList<Sms>();
            File filepath = new File(Environment.getExternalStorageDirectory(), "Backup");
            File file = new File(filepath, fileName);

            if (file.exists()) {
                InputStream is = new FileInputStream(file.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("sms");

                smsbackup = new ArrayList<>();
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element2 = (Element) node;
                        Sms sms = new Sms();
                        if (getValue("time", element2) != null) {
                            sms.setTime(getValue("time", element2));
                        } else {
                            sms.setTime("");

                        }
                        if (getValue("address", element2) != null) {
                            sms.setAddress(getValue("address", element2));

                        } else {
                            sms.setAddress("");

                        }
                        if (getValue("body", element2) != null) {
                            sms.setMsg(getValue("body", element2));

                        } else {
                            sms.setMsg("");

                        }
                        smsbackup.add(sms);
                    }
                }
            } else {
                CM.showToast(this, "File Not Find");
            }
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        String returnn = "";
        if (node == null) {
            returnn = "";
        } else {
            returnn = node.getNodeValue();
        }

        return returnn;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public boolean saveSms(String phoneNumber, String message, String readState, String time, String folderName) {
        Boolean ret = false;
        try {
            ContentValues values = new ContentValues();
            if (phoneNumber != null && !phoneNumber.equals("")) {
                values.put("address", phoneNumber);

            } else {
                values.put("address", "");

            }
            if (message != null && !message.equals("")) {
                values.put("body", message);

            } else {
                values.put("body", "");

            }

            if (readState != null && !readState.equals("")) {
                values.put("read", readState); //"0" for have not read sms and "1" for have read sms

            } else {
                values.put("read", "1"); //"0" for have not read sms and "1" for have read sms

            }

            if (time != null && !time.equals("")) {
                values.put("date", time);

            } else {
                values.put("date", "");

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Uri uri = Telephony.Sms.Sent.CONTENT_URI;
                if (folderName.equals("inbox")) {
                    uri = Telephony.Sms.Inbox.CONTENT_URI;
                }
                getContentResolver().insert(uri, values);
            } else {
                if (folderName.equals("inbox")) {
                    getContentResolver().insert(Uri.parse("content://sms/" + folderName), values);
                }
            }


            ret = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            ret = false;
        }
        return ret;
    }


    public void createNewXml(List<Sms> lstSms, String fileName) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("http://crunchify.com/CrunchifyCreateXMLDOM", "content");
            doc.appendChild(mainRootElement);

            for (int i = 0; i < lstSms.size(); i++) {
                String id, address, msg, time, readState;
                if (lstSms.get(i).getId() != null) {
                    id = lstSms.get(i).getId();
                } else {
                    id = "";

                }
                if (lstSms.get(i).getAddress() != null) {
                    address = lstSms.get(i).getAddress();
                } else {
                    address = "";
                }
                if (lstSms.get(i).getMsg() != null) {
                    msg = lstSms.get(i).getMsg();
                } else {
                    msg = "";
                }
                if (lstSms.get(i).getTime() != null) {
                    time = lstSms.get(i).getTime();
                } else {
                    time = "";
                }

                if (lstSms.get(i).getReadState() != null) {
                    readState = lstSms.get(i).getReadState();
                } else {
                    readState = "";
                }
                mainRootElement.appendChild(getCompany(doc, id, address, msg, time, readState));
            }
            // output DOM XML to console
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);

            File file = new File(smsFile, fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("IOException", "Exception in create new File(");
            }
            if (file.exists()) {
                StreamResult streamResult = new StreamResult(file);
                transformer.transform(source, streamResult);
                status = "success";
                System.out.println("\nXML DOM Created Successfully..");

            } else {
                CM.showToast(ViewSms.this, "FILE PATH DOES NOT EXIST");

            }

        } catch (Exception e) {
            e.printStackTrace();
            status = "fail";
        }


    }


    private static Node getCompany(Document doc, String id, String address, String msg, String time, String readState) {
        Element company = doc.createElement("sms");
        company.setAttribute("id", id);
        company.appendChild(getCompanyElements(doc, company, "time", time));
        company.appendChild(getCompanyElements(doc, company, "address", address));
        company.appendChild(getCompanyElements(doc, company, "body", msg));
        company.appendChild(getCompanyElements(doc, company, "read", readState));

        return company;
    }

    // utility method to create text node
    private static Node getCompanyElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }


    class TestAsyncRestore extends AsyncTask<String, Object, ArrayList<Sms>> {
        String TAG = getClass().getSimpleName();
        String fileName = "";

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewSms.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected ArrayList<Sms> doInBackground(String... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            parseXML(arg0[0]);

            return smsbackup;
        }

        protected void onProgressUpdate(final Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(ArrayList<Sms> result) {

            if (result != null) {
                if (result.size() > 0) {
                    for (int i = 0; i < smsbackup.size(); i++) {
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            uri = Telephony.Sms.Sent.CONTENT_URI;
                        } else {
                            uri = Uri.parse("content://sms/");
                        }

                        if (smsbackup.get(i).getMsg() != null && !smsbackup.get(i).getMsg().equals("")) {
                            String string = "\"" + smsbackup.get(i).getMsg().replace("'", "''").replace("\"", "\"\"") + "\"";
                            Cursor c = null;
                            try {
                                c = getContentResolver().query(uri, null, " body " + " = " + string, null, null);
                            } catch (Exception e) {

                            }
                            try {
                                if (c != null) {
                                    if (c.getCount() == 0) {
                                        Log.i(TAG, "onClick:Data not found in database");
                                        saveSms(smsbackup.get(i).getAddress(), smsbackup.get(i).getMsg(), "1", smsbackup.get(i).getTime(), "inbox");
                                    } else {
                                        Log.i(TAG, "onClick:Data found in database ");

                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    }

                    CM.showToast(ViewSms.this, "SMS RESTORE DONE");
                } else {
                    CM.showToast(ViewSms.this, "NO DATA FOUND");
                }


            } else {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewSms.this, "SMS RESTORE FAIL");
            }
            mProgressDialog.dismiss();

        }
    }

    class TestAsync extends AsyncTask<String, Object, String> {
        String TAG = getClass().getSimpleName();
        String fileName = "";

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewSms.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected String doInBackground(String... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            fileName = arg0[0].toString();
            getAllSms(arg0[0].toString());
            return status;
        }

        protected void onProgressUpdate(final Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(String result) {

            if (result.equals("success")) {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewSms.this, "SMS BACKUP DONE");

                if (CM.getSp(ViewSms.this, "cloud", "").equals("1")) {
                    ShowDialog(fileName);
                }


            } else {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewSms.this, "SMS BACKUP FAIL");
            }
            mProgressDialog.dismiss();

        }
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
                DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewSms.this);
                AndroidAuthSession session = mDBApi.getSession();
                if (session.authenticationSuccessful()) {
                    session.finishAuthentication();
                    TokenPair tokens = session.getAccessTokenPair();
                    CM.setSp(ViewSms.this, "key", tokens.key.toString());
                    CM.setSp(ViewSms.this, "secret", tokens.secret.toString());
                    File file = new File(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath(), fileName);
                    if (file.exists()) {
                        UploadFile upload = new UploadFile(ViewSms.this, mDBApi, CONSTANTS.DIRSMS, file);
                        upload.execute();
                    } else {
                        CM.showToast(ViewSms.this, "FILE NOT FOUND");
                    }
                } else {
                    mDBApi.getSession().startAuthentication(ViewSms.this);

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

    public void showPopup(Context context, final String item) {
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to delete file ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        File file = new File(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath(), item);
                        if (file.exists()) {
                            if (file.exists()) {
                                file.delete();
                                CM.showToast(ViewSms.this, "FILE DELETED");
                            }

                        } else {
                            CM.showToast(ViewSms.this, "FILE NOT FOUND");
                        }


                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setIcon(R.drawable.delete).show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSms.this);
        builder.setTitle("Select Files");

        //list of items
        final String[] items = dialog_item.toArray(new String[dialog_item.size()]);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        String name = items[which];
                        CM.showToast(ViewSms.this, name);


                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if (code.equals("2")) {
                            new TestAsyncRestore().execute(items[selectedPosition]);
                        } else if (code.equals("3")) {

                            Intent intent = new Intent(ViewSms.this, ViewSmsDetail.class);
                            intent.putExtra("fileName", items[selectedPosition]);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_in_from_left,
                                    R.anim.push_out_to_right);

                            //CM.startActivity(ViewSms.this, ViewSmsDetail.class);
                        } else if (code.equals("4")) {

                            if (CM.isInternetAvailable(ViewSms.this)) {

                                DropBoxCommon dropBoxCommon = new DropBoxCommon();
                                mDBApi = dropBoxCommon.initView(ViewSms.this);
                                AndroidAuthSession session = mDBApi.getSession();
                                if (session.authenticationSuccessful()) {
                                    session.finishAuthentication();
                                    File file = new File(CONSTANTS.createAppFolderForSMS(ViewSms.this).getAbsolutePath(), items[selectedPosition]);
                                    if (file.exists()) {
                                        UploadFile upload = new UploadFile(ViewSms.this, mDBApi, CONSTANTS.DIRSMS, file);
                                        upload.execute();
                                    } else {
                                        CM.showToast(ViewSms.this, "FILE NOT FOUND");
                                    }
                                } else {
                                    CM.showToast(ViewSms.this, getString(R.string.sessionExp));
                                    mDBApi.getSession().startAuthentication(ViewSms.this);
                                }
                            } else {
                                CM.showToast(ViewSms.this, getString(R.string.msg_internet_unavailable_msg));
                            }
                        } else if (code.equals("9")) {
                            showPopup(ViewSms.this, items[selectedPosition]);
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
           // titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewSms.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }


}
