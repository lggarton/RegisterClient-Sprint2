package edu.uark.uarkregisterapp.models.transition;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
import edu.uark.uarkregisterapp.models.api.Transaction;

public class TransactionTransition implements Parcelable {
    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public TransactionTransition setId(UUID id) {
        this.id = id;
        return this;
    }

    private UUID cashierId;
    public UUID getCashierId() {
        return this.cashierId;
    }
    public TransactionTransition setCashierId(UUID id) {
        this.cashierId = id;
        return this;
    }

    private double totalAmount;
    public double getTotalAmount() {
        return this.totalAmount;
    }
    public TransactionTransition setTotalAmount(double amt) {
        this.totalAmount = amt;
        return this;
    }

    private int transactionType;
    public int getTransactionType() {
        return this.transactionType;
    }
    public TransactionTransition setTransactionType(int type) {
        this.transactionType = type;
        return this;
    }

    private UUID referenceId;
    public UUID getReferenceId() {
        return this.referenceId;
    }
    public TransactionTransition setReferenceId(UUID id) {
        this.referenceId = id;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return this.createdOn;
    }
    public TransactionTransition setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.id).execute());
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.cashierId).execute());
        destination.writeDouble(this.totalAmount);
        destination.writeInt(this.transactionType);
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.referenceId).execute());
        destination.writeLong(this.createdOn.getTime());
    }

    public static final Parcelable.Creator<TransactionTransition> CREATOR = new Parcelable.Creator<TransactionTransition>() {
        public TransactionTransition createFromParcel(Parcel transactionTransitionParcel) {
            return new TransactionTransition(transactionTransitionParcel);
        }

        public TransactionTransition[] newArray(int size) {
            return new TransactionTransition[size];
        }
    };

    public TransactionTransition() {
        this.id = new UUID(0, 0);
        this.cashierId = new UUID(0, 0);
        this.totalAmount = 0.0;
        this.transactionType = 0;
        this.referenceId = new UUID(0, 0);
        this.createdOn = new Date();
    }

    public TransactionTransition(Transaction transaction) {
        this.id = transaction.getId();
        this.cashierId = transaction.getCashierId();
        this.totalAmount = transaction.getTotalAmount();
        this.transactionType = transaction.getTransactionType();
        this.referenceId = transaction.getReferenceId();
        this.createdOn = transaction.getCreatedOn();
    }

    public TransactionTransition(Parcel transactionTransitionParcel) {
        this.id = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();
        this.cashierId = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();
        this.totalAmount = transactionTransitionParcel.readDouble();
        this.transactionType = transactionTransitionParcel.readInt();
        this.referenceId = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();

        this.createdOn = new Date();
        this.createdOn.setTime(transactionTransitionParcel.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
