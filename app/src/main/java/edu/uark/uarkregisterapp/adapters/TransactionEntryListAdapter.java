package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import edu.uark.uarkregisterapp.CreateTransactionActivity;
import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;

public class TransactionEntryListAdapter extends BaseAdapter implements ListAdapter{
    private ArrayList<TransactionEntry> listEntries = new ArrayList<TransactionEntry>();
    private Context context;
    private final CreateTransactionActivity cta;

    @Override
    public int getCount() {
        return listEntries.size();
    }

    @Override
    public Object getItem(int i) {
        return listEntries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listEntries.get(i).getEntryId().getMostSignificantBits() & Long.MAX_VALUE;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_item_transaction_entry, null);
        }

        TextView lookupCodeTextView = (TextView) view.findViewById(R.id.text_view_create_transaction_product_lookup_code);
        lookupCodeTextView.setText(listEntries.get(position).getLookupCode());

        TextView priceTextView = (TextView) view.findViewById(R.id.list_view_item_transaction_entry_price);
        priceTextView.setText(String.format(Locale.getDefault(), "$%.2f" , listEntries.get(position).getPrice()));

        TextView countTextView = (TextView) view.findViewById(R.id.list_view_item_transaction_entry_count);
        countTextView.setText(String.format(Locale.getDefault(), "x%d", listEntries.get(position).getQuantity()));

        Button deleteBtn = (Button)view.findViewById(R.id.button_delete_transaction_entry);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TransactionEntry toRemove = listEntries.get(position);
                listEntries.remove(position);
                cta.removeEntryQuantity(toRemove);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public TransactionEntryListAdapter(@NonNull Context context, ArrayList<TransactionEntry> entries, final CreateTransactionActivity cta) {
        this.context = context;
        this.listEntries = entries;
        this.cta = cta;
    }
}
