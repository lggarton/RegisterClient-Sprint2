package edu.uark.uarkregisterapp.models.transition;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;

public class TransactionEntryTransition implements Parcelable {
    private static String TAG = "TET";

    private UUID entryId;
    public UUID getEntryId() {
        return this.entryId;
    }
    public TransactionEntryTransition setId(UUID id) {
        this.entryId = id;
        return this;
    }

    private String lookupCode;
    public String getLookupCode() {
        return this.lookupCode;
    }
    public TransactionEntryTransition setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
        return this;
    }

    private int quantity;
    public int getQuantity() {
        return this.quantity;
    }
    public TransactionEntryTransition setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    private double price;
    public double getPrice() {
        return this.price;
    }
    public TransactionEntryTransition setPrice(double price) {
        this.price = price;
        return this;
    }

    private UUID transactionId;
    public UUID getTransactionId() {
        return this.transactionId;
    }
    public TransactionEntryTransition setTransactionId(UUID id) {
        this.transactionId = id;
        return this;
    }


    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.entryId).execute());
        destination.writeString(this.lookupCode);
        destination.writeInt(this.quantity);
        destination.writeDouble(this.price);
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.transactionId).execute());
    }

    public static final Parcelable.Creator<TransactionEntryTransition> CREATOR = new Parcelable.Creator<TransactionEntryTransition>() {
        public TransactionEntryTransition createFromParcel(Parcel transactionEntryTransitionParcel) {
            return new TransactionEntryTransition(transactionEntryTransitionParcel);
        }

        public TransactionEntryTransition[] newArray(int size) {
            return new TransactionEntryTransition[size];
        }
    };

    public TransactionEntryTransition() {
        this.entryId = new UUID(0, 0);
        this.lookupCode = StringUtils.EMPTY;
        this.quantity = 0;
        this.price = 0.0;
        this.transactionId = new UUID(0, 0);
    }

    public TransactionEntryTransition(TransactionEntry transactionEntry) {
        this.entryId = transactionEntry.getEntryId();
        this.lookupCode = transactionEntry.getLookupCode();
        this.quantity = transactionEntry.getQuantity();
        this.price = transactionEntry.getPrice();
        this.transactionId = transactionEntry.getTransactionId();
    }

    public TransactionEntryTransition(Parcel transactionEntryTransitionParcel) {

        this.entryId = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionEntryTransitionParcel.createByteArray()).execute();
        this.lookupCode = transactionEntryTransitionParcel.readString();
        this.quantity = transactionEntryTransitionParcel.readInt();
        this.price = transactionEntryTransitionParcel.readDouble();
        this.transactionId = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionEntryTransitionParcel.createByteArray()).execute();
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
