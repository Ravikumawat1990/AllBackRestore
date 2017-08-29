package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import backuprestore.udr.rk.allbackuprestore.Model.Sms;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.adapter.smsadapter;
import backuprestore.udr.rk.allbackuprestore.database.SMSTable;
import backuprestore.udr.rk.allbackuprestore.interfac.OnItemClickListener;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;

public class ViewSmsDetail extends AppCompatActivity {

    private ArrayList<Sms> smsArrayList;
    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    smsadapter smsadapter;
    private File smsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sms_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SMS");
        setSupportActionBar(toolbar);
        smsFile = new File(CONSTANTS.createAppFolderForSMS(ViewSmsDetail.this).getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewSmsDetail.this);

            }
        });

        getActionBarTextView(toolbar);
        SMSTable.deleteData();

        Intent intent = getIntent();

        if (intent != null && intent.getStringExtra("fileName") != null) {
            new TestAsync().execute(intent.getStringExtra("fileName"));

        }
    }


    private void parseXML(String fileName) {
        try {

            smsArrayList = new ArrayList<Sms>();
            File file = new File(smsFile, fileName);
            if (file.exists()) {
                InputStream is = new FileInputStream(file.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("sms");
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
                        if (getValue("read", element2) != null) {
                            sms.setReadState(getValue("read", element2));

                        } else {
                            sms.setReadState("");

                        }
                        sms.setIsSelected("0");
                        sms.setIsBackup("0");
                        smsArrayList.add(sms);
                    }
                }
            } else {
                CM.showToast(this, "FILE NOT FIND");
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

    class TestAsync extends AsyncTask<String, Object, ArrayList<Sms>> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewSmsDetail.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected ArrayList<Sms> doInBackground(String... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");

            parseXML(arg0[0]);
            SMSTable.Insert(smsArrayList);
            ArrayList<Sms> smsArrayList1 = SMSTable.getAppData();
            return smsArrayList1;
        }

        protected void onProgressUpdate(Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(ArrayList<Sms> result) {

            if (result != null) {
                if (result.size() > 0) {
                    Log.d(TAG + " onPostExecute", "" + result);
                    mRecyclerView = (RecyclerView) findViewById(R.id.smsrecycleView);
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewSmsDetail.this));
                    smsadapter = new smsadapter(result, ViewSmsDetail.this);
                    mRecyclerView.setAdapter(smsadapter);
                    mProgressDialog.dismiss();

                    smsadapter.SetOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(String value) {

                            Intent intent = new Intent(ViewSmsDetail.this, ViewReadSms.class);
                            intent.putExtra("key", value);
                            startActivity(intent);
                        }
                    });
                } else {
                    CM.showToast(ViewSmsDetail.this, getString(R.string.notDat));
                }
                mProgressDialog.dismiss();
            } else {
                CM.showToast(ViewSmsDetail.this, getString(R.string.notDat));
            }
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(this);
    }

    private TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Typeface khandBold = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            titleTextView.setTypeface(khandBold);
            //     titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewSmsDetail.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
