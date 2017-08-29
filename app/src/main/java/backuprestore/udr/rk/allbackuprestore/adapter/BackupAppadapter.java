package backuprestore.udr.rk.allbackuprestore.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import backuprestore.udr.rk.allbackuprestore.Model.AppModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.database.AppBackupTable;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;


/**
 * Created by NetSupport on 07-02-2017.
 */

public class BackupAppadapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater inflater;
    public List<AppModel> arraylistData = null;
    private ArrayList<AppModel> arraylist;
    ImageView checkBox;

    public BackupAppadapter(Context context, List<AppModel> listbean, ImageView checkBox) {
        mContext = context;
        this.arraylistData = listbean;
        inflater = LayoutInflater.from(mContext);
        this.checkBox = checkBox;
        this.arraylist = new ArrayList<AppModel>();
        this.arraylist.addAll(listbean);

    }


    @Override
    public void onClick(View v) {

        if (v == checkBox) {
            if (checkBox.isSelected()) {
                checkBox.setSelected(false);
                AppBackupTable.setAllChecked("0");
                for (int i = 0; i < arraylistData.size(); i++) {
                    arraylistData.get(i).setSelected(false);
                }
            } else {
                checkBox.setSelected(true);
                AppBackupTable.setAllChecked("1");
                for (int i = 0; i < arraylistData.size(); i++) {
                    arraylistData.get(i).setSelected(true);
                }
            }
            notifyDataSetChanged();
        } else {

            int position = (Integer) v.getTag();
            File isPathExist = new File(CONSTANTS.createAppFolderForApp(mContext).getAbsoluteFile(), arraylistData.get(position).getAppName());

            try {
                PackageManager pm = mContext.getPackageManager();
                PackageInfo packageInfo = pm.getPackageArchiveInfo(isPathExist.getAbsolutePath(), 0);
                if (Build.VERSION.SDK_INT >= 8) {
                    // those two lines do the magic:
                    try {
                        packageInfo.applicationInfo.sourceDir = isPathExist.getAbsolutePath();

                    } catch (Exception e) {
                        e.getMessage();
                    }

                    try {
                        packageInfo.applicationInfo.publicSourceDir = isPathExist.getAbsolutePath();

                    } catch (Exception e) {
                        e.getMessage();

                    }


                }
                String pack = "";
                try {
                    pack = "\"" + packageInfo.packageName + "\"";

                } catch (Exception e) {

                }
                if (!v.isSelected()) {
                    arraylistData.get(position).setSelected(true);
                    String isChecked = AppBackupTable.checkIsChecked(pack);
                    if (isChecked.equals("0")) {
                        AppBackupTable.updateKey(pack, "1");
                    } else if (isChecked.equals("1")) {
                        AppBackupTable.updateKey(pack, "0");
                    } else {
                        AppBackupTable.updateKey(pack, "1");
                    }

                } else {
                    arraylistData.get(position).setSelected(false);
                    AppBackupTable.updateKey(pack, "1");
                    if (checkBox.isSelected()) {
                        checkBox.setSelected(false);
                        for (int i = 0; i < arraylistData.size(); i++) {
                            arraylistData.get(i).setSelected(true);
                            arraylistData.get(position).setSelected(false);
                        }
                    }
                }
            } catch (Exception e) {
                e.getMessage();

            }
            notifyDataSetChanged();
        }

    }

    public class ViewHolder {
        public MtplTextView tvName;
        public MtplTextView tvVerSenName, tvDateTime, tvAppSize, tvIsInstall;
        public ImageView chkSelected;
        ImageView imageView;
        CardView rootLayout;
    }

    @Override
    public int getCount() {
        return arraylistData.size();
    }

    @Override
    public AppModel getItem(int position) {
        return arraylistData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.cardview_row, null);
            holder.tvName = (MtplTextView) view.findViewById(R.id.tvName);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.tvVerSenName = (MtplTextView) view.findViewById(R.id.tvVerSenName);
            holder.tvDateTime = (MtplTextView) view.findViewById(R.id.tvDateTime);
            holder.tvAppSize = (MtplTextView) view.findViewById(R.id.tvAppSize);
            holder.chkSelected = (ImageView) view.findViewById(R.id.chkSelected);
            holder.rootLayout = (CardView) view.findViewById(R.id.rootLayout);
            holder.tvIsInstall = (MtplTextView) view.findViewById(R.id.tvIsInstall);

            checkBox.setOnClickListener(this);
            holder.chkSelected.setOnClickListener(this);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        File isPathExist = null;
        if (arraylistData.get(position).getAppName() != null) {
            isPathExist = new File(CONSTANTS.createAppFolderForApp(mContext).getAbsoluteFile(), arraylistData.get(position).getAppName());

        }


        // Set data to view=================================================
        try {
            AppModel bean = arraylistData.get(position);

            if (bean.isSelected()) {
                holder.chkSelected.setSelected(true);
            } else {
                holder.chkSelected.setSelected(false);
            }
            holder.chkSelected.setTag(position);

            if (arraylistData.get(position).getAppName() != null) {
                holder.tvName.setText(arraylistData.get(position).getAppName());

            }
            if (arraylistData.get(position).getAppVerName() != null) {
                holder.tvVerSenName.setText(arraylistData.get(position).getAppVerName());
            }

            if (arraylistData.get(position).getAppDateTime() != null) {
                holder.tvDateTime.setText(arraylistData.get(position).getAppDateTime());

            }

            if (arraylistData.get(position).getAppSize() != null) {
                holder.tvAppSize.setText(arraylistData.get(position).getAppSize());

            }


            try {
                PackageManager pm = mContext.getPackageManager();
                PackageInfo packageInfo = pm.getPackageArchiveInfo(isPathExist.getAbsolutePath(), 0);

                if (packageInfo != null) {


                    if (Build.VERSION.SDK_INT >= 8) {
                        // those two lines do the magic:

                        try {
                            packageInfo.applicationInfo.sourceDir = isPathExist.getAbsolutePath();

                        } catch (Exception e) {

                        }

                        try {
                            packageInfo.applicationInfo.publicSourceDir = isPathExist.getAbsolutePath();

                        } catch (Exception e) {

                        }


                    }
                    if (packageInfo.packageName != null) {
                        if (CM.isPackageInstalled(mContext, packageInfo.packageName)) {
                            holder.tvIsInstall.setText("Installed");
                        } else {
                            holder.tvIsInstall.setText("");
                        }
                        CharSequence label = pm.getApplicationLabel(packageInfo.applicationInfo);
                        Drawable icon = mContext.getPackageManager().getApplicationIcon(packageInfo.applicationInfo);
                        holder.imageView.setImageDrawable(icon);
                    }
                }
            } catch (Exception e) {

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        arraylistData.clear();

        if (arraylist != null) {
            if (charText.length() == 0) {
                arraylistData.addAll(arraylist);
            } else {
                for (AppModel contact : arraylist) {
                    if (contact.getAppName().toLowerCase().contains(charText)) {
                        arraylistData.add(contact);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }


//    public String getAppLabel(PackageManager pm, String pathToApk) {
//
//
//        PackageInfo packageInfo = pm.getPackageArchiveInfo(file.getAbsolutePath(), 0);
//
//        if (Build.VERSION.SDK_INT >= 8) {
//            // those two lines do the magic:
//            packageInfo.applicationInfo.sourceDir = pathToApk;
//            packageInfo.applicationInfo.publicSourceDir = pathToApk;
//
//        }
//
//        CharSequence label = pm.getApplicationLabel(packageInfo.applicationInfo);
//        Drawable icon = thisActivity.getPackageManager().getApplicationIcon(packageInfo.applicationInfo);
//        imageViewCatone.setImageDrawable(icon);
//
//        return label != null ? label.toString() : null;
//    }
}
