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
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import backuprestore.udr.rk.allbackuprestore.Model.Sms;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.interfac.OnItemClickListener;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;

/**
 * Created by Elixir on 09-Aug-2016.
 */
public class readadapter extends RecyclerView.Adapter<readadapter.MyViewHolder> {


    private ArrayList<Sms> dataSet;
    Context context;
    public OnItemClickListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName;
        public TextView tvVerSenName, tvDateTime, tvAppSize;

        public CheckBox chkSelected;
        ImageView imageView;
        MtplTextView txtCount, txtDateTime;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            tvVerSenName = (TextView) itemView.findViewById(R.id.tvVerSenName);
            tvVerSenName.setSelected(true);
            tvDateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
            tvAppSize = (TextView) itemView.findViewById(R.id.tvAppSize);
            txtDateTime = (MtplTextView) itemView.findViewById(R.id.txtDateTime);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            txtCount = (MtplTextView) itemView.findViewById(R.id.txtCount);

        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(dataSet.get(getPosition()).getSmsAppId());

        }
    }

    public void update(ArrayList<Sms> modelList) {
        dataSet.clear();
        for (Sms model : modelList) {
            dataSet.add(model);
        }
        notifyDataSetChanged();
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {

        this.listener = mItemClickListener;
    }

    public readadapter(ArrayList<Sms> dataSet, Activity context) {
        this.dataSet = dataSet;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.readsms_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {

        holder.tvName.setText(dataSet.get(listPosition).getAddress());
        holder.tvVerSenName.setText(dataSet.get(listPosition).getMsg());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            holder.txtDateTime.setText(formatter.format(new Date(dataSet.get(listPosition).getTime())));

        } catch (Exception e) {

        }

        //  String dateString = formatter.format(new Date(dataSet.get(listPosition).getTime()));

//        holder.txtCount.setText(SMSTable.getCount(dataSet.get(listPosition).getAddress()));

        if (dataSet.get(listPosition).getAddress() != null && !dataSet.get(listPosition).getAddress().equals("")) {

        } else {
            holder.cardView.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.GONE);
            holder.tvVerSenName.setVisibility(View.GONE);
            holder.txtCount.setVisibility(View.GONE);
        }


//        if (dataSet.get(listPosition).getIsSelected().equals("0")) {
//            holder.chkSelected.setChecked(false);
//
//        } else {
//            holder.chkSelected.setChecked(true);
//
//        }


    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}