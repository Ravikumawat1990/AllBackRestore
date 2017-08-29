package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import backuprestore.udr.rk.allbackuprestore.Model.CollLogsModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.adapter.callLogadapter;
import backuprestore.udr.rk.allbackuprestore.interfac.OnItemClickCallLogListener;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;

public class ViewBackUpCallLogs extends AppCompatActivity {

    private String status;
    private ProgressDialog mProgressDialog;
    private ArrayList<CollLogsModel> collLogsModels;
    private RecyclerView mRecyclerView;
    callLogadapter callLogadapter;
    private File callLogFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_backup_call_logs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("VIEW CALL LOGS");
        setSupportActionBar(toolbar);
        callLogFile = new File(CONSTANTS.createAppFolderForCallog(ViewBackUpCallLogs.this).getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewBackUpCallLogs.this);

            }
        });


        getActionBarTextView(toolbar);
        initView();


    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        collLogsModels = new ArrayList<CollLogsModel>();

        Intent intent = getIntent();


        if (intent != null && intent.getStringExtra("fileName") != null) {
            new TestAsync().execute(intent.getStringExtra("fileName"));

        }
    }


    class TestAsync extends AsyncTask<String, Object, ArrayList<CollLogsModel>> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewBackUpCallLogs.this);
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

                if (result.size() > 0) {
                    callLogadapter = new callLogadapter(result, ViewBackUpCallLogs.this);
                    mRecyclerView.setAdapter(callLogadapter);
                    callLogadapter.SetOnItemClickListener(new OnItemClickCallLogListener() {
                        @Override
                        public void onItemClick(String name, String number) {


                            if (name.equals("")) {
                                if (number != null && number.equals("")) {
                                    ShowDilog("UnKnown", number);

                                }

                            } else {
                                if (number != null && number.equals("")) {
                                    ShowDilog(name, number);
                                }
                            }

                        }
                    });
                    //   CM.showToast(ViewBackUpCallLogs.this, "SMS BACKUP DONE");
                } else {
                    CM.showToast(ViewBackUpCallLogs.this, "NO DATA FOUND");

                }
            } else {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewBackUpCallLogs.this, "NO DATA FOUND");
            }
            mProgressDialog.dismiss();

        }
    }

    private void parseXML(String fileName) {
        try {

            //  File filepath = new File(Environment.getExternalStorageDirectory(), "Backup");
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

    public void ShowDilog(String name, final String number) {
        new AlertDialog.Builder(ViewBackUpCallLogs.this)
                .setTitle(name)
                .setMessage(number)
                .setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + number));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("SEND SMS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.putExtra("sms_body", "default content");
                        sendIntent.setType("vnd.android-dir/mms-sms");
                        startActivity(sendIntent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Typeface khandBold = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            titleTextView.setTypeface(khandBold);
            //  titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewBackUpCallLogs.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
