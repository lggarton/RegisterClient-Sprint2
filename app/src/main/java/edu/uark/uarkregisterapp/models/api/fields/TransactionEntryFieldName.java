package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionEntryFieldName implements FieldNameInterface {
    ID("id"),
    LOOKUP_CODE("lookupCode"),
    QUANTITY("quantity"),
    PRICE("price"),
    TRANSACTION_ID("transactionId");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    TransactionEntryFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
