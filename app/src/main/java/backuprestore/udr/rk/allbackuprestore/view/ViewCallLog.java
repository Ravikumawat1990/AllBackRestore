package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
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
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import backuprestore.udr.rk.allbackuprestore.Model.CollLogsModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.CallLogUtility;
import backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon;
import backuprestore.udr.rk.allbackuprestore.util.UploadFile;

import static backuprestore.udr.rk.allbackuprestore.util.CM.convertPixelsToDp;

public class ViewCallLog extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ViewCallLog";
    LinearLayout backupLayout, restoreLayout, viewBackupLayout, sendToCloudLayout, deleteLayout, deleteAlLCallLayout;
    private String status;
    private ProgressDialog mProgressDialog;
    private ArrayList<CollLogsModel> collLogsModels;
    private File callLogFile;
    private DropboxAPI<AndroidAuthSession> mdbApi;
    Boolean aBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Call Logs");
        setSupportActionBar(toolbar);
        callLogFile = new File(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewCallLog.this);

            }
        });


        getActionBarTextView(toolbar);
        initView();

    }

    private void initView() {
        backupLayout = (LinearLayout) findViewById(R.id.backupLayout);
        restoreLayout = (LinearLayout) findViewById(R.id.restoreLayout);
        viewBackupLayout = (LinearLayout) findViewById(R.id.viewBackupLayout);
        sendToCloudLayout = (LinearLayout) findViewById(R.id.sendToCloudLayout);
        deleteLayout = (LinearLayout) findViewById(R.id.deleteLayout);
        deleteAlLCallLayout = (LinearLayout) findViewById(R.id.deleteAlLCallLayout);
        collLogsModels = new ArrayList<CollLogsModel>();
        backupLayout.setOnClickListener(this);
        restoreLayout.setOnClickListener(this);
        viewBackupLayout.setOnClickListener(this);
        sendToCloudLayout.setOnClickListener(this);
        deleteLayout.setOnClickListener(this);
        deleteAlLCallLayout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(ViewCallLog.this);
    }

    public void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewCallLog.this);
        alert.setTitle(getString(R.string.app_name)); //Set Alert dialog title here
        alert.setMessage(getString(R.string.backuoMsg)); //Message here
        final TextView textView = new TextView(ViewCallLog.this);
        textView.setText(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath());
        textView.setPadding(40, 0, 0, 0);
        alert.setView(textView);

        LinearLayout linearLayout = new LinearLayout(ViewCallLog.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);

        final EditText input = new EditText(ViewCallLog.this);
        input.setText(CONSTANTS.CALLLOGES_FILE_NAME);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertPixelsToDp(50, this), 0, convertPixelsToDp(50, this), 0);
        input.setLayoutParams(params);
        input.setSelection(input.getText().length());
        linearLayout.addView(input);

        alert.setView(linearLayout);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(ViewCallLog.this, "Please Enter File Name with ext.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backupLayout:
                // new TestAsync().execute();
                showDialog();
                break;
            case R.id.restoreLayout:

                File directory = new File(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath());
                if (directory.exists()) {
                    File[] files = directory.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "1");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;

            case R.id.viewBackupLayout:


                File directory1 = new File(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath());
                if (directory1.exists()) {
                    File[] files = directory1.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "2");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;

            case R.id.sendToCloudLayout:


                File directory3 = new File(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath());
                if (directory3.exists()) {
                    File[] files = directory3.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "3");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;

            case R.id.deleteLayout:

                File directory4 = new File(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath());
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

            case R.id.deleteAlLCallLayout:
                break;

        }

    }

    class TestAsync extends AsyncTask<String, Object, String> {
        String TAG = getClass().getSimpleName();

        String fileName;

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewCallLog.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected String doInBackground(String... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            fileName = arg0[0];
            getCallDetails(arg0[0]);

            return status;
        }

        protected void onProgressUpdate(final Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(String result) {


            if (result != null) {
                if (result.equals("success")) {
                    Log.d(TAG + " onPostExecute", "" + result);
                    CM.showToast(ViewCallLog.this, "CALL LOG BACKUP DONE");

                    if (CM.getSp(ViewCallLog.this, "cloud", "").equals("1")) {
                        ShowDialog(fileName);
                    }
                } else {
                    Log.d(TAG + " onPostExecute", "" + result);
                    CM.showToast(ViewCallLog.this, "CALL LOG BACKUP FAIL");
                }
            } else {
                CM.showToast(ViewCallLog.this, "CALL LOG BACKUP FAIL");
            }
            mProgressDialog.dismiss();

        }
    }


    private void getCallDetails(String fileName) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("http://crunchify.com/CrunchifyCreateXMLDOM", "callLogs");
            doc.appendChild(mainRootElement);

            StringBuffer sb = new StringBuffer();
            Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            int newCall = managedCursor.getColumnIndex(CallLog.Calls.NEW);
            int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);


            sb.append("Call Details :");
            while (managedCursor.moveToNext()) {
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                String newcall = managedCursor.getString(newCall);
                String callname = managedCursor.getString(name);
                Date callDayTime = new Date(Long.valueOf(callDate));


                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                mainRootElement.appendChild(getCompany(doc, phNumber, callDate, callDuration, dir, newcall, callname));
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);


            if (!callLogFile.exists()) {
                callLogFile.mkdir();
            }
            File file = new File(callLogFile, fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("IOException", "Exception in create new File(");
            }
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(source, streamResult);
            status = "success";
            System.out.println("\nXML DOM Created Successfully..");


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private static Node getCompany(Document doc, String number, String date, String duration, String type, String newcall, String catchName) {
        Element company = doc.createElement("calllog");

        if (number != null) {
            company.appendChild(getCompanyElements(doc, company, "number", number));

        } else {
            company.appendChild(getCompanyElements(doc, company, "number", ""));

        }
        if (date != null) {
            company.appendChild(getCompanyElements(doc, company, "date", String.valueOf(date)));

        } else {
            company.appendChild(getCompanyElements(doc, company, "date", ""));

        }
        if (duration != null) {
            company.appendChild(getCompanyElements(doc, company, "duration", duration));

        } else {
            company.appendChild(getCompanyElements(doc, company, "duration", ""));

        }
        if (type != null) {
            company.appendChild(getCompanyElements(doc, company, "type", type));

        } else {
            company.appendChild(getCompanyElements(doc, company, "type", ""));

        }
        if (newcall != null) {
            company.appendChild(getCompanyElements(doc, company, "newcall", newcall));

        } else {
            company.appendChild(getCompanyElements(doc, company, "newcall", ""));

        }
        if (catchName != null) {
            company.appendChild(getCompanyElements(doc, company, "callName", catchName));

        } else {
            company.appendChild(getCompanyElements(doc, company, "callName", ""));
        }

        return company;
    }

    // utility method to create text node
    private static Node getCompanyElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    class TestAsyncParce extends AsyncTask<String, Object, ArrayList<CollLogsModel>> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewCallLog.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected ArrayList<CollLogsModel> doInBackground(String... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            parseXML(arg0[0]);
            return collLogsModels;
        }

        protected void onProgressUpdate(final Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(ArrayList<CollLogsModel> result) {

            if (result != null) {
                Log.d(TAG + " onPostExecute", "" + result);

                if (collLogsModels.size() > 0) {
                    CM.showToast(ViewCallLog.this, "CALL LOG RESTORE DONE");


                } else {
                    CM.showToast(ViewCallLog.this, "NO DATA FOUND");

                }

            } else {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewCallLog.this, "CALL LOG RESTORE DONE");
            }
            mProgressDialog.dismiss();

        }
    }

    private void parseXML(String fileName) {
        try {

            File file = new File(callLogFile, fileName);
            if (file.exists()) {
                InputStream is = new FileInputStream(file.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("calllog");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        CollLogsModel collLogsModel = new CollLogsModel();
                        Element element2 = (Element) node;
                        if (getValue("number", element2) != null) {
                            collLogsModel.setNumber(getValue("number", element2));
                        } else {
                            collLogsModel.setNumber("");

                        }
                        if (getValue("date", element2) != null) {
                            collLogsModel.setDate(getValue("date", element2));

                        } else {
                            collLogsModel.setDate("");

                        }
                        if (getValue("duration", element2) != null) {
                            collLogsModel.setDuration(getValue("duration", element2));

                        } else {
                            collLogsModel.setDuration("");

                        }
                        if (getValue("type", element2) != null) {
                            collLogsModel.setType(getValue("type", element2));

                        } else {
                            collLogsModel.setType("");

                        }

                        if (getValue("callName", element2) != null) {
                            collLogsModel.setCached_name(getValue("callName", element2));

                        } else {
                            collLogsModel.setCached_name("");

                        }
                        collLogsModel.setNewNo("");


                        String callType;
                        if (collLogsModel.getType().equals("OUTGOING")) {
                            callType = "1";
                        } else if (collLogsModel.getType().equals("INCOMING")) {
                            callType = "2";

                        } else {
                            callType = "3";
                        }

                        if (collLogsModel.getDate() != null && !collLogsModel.getDate().equals("")) {
                            Uri uri = CallLog.Calls.CONTENT_URI;
                            Cursor c = null;
                            try {
                                c = getContentResolver().query(uri, null, CallLog.Calls.DATE + " = " + Long.parseLong(collLogsModel.getDate()), null, null);
                            } catch (Exception e) {
                                e.getMessage();

                            }
                            try {
                                if (c != null) {
                                    if (c.getCount() == 0) {
                                        CallLogUtility.AddNumToCallLog(getBaseContext().getContentResolver(), collLogsModel.getNumber().toString(), Integer.parseInt(callType), collLogsModel.getDate(), collLogsModel.getCached_name().toString(), collLogsModel.getDuration());
                                        Log.i(TAG, "onClick:Data not found in database ");
                                    } else {
                                        Log.i(TAG, "onClick:Data found in database ");

                                    }
                                }
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                        collLogsModels.add(collLogsModel);
                    }
                }
                collLogsModels.size();
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

    public void showPopup(Context context, final String item) {
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to delete file ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        File file = new File(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath(), item);
                        if (file.exists()) {
                            if (file.exists()) {
                                file.delete();
                                CM.showToast(ViewCallLog.this, "FILE DELETED");
                            }

                        } else {
                            CM.showToast(ViewCallLog.this, "FILE NOT FOUND");

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
                DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewCallLog.this);
                AndroidAuthSession session = mDBApi.getSession();
                if (session.authenticationSuccessful()) {
                    session.finishAuthentication();


                    TokenPair tokens = session.getAccessTokenPair();
                    CM.setSp(ViewCallLog.this, "key", tokens.key.toString());
                    CM.setSp(ViewCallLog.this, "secret", tokens.secret.toString());
                    File file = new File(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath(), fileName);
                    if (file.exists()) {
                        UploadFile upload = new UploadFile(ViewCallLog.this, mDBApi, CONSTANTS.DIRCALLLOGES, file);
                        upload.execute();
                    } else {
                        CM.showToast(ViewCallLog.this, "FILE NOT FOUND");
                    }
                } else {
                    mdbApi.getSession().startAuthentication(ViewCallLog.this);
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

    void showListDialog(File[] files, final String code) {

        //String path = Environment.getExternalStorageDirectory().toString() + "/AyalaPDF";
        // Log.d("Files", "Path: " + path);

        Log.d("Files", "Size: " + files.length);
        ArrayList<String> dialog_item = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            dialog_item.add(files[i].getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCallLog.this);
        builder.setTitle("Select Files");

        //list of items
        final String[] items = dialog_item.toArray(new String[dialog_item.size()]);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        String name = items[which];
                        CM.showToast(ViewCallLog.this, name);
                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if (code.equals("1")) {
                            new TestAsyncParce().execute(items[selectedPosition]);
                        } else if (code.equals("2")) {
                            Intent intent = new Intent(ViewCallLog.this, ViewBackUpCallLogs.class);
                            intent.putExtra("fileName", items[selectedPosition]);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_in_from_left,
                                    R.anim.push_out_to_right);

                        } else if (code.equals("3")) {


                            if (CM.isInternetAvailable(ViewCallLog.this)) {


                                DropBoxCommon dropBoxCommon = new DropBoxCommon();
                                mdbApi = dropBoxCommon.getDropboxAPI(ViewCallLog.this);
                                AndroidAuthSession session = mdbApi.getSession();
                                if (session.authenticationSuccessful()) {
                                    session.finishAuthentication();
                                    File file = new File(CONSTANTS.createAppFolderForCallog(ViewCallLog.this).getAbsolutePath(), items[selectedPosition]);
                                    TokenPair tokens = session.getAccessTokenPair();
                                    CM.setSp(ViewCallLog.this, "key", tokens.key.toString());
                                    CM.setSp(ViewCallLog.this, "secret", tokens.secret.toString());
                                    if (file.exists()) {
                                        UploadFile upload = new UploadFile(ViewCallLog.this, mdbApi, CONSTANTS.DIRCALLLOGES, file);
                                        upload.execute();
                                    } else {
                                        CM.showToast(ViewCallLog.this, "FILE NOT FOUND");
                                    }
                                } else {
                                    CM.showToast(ViewCallLog.this, getString(R.string.sessionExp));
                                    mdbApi.getSession().startAuthentication(ViewCallLog.this);

                                }
                            } else {
                                CM.showToast(ViewCallLog.this, getString(R.string.msg_internet_unavailable_msg));
                            }
                        } else if (code.equals("9")) {
                            showPopup(ViewCallLog.this, items[selectedPosition]);
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


    @Override
    protected void onResume() {
        super.onResume();
    }

    private TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Typeface khandBold = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            titleTextView.setTypeface(khandBold);
            //titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewCallLog.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
