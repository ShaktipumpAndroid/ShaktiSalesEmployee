
package bean.vkbean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RemarkModelView {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private List<RemarkResponseList> mResponse;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<RemarkResponseList> getResponse() {
        return mResponse;
    }

    public void setResponse(List<RemarkResponseList> response) {
        mResponse = response;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
