package edu.uark.uarkregisterapp.models.transition;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
import edu.uark.uarkregisterapp.models.api.Transaction;

public class TransactionTransition implements Parcelable {
    // Note: this is not an exact replica of the Transaction class, this class holds a list of
    // TransactionEntries corresponding to the Transaction.

    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public TransactionTransition setId(UUID id) {
        this.id = id;
        return this;
    }

    private String cashierId;
    public String getCashierId() {
        return this.cashierId;
    }
    public TransactionTransition setCashierId(String id) {
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

    private boolean isRefund;
    public boolean getIsRefund() {
        return this.isRefund;
    }
    public TransactionTransition setTransactionType(boolean isRefund) {
        this.isRefund = isRefund;
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

    private List<TransactionEntryTransition> entryTransitions;
    public List<TransactionEntryTransition> getEntryTransitions() {
        return this.entryTransitions;
    }
    public TransactionTransition setEntryTransitions(List<TransactionEntryTransition> entries) {
        this.entryTransitions = entries;
        return this;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.id).execute());
        destination.writeString(this.cashierId);
        destination.writeDouble(this.totalAmount);
        destination.writeInt(this.isRefund ? 1 : 0);
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.referenceId).execute());
        destination.writeLong(this.createdOn.getTime());
        destination.writeTypedList(this.entryTransitions);
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
        this.cashierId = StringUtils.EMPTY;
        this.totalAmount = 0.0;
        this.isRefund = false;
        this.referenceId = new UUID(0, 0);
        this.createdOn = new Date();
        this.entryTransitions = new ArrayList<TransactionEntryTransition>();
    }

    public TransactionTransition(Transaction transaction, List<TransactionEntryTransition> transactionEntriesTransitions) {
        this.id = transaction.getId();
        this.cashierId = transaction.getCashierId();
        this.totalAmount = transaction.getTotalAmount();
        this.isRefund = transaction.getIsRefund();
        this.referenceId = transaction.getReferenceId();
        this.createdOn = transaction.getCreatedOn();
        this.entryTransitions = new ArrayList<TransactionEntryTransition>(transactionEntriesTransitions);
    }

    public TransactionTransition(Parcel transactionTransitionParcel) {
        this.id = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();
        this.cashierId = transactionTransitionParcel.readString();
        this.totalAmount = transactionTransitionParcel.readDouble();
        this.isRefund = transactionTransitionParcel.readInt() != 0;
        this.referenceId = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();

        this.createdOn = new Date();

        this.entryTransitions = new ArrayList<TransactionEntryTransition>();
        transactionTransitionParcel.readTypedList(this.entryTransitions, TransactionEntryTransition.CREATOR);

        this.createdOn.setTime(transactionTransitionParcel.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
