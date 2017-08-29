package backuprestore.udr.rk.allbackuprestore.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import backuprestore.udr.rk.allbackuprestore.database.AppTable;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;
import backuprestore.udr.rk.allbackuprestore.util.CONSTANTS;

import static backuprestore.udr.rk.allbackuprestore.R.id.chkSelected;
import static backuprestore.udr.rk.allbackuprestore.R.id.imageView;
import static backuprestore.udr.rk.allbackuprestore.R.id.tvAppSize;
import static backuprestore.udr.rk.allbackuprestore.R.id.tvDateTime;
import static backuprestore.udr.rk.allbackuprestore.R.id.tvVerSenName;


/**
 * Created by NetSupport on 07-02-2017.
 */

public class InstalledAppadapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private LayoutInflater inflater;
    public List<AppModel> arraylistData = null;
    private ArrayList<AppModel> arraylist;
    ImageView checkBox;

    public InstalledAppadapter(Context context, List<AppModel> listbean, ImageView checkBox) {
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
                AppTable.setAllChecked("0");
                for (int i = 0; i < arraylistData.size(); i++) {
                    arraylistData.get(i).setSelected(false);
                }
            } else {
                checkBox.setSelected(true);
                AppTable.setAllChecked("1");
                for (int i = 0; i < arraylistData.size(); i++) {
                    arraylistData.get(i).setSelected(true);
                }
            }
            notifyDataSetChanged();
        } else {
            int position = (Integer) v.getTag();
            if (!v.isSelected()) {
                arraylistData.get(position).setSelected(true);
                String isChecked = AppTable.checkIsChecked(arraylistData.get(position).getAppPackage());
                if (isChecked.equals("0")) {
                    AppTable.updateKey(arraylistData.get(position).getAppPackage(), "1");
                } else if (isChecked.equals("1")) {
                    AppTable.updateKey(arraylistData.get(position).getAppPackage(), "0");
                } else {
                    AppTable.updateKey(arraylistData.get(position).getAppPackage(), "1");
                }

            } else {
                arraylistData.get(position).setSelected(false);
                AppTable.updateKey(arraylistData.get(position).getAppPackage(), "1");
                if (checkBox.isSelected()) {
                    checkBox.setSelected(false);
                    for (int i = 0; i < arraylistData.size(); i++) {
                        arraylistData.get(i).setSelected(true);
                        arraylistData.get(position).setSelected(false);
                    }
                }
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
            holder.imageView = (ImageView) view.findViewById(imageView);
            holder.tvVerSenName = (MtplTextView) view.findViewById(tvVerSenName);
            holder.tvDateTime = (MtplTextView) view.findViewById(tvDateTime);
            holder.tvAppSize = (MtplTextView) view.findViewById(tvAppSize);
            holder.chkSelected = (ImageView) view.findViewById(chkSelected);
            holder.rootLayout = (CardView) view.findViewById(R.id.rootLayout);
            holder.tvIsInstall = (MtplTextView) view.findViewById(R.id.tvIsInstall);
            checkBox.setOnClickListener(this);
            holder.chkSelected.setOnClickListener(this);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
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
            holder.tvName.setText(arraylistData.get(position).getAppName());
            holder.tvVerSenName.setText(arraylistData.get(position).getAppVerName());
            holder.tvDateTime.setText(arraylistData.get(position).getAppDateTime());
            holder.tvAppSize.setText(arraylistData.get(position).getAppSize());


            File isPathExist = new File(CONSTANTS.createAppFolderForApp(mContext).getAbsoluteFile(), arraylistData.get(position).getAppName() + ".apk");


            if (isPathExist.exists()) {
                Log.i("App Exist", "getView: Exist");
                holder.tvIsInstall.setText("Backup");
            } else {
                Log.i("App Exist", "getView: Not Exist");
                holder.tvIsInstall.setText("");
            }

            try {
                Drawable icon = mContext.getPackageManager().getApplicationIcon(arraylistData.get(position).getAppPackage().replace("\"", ""));
                holder.imageView.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {

                e.printStackTrace();
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
