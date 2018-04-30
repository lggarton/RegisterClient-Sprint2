package edu.uark.uarkregisterapp.models.api.services;

import org.json.JSONObject;

import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.enums.ApiObject;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;

public class TransactionEntryService extends BaseRemoteService {
    // Note: The IDs here are the transactionentry entry id, not the transaction id

    public ApiResponse<TransactionEntry> getTransactionEntry(UUID entryId) {
        return this.readTransactionEntryDetailsFromResponse(
                this.<TransactionEntry>performGetRequest(
                        this.buildPath(entryId)
                )
        );
    }

    public ApiResponse<TransactionEntry> updateTransactionEntry(TransactionEntry entry) {
        return this.readTransactionEntryDetailsFromResponse(
                this.<TransactionEntry>performPutRequest(
                        this.buildPath(entry.getEntryId())
                        , entry.convertToJson()
                )
        );
    }

    public ApiResponse<TransactionEntry> createTransactionEntry(TransactionEntry entry) {
        return this.readTransactionEntryDetailsFromResponse(
                this.<TransactionEntry>performPostRequest(
                        this.buildPath()
                        , entry.convertToJson()
                )
        );
    }

    public ApiResponse<String> deleteTransactionEntry(UUID entryId) {
        return this.<String>performDeleteRequest(
                this.buildPath(entryId)
        );
    }

    private ApiResponse<TransactionEntry> readTransactionEntryDetailsFromResponse(ApiResponse<TransactionEntry> apiResponse) {
        return this.readDetailsFromResponse(
                apiResponse, (new TransactionEntry())
        );
    }

    private <T extends LoadFromJsonInterface<T>> ApiResponse<T> readDetailsFromResponse(ApiResponse<T> apiResponse, T apiObject) {
        JSONObject rawJsonObject = this.rawResponseToJSONObject(
                apiResponse.getRawResponse()
        );

        if (rawJsonObject != null) {
            apiResponse.setData(
                    apiObject.loadFromJson(rawJsonObject)
            );
        }

        return apiResponse;
    }

    public TransactionEntryService() {
        super(ApiObject.TRANSACTION_ENTRY);
    }
}
