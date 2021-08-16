
package com.rm.rmjbm.model.MTPSubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MTMTPSubmitRec {

    @SerializedName("MT_MTP_Submit_rec")
    @Expose
    private MTMTPSubmitRec__1 mTMTPSubmitRec;

    public MTMTPSubmitRec__1 getMTMTPSubmitRec() {
        return mTMTPSubmitRec;
    }

    public void setMTMTPSubmitRec(MTMTPSubmitRec__1 mTMTPSubmitRec) {
        this.mTMTPSubmitRec = mTMTPSubmitRec;
    }

}
