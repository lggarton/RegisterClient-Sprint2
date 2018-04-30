package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;

public class TransactionReportAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<TransactionEntryTransition> listEntries;
    private Context context;

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
            view = inflater.inflate(R.layout.transaction_report_entries, null);
        }

        TextView lookupCodeTextView = (TextView) view.findViewById(R.id.text_view_create_transaction_product_lookup_code);
        lookupCodeTextView.setText(listEntries.get(position).getLookupCode());

        TextView priceTextView = (TextView) view.findViewById(R.id.list_view_item_transaction_entry_price);
        priceTextView.setText(String.format(Locale.getDefault(), "$%.2f" , listEntries.get(position).getPrice()));

        TextView countTextView = (TextView) view.findViewById(R.id.list_view_item_transaction_entry_count);
        countTextView.setText(String.format(Locale.getDefault(), "x%d", listEntries.get(position).getQuantity()));

      return view;
    }

    public TransactionReportAdapter(@NonNull Context context, ArrayList<TransactionEntryTransition> entries) {
        this.context = context;
        this.listEntries = entries;
    }
}
