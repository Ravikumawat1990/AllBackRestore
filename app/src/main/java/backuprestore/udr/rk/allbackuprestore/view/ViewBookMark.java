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

import backuprestore.udr.rk.allbackuprestore.Model.BookMarkModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.adapter.bookmarkadapter;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;

public class ViewBookMark extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private ArrayList<BookMarkModel> bookMarkModels;
    private RecyclerView mRecyclerView;
    bookmarkadapter bookmarkadapter;
    private File bookMarkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_mark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bookmark's");
        setSupportActionBar(toolbar);
        bookMarkFile = new File(CONSTANTS.createAppFolderForBookMark(ViewBookMark.this).getAbsolutePath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewBookMark.this);

            }
        });


        getActionBarTextView(toolbar);
        bookMarkModels = new ArrayList<>();
        initView();
    }

    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();


        if (intent != null && intent.getStringExtra("fileName") != null) {
            new TestAsync().execute(intent.getStringExtra("fileName"));
        }


    }

    class TestAsync extends AsyncTask<String, Object, ArrayList<BookMarkModel>> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");

            mProgressDialog = new ProgressDialog(ViewBookMark.this);
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
                    bookmarkadapter = new bookmarkadapter(bookMarkModels, ViewBookMark.this);
                    mRecyclerView.setAdapter(bookmarkadapter);
                } else {

                }
            } else {
                Log.d(TAG + " onPostExecute", "" + result);
                CM.showToast(ViewBookMark.this, "BOOKMARK BACKUP FAIL");
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

    private TextView getActionBarTextView(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Typeface khandBold = Typeface.createFromAsset(getAssets(), "DroidSerif-Regular.ttf");

            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
            titleTextView.setTypeface(khandBold);
            //titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewBookMark.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
