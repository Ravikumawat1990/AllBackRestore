package backuprestore.udr.rk.allbackuprestore.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.Model.CollLogsModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.interfac.OnItemClickCallLogListener;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;

/**
 * Created by Elixir on 09-Aug-2016.
 */
public class callLogadapter extends RecyclerView.Adapter<callLogadapter.MyViewHolder> {


    private ArrayList<CollLogsModel> dataSet;
    Context context;
    public OnItemClickCallLogListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName, tvNumber;
        public TextView tvVerSenName, tvDateTime, tvAppSize, tvCallType;

        public CheckBox chkSelected;
        ImageView imageView;
        MtplTextView txtCount;
        CardView cardView;
        LinearLayout smsLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);


            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            tvVerSenName = (TextView) itemView.findViewById(R.id.tvVerSenName);

            tvVerSenName.setSelected(true);
            tvDateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
            tvAppSize = (TextView) itemView.findViewById(R.id.tvAppSize);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

            txtCount = (MtplTextView) itemView.findViewById(R.id.txtCount);

            smsLayout = (LinearLayout) itemView.findViewById(R.id.smsLayout);
            tvCallType = (MtplTextView) itemView.findViewById(R.id.tvCallType);


            smsLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(dataSet.get(getPosition()).getCached_name(), dataSet.get(getPosition()).getNumber());

        }
    }

    public void update(ArrayList<CollLogsModel> modelList) {
        dataSet.clear();
        for (CollLogsModel model : modelList) {
            dataSet.add(model);
        }
        notifyDataSetChanged();
    }

    public void SetOnItemClickListener(OnItemClickCallLogListener mItemClickListener) {

        this.listener = mItemClickListener;
    }

    public callLogadapter(ArrayList<CollLogsModel> dataSet, Activity context) {
        this.dataSet = dataSet;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calllog_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {

        if (dataSet.get(listPosition).getCached_name() != null) {

            if (!dataSet.get(listPosition).getCached_name().equals("")) {
                holder.tvName.setText(dataSet.get(listPosition).getCached_name());
            } else {
                holder.tvName.setText("Unknown");

            }
        } else {
            holder.tvName.setText("Unknown");

        }

        if (dataSet.get(listPosition).getNumber() != null) {
            holder.tvNumber.setText(" ( " + dataSet.get(listPosition).getNumber() + " ) ");
        } else {
            holder.tvNumber.setText("");

        }

        if (dataSet.get(listPosition).getDate() != null) {
            holder.tvVerSenName.setText(dataSet.get(listPosition).getDate());

        } else {
            holder.tvVerSenName.setText(dataSet.get(listPosition).getDate());

        }

        if (dataSet.get(listPosition).getDuration() != null) {
            holder.txtCount.setText(getDurationString(Integer.parseInt(dataSet.get(listPosition).getDuration())));

        } else {
            holder.txtCount.setText("");

        }
        if (dataSet.get(listPosition).getType() != null) {
            holder.tvCallType.setText(dataSet.get(listPosition).getType());

        } else {
            holder.tvCallType.setText("");
        }


    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}