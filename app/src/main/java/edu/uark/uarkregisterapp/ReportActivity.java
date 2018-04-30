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
    private double totalAmount;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_report_screen);

        list = findViewById(R.id.list_view_products);
        this.transactionTransition = this.getIntent().getParcelableExtra(getString(R.string.intent_extra_transaction));
        this.arrayList = this.getIntent().getParcelableArrayListExtra(getString(R.string.intent_extra_transaction_entries));
        this.reportAdapter = new TransactionReportAdapter(this, this.arrayList);
        this.getProductsListView().setAdapter(this.reportAdapter);

//        text = (TextView)findViewById(R.id.total);
//        this.totalAmount = transaction.getTotalAmount();
//        this.getIntent().putExtra("Total", totalAmount);
        // cant get the total but the products are listed

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                // stopped working
            }
        });
    }

    private ListView getProductsListView() {
        return (ListView) this.findViewById(R.id.list_view_products);
    }

    private TextView getTotalListed() {
        return (TextView) this.findViewById(R.id.total);
    }
}