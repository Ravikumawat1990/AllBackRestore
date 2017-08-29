package backuprestore.udr.rk.allbackuprestore.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.Model.AppModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.adapter.BackupAppadapter;
import backuprestore.udr.rk.allbackuprestore.database.AppBackupTable;
import backuprestore.udr.rk.allbackuprestore.database.AppTable;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;

/**
 * Created by NetSupport on 25-01-2017.
 */

public class TabFragment2 extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    Activity thisActivity;
    private RecyclerView mRecyclerView;
    Button btnInstall, btnDelete;
    MtplTextView txtPath;
    public ArrayList<AppModel> appModels1;
    BackupAppadapter addadapter;
    private ImageView checkBoxAlLChecked;
    private ArrayList<AppModel> modelArrayList;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment_2, container, false);
        thisActivity = getActivity();
        setHasOptionsMenu(true);

        initView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public void initView(View rootView) {

        btnInstall = (Button) rootView.findViewById(R.id.btnInstall);
        btnDelete = (Button) rootView.findViewById(R.id.btnDelete);
        txtPath = (MtplTextView) rootView.findViewById(R.id.txtPath);
        txtPath.setText(CONSTANTS.createAppFolderForApp(thisActivity).getAbsolutePath());
        checkBoxAlLChecked = (ImageView) rootView.findViewById(R.id.allChecked);

        ArrayList<AppModel> dialog_item = new ArrayList<>();
        File directory4 = new File(CONSTANTS.createAppFolderForApp(thisActivity).getAbsolutePath());

        if (directory4.exists()) {
            File[] files = directory4.listFiles();
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                AppModel appModel = new AppModel();
                appModel.setAppName(files[i].getName());
                appModel.setSelected(false);
                appModel.setIsSelected("0");
                appModel.setAppIsBackup("1");
                appModel.setAppIsDelete("0");

                File isPathExist = new File(CONSTANTS.createAppFolderForApp(thisActivity).getAbsoluteFile(), files[i].getName());
                try {
                    PackageManager pm = thisActivity.getPackageManager();
                    PackageInfo packageInfo = pm.getPackageArchiveInfo(isPathExist.getAbsolutePath(), 0);
                    if (Build.VERSION.SDK_INT >= 8) {
                        // those two lines do the magic:
                        packageInfo.applicationInfo.sourceDir = isPathExist.getAbsolutePath();
                        packageInfo.applicationInfo.publicSourceDir = isPathExist.getAbsolutePath();

                    }
                    appModel.setAppPackage(packageInfo.packageName);
                } catch (Exception e) {
                    appModel.setAppPackage("");
                }
                dialog_item.add(appModel);
                AppBackupTable.Insert(dialog_item);
            }
        }

        AppBackupTable.setAllChecked("0");
        listView = (ListView) rootView.findViewById(R.id.listvView);
        modelArrayList = AppBackupTable.getAppData();
        if (modelArrayList != null) {

            addadapter = new BackupAppadapter(thisActivity, modelArrayList, checkBoxAlLChecked);
            listView.setAdapter(addadapter);


        } else {
            modelArrayList = new ArrayList<>();
            addadapter = new BackupAppadapter(thisActivity, modelArrayList, checkBoxAlLChecked);
            listView.setAdapter(addadapter);

        }

        btnInstall.setOnClickListener(this);
        btnDelete.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInstall:
                ArrayList<AppModel> appModel = AppBackupTable.getAppDataWithChecked();
                if (appModel != null) {
                    for (int i = 0; i < appModel.size(); i++) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(CONSTANTS.createAppFolderForApp(thisActivity), appModel.get(i).getAppName())), "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                }
                break;
            case R.id.btnDelete:
                ArrayList<AppModel> appModel1 = AppBackupTable.getAppDataWithChecked();
                if (appModel1 != null) {
                    for (int i = 0; i < appModel1.size(); i++) {
                        AppTable.updateKeyForBackup(appModel1.get(i).getAppPackage(), "0");
                        AppBackupTable.updateKeyForBackup(appModel1.get(i).getAppPackage(), "0");
                        AppBackupTable.updateKeyForDelete(appModel1.get(i).getAppPackage(), "1");


                    }
                }
                if (addadapter != null) {
                    modelArrayList.clear();

                }
                modelArrayList = AppBackupTable.getAppDataWithBackUp();
                if (modelArrayList != null) {


                    addadapter = new BackupAppadapter(thisActivity, modelArrayList, checkBoxAlLChecked);
                    listView.setAdapter(addadapter);

                } else {
                    if (modelArrayList == null) {

                        modelArrayList = new ArrayList<>();
                        addadapter = new BackupAppadapter(thisActivity, modelArrayList, checkBoxAlLChecked);
                        if (addadapter != null) {
                            listView.setAdapter(addadapter);
                        }
                    }
                }
                break;

        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu1, menu);

        final MenuItem item = menu.findItem(R.id.action_search1);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        addadapter.filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


}