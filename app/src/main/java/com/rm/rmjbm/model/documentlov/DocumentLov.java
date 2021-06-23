package com.rm.rmjbm.model.documentlov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentLov {
    @SerializedName("MT_RM_BC_PHY_INV_DOC_REC")
    @Expose
    private MtRmBcPhyInvDocRec mtRmBcPhyInvDocRec;

    public MtRmBcPhyInvDocRec getMtRmBcPhyInvDocRec() {
        return mtRmBcPhyInvDocRec;
    }

    public void setMtRmBcPhyInvDocRec(MtRmBcPhyInvDocRec mtRmBcPhyInvDocRec) {
        this.mtRmBcPhyInvDocRec = mtRmBcPhyInvDocRec;
    }

}
