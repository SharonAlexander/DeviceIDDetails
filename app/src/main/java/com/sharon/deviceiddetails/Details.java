package com.sharon.deviceiddetails;

import android.content.Context;

class Details {

    private String deviceID,deviceModel,IMEI1,IMEI2,simSubID,simSerial,localIPAdd,wifiMac,processorArch,deviceBuildFingerprints;

    public String getIMEI1() {
        return IMEI1;
    }

    public void setIMEI1(String IMEI1) {
        this.IMEI1 = IMEI1;
    }

    public String getIMEI2() {
        return IMEI2;
    }

    public void setIMEI2(String IMEI2) {
        this.IMEI2 = IMEI2;
    }

    public String getSimSubID() {
        return simSubID;
    }

    public void setSimSubID(String simSubID) {
        this.simSubID = simSubID;
    }

    public String getSimSerial() {
        return simSerial;
    }

    public void setSimSerial(String simSerial) {
        this.simSerial = simSerial;
    }

    public String getLocalIPAdd() {
        return localIPAdd;
    }

    public void setLocalIPAdd(String localIPAdd) {
        this.localIPAdd = localIPAdd;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public String getDeviceBuildFingerprints() {
        return deviceBuildFingerprints;
    }

    public void setDeviceBuildFingerprints(String deviceBuildFingerprints) {
        this.deviceBuildFingerprints = deviceBuildFingerprints;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getProcessorArch() {
        return processorArch;
    }

    public void setProcessorArch(String processorArch) {
        this.processorArch = processorArch;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
