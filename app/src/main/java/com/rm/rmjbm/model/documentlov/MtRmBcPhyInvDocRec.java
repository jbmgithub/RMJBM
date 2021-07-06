package com.rm.rmjbm.model.documentlov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rm.rmjbm.model.documentlov.PhyInvNos;

public class MtRmBcPhyInvDocRec {

    @SerializedName("PHY_INV_NOS")
    @Expose
    private PhyInvNos phyInvNos;

    public PhyInvNos getPhyInvNos() {
        return phyInvNos;
    }

    public void setPhyInvNos(PhyInvNos phyInvNos) {
        this.phyInvNos = phyInvNos;
    }

}