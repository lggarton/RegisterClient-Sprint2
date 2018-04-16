package edu.uark.uarkregisterapp.models.api;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.fields.TransactionEntryFieldName;
import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;

public class TransactionEntry implements ConvertToJsonInterface, LoadFromJsonInterface {

    // This is the Entry ID
    private UUID entryId;
    public UUID getEntryId() {
        return this.entryId;
    }
    public TransactionEntry setId(UUID id) {
        this.entryId = id;
        return this;
    }

    private String lookupCode;
    public String getLookupCode() {
        return this.lookupCode;
    }
    public TransactionEntry setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
        return this;
    }

    private int quantity;
    public int getQuantity() {
        return this.quantity;
    }
    public TransactionEntry setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    private double price;
    public double getPrice() {
        return this.price;
    }
    public TransactionEntry setPrice(double price) {
        this.price = price;
        return this;
    }

    private UUID transactionId;
    public UUID getTransactionId() {
        return this.transactionId;
    }
    public TransactionEntry setTransactionId(UUID id) {
        this.transactionId = id;
        return this;
    }

    @Override
    public TransactionEntry loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(TransactionEntryFieldName.ENTRY_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.entryId = UUID.fromString(value);
        }

        this.lookupCode = rawJsonObject.optString(TransactionEntryFieldName.LOOKUP_CODE.getFieldName());
        this.quantity = rawJsonObject.optInt(TransactionEntryFieldName.QUANTITY.getFieldName());
        this.price = rawJsonObject.optDouble(TransactionEntryFieldName.PRICE.getFieldName());

        value = rawJsonObject.optString(TransactionEntryFieldName.TRANSACTION_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.transactionId = UUID.fromString(value);
        }

        return this;
    }

    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TransactionEntryFieldName.ENTRY_ID.getFieldName(), this.entryId.toString());
            jsonObject.put(TransactionEntryFieldName.LOOKUP_CODE.getFieldName(), this.lookupCode);
            jsonObject.put(TransactionEntryFieldName.QUANTITY.getFieldName(), this.quantity);
            jsonObject.put(TransactionEntryFieldName.PRICE.getFieldName(), this.price);
            jsonObject.put(TransactionEntryFieldName.TRANSACTION_ID.getFieldName(), this.transactionId.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public TransactionEntry() {
        this.entryId = new UUID(0, 0);
        this.lookupCode = StringUtils.EMPTY;
        this.quantity = 0;
        this.price = 0.0;
        this.transactionId = new UUID(0, 0);
    }

}
