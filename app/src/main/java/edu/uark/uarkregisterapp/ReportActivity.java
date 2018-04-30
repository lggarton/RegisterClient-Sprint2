package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import edu.uark.uarkregisterapp.models.transition.TransactionTransition;

public class ReportActivity extends AppCompatActivity {
    private Button returnButton;
    private TransactionTransition transactionTransition;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_report_screen);

        // Pull the Transaction and TransactionEntries in from the previous activity
        this.transactionTransition = this.getIntent().getParcelableExtra(this.getString(R.string.intent_extra_transaction));
        if (this.getIntent() != null) {
            list = findViewById(R.id.list_view_products);
//            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getEntries());
//            list.setAdapter(adapter);
            // does not print out the list -- still working on this
        }

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private double getTotal() {
        return this.transactionTransition.getTotalAmount();
    }

//    private List<TransactionEntryTransition> getEntries() {
//        return this.transactionTransition.getEntryTransitions();
//    }

    private ListView getTransactions() {
        return (ListView) this.findViewById(R.id.list_view_products);
    }
}