package edu.uark.uarkregisterapp.models.api;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.fields.TransactionFieldName;
import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;

public class Transaction implements ConvertToJsonInterface, LoadFromJsonInterface {
    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public Transaction setId(UUID id) {
        this.id = id;
        return this;
    }

    private String cashierId;
    public String getCashierId() {
        return this.cashierId;
    }
    public Transaction setCashierId(String id) {
        this.cashierId = id;
        return this;
    }

    private double totalAmount;
    public double getTotalAmount() {
        return this.totalAmount;
    }
    public Transaction setTotalAmount(double amt) {
        this.totalAmount = amt;
        return this;
    }
    public Transaction addToTotalAmount (double amt) {
        this.totalAmount = this.totalAmount + amt;
        return this;
    }

    private boolean isRefund;
    public boolean getIsRefund() {
        return this.isRefund;
    }
    public Transaction setIsRefund(boolean isRefund) {
        this.isRefund = isRefund;
        return this;
    }

    private UUID referenceId;
    public UUID getReferenceId() {
        return this.referenceId;
    }
    public Transaction setReferenceId(UUID id) {
        this.referenceId = id;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return this.createdOn;
    }
    public Transaction setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    @Override
    public Transaction loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(TransactionFieldName.ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.id = UUID.fromString(value);
        }

        this.cashierId = rawJsonObject.optString(TransactionFieldName.CASHIER_ID.getFieldName());

        this.totalAmount = rawJsonObject.optDouble(TransactionFieldName.TOTAL_AMOUNT.getFieldName());
        this.isRefund = rawJsonObject.optBoolean(TransactionFieldName.IS_REFUND.getFieldName());

        value = rawJsonObject.optString(TransactionFieldName.REFERENCE_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.referenceId = UUID.fromString(value);
        }

        value = rawJsonObject.optString(TransactionFieldName.CREATED_ON.getFieldName());
        if (!StringUtils.isBlank(value)) {
            try {
                this.createdOn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TransactionFieldName.ID.getFieldName(), this.id.toString());
            jsonObject.put(TransactionFieldName.CASHIER_ID.getFieldName(), this.cashierId);
            jsonObject.put(TransactionFieldName.TOTAL_AMOUNT.getFieldName(), this.totalAmount);
            jsonObject.put(TransactionFieldName.IS_REFUND.getFieldName(), this.isRefund);
            jsonObject.put(TransactionFieldName.REFERENCE_ID.getFieldName(), this.referenceId.toString());
            jsonObject.put(TransactionFieldName.CREATED_ON.getFieldName(), (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)).format(this.createdOn));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public Transaction() {
        this.id = new UUID(0, 0);
        this.cashierId = StringUtils.EMPTY;
        this.totalAmount = 0.0;
        this.isRefund = false;
        this.referenceId = new UUID(0, 0);
        this.createdOn = new Date();
    }

    public Transaction(TransactionTransition transition) {
        this.id = transition.getId();
        this.cashierId = transition.getCashierId();
        this.totalAmount = transition.getTotalAmount();
        this.isRefund = transition.getIsRefund();
        this.referenceId = transition.getReferenceId();
        this.createdOn = transition.getCreatedOn();
    }
}
