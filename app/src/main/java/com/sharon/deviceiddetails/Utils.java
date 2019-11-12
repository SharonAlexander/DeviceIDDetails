package com.sharon.deviceiddetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

class Utils {

    private TelephonyManager telephonyManager;
    private Details details;
    private static final String TAG = "Utils";

    Utils(Context context, Details details) {
        this.details = details;
        getLocalIpAddress();
        getImei(context);
        getSimSubscriberID();
        getSimSerial();
        getDeviceFingerPrint();
        getModel();
        getWifiMac();
        getProcessorName();
        getDeviceID(context);
    }

    private void getDeviceID(Context context) {
        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        details.setDeviceID(deviceId);
    }

    private void getProcessorName() {
        try {
            BufferedReader br = new BufferedReader (new FileReader ("/proc/cpuinfo"));
            String str;
            while ((str = br.readLine ()) != null) {
                String[] data = str.split (":");
                if (data.length > 1) {
                    String key = data[0].trim();
                    if (key.equals("Hardware")) {
                        String value = data[1].trim();
//                        value = value.replaceAll("\\s+", " ");
                        details.setProcessorArch(value);
                        return;
                    }else{
                        details.setProcessorArch("Error");
                    }
                }
            }
            br.close ();
        } catch (Exception e) {
            details.setProcessorArch("Error");
        }
    }

    private void getWifiMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    details.setWifiMac("Error");
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                details.setWifiMac(res1.toString());
            }
        } catch (Exception ex) {
            details.setWifiMac("02:00:00:00:00:00");
        }
    }

    private void getModel() {
        details.setDeviceModel(Build.BRAND+" "+Build.MODEL);
    }

    private void getDeviceFingerPrint() {
        details.setDeviceBuildFingerprints(Build.FINGERPRINT);
    }

    private void getLocalIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if((intf.getDisplayName().equals("wlan0") || intf.getDisplayName().equals("rmnet_data1") )&& addr instanceof Inet4Address){
                        details.setLocalIPAdd(addr.toString().substring(1));
                    }
                }
            }
        } catch (Exception ex) {
            details.setLocalIPAdd("Error");
        }
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private void getImei(Context context){
        telephonyManager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        try {
            if (Build.VERSION.SDK_INT>=29) {
                details.setIMEI1(telephonyManager != null ? telephonyManager.getImei(0) : "Error");
                details.setIMEI2(telephonyManager != null ? telephonyManager.getImei(1) : "Error");
            } else {
                details.setIMEI1(telephonyManager != null ? telephonyManager.getDeviceId(0) : "Error");
                details.setIMEI2(telephonyManager != null ? telephonyManager.getDeviceId(1) : "Error");
            }
        } catch (Exception e) {
            details.setIMEI1("Error");
        }
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private void getSimSubscriberID(){
        try {
            details.setSimSubID(telephonyManager.getSubscriberId());
        } catch (Exception e) {
            details.setSimSubID("Error");
        }
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private void getSimSerial(){
        try {
            details.setSimSerial(telephonyManager.getSimSerialNumber());
        } catch (Exception e) {
            details.setSimSerial("Error");
        }
    }
}
