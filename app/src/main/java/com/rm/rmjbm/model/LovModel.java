package com.rm.rmjbm.model;

import android.telephony.mbms.StreamingServiceInfo;

import java.io.Serializable;

public class LovModel implements Serializable {
    private String LGORT;
    private String LGOBE;

    public String getLGORT() {
        return LGORT;
    }

    public void setLGORT(String LGORT) {
        this.LGORT = LGORT;
    }

    public String getLGOBE() {
        return LGOBE;
    }

    public void setLGOBE(String LGOBE) {
        this.LGOBE = LGOBE;
    }

}
