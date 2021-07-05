
package com.rm.rmjbm.model.scanPhyInventoryData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhyInventoryData {

    @SerializedName("MT_RM_BC_PHY_INV_SCAN_REC")
    @Expose
    private MtRmBcPhyInvScanRec mtRmBcPhyInvScanRec;

    public MtRmBcPhyInvScanRec getMtRmBcPhyInvScanRec() {
        return mtRmBcPhyInvScanRec;
    }

    public void setMtRmBcPhyInvScanRec(MtRmBcPhyInvScanRec mtRmBcPhyInvScanRec) {
        this.mtRmBcPhyInvScanRec = mtRmBcPhyInvScanRec;
    }

}
