
package com.rm.rmjbm.model.noMovementbasLov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MtMtpDdlistRec {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("mvtype")
    @Expose
    private String mvtype;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMvtype() {
        return mvtype;
    }

    public void setMvtype(String mvtype) {
        this.mvtype = mvtype;
    }

}
