
package bean.vkbean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class ComplainDetailListModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private List<ComplainDetailListResponse> mResponse;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<ComplainDetailListResponse> getResponse() {
        return mResponse;
    }

    public void setResponse(List<ComplainDetailListResponse> response) {
        mResponse = response;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
