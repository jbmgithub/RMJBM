
package com.rm.rmjbm.model.movementLov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MtMtpDdlistRec {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("success")
    @Expose
    private String success;

    public Object getMvtype() {
        return mvtype;
    }

    public void setMvtype(Object mvtype) {
        this.mvtype = mvtype;
    }

    @SerializedName("mvtype")
    @Expose
    private Object  mvtype = null;


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

//    public Mvtype getMvtype() {
//        return mvtype;
//    }
//
//    public void setMvtype(Mvtype mvtype) {
//        this.mvtype = mvtype;
//    }

}
