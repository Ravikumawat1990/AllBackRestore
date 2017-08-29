package backuprestore.udr.rk.allbackuprestore.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import backuprestore.udr.rk.allbackuprestore.Model.AppModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.interfac.OnItemClickListener;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;

/**
 * Created by Elixir on 09-Aug-2016.
 */
public class addadapterinsatalled extends RecyclerView.Adapter<addadapterinsatalled.MyViewHolder> {


    private ArrayList<AppModel> dataSet;
    Context context;
    public OnItemClickListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MtplTextView tvName;
        public MtplTextView tvVerSenName, tvDateTime, tvAppSize;

        public ImageView chkSelected;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (MtplTextView) itemView.findViewById(R.id.tvName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            tvVerSenName = (MtplTextView) itemView.findViewById(R.id.tvVerSenName);
            tvDateTime = (MtplTextView) itemView.findViewById(R.id.tvDateTime);
            tvAppSize = (MtplTextView) itemView.findViewById(R.id.tvAppSize);
            chkSelected = (ImageView) itemView.findViewById(R.id.chkSelected);
            chkSelected.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {


            listener.onItemClick(dataSet.get(getPosition()).getAppPackage());

        }
    }

    public void update(ArrayList<AppModel> modelList) {
        dataSet.clear();
        for (AppModel model : modelList) {
            dataSet.add(model);
        }
        notifyDataSetChanged();
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {

        this.listener = mItemClickListener;
    }

    public addadapterinsatalled(ArrayList<AppModel> dataSet, Activity context) {
        this.dataSet = dataSet;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {

        holder.tvName.setText(dataSet.get(listPosition).getAppName());
        holder.tvVerSenName.setText(dataSet.get(listPosition).getAppVerName());
        holder.tvDateTime.setText(dataSet.get(listPosition).getAppDateTime());
        holder.tvAppSize.setText(dataSet.get(listPosition).getAppSize());

        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(dataSet.get(listPosition).getAppPackage());
            holder.imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }


        if (dataSet.get(listPosition).getIsSelected().equals("0")) {
            holder.chkSelected.setSelected(false);

        } else {
            holder.chkSelected.setSelected(true);

        }


    }

    public void setFilter(List<AppModel> countryModels) {
        //dataSet.clear();
        dataSet = new ArrayList<>();
        dataSet.addAll(countryModels);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}