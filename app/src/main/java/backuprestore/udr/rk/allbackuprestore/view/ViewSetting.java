package backuprestore.udr.rk.allbackuprestore.view;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.SimpleFileDialog;


public class ViewSetting extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "ViewSetting";
    //  FileChooserDialog dialog;
    private LinearLayout selectPathLayout;
    private MtplTextView mtplPath;
    String m_chosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SETTINGS");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CM.finishActivity(ViewSetting.this);

            }
        });

        getActionBarTextView(toolbar);

        initView();

    }

    private void initView() {

        selectPathLayout = (LinearLayout) findViewById(R.id.selectPathLayout);
        mtplPath = (MtplTextView) findViewById(R.id.txtSelectpath);
        CheckBox checkboxNoti = (CheckBox) findViewById(R.id.checkboxNoti);
        CheckBox checkboxDialogShow = (CheckBox) findViewById(R.id.checkboxCloud);
        checkboxNoti.setOnCheckedChangeListener(this);
        checkboxDialogShow.setOnCheckedChangeListener(this);

        if (!CM.getSp(ViewSetting.this, "path", "").toString().equals("")) {
            mtplPath.setText(CM.getSp(ViewSetting.this, "path", "").toString());

        } else {
            mtplPath.setText(CONSTANTS.createAppFolderForApp(ViewSetting.this).getAbsolutePath());


        }

        if (CM.getSp(ViewSetting.this, "noti", "").equals("1")) {
            checkboxNoti.setChecked(true);
        } else {
            checkboxNoti.setChecked(false);
        }
        if (CM.getSp(ViewSetting.this, "cloud", "").equals("1")) {
            checkboxDialogShow.setChecked(true);
        } else {
            checkboxDialogShow.setChecked(false);
        }


        selectPathLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CM.finishActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectPathLayout:

                SimpleFileDialog FileOpenDialog = new SimpleFileDialog(ViewSetting.this, "FolderChoose",
                        new SimpleFileDialog.SimpleFileDialogListener() {
                            @Override
                            public void onChosenDir(String chosenDir) {
                                m_chosen = chosenDir;
                                mtplPath.setText(m_chosen);
                                CM.setSp(ViewSetting.this, "path", m_chosen);
                            }
                        });

                FileOpenDialog.chooseFile_or_Dir();


                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.checkboxCloud:
                if (isChecked) {
                    CM.setSp(ViewSetting.this, "cloud", "1");
                } else {
                    CM.setSp(ViewSetting.this, "cloud", "0");

                }
                break;
            case R.id.checkboxNoti:
                if (isChecked) {

                    CM.setSp(ViewSetting.this, "noti", "1");
                } else {
                    CM.setSp(ViewSetting.this, "noti", "0");
                }
                break;

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
//            titleTextView.setTextSize(CM.convertPixelsToDp(30, ViewSetting.this));

        } catch (NoSuchFieldException e) {
            e.getMessage();
        } catch (IllegalAccessException e) {
            e.getMessage();
        }
        return titleTextView;
    }
}
