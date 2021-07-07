package com.rm.rmjbm.model.totalCount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalCount {

    @SerializedName("MT_RM_BC_PHY_INV_DET_REC")
    @Expose
    private MtRmBcPhyInvDetRec mtRmBcPhyInvDetRec;

    public MtRmBcPhyInvDetRec getMtRmBcPhyInvDetRec() {
        return mtRmBcPhyInvDetRec;
    }

    public void setMtRmBcPhyInvDetRec(MtRmBcPhyInvDetRec mtRmBcPhyInvDetRec) {
        this.mtRmBcPhyInvDetRec = mtRmBcPhyInvDetRec;
    }

}