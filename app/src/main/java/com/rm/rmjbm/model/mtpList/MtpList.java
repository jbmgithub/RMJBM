
package com.rm.rmjbm.model.mtpList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MtpList {

    @SerializedName("MT_MTP_Getlist_rec")
    @Expose
    private MTMTPGetlistRec mTMTPGetlistRec;

    public MTMTPGetlistRec getMTMTPGetlistRec() {
        return mTMTPGetlistRec;
    }

    public void setMTMTPGetlistRec(MTMTPGetlistRec mTMTPGetlistRec) {
        this.mTMTPGetlistRec = mTMTPGetlistRec;
    }

}
