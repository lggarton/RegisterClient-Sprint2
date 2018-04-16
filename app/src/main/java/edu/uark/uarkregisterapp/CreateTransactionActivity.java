package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.uark.uarkregisterapp.adapters.TransactionEntryListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.services.ProductService;

public class CreateTransactionActivity extends AppCompatActivity {

    private Transaction transaction;

    private ListView mListView;
    private EditText mLookupCodeEditText;
    private EditText mQuantityEditText;
    private TransactionEntryListAdapter listAdapter;
    private ArrayList<TransactionEntry> transactionEntries;

    List<Product> availableProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        // TODO: Get an intent of the employee id to set the transaction cashierID here

        availableProducts = new ArrayList<Product>();

        mListView = (ListView)findViewById(R.id.list_view_create_transaction);
        mLookupCodeEditText = (EditText)findViewById(R.id.edit_text_create_transaction_product_lookup);
        mQuantityEditText = (EditText)findViewById(R.id.edit_text_create_transaction_product_quantity);

        this.transactionEntries = new ArrayList<TransactionEntry>();

        this.listAdapter = new TransactionEntryListAdapter(this, this.transactionEntries);
        this.mListView.setAdapter(this.listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new RetrieveProductsTask()).execute();
    }

    public void onAddTransactionEntryClick(View v) {
        TransactionEntry newEntry = new TransactionEntry();
        newEntry.setLookupCode(this.mLookupCodeEditText.getText().toString());
        newEntry.setQuantity(Integer.parseInt(this.mQuantityEditText.getText().toString()));
        newEntry.setId(UUID.randomUUID());
        newEntry.setTransactionId(transaction.getId());
        // TODO: Set the product's price

        transactionEntries.add(newEntry);
        listAdapter.notifyDataSetChanged();
    }

    public void onSubmitTransactionClick(View v) {
        transaction.setTotalAmount(getTotalTransactionCost());
    }

    private double getTotalTransactionCost() {
        double total = 0;
        for (TransactionEntry entry : transactionEntries) {
            total += entry.getPrice() * entry.getQuantity();
        }

        return total;
    }

    private class RetrieveProductsTask extends AsyncTask<Void, Void, ApiResponse<List<Product>>> {

        @Override
        protected ApiResponse<List<Product>> doInBackground(Void... params) {
            ApiResponse<List<Product>> apiResponse = (new ProductService()).getProducts();

            if (apiResponse.isValidResponse()) {
                availableProducts.clear();
                availableProducts.addAll(apiResponse.getData());

                for (Product p : availableProducts) {
                    Log.d("RetrieveProductsTask", "Got product: " + p.getLookupCode());
                }
            }

            return apiResponse;
        }

        @Override
        protected void onPostExecute(ApiResponse<List<Product>> apiResponse) {

            if (!apiResponse.isValidResponse()) {
                new AlertDialog.Builder(CreateTransactionActivity.this).
                        setMessage(R.string.alert_dialog_products_load_failure).
                        setPositiveButton(
                                R.string.button_dismiss,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }
                        ).
                        create().
                        show();
            }
        }


    }
}
