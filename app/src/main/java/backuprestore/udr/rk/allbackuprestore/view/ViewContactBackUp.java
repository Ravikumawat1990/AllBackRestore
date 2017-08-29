package backuprestore.udr.rk.allbackuprestore.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import backuprestore.udr.rk.allbackuprestore.Model.EntryItem;
import backuprestore.udr.rk.allbackuprestore.Model.SectionItem;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.adapter.EntryAdapter;
import backuprestore.udr.rk.allbackuprestore.interfac.Item;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Telephone;

public class ViewContactBackUp extends AppCompatActivity {
    private String vfile;
    private ProgressDialog mProgressDialog;
    String status;
    private ArrayList<Item> items;
    private ListView listView;
    private File contactFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact");
        setSupportActionBar(toolbar);
        //      vfile = "Contacts.vcf";
        contactFile = new File(CONSTANTS.createAppFolderForContact(ViewContactBackUp.this) + File.separator);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewContactBackUp.this);

            }
        });

        getActionBarTextView(toolbar);
        items = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("fileName") != null) {
            new TestAsyncs().execute(intent.getStringExtra("fileName"));
        }


    }

    public void readVCF(String fileName) {
        try {
            File file = new File(contactFile, fileName);
            if (file.exists()) {
                List<VCard> vcards = Ezvcard.parse(file).all();
                for (VCard vcard : vcards) {
                    try {
                        System.out.println("Name: " + vcard.getFormattedName().getValue());
                        System.out.println("Telephone numbers:");
                        if (vcard.getFormattedName() != null) {
                            items.add(new SectionItem(vcard.getFormattedName().getValue()));
                        } else {

                        }

                    } catch (Exception e) {
                        e.getMessage();

                    }
                    try {
                        for (Telephone tel : vcard.getTelephoneNumbers()) {
                            System.out.println(tel.getTypes() + ": " + tel.getText());
                            if (tel.getTypes() != null && tel.getText() != null) {
                                items.add(new EntryItem(tel.getText(), tel.getTypes().toString()));
                            }

                        }
                    } catch (Exception e) {
                        e.getMessage();

                    }

                    status = "success";
                }
                items.size();
            } else {
                status = "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = "fail";
        }
    }


    class TestAsyncs extends AsyncTask<String, Object, ArrayList<Item>> {
        String TAG = getClass().getSimpleName();


        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");
            mProgressDialog = new ProgressDialog(ViewContactBackUp.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        protected ArrayList<Item> doInBackground(String... params) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            readVCF(params[0]);
            return items;
        }

        protected void onProgressUpdate(Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(ArrayList<Item> result) {

            if (result != null) {
                if (result.size() > 0) {
                    EntryAdapter entryAdapter = new EntryAdapter(ViewContactBackUp.this, result);
                    listView.setAdapter(entryAdapter);
                    CM.showToast(ViewContactBackUp.this, "CONTACT BACKUP DONE");
                } else {
                    CM.showToast(ViewContactBackUp.this, "NO DATA FOUND");
                }
            } else {
                CM.showToast(ViewContactBackUp.this, "NO DATA FOUND");
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
         //   titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewContactBackUp.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
