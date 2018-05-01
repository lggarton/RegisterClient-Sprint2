package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import edu.uark.uarkregisterapp.adapters.TransactionReportAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.api.services.TransactionEntryService;
import edu.uark.uarkregisterapp.models.api.services.TransactionService;
import edu.uark.uarkregisterapp.models.transition.ProductTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;


public class ReportActivity extends AppCompatActivity {
    private Button returnButton;
    private ListView list;
    private TransactionTransition transactionTransition;
    private Transaction transaction;
    private ArrayList<TransactionEntryTransition> arrayList;
    private TransactionReportAdapter reportAdapter;
    private ArrayList<ProductTransition> productTransitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_report_screen);

        list = findViewById(R.id.list_view_products);
        this.transactionTransition = this.getIntent().getParcelableExtra(getString(R.string.intent_extra_transaction));
        this.arrayList = this.getIntent().getParcelableArrayListExtra(getString(R.string.intent_extra_transaction_entries));
        this.productTransitions = this.getIntent().getParcelableArrayListExtra(getString(R.string.intent_extra_product));
        this.reportAdapter = new TransactionReportAdapter(this, this.arrayList);
        this.getProductsListView().setAdapter(this.reportAdapter);

        for (TransactionEntryTransition t : this.arrayList) {
            Log.d("Test-newentry-next-next", t.getEntryId().toString());
        }

        this.transaction = new Transaction(this.transactionTransition);
        TextView totalAmountText = (TextView) this.findViewById(R.id.total);
        totalAmountText.setText(String.format(Locale.getDefault(), "$%.2f" , this.transaction.getTotalAmount()));

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        uploadTransactionWithEntries();
        uploadProductUpdates();
    }

    private ListView getProductsListView() {
        return (ListView) this.findViewById(R.id.list_view_products);
    }

    private TextView getTotalListed() {
        return (TextView) this.findViewById(R.id.total);
    }

    private void uploadTransactionWithEntries() {
        (new CreateTransactionTask()).execute(this.transaction);
        for (TransactionEntryTransition transition : this.arrayList) {
            TransactionEntry entry = new TransactionEntry(transition);
            (new CreateTransactionEntryTask()).execute(entry);
        }
    }

    private void uploadProductUpdates() {
        HashMap<String, Integer> totalQuantities = new HashMap<>();
        for (TransactionEntryTransition entryTransition : this.arrayList) {
            String lookup = entryTransition.getLookupCode();
            if (totalQuantities.containsKey(lookup)) {
                totalQuantities.put(lookup, totalQuantities.get(lookup) + entryTransition.getQuantity());
            } else {
                totalQuantities.put(lookup, entryTransition.getQuantity());
            }
        }
        for (ProductTransition productTransition : this.productTransitions) {
            Product product = new Product(productTransition);
            product.setCount(product.getCount() - totalQuantities.get(product.getLookupCode()));
            (new UpdateProductTask()).execute(product);
        }
    }

    private class CreateTransactionTask extends AsyncTask<Transaction, Void, ApiResponse<Transaction>> {

        @Override
        protected ApiResponse<Transaction> doInBackground(Transaction... transactions) {
            if (transactions.length > 0) {
                return (new TransactionService()).createTransaction(transactions[0]);
            } else {
                return (new ApiResponse<Transaction>())
                        .setValidResponse(false);
            }
        }

        @Override
        protected void onPostExecute(ApiResponse<Transaction> apiResponse) {
            super.onPostExecute(apiResponse);
            Log.d("Test-CT", apiResponse.getRawResponse());
        }
    }

    private class CreateTransactionEntryTask extends AsyncTask<TransactionEntry, Void, ApiResponse<TransactionEntry>> {
        @Override
        protected ApiResponse<TransactionEntry> doInBackground(TransactionEntry... transactionEntries) {
            if (transactionEntries.length > 0) {
                return (new TransactionEntryService()).createTransactionEntry(transactionEntries[0]);
            } else {
                return (new ApiResponse<TransactionEntry>()).setValidResponse(false);
            }
        }

        @Override
        protected void onPostExecute(ApiResponse<TransactionEntry> transactionEntryApiResponse) {
            super.onPostExecute(transactionEntryApiResponse);
            Log.d("Test-CTE", transactionEntryApiResponse.getRawResponse());
        }
    }

    private class UpdateProductTask extends AsyncTask<Product, Void, ApiResponse<Product>> {

        @Override
        protected ApiResponse<Product> doInBackground(Product... products) {
            if (products.length > 0) {
                return (new ProductService()).updateProduct(products[0]);
            } else {
                return (new ApiResponse<Product>().setValidResponse(false));
            }
        }

        @Override
        protected void onPostExecute(ApiResponse<Product> productApiResponse) {
            super.onPostExecute(productApiResponse);
            Log.d("Test-UP", productApiResponse.getRawResponse());
        }

    }

}
