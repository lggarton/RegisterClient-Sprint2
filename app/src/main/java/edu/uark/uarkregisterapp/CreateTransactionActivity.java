package edu.uark.uarkregisterapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.uark.uarkregisterapp.adapters.TransactionEntryListAdapter;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;

public class CreateTransactionActivity extends AppCompatActivity {

    private ListView mListView;
    private TransactionEntryListAdapter listAdapter;
    private List<TransactionEntry> transactionEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        mListView = (ListView)findViewById(R.id.list_view_create_transaction);

        this.transactionEntries = new ArrayList<TransactionEntry>();
        TransactionEntry testEntry = new TransactionEntry();
        testEntry.setPrice(20.0);
        testEntry.setQuantity(3);
        this.transactionEntries.add(testEntry);
        this.transactionEntries.add(new TransactionEntry().setPrice(23));

        this.listAdapter = new TransactionEntryListAdapter(this, this.transactionEntries);
        this.mListView.setAdapter(this.listAdapter);
    }
}
