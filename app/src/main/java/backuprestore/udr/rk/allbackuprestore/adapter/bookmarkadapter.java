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

import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.Model.BookMarkModel;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.interfac.OnItemClickCallLogListener;
import backuprestore.udr.rk.allbackuprestore.mtplview.MtplTextView;

/**
 * Created by Elixir on 09-Aug-2016.
 */
public class bookmarkadapter extends RecyclerView.Adapter<bookmarkadapter.MyViewHolder> {


    private ArrayList<BookMarkModel> dataSet;
    Context context;
    public OnItemClickCallLogListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvtitle, tvVerUrl;
        public TextView tvVerSenName, tvDateTime, tvAppSize, tvCallType;

        public CheckBox chkSelected;
        ImageView imageView;
        MtplTextView txtCount;
        CardView cardView;
        CardView mainLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvtitle = (TextView) itemView.findViewById(R.id.tvtitle);
            tvVerUrl = (TextView) itemView.findViewById(R.id.tvVerUrl);
            mainLayout = (CardView) itemView.findViewById(R.id.cardView);
            mainLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            //  listener.onItemClick(dataSet.get(getPosition()).getCached_name(), dataSet.get(getPosition()).getNumber());

        }
    }

    public void update(ArrayList<BookMarkModel> modelList) {
        dataSet.clear();
        for (BookMarkModel model : modelList) {
            dataSet.add(model);
        }
        notifyDataSetChanged();
    }

    public void SetOnItemClickListener(OnItemClickCallLogListener mItemClickListener) {

        this.listener = mItemClickListener;
    }

    public bookmarkadapter(ArrayList<BookMarkModel> dataSet, Activity context) {
        this.dataSet = dataSet;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarkadapter, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {

        if (dataSet.get(listPosition).getTitle() != null) {
            if (!dataSet.get(listPosition).getTitle().equals("")) {
                holder.tvtitle.setText(dataSet.get(listPosition).getTitle());
            } else {
                holder.tvtitle.setText("");

            }
        } else {
            holder.tvtitle.setText("");
        }
        if (dataSet.get(listPosition).getUrl() != null) {
            holder.tvVerUrl.setText(dataSet.get(listPosition).getUrl());
        } else {
            holder.tvVerUrl.setText("");

        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}