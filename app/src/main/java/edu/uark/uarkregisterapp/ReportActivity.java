package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.uark.uarkregisterapp.models.transition.TransactionTransition;

public class ReportActivity extends AppCompatActivity {
    private Button returnButton;
    private TransactionTransition transactionTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_report_screen);

        // Pull the Transaction and TransactionEntries in from the previous activity
        this.transactionTransition = this.getIntent().getParcelableExtra(this.getString(R.string.intent_extra_transaction));

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        //ScrollView scroll = (ScrollView)findViewById(R.id.scroll);

    }
}
