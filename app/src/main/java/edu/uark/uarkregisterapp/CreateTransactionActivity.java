package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import edu.uark.uarkregisterapp.adapters.TransactionEntryListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;

public class CreateTransactionActivity extends AppCompatActivity {

    private static final String TAG = "CreateTransaction";
    private static final String DEMO_CASHIER_ID = "0001";

    private Transaction transaction;
    private EmployeeTransition employeeTransition;

    private ListView mListView;
    private EditText mLookupCodeEditText;
    private EditText mQuantityEditText;
    private Button mAddProductButton;
    private Button mSubmitTransactionButton;

    private TransactionEntryListAdapter listAdapter;
    private ArrayList<TransactionEntry> transactionEntries;

    private HashMap<String, Integer> totalQuantities;

    List<Product> availableProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        this.totalQuantities = new HashMap<>();

        this.employeeTransition = this.getIntent().getParcelableExtra(this.getString(R.string.intent_extra_employee));

        transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        if (this.employeeTransition != null) {
            transaction.setCashierId(this.employeeTransition.getEmployeeId());
        } else {
            transaction.setCashierId(DEMO_CASHIER_ID);
        }
        transaction.setIsRefund(false);

        availableProducts = new ArrayList<Product>();

        mListView = (ListView)findViewById(R.id.list_view_create_transaction);
        mLookupCodeEditText = (EditText)findViewById(R.id.edit_text_create_transaction_product_lookup);
        mQuantityEditText = (EditText)findViewById(R.id.edit_text_create_transaction_product_quantity);
        mAddProductButton = (Button)findViewById(R.id.button_create_transaction_add_product);
        mSubmitTransactionButton = (Button)findViewById(R.id.button_submit_transaction);

        this.transactionEntries = new ArrayList<TransactionEntry>();

        this.listAdapter = new TransactionEntryListAdapter(this, this.transactionEntries, this);
        this.mListView.setAdapter(this.listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new RetrieveProductsTask()).execute();
    }

    public void onAddTransactionEntryClick(View v) {
        if (!checkValidForm()) {
            return;
        }

        String lookup = this.mLookupCodeEditText.getText().toString();
        Product selectedProduct = getProduct(lookup);
        if (selectedProduct == null) {
            showProductNotFoundDialog();
            return;
        }

        Integer requestedQuantity = Integer.parseInt(this.mQuantityEditText.getText().toString());
        if (!canAddAdditionalProduct(selectedProduct, requestedQuantity)) {
            showQuantityTooHighDialog();
            return;
        }

        int currentQuantity = 0;
        if (this.totalQuantities.containsKey(selectedProduct.getLookupCode())) {
            currentQuantity = this.totalQuantities.get(selectedProduct.getLookupCode());
        }
        currentQuantity += requestedQuantity;
        this.totalQuantities.put(selectedProduct.getLookupCode(), currentQuantity);

        TransactionEntry newEntry = new TransactionEntry();
        newEntry.setLookupCode(lookup);
        newEntry.setQuantity(Integer.parseInt(this.mQuantityEditText.getText().toString()));
        newEntry.setId(UUID.randomUUID());
        newEntry.setTransactionId(transaction.getId());
        newEntry.setPrice(selectedProduct.getPrice());

        transactionEntries.add(newEntry);
        listAdapter.notifyDataSetChanged();
    }

    private boolean canAddAdditionalProduct(Product product, int requestedQuantity) {
        int maxQuantity = product.getCount();
        int currentQuantity = 0;
        if (this.totalQuantities.containsKey(product.getLookupCode())) {
            currentQuantity = this.totalQuantities.get(product.getLookupCode());
        }
        return (requestedQuantity + currentQuantity) <= maxQuantity;
    }

    private Product getProduct(String lookupCode) {
        for (Product product : this.availableProducts) {
            if (product.getLookupCode().equals(lookupCode)) {
                return product;
            }
        }
        return null;
    }

    public void onSubmitTransactionClick(View v) {
        if (transactionEntries.size() <= 0) {
            showNoEntriesDialog();
            return;
        }

        transaction.setTotalAmount(getTotalTransactionCost());

        ArrayList<TransactionEntryTransition> entryTransitions = new ArrayList<TransactionEntryTransition>();
        for (TransactionEntry entry : this.transactionEntries) {
            entryTransitions.add(new TransactionEntryTransition(entry));
        }

        Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
        intent.putParcelableArrayListExtra(getString(R.string.intent_extra_transaction_entries), entryTransitions);
        intent.putExtra(getString(R.string.intent_extra_transaction), new TransactionTransition(this.transaction));
//        intent.putExtra(getString(R.string.intent_extra_transaction),
//                entryTransitions[0]);

        startActivity(intent);
    }

    private double getTotalTransactionCost() {
        double total = 0;
        for (TransactionEntry entry : transactionEntries) {
            total += entry.getPrice() * entry.getQuantity();
        }

        return total;
    }

    private boolean checkValidForm() {
        String lookup = this.mLookupCodeEditText.getText().toString();
        if (StringUtils.isEmpty(lookup)) {
            showBadLookupDialog();
            return false;
        }
        String quantity = this.mQuantityEditText.getText().toString();
        if (StringUtils.isEmpty(quantity)) {
            showBadQuantityDialog();
            return false;
        }
        if (Integer.parseInt(quantity) <= 0) {
            showBadQuantityDialog();
            return false;
        }
        return true;
    }

    private void showBadLookupDialog() {
        new AlertDialog.Builder(CreateTransactionActivity.this).
                setMessage(R.string.alert_dialog_bad_lookup).
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

    private void showBadQuantityDialog() {
        new AlertDialog.Builder(CreateTransactionActivity.this).
                setMessage(R.string.alert_dialog_bad_quantity).
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

    private void showQuantityTooHighDialog() {
        new AlertDialog.Builder(CreateTransactionActivity.this).
                setMessage(R.string.alert_dialog_not_enough_product).
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

    private void showNoEntriesDialog() {
        new AlertDialog.Builder(CreateTransactionActivity.this).
                setMessage(R.string.alert_dialog_no_entries).
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

    private void showProductNotFoundDialog() {
        new AlertDialog.Builder(CreateTransactionActivity.this).
                setMessage(R.string.alert_dialog_product_not_found).
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

    public void removeEntryQuantity(TransactionEntry entry) {
        String lookup = entry.getLookupCode();
        int quantityToRemove = entry.getQuantity();
        totalQuantities.put(lookup, totalQuantities.get(lookup) - quantityToRemove);
    }

    private class RetrieveProductsTask extends AsyncTask<Void, Void, ApiResponse<List<Product>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAddProductButton.setEnabled(false);
            mAddProductButton.setText(R.string.button_add_product_loading);
        }

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
            } else {
                mAddProductButton.setEnabled(true);
                mAddProductButton.setText(R.string.button_add_product);
            }
        }
    }
}
