package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;

public class TransactionEntryListAdapter extends ArrayAdapter<TransactionEntry> {
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(R.layout.list_view_item_transaction_entry, parent, false);
        }

        TransactionEntry transactionEntry = this.getItem(position);
        if (transactionEntry != null) {
            TextView lookupCodeTextView = (TextView) view.findViewById(R.id.list_view_item_transaction_entry_price);
            if (lookupCodeTextView != null) {
                lookupCodeTextView.setText(Double.toString(transactionEntry.getPrice()));
            }

            TextView countTextView = (TextView) view.findViewById(R.id.list_view_item_transaction_entry_count);
            if (countTextView != null) {
                countTextView.setText(String.format(Locale.getDefault(), "%d", transactionEntry.getQuantity()));
            }
        }

        return view;
    }

    public TransactionEntryListAdapter(@NonNull Context context, List<TransactionEntry> entries) {
        super(context, R.layout.list_view_item_transaction_entry, entries);
    }
}
