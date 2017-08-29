package backuprestore.udr.rk.allbackuprestore.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import backuprestore.udr.rk.allbackuprestore.Model.Sms;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.database.SMSTable;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;

/**
 * Created by NetSupport on 31-01-2017.
 */

public class MyAdapter extends ArrayAdapter<Sms> implements CompoundButton.OnCheckedChangeListener {

    private final List<Sms> list;
    private final Activity context;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;
    CheckBox check_all;

    public MyAdapter(Activity context, List<Sms> list, CheckBox check_all) {
        super(context, R.layout.sms_conv_row, list);
        this.context = context;
        this.list = list;
        this.check_all = check_all;
    }

    public void update(ArrayList<Sms> modelList) {
        this.list.clear();
        this.list.addAll(modelList);
        notifyDataSetChanged();


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == check_all) {
            check_all.setChecked(isChecked);
            SMSTable.setAllChecke("true");
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelected(isChecked);
            }
            notifyDataSetChanged();
        } else {
            int position = (Integer) buttonView.getTag();
            if (isChecked) {
                list.get(position).setSelected(true);
                String isCheck = SMSTable.checkIsChecked(list.get(position).getId());
                if (isCheck.equals("false")) {
                    SMSTable.updateKey(list.get(position).getId(), "true");
                } else if (isCheck.equals("true")) {
                    SMSTable.updateKey(list.get(position).getId(), "false");
                } else {
                    SMSTable.updateKey(list.get(position).getId(), "true");
                }
            } else {
                list.get(position).setSelected(false);
                SMSTable.updateKey(list.get(position).getId(), "false");
                if (check_all.isChecked()) {
                    check_all.setChecked(false);
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelected(true);
                        list.get(position).setSelected(false);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
        protected MtplTextView txtCount, tvVerSenName;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.sms_conv_row, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.text.setSelected(true);
            viewHolder.tvVerSenName = (MtplTextView) convertView.findViewById(R.id.tvVerSenName);
            viewHolder.tvVerSenName.setSelected(true);
            viewHolder.txtCount = (MtplTextView) convertView.findViewById(R.id.txtCount);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            check_all.setOnCheckedChangeListener(this);
            viewHolder.checkbox.setOnCheckedChangeListener(this);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tvName, viewHolder.text);
            convertView.setTag(R.id.checkbox, viewHolder.checkbox);
            convertView.setTag(R.id.txtCount, viewHolder.txtCount);
            convertView.setTag(R.id.checkbox, viewHolder.checkbox);
            convertView.setTag(R.id.tvVerSenName, viewHolder.tvVerSenName);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkbox.setTag(position); // This line is important.
        viewHolder.txtCount.setText(SMSTable.getCount(list.get(position).getAddress()));
        viewHolder.text.setText(list.get(position).getAddress());
        viewHolder.tvVerSenName.setText(list.get(position).getTime());

        //
        //Boolean.parseBoolean(list.get(position).getIsSelected())
        viewHolder.checkbox.setChecked(list.get(position).isSelected());

        return convertView;
    }
}

