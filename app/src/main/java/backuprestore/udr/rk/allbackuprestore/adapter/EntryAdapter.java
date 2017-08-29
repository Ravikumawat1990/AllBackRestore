package backuprestore.udr.rk.allbackuprestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import backuprestore.udr.rk.allbackuprestore.Model.EntryItem;
import backuprestore.udr.rk.allbackuprestore.Model.SectionItem;
import backuprestore.udr.rk.allbackuprestore.R;
import backuprestore.udr.rk.allbackuprestore.interfac.Item;


public class EntryAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater vi;

    public EntryAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if (i.isSection()) {
                SectionItem si = (SectionItem) i;
                v = vi.inflate(R.layout.list_item_section, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                TextView sectionView = (TextView) v.findViewById(R.id.textViewHeader);
                sectionView.setText(si.name);

            } else {
                EntryItem ei = (EntryItem) i;
                v = vi.inflate(R.layout.list_item_entry, null);
                TextView textView = (TextView) v.findViewById(R.id.txt_expand_contact);
                TextView textViewemail = (TextView) v.findViewById(R.id.expandable_email_text);

                //     ExpandableTextView expTvContact = (ExpandableTextView) v.findViewById(R.id.txt_expand_contact).findViewById(R.id.expand_contact);
                //    ExpandableTextView expTvEamil = (ExpandableTextView) v.findViewById(R.id.expandable_email_text).findViewById(R.id.expand_email);
                if (ei != null)
                    textView.setText(ei.type + "" + ei.contact);


            }
        }
        return v;
    }

}
