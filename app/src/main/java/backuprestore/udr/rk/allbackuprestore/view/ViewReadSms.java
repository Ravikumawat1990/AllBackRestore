package backuprestore.udr.rk.allbackuprestore.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.Model.Sms;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.adapter.readadapter;
import backuprestore.udr.rk.allbackuprestore.database.SMSTable;
import backuprestore.udr.rk.allbackuprestore.util.CM;

public class ViewReadSms extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Intent intent;
    private ArrayList<Sms> smsArrayList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_read_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        intent = getIntent();
        if (intent.getStringExtra("key") != null) {
            toolbar.setTitle(intent.getStringExtra("key"));
        }
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewReadSms.this);

            }
        });
        getActionBarTextView(toolbar);

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.readSmsRecycleView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewReadSms.this));
        if (intent.getStringExtra("key") != null) {
            smsArrayList1 = SMSTable.getAppDataById(intent.getStringExtra("key"));
        } else {

        }
        if (smsArrayList1 != null) {
            if (smsArrayList1.size() > 0) {
                readadapter readadapter = new readadapter(smsArrayList1, ViewReadSms.this);
                mRecyclerView.setAdapter(readadapter);

            }

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
           // titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewReadSms.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
