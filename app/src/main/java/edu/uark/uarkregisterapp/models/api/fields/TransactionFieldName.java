package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionFieldName implements FieldNameInterface {
    ID("id"),
    CASHIER_ID("cashierId"),
    TOTAL_AMOUNT("totalAmount"),
    TRANSACTION_TYPE("transactionType"),
    REFERENCE_ID("referenceId"),
    CREATED_ON("createdOn");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    TransactionFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
