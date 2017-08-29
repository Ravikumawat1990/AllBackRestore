package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import backuprestore.udr.rk.allbackuprestore.Model.CalenderParentModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon;
import backuprestore.udr.rk.allbackuprestore.util.UploadFile;
import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;

import static backuprestore.udr.rk.allbackuprestore.util.CM.convertPixelsToDp;

public class ViewCalendar extends AppCompatActivity implements View.OnClickListener {

    private List<Event> calendar;
    private String status;
    private ProgressDialog mProgressDialog;
    private CalendarProvider calendarProvider;
    private List<Calendar> calendars;
    private ArrayList<CalenderParentModel> calenderParentModels, calenderParentModels1;
    LinearLayout backupLayout, restoreLayout, sendToCloudLayout, deleteBackupLayout, deleteAlLLayout;
    private File calendraFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canalder);
        calendraFile = new File(CONSTANTS.createAppFolderForCalendra(ViewCalendar.this).getAbsolutePath());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Calender");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewCalendar.this);

            }
        });

        getActionBarTextView(toolbar);
        calendarProvider = new CalendarProvider(ViewCalendar.this);
        calenderParentModels1 = new ArrayList<>();

        initView();
    }

    private void initView() {
        backupLayout = (LinearLayout) findViewById(R.id.backupLayout);
        restoreLayout = (LinearLayout) findViewById(R.id.restoreLayout);
        sendToCloudLayout = (LinearLayout) findViewById(R.id.sendToCloudLayout);
        deleteBackupLayout = (LinearLayout) findViewById(R.id.deleteBackupLayout);
        deleteAlLLayout = (LinearLayout) findViewById(R.id.deleteAlLLayout);
        backupLayout.setOnClickListener(this);
        restoreLayout.setOnClickListener(this);
        sendToCloudLayout.setOnClickListener(this);
        deleteBackupLayout.setOnClickListener(this);
        deleteAlLLayout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(ViewCalendar.this);
    }


    public void createNewXml(List<CalenderParentModel> lstSms, String fileName) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("http://crunchify.com/CrunchifyCreateXMLDOM", "calender");
            doc.appendChild(mainRootElement);

            for (int i = 0; i < lstSms.size(); i++) {
                String id, title, description, time, readState, eventlocation, dtstart, dtend, eventstatus, eventtimezone, hasalarm;
                if (lstSms.get(i).getCalendar_id() != null) {
                    id = lstSms.get(i).getCalendar_id();
                } else {
                    id = "";

                }
                if (lstSms.get(i).getTitle() != null) {
                    title = lstSms.get(i).getTitle();
                } else {
                    title = "";
                }
                if (lstSms.get(i).getDescription() != null) {
                    description = lstSms.get(i).getDescription();
                } else {
                    description = "";
                }
                if (lstSms.get(i).getEventLocation() != null) {
                    eventlocation = lstSms.get(i).getEventLocation();
                } else {
                    eventlocation = "";
                }

                if (lstSms.get(i).getDtstart() != null) {
                    dtstart = lstSms.get(i).getDtstart();
                } else {
                    dtstart = "";
                }
                if (lstSms.get(i).getDtend() != null) {
                    dtend = lstSms.get(i).getDtend();
                } else {
                    dtend = "";
                }

                if (lstSms.get(i).getEventStatus() != null) {
                    eventstatus = lstSms.get(i).getEventStatus();
                } else {
                    eventstatus = "";
                }
                if (lstSms.get(i).getEventTimezone() != null) {
                    eventtimezone = lstSms.get(i).getEventTimezone();
                } else {
                    eventtimezone = "";
                }
                if (lstSms.get(i).getHasAlarm() != null) {
                    hasalarm = lstSms.get(i).getHasAlarm();
                } else {
                    hasalarm = "";
                }


                mainRootElement.appendChild(getCompany(doc, id, title, description, eventlocation, dtstart, dtend, eventstatus, eventtimezone, hasalarm));
            }
            // output DOM XML to console
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);

            // File filepath = new File(Environment.getExternalStorageDirectory(), "Backup");
            if (!calendraFile.exists()) {
                calendraFile.mkdir();
            }
            ///mnt/sdcard/Backup/sms.xml
            File file = new File(calendraFile, fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("IOException", "Exception in create new File(");
            }
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(source, streamResult);
            status = "success";
            System.out.println("\nXML DOM Created Successfully..");

        } catch (Exception e) {
            e.printStackTrace();
            status = "fail";
        }


    }


    private static Node getCompany(Document doc, String id, String title, String description, String eventlocation, String dtstart, String dtend, String eventstatus, String eventtimezone, String hasalarm) {

        Element company = doc.createElement("calender");

        company.appendChild(getCompanyElements(doc, company, "calendar_id", id));
        company.appendChild(getCompanyElements(doc, company, "title", title));
        company.appendChild(getCompanyElements(doc, company, "description", description));
        company.appendChild(getCompanyElements(doc, company, "eventLocation", eventlocation));
        company.appendChild(getCompanyElements(doc, company, "dtstart", dtstart));
        company.appendChild(getCompanyElements(doc, company, "dtend", dtend));
        company.appendChild(getCompanyElements(doc, company, "eventStatus", eventstatus));
        company.appendChild(getCompanyElements(doc, company, "eventTimezone", eventtimezone));
        company.appendChild(getCompanyElements(doc, company, "hasAlarm", hasalarm));
        return company;
    }

    // utility method to create text node
    private static Node getCompanyElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }


    public void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewCalendar.this);
        alert.setTitle(getString(R.string.app_name)); //Set Alert dialog title here
        alert.setMessage(getString(R.string.backuoMsg)); //Message here
        final TextView textView = new TextView(ViewCalendar.this);
        textView.setText(CONSTANTS.createAppFolderForCalendra(ViewCalendar.this).getAbsolutePath());
        textView.setPadding(40, 0, 0, 0);
        alert.setView(textView);

        LinearLayout linearLayout = new LinearLayout(ViewCalendar.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);

        final EditText input = new EditText(ViewCalendar.this);
        input.setText(CONSTANTS.CALENDRA_FILE_NAME);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertPixelsToDp(50, this), 0, convertPixelsToDp(50, this), 0);
        input.setLayoutParams(params);

        input.setSelection(input.getText().length());
        linearLayout.addView(input);

        alert.setView(linearLayout);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(ViewCalendar.this, "Please Enter File Name with ext.", Toast.LENGTH_SHORT).show();
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

                showDialog();
                // new TestAsync().execute();
                break;
            case R.id.restoreLayout:

                File directory = new File(CONSTANTS.createAppFolderForCalendra(ViewCalendar.this).getAbsolutePath());
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
            case R.id.sendToCloudLayout:


                File directory1 = new File(CONSTANTS.createAppFolderForCalendra(ViewCalendar.this).getAbsolutePath());
                if (directory1.exists()) {
                    File[] files = directory1.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "4");
                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;
            case R.id.deleteLayout:


                File directory3 = new File(CONSTANTS.createAppFolderForCalendra(ViewCalendar.this).getAbsolutePath());
                if (directory3.exists()) {
                    File[] files = directory3.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "5");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;


        }

    }


    class TestAsync extends AsyncTask<String, Object, String> {
        String TAG = getClass().getSimpleName();
        String fileName = "";

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewCalendar.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected String doInBackground(String... arg0) {

            fileName = arg0[0];
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            calendars = calendarProvider.getCalendars().getList();
            calenderParentModels = new ArrayList<>();
            for (int i = 0; i < calendars.size(); i++) {
                calendar = calendarProvider.getEvents(calendars.get(i).id).getList();
                for (int j = 0; j < calendar.size(); j++) {
                    CalenderParentModel calenderParentModel = new CalenderParentModel();
                    calenderParentModel.setCalendar_id(String.valueOf(calendars.get(i).id));
                    calenderParentModel.setTitle(calendar.get(j).title);
                    calenderParentModel.setDescription(String.valueOf(calendar.get(j).description));
                    calenderParentModel.setDtstart(String.valueOf(calendar.get(j).dTStart));
                    calenderParentModel.setDtend(String.valueOf(calendar.get(j).dTend));
                    calenderParentModel.setEventStatus(calendar.get(j).status);
                    calenderParentModel.setEventTimezone(calendar.get(j).eventTimeZone);
                    calenderParentModel.setHasAlarm(String.valueOf(calendar.get(j).hasAlarm));
                    calenderParentModels.add(calenderParentModel);

                }
            }

            calenderParentModels.size();
            createNewXml(calenderParentModels, arg0[0]);
            return status;
        }

        protected void onProgressUpdate(final Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(String result) {

            if (result != null) {
                if (result.equals("success")) {
                    Log.d(TAG + " onPostExecute", "" + result);
                    CM.showToast(ViewCalendar.this, "CALENDER BACKUP DONE");

                    if (CM.getSp(ViewCalendar.this, "cloud", "").equals("1")) {
                        ShowDialog(fileName);
                    }

                } else {
                    Log.d(TAG + " onPostExecute", "" + result);
                    CM.showToast(ViewCalendar.this, "CALENDER BACKUP FAIL");
                }
            } else {
                CM.showToast(ViewCalendar.this, "CALENDER BACKUP FAIL");
            }
            mProgressDialog.dismiss();

        }
    }

    class TestAsyncRestore extends AsyncTask<String, Object, ArrayList<CalenderParentModel>> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewCalendar.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected ArrayList<CalenderParentModel> doInBackground(String... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");

            parseXML("calender.xml");
            return calenderParentModels1;
        }

        protected void onProgressUpdate(final Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(ArrayList<CalenderParentModel> result) {

            if (result != null) {
                Log.d(TAG + " onPostExecute", "" + result);
                if (result.size() > 0) {
                    CM.showToast(ViewCalendar.this, "CALENDER BACKUP DONE");

                    //For deletion of calendar event
//                    Uri uri = ContentUris.withAppendedId(CALENDAR_URI, Integer.parseInt(id));
//                    getContentResolver().delete(uri, null, null);
//

                    for (int i = 0; i < result.size(); i++) {
                        try {
                            String eventUriString = "content://com.android.calendar/events";
                            ContentValues eventValues = new ContentValues();
                            eventValues.put("calendar_id", result.get(i).getCalendar_id()); // id, We need to choose from
                            eventValues.put("title", result.get(i).getTitle());
                            eventValues.put("description", result.get(i).getDescription());
                            eventValues.put("eventLocation", result.get(i).getEventLocation());
                            eventValues.put("dtstart", result.get(i).getDtstart());
                            eventValues.put("dtend", result.get(i).getDtend());
                            eventValues.put("eventStatus", result.get(i).getEventStatus()); // This information is
                            eventValues.put("eventTimezone", result.get(i).getEventStatus());
                            eventValues.put("hasAlarm", 1); // 0 for false, 1 for true
                            Uri eventUri = getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
                            //  eventID = Long.parseLong(eventUri.getLastPathSegment());
                        } catch (Exception ex) {
                            Log.i(TAG, "onPostExecute: " + ex.getMessage());
                            //  Log.error("Error in adding event on calendar" + ex.getMessage());
                        }


                    }


                } else {
                    CM.showToast(ViewCalendar.this, "NO DATA FOUND");

                }
            } else {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewCalendar.this, "CALENDER BACKUP FAIL");
            }
            mProgressDialog.dismiss();

        }
    }


    private void parseXML(String fileName) {
        try {

            //   File filepath = new File(Environment.getExternalStorageDirectory(), "Backup");
            ///mnt/sdcard/Backup/sms.xml
            File file = new File(calendraFile, fileName);
            if (file.exists()) {
                InputStream is = new FileInputStream(file.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("calender");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        CalenderParentModel collLogsModel = new CalenderParentModel();
                        Element element2 = (Element) node;
                        if (getValue("calendar_id", element2) != null) {
                            collLogsModel.setCalendar_id(getValue("calendar_id", element2));
                        } else {
                            collLogsModel.setCalendar_id("");

                        }
                        if (getValue("title", element2) != null) {
                            collLogsModel.setTitle(getValue("title", element2));

                        } else {
                            collLogsModel.setTitle("");

                        }
                        if (getValue("description", element2) != null) {
                            collLogsModel.setDescription(getValue("description", element2));

                        } else {
                            collLogsModel.setDescription("");

                        }
                        if (getValue("eventLocation", element2) != null) {
                            collLogsModel.setEventLocation(getValue("eventLocation", element2));

                        } else {
                            collLogsModel.setEventLocation("");

                        }

                        if (getValue("dtstart", element2) != null) {
                            collLogsModel.setDtstart(getValue("dtstart", element2));

                        } else {
                            collLogsModel.setDtstart("");

                        }

                        if (getValue("dtend", element2) != null) {
                            collLogsModel.setDtend(getValue("dtend", element2));

                        } else {
                            collLogsModel.setDtend("");

                        }
                        if (getValue("eventStatus", element2) != null) {
                            collLogsModel.setEventStatus(getValue("eventStatus", element2));

                        } else {
                            collLogsModel.setEventStatus("");

                        }
                        if (getValue("eventTimezone", element2) != null) {
                            collLogsModel.setEventTimezone(getValue("eventTimezone", element2));

                        } else {
                            collLogsModel.setEventTimezone("");

                        }
                        if (getValue("hasAlarm", element2) != null) {
                            collLogsModel.setHasAlarm(getValue("hasAlarm", element2));

                        } else {
                            collLogsModel.setHasAlarm("");

                        }

                        calenderParentModels1.add(collLogsModel);
                    }
                }
                calenderParentModels1.size();
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
                        File file = new File(CONSTANTS.createAppFolderForCalendra(ViewCalendar.this).getAbsolutePath(), item);
                        if (file.exists()) {
                            if (file.exists()) {
                                file.delete();
                                CM.showToast(ViewCalendar.this, "FILE DELETED");

                            }
                        } else {
                            CM.showToast(ViewCalendar.this, "FILE NOT FOUND");

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
                DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewCalendar.this);
                AndroidAuthSession session = mDBApi.getSession();
                if (session.authenticationSuccessful()) {
                    session.finishAuthentication();
                    TokenPair tokens = session.getAccessTokenPair();
                    CM.setSp(ViewCalendar.this, "key", tokens.key.toString());
                    CM.setSp(ViewCalendar.this, "secret", tokens.secret.toString());

                    File file = new File(CONSTANTS.createAppFolderForCalendra(ViewCalendar.this).getAbsolutePath(), fileName);
                    if (file.exists()) {
                        UploadFile upload = new UploadFile(ViewCalendar.this, mDBApi, CONSTANTS.DIRCALENDRA, file);
                        upload.execute();
                    } else {
                        CM.showToast(ViewCalendar.this, "FILE NOT FOUND");
                    }
                } else {
                    mDBApi.getSession().startAuthentication(ViewCalendar.this);

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
        //  Log.d("Files", "Path: " + path);

        Log.d("Files", "Size: " + files.length);
        ArrayList<String> dialog_item = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            dialog_item.add(files[i].getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCalendar.this);
        builder.setTitle("Select Files");

        //list of items
        final String[] items = dialog_item.toArray(new String[dialog_item.size()]);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        String name = items[which];
                        CM.showToast(ViewCalendar.this, name);


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

                            Intent intent = new Intent(ViewCalendar.this, ViewSmsDetail.class);
                            intent.putExtra("fileName", items[selectedPosition]);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_in_from_left,
                                    R.anim.push_out_to_right);


                        } else if (code.equals("4")) {

                            if (CM.isInternetAvailable(ViewCalendar.this)) {


                                DropBoxCommon dropBoxCommon = new DropBoxCommon();
                                DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewCalendar.this);
                                AndroidAuthSession session = mDBApi.getSession();
                                if (session.authenticationSuccessful()) {
                                    session.finishAuthentication();
                                    TokenPair tokens = session.getAccessTokenPair();
                                    CM.setSp(ViewCalendar.this, "key", tokens.key.toString());
                                    CM.setSp(ViewCalendar.this, "secret", tokens.secret.toString());

                                    File file = new File(CONSTANTS.createAppFolderForCalendra(ViewCalendar.this).getAbsolutePath(), items[selectedPosition]);
                                    if (file.exists()) {
                                        UploadFile upload = new UploadFile(ViewCalendar.this, mDBApi, CONSTANTS.DIRCALENDRA, file);
                                        upload.execute();
                                    } else {
                                        CM.showToast(ViewCalendar.this, "FILE NOT FOUND");
                                    }
                                } else {
                                    CM.showToast(ViewCalendar.this, getString(R.string.sessionExp));
                                    mDBApi.getSession().startAuthentication(ViewCalendar.this);
                                }
                            } else {
                                CM.showToast(ViewCalendar.this, getString(R.string.msg_internet_unavailable_msg));
                            }
                        } else if (code.equals("5")) {
                            showPopup(ViewCalendar.this, items[selectedPosition]);
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
            //  titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewCalendar.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
