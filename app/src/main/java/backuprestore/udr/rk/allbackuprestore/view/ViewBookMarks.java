package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import backuprestore.udr.rk.allbackuprestore.Model.BookMarkModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon;
import backuprestore.udr.rk.allbackuprestore.util.UploadFile;
import me.everything.providers.android.browser.Bookmark;
import me.everything.providers.android.browser.BrowserProvider;

import static backuprestore.udr.rk.allbackuprestore.util.CM.convertPixelsToDp;

public class ViewBookMarks extends AppCompatActivity implements View.OnClickListener {
    LinearLayout backupLayout, restoreLayout, viewBackupLayout, sendToCloudLayout, deleteBackup, deleteMrakLayout;
    private ProgressDialog mProgressDialog;
    private String status;
    private BrowserProvider browserProvider;
    private ArrayList<BookMarkModel> bookMarkModels;
    public Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");
    public final String[] HISTORY_PROJECTION = new String[]{
            "_id", // 0
            "url", // 1
            "visits", // 2
            "date", // 3
            "bookmark", // 4
            "title", // 5
            "favicon", // 6
            "thumbnail", // 7
            "touch_icon", // 8
            "user_entered"}; // 9;
    private File bookMarkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bookmark's");
        setSupportActionBar(toolbar);
        bookMarkFile = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewBookMarks.this);

            }
        });
        browserProvider = new BrowserProvider(ViewBookMarks.this);

        getActionBarTextView(toolbar);
        initView();

    }

    private void initView() {

        backupLayout = (LinearLayout) findViewById(R.id.backupLayout);
        restoreLayout = (LinearLayout) findViewById(R.id.restoreLayout);
        viewBackupLayout = (LinearLayout) findViewById(R.id.viewBackupLayout);
        sendToCloudLayout = (LinearLayout) findViewById(R.id.sendToCloudLayout);
        deleteBackup = (LinearLayout) findViewById(R.id.deleteBackup);
        deleteMrakLayout = (LinearLayout) findViewById(R.id.deleteMrakLayout);

        bookMarkModels = new ArrayList<>();
        backupLayout.setOnClickListener(this);
        restoreLayout.setOnClickListener(this);
        viewBackupLayout.setOnClickListener(this);
        sendToCloudLayout.setOnClickListener(this);
        deleteBackup.setOnClickListener(this);
        deleteMrakLayout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(ViewBookMarks.this);
    }

    public void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewBookMarks.this);
        alert.setTitle(getString(R.string.app_name)); //Set Alert dialog title here
        alert.setMessage(getString(R.string.backuoMsg)); //Message here
        final TextView textView = new TextView(ViewBookMarks.this);
        textView.setText(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath());
        textView.setPadding(40, 0, 0, 0);
        alert.setView(textView);

        LinearLayout linearLayout = new LinearLayout(ViewBookMarks.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);

        final EditText input = new EditText(ViewBookMarks.this);
        input.setText(CONSTANTS.BOOK_MARK_FILE_NAME);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertPixelsToDp(50, this), 0, convertPixelsToDp(50, this), 0);
        input.setLayoutParams(params);
        input.setSelection(input.getText().length());
        linearLayout.addView(input);

        alert.setView(linearLayout);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(ViewBookMarks.this, "Please Enter File Name with ext.", Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.restoreLayout:
                File directory = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath());
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

            case R.id.viewBackupLayout:

                File directory1 = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath());
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

            case R.id.sendToCloudLayout:


                File directory2 = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath());
                if (directory2.exists()) {
                    File[] files = directory2.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "4");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }


                break;

            case R.id.deleteBackup:

                File directory4 = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath());
                if (directory4.exists()) {
                    File[] files = directory4.listFiles();
                    if (files.length > 0) {
                        showListDialog(files, "8");

                    } else {
                        CM.showToast(this, "NO DATA FOUND");
                    }

                } else {
                    CM.showToast(this, "NO DIRECTORY FOUND");
                }

                break;

            case R.id.deleteMrakLayout:
                break;

        }
    }

    class TestAsync extends AsyncTask<String, Object, String> {
        String TAG = getClass().getSimpleName();
        String fileName = "";

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewBookMarks.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected String doInBackground(String... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");

            List<Bookmark> bookmarks = browserProvider.getBookmarks().getList();
            bookmarks.size();
            createNewXml(bookmarks, arg0[0]);


            return status;
        }

        protected void onProgressUpdate(final Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(String result) {

            if (result.equals("success")) {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewBookMarks.this, "BOOKMARK BACKUP DONE");
                if (CM.getSp(ViewBookMarks.this, "cloud", "").equals("1")) {
                    ShowDialog(fileName);
                }
            } else {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewBookMarks.this, "BOOKMARK BACKUP FAIL");
            }
            mProgressDialog.dismiss();

        }
    }


    public void createNewXml(List<Bookmark> lstSms, String fileName) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("http://crunchify.com/CrunchifyCreateXMLDOM", "bookmark");
            doc.appendChild(mainRootElement);

            for (int i = 0; i < lstSms.size(); i++) {
                String id, title, url, bookmark, date;
                if (lstSms.get(i).title != null) {
                    title = lstSms.get(i).title;
                } else {
                    title = "";

                }
                if (lstSms.get(i).url != null) {
                    url = lstSms.get(i).url;
                } else {
                    url = "";
                }
                if (String.valueOf(lstSms.get(i).bookmark) != null) {
                    bookmark = String.valueOf(lstSms.get(i).bookmark);
                } else {
                    bookmark = "";
                }
                if (String.valueOf(lstSms.get(i).date) != null) {
                    date = String.valueOf(lstSms.get(i).date);
                } else {
                    date = "";
                }


                mainRootElement.appendChild(getCompany(doc, title, url, bookmark, date));
            }
            // output DOM XML to console
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);

            if (!bookMarkFile.exists()) {
                bookMarkFile.mkdir();
            }
            File file = new File(bookMarkFile, fileName);
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


    private static Node getCompany(Document doc, String title, String url, String bookmark, String date) {

        Element company = doc.createElement("bookmark");
        company.appendChild(getCompanyElements(doc, company, "TITLE", title));
        company.appendChild(getCompanyElements(doc, company, "URL", url));
        company.appendChild(getCompanyElements(doc, company, "BOOKMARK", bookmark));
        company.appendChild(getCompanyElements(doc, company, "DATE", date));

        return company;
    }

    // utility method to create text node
    private static Node getCompanyElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    class TestAsyncbackup extends AsyncTask<String, Object, ArrayList<BookMarkModel>> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewBookMarks.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected ArrayList<BookMarkModel> doInBackground(String... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");


            parseXML(arg0[0]);

            return bookMarkModels;
        }

        protected void onProgressUpdate(final Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(ArrayList<BookMarkModel> result) {


            if (result != null) {
                if (result.size() > 0) {
                    Log.d(TAG + " onPostExecute", "" + result);

                    for (int i = 0; i < result.size(); i++) {

                        try {
                            ContentValues values = new ContentValues();
                            values.put(HISTORY_PROJECTION[5], result.get(i).getTitle());
                            values.put(HISTORY_PROJECTION[1], result.get(i).getUrl());
                            values.put(HISTORY_PROJECTION[4], result.get(i).getBookmark());
                            values.put(HISTORY_PROJECTION[3], result.get(i).getDate());
                            getContentResolver().insert(BOOKMARKS_URI, values);

                            //com.android.chrome

                            try {
                                PackageManager pm = getPackageManager();
                                boolean isInstalled = isPackageInstalled("com.android.chrome", pm);
                                if (isInstalled) {
                                    Uri chromeUri = Uri.parse("content://com.android.chrome.browser/bookmarks");
                                    getContentResolver().insert(chromeUri, values);
                                }
                            } catch (Exception e) {

                            }


                        } catch (Exception e) {
                            e.getMessage();
                            CM.showToast(ViewBookMarks.this, "BOOKMARK BACKUP FAIL");

                        }
                        CM.showToast(ViewBookMarks.this, "BOOKMARK BACKUP DONE");
                    }


                } else {

                }
            } else {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewBookMarks.this, "BOOKMARK BACKUP FAIL");
            }
            mProgressDialog.dismiss();

        }
    }

    private void parseXML(String fileName) {
        try {
            File file = new File(bookMarkFile, fileName);
            if (file.exists()) {
                InputStream is = new FileInputStream(file.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("bookmark");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        BookMarkModel collLogsModel = new BookMarkModel();
                        Element element2 = (Element) node;
                        if (getValue("TITLE", element2) != null) {
                            collLogsModel.setTitle(getValue("TITLE", element2));
                        } else {
                            collLogsModel.setTitle("");

                        }
                        if (getValue("URL", element2) != null) {
                            collLogsModel.setUrl(getValue("URL", element2));

                        } else {
                            collLogsModel.setUrl("");

                        }
                        if (getValue("BOOKMARK", element2) != null) {
                            collLogsModel.setBookmark(getValue("BOOKMARK", element2));

                        } else {
                            collLogsModel.setBookmark("");

                        }
                        if (getValue("DATE", element2) != null) {
                            collLogsModel.setDate(getValue("DATE", element2));

                        } else {
                            collLogsModel.setDate("");

                        }


                        bookMarkModels.add(collLogsModel);
                    }
                }
                bookMarkModels.size();
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

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void showPopup(Context context, final String item) {
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to delete file ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        File file = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath(), item);
                        if (file.exists()) {
                            if (file.exists()) {
                                file.delete();
                                CM.showToast(ViewBookMarks.this, "FILE DELETED");
                            }
                        } else {
                            CM.showToast(ViewBookMarks.this, "FILE NOT FOUND");
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
                DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewBookMarks.this);
                AndroidAuthSession session = mDBApi.getSession();
                if (session.authenticationSuccessful()) {
                    session.finishAuthentication();
                    TokenPair tokens = session.getAccessTokenPair();
                    CM.setSp(ViewBookMarks.this, "key", tokens.key.toString());
                    CM.setSp(ViewBookMarks.this, "secret", tokens.secret.toString());

                    File file = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath(), fileName);
                    if (file.exists()) {
                        UploadFile upload = new UploadFile(ViewBookMarks.this, mDBApi, CONSTANTS.DIRBOOKMARK, file);
                        upload.execute();
                    } else {
                        CM.showToast(ViewBookMarks.this, "FILE NOT FOUND");
                    }
                } else {
                    mDBApi.getSession().startAuthentication(ViewBookMarks.this);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewBookMarks.this);
        builder.setTitle("Select Files");

        //list of items
        final String[] items = dialog_item.toArray(new String[dialog_item.size()]);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        String name = items[which];
                        CM.showToast(ViewBookMarks.this, name);


                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if (code.equals("2")) {
                            new TestAsyncbackup().execute(items[selectedPosition]);
                        } else if (code.equals("3")) {

                            Intent intent = new Intent(ViewBookMarks.this, ViewBookMark.class);
                            intent.putExtra("fileName", items[selectedPosition]);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_in_from_left,
                                    R.anim.push_out_to_right);


                        } else if (code.equals("4")) {

                            if (CM.isInternetAvailable(ViewBookMarks.this)) {
                                DropBoxCommon dropBoxCommon = new DropBoxCommon();
                                DropboxAPI<AndroidAuthSession> mDBApi = dropBoxCommon.initView(ViewBookMarks.this);
                                AndroidAuthSession session = mDBApi.getSession();
                                if (session.authenticationSuccessful()) {
                                    session.finishAuthentication();
                                    TokenPair tokens = session.getAccessTokenPair();
                                    CM.setSp(ViewBookMarks.this, "key", tokens.key.toString());
                                    CM.setSp(ViewBookMarks.this, "secret", tokens.secret.toString());
                                    File file = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMarks.this).getAbsolutePath(), items[selectedPosition]);
                                    if (file.exists()) {
                                        UploadFile upload = new UploadFile(ViewBookMarks.this, mDBApi, CONSTANTS.DIRBOOKMARK, file);
                                        upload.execute();
                                    } else {
                                        CM.showToast(ViewBookMarks.this, "FILE NOT FOUND");
                                    }
                                } else {
                                    CM.showToast(ViewBookMarks.this, getString(R.string.sessionExp));
                                    mDBApi.getSession().startAuthentication(ViewBookMarks.this);

                                }
                            } else {
                                CM.showToast(ViewBookMarks.this, getString(R.string.msg_internet_unavailable_msg));
                            }

                        } else if (code.equals("8")) {
                            showPopup(ViewBookMarks.this, items[selectedPosition]);
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
            // titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewBookMarks.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
