package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionFieldName implements FieldNameInterface {
    ID("id"),
    CASHIER_ID("cashierId"),
    TOTAL_AMOUNT("totalAmount"),
    IS_REFUND("is_refund"),
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
