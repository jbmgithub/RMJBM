
package com.rm.rmjbm.model.noMovementbasLov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotMovementbaseLov {

    @SerializedName("MT_MTP_DDLIST_REC")
    @Expose
    private MtMtpDdlistRec mtMtpDdlistRec;

    public MtMtpDdlistRec getMtMtpDdlistRec() {
        return mtMtpDdlistRec;
    }

    public void setMtMtpDdlistRec(MtMtpDdlistRec mtMtpDdlistRec) {
        this.mtMtpDdlistRec = mtMtpDdlistRec;
    }

}
