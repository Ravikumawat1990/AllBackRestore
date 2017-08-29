package backuprestore.udr.rk.allbackuprestore.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
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

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.TokenPair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import backuprestore.udr.rk.allbackuprestore.Model.AppModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.adapter.InstalledAppadapter;
import backuprestore.udr.rk.allbackuprestore.database.AppTable;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;
import backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon;
import backuprestore.udr.rk.allbackuprestore.util.UploadMultiFile;

import static backuprestore.udr.rk.allbackuprestore.util.DropBoxCommon.mDBApi;

/**
 * Created by NetSupport on 25-01-2017.
 */

public class TabFragment1 extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    Activity thisActivity;
    private RecyclerView mRecyclerView;
    ArrayList<AppModel> appModels = new ArrayList<>();
    Button button;
    public ArrayList<AppModel> appModels1;
    InstalledAppadapter addadapter;
    private ImageView checkBoxAlLChecked;
    private ProgressDialog progressDialog;
    private boolean onResume = false;
    private ArrayList<File> fileArrayList;
    private ArrayList<String> packageArrayList;
    private ProgressDialog mProgressDialog;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_1, container, false);
        thisActivity = getActivity();
        setHasOptionsMenu(true);
        initView(rootView);
        return rootView;
    }


    public void initView(View rootView) {

        button = (Button) rootView.findViewById(R.id.btnBackup);
        checkBoxAlLChecked = (ImageView) rootView.findViewById(R.id.allChecked);
        listView = (ListView) rootView.findViewById(R.id.listvView);


        new TestAsync().execute();


        button.setOnClickListener(this);

    }


    public Set<PackageInfo> getInstalledApps(Context ctx) {
        final PackageManager packageManager = ctx.getPackageManager();
        if (appModels != null) {
            appModels.clear();
        }
        final List<PackageInfo> allInstalledPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        final Set<PackageInfo> filteredPackages = new HashSet();

        Drawable defaultActivityIcon = packageManager.getDefaultActivityIcon();

        for (PackageInfo each : allInstalledPackages) {
            if (ctx.getPackageName().equals(each.packageName)) {
                continue;  // skip own app
            }

            try {

                Intent intentOfStartActivity = packageManager.getLaunchIntentForPackage(each.packageName);
                if (intentOfStartActivity == null)
                    continue;
                Drawable applicationIcon = packageManager.getActivityIcon(intentOfStartActivity);
                if (applicationIcon != null && !defaultActivityIcon.equals(applicationIcon)) {
                    AppModel appModel = new AppModel();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String installTime = dateFormat.format(new Date(each.firstInstallTime));
                    appModel.setAppDateTime(installTime);
                    appModel.setIsSelected("0");
                    File apkFile = new File(each.applicationInfo.publicSourceDir);
                    if (apkFile.exists()) {
                        appModel.setAppPath(apkFile.getAbsolutePath());
                    }

                    if (each.applicationInfo.loadIcon(ctx.getPackageManager()) != null) {

                /*        appModel.setAppImage(each.applicationInfo.loadIcon(ctx.getPackageManager()).toString());
                        Drawable icon = each.applicationInfo.loadIcon(ctx.getPackageManager());
                        appModel.setAppIcon(icon);
                        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
                        appModel.setAppImage(BitMapToString(bitmap));*/

                    } else {

                    }
                    appModel.setAppVerName(each.versionName);

                    File file = new File(each.applicationInfo.publicSourceDir);
                    long size = file.length();
                    appModel.setAppSize(formatFileSize(size));
                    if (each.applicationInfo.loadLabel(packageManager) != null) {
                        appModel.setAppName(each.applicationInfo.loadLabel(packageManager).toString());
                    } else {
                        appModel.setAppName("");
                    }
                    if (each.packageName != null) {
                        appModel.setAppPackage(each.packageName);
                    } else {
                        appModel.setAppPackage("");
                    }

                    appModels.add(appModel);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.i("MyTag", "Unknown package name " + each.packageName);
            }
        }
        appModels.size();
        return filteredPackages;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackup:

                if (CM.isInternetAvailable(thisActivity)) {
                    new DownloadFileAsync().execute();
                } else {
                    CM.showToast(thisActivity, getString(R.string.msg_internet_unavailable_msg));
                }
                break;
        }
    }

    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.URL_SAFE);
        return temp;
    }


    class DownloadFileAsync extends AsyncTask<String, String, String> {

        File file = null;
        ArrayList<AppModel> appModel;
        int countItem = 0;
        String status = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(thisActivity);
            progressDialog.setTitle("In progress...");
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count = 0;
            int read = 0;

            try {
                appModel = AppTable.getAppDataWithChecked();

                if (appModel != null) {


                    InputStream in = null;
                    OutputStream out = null;
                    fileArrayList = new ArrayList<>();
                    packageArrayList = new ArrayList<>();

                    for (int i = 0; i < appModel.size(); i++) {
                        try {
                            File files = new File(appModel.get(i).getAppPath());
                            in = new FileInputStream(appModel.get(i).getAppPath());
                            File file = null;

                            try {
                                file = new File(CONSTANTS.createAppFolderForApp(thisActivity).getAbsolutePath());

                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                String fileName = appModel.get(i).getAppName() + ".apk";
                                file = new File(file, fileName);
                                if (file.exists()) {
                                    file.delete();
                                }
                                file.createNewFile();
                                out = new FileOutputStream(file);
                                byte[] buffer = new byte[1024];
                                long total = 0;
                                while ((read = in.read(buffer)) != -1) {
                                    total += read;
                                    out.write(buffer, 0, read);
                                }
                                countItem++;
                                in.close();
                                in = null;
                                out.flush();
                                out.close();
                                out = null;
                                fileArrayList.add(file);
                                packageArrayList.add(appModel.get(i).getAppPackage());

                                status = "success";
                            } catch (Exception ex) {
                                System.out.println("ex: " + ex);
                                status = ex.getMessage();
                                //AppTable.updateKeyForBackup(appModel.get(i).getAppPackage(), "0");
                            }

                        } catch (Exception e) {
                            //  AppTable.updateKeyForBackup(appModel.get(i).getAppPackage(), "0");
                            e.getMessage();
                            status = e.getMessage();
                        }
                    }
                } else {
                    thisActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CM.showToast(thisActivity, "NO DATA FOUND");
                        }
                    });
                }
            } catch (Exception e) {
                //AppTable.updateKeyForBackup(appModel.get(i).getAppPackage(), "0");
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);

            progressDialog.setProgress(Integer.parseInt(progress[0]));
            //"Loading " + (countItem) + "/" + appModel.size())
            progressDialog.setMessage("Please wait...");
        }

        @Override
        protected void onPostExecute(String unused) {
            progressDialog.dismiss();

            if (status != null && status.equals("success")) {
                DropBoxCommon dropBoxCommon = new DropBoxCommon();
                mDBApi = dropBoxCommon.initView(thisActivity);
                AndroidAuthSession session = mDBApi.getSession();
                if (session.authenticationSuccessful()) {
                    session.finishAuthentication();

                    TokenPair tokens = session.getAccessTokenPair();
                    CM.setSp(thisActivity, "key", tokens.key.toString());
                    CM.setSp(thisActivity, "secret", tokens.secret.toString());


                    File files[] = fileArrayList.toArray(new File[0]);
                    UploadMultiFile upload = new UploadMultiFile(2, thisActivity, mDBApi, CONSTANTS.DIRAPPS, files, packageArrayList);
                    upload.execute();


                } else {
                    mDBApi.getSession().startAuthentication(thisActivity);
                    onResume = true;
                }
            } else {
                CM.showToast(thisActivity, status);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
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

    @Override
    public void onResume() {
        super.onResume();

        if (onResume) {
            DropBoxCommon dropBoxCommon = new DropBoxCommon();
            DropboxAPI<AndroidAuthSession> api = dropBoxCommon.getDropboxAPI(thisActivity);
            AndroidAuthSession session = api.getSession();
            if (session.authenticationSuccessful()) {
                session.finishAuthentication();
                TokenPair tokens = session.getAccessTokenPair();

                CM.setSp(thisActivity, "key", tokens.key.toString());
                CM.setSp(thisActivity, "secret", tokens.secret.toString());


                File files[] = fileArrayList.toArray(new File[0]);
                UploadMultiFile upload = new UploadMultiFile(2, thisActivity, mDBApi, CONSTANTS.DIRAPPS, files, packageArrayList);
                upload.execute();
                onResume = false;
            }
        }
    }


    class TestAsync extends AsyncTask<Object, Object, ArrayList<AppModel>> {
        String TAG = getClass().getSimpleName();
        String fileName = "";

        protected void onPreExecute() {
            Log.d(TAG + " PreExceute", "On pre Exceute......");
            mProgressDialog = ProgressDialog.show(thisActivity, "", "Please wait...", true, false);
        }

        protected ArrayList<AppModel> doInBackground(Object... params) {

            getInstalledApps(thisActivity);
            AppTable.Insert(appModels);
            appModels1 = AppTable.getAppData();
            return appModels1;
        }

        protected void onProgressUpdate(Object... a) {
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(ArrayList<AppModel> result) {

            if (result != null) {
                if (result.size() > 0) {
                    addadapter = new InstalledAppadapter(thisActivity, result, checkBoxAlLChecked);
                    listView.setAdapter(addadapter);
                } else {
                    CM.showToast(thisActivity, getString(R.string.notDat));
                }
            } else {
                CM.showToast(thisActivity, getString(R.string.notDat));
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }

        }
    }
}
