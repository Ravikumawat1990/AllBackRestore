package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import backuprestore.udr.rk.allbackuprestore.adapter.MyAdapter;
import backuprestore.udr.rk.allbackuprestore.database.SMSTable;
import backuprestore.udr.rk.allbackuprestore.interfac.AsyncResponse;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;

public class ViewSmsConvercation extends AppCompatActivity implements ListView.OnItemClickListener, View.OnClickListener {

    private ArrayList<Sms> smsArrayList;
    private ProgressDialog mProgressDialog;
    private ArrayList<Sms> smsArrayList1;
    TestAsync asyncTask = new TestAsync();
    private MyAdapter adapter;
    private ListView listView;
    private CheckBox checkBoxAlLChecked;
    private Button buttonBackup;
    private String status;
    private File smsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sms_conv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.selectConv));
        setSupportActionBar(toolbar);
        smsFile = new File(CONSTANTS.createAppFolderForSMS(ViewSmsConvercation.this).getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewSmsConvercation.this);

            }
        });

        getActionBarTextView(toolbar);
        SMSTable.deleteData();

        smsArrayList1 = new ArrayList<>();
        buttonBackup = (Button) findViewById(R.id.btnBackup);
        checkBoxAlLChecked = (CheckBox) findViewById(R.id.allChecked);
        listView = (ListView) findViewById(R.id.my_list);
        adapter = new MyAdapter(this, smsArrayList1, checkBoxAlLChecked);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        buttonBackup.setOnClickListener(this);


        asyncTask.execute();


    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
        TextView label = (TextView) v.getTag(R.id.text);
        CheckBox checkbox = (CheckBox) v.getTag(R.id.checkbox);
        Toast.makeText(v.getContext(), label.getText().toString() + " " + isCheckedOrNot(checkbox), Toast.LENGTH_LONG).show();
    }

    private String isCheckedOrNot(CheckBox checkbox) {
        if (checkbox.isChecked())
            return "is checked";
        else
            return "is not checked";
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackup:

                showDialog();

                break;

        }
    }


    class TestAsync extends AsyncTask<Object, Object, ArrayList<Sms>> {
        String TAG = getClass().getSimpleName();
        public AsyncResponse delegate = null;


        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewSmsConvercation.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected ArrayList<Sms> doInBackground(Object... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            parseXML(CONSTANTS.SMS_FILE_NAME);
            SMSTable.Insert(smsArrayList);
            ArrayList<Sms> smsArrayList1 = SMSTable.getAppData();
            return smsArrayList1;
        }

        protected void onProgressUpdate(Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(final ArrayList<Sms> result) {
            if (result != null) {
                if (result.size() > 0) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG + " onPostExecute", "" + result);
                            if (result != null) {
                                if (result.size() > 0) {
                                    adapter.update(result);
                                } else {
                                    CM.showToast(ViewSmsConvercation.this, "NO DATA FOUND");
                                }
                            } else {
                                CM.showToast(ViewSmsConvercation.this, "NO DATA FOUND");
                            }
                            mProgressDialog.dismiss();

                        }
                    });

                }

            }
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(this);
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
                CM.showToast(ViewSmsConvercation.this, "FILE NOT FOUND");
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

    public void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewSmsConvercation.this);
        alert.setTitle(getString(R.string.app_name)); //Set Alert dialog title here
        alert.setMessage(getString(R.string.backuoMsg)); //Message here

        final TextView textView = new TextView(ViewSmsConvercation.this);
        textView.setText(smsFile.getAbsolutePath());
        textView.setPadding(40, 0, 0, 0);
        alert.setView(textView);

        LinearLayout linearLayout = new LinearLayout(ViewSmsConvercation.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);

        final EditText input = new EditText(ViewSmsConvercation.this);
        input.setText(CONSTANTS.SMS_FILE_NAME);
        linearLayout.addView(input);

        alert.setView(linearLayout);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //You will get as string input data in this variable.textView
                // here we convert the input to a string and show in a toast.

                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(ViewSmsConvercation.this, "Please Enter File Name with ext.", Toast.LENGTH_SHORT).show();
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
                                    //Do something after 100ms


                                    new TestAsyncs().execute(input.getText().toString());
                                    // new ViewSms.TestAsync().execute(input.getText().toString());

                                }
                            }, 1500);


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


    class TestAsyncs extends AsyncTask<Object, Object, String> {
        String TAG = getClass().getSimpleName();
        public AsyncResponse delegate = null;


        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewSmsConvercation.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected String doInBackground(Object... params) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");

            ArrayList<Sms> appDataWithChecked = SMSTable.getAppDataWithChecked();
            if (appDataWithChecked != null) {
                if (appDataWithChecked.size() > 0) {
                    createNewXml(appDataWithChecked, params[0].toString());
                }

            }

            return status;
        }

        protected void onProgressUpdate(Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {

            if (result != null) {
                if (result.equals("success")) {
                    CM.showToast(ViewSmsConvercation.this, "SMS CONVERSATION BACKUP DONE");
                } else {
                    CM.showToast(ViewSmsConvercation.this, "SMS CONVERSATION BACKUP NOT DONE");

                }
            }


            mProgressDialog.dismiss();
        }
    }

    private TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Typeface khandBold = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            titleTextView.setTypeface(khandBold);
          //  titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewSmsConvercation.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
