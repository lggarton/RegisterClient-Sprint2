package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.TextView;

import edu.uark.uarkregisterapp.adapters.TransactionReportAdapter;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;
import edu.uark.uarkregisterapp.models.api.Transaction;


public class ReportActivity extends AppCompatActivity {
    private Button returnButton;
    private ListView list;
    private TransactionTransition transactionTransition;
    private Transaction transaction;
    private ArrayList<TransactionEntryTransition> arrayList;
    private TransactionReportAdapter reportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_report_screen);

        list = findViewById(R.id.list_view_products);
        this.transactionTransition = this.getIntent().getParcelableExtra(getString(R.string.intent_extra_transaction));
        this.arrayList = this.getIntent().getParcelableArrayListExtra(getString(R.string.intent_extra_transaction_entries));
        this.reportAdapter = new TransactionReportAdapter(this, this.arrayList);
        this.getProductsListView().setAdapter(this.reportAdapter);

        this.transaction = new Transaction(this.transactionTransition);
        TextView totalAmountText = (TextView) this.findViewById(R.id.total);
        totalAmountText.setText(Double.toString(transaction.getTotalAmount()));

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private ListView getProductsListView() {
        return (ListView) this.findViewById(R.id.list_view_products);
    }
}