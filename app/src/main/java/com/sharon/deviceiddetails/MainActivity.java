package com.sharon.deviceiddetails;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    LinearLayout mainLinearLayout;
    Details details;
    PermissionListener readPhoneStatePermissionlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("28C860176FFCDA81CE79CBEE1E3F6D38").build();
        mAdView.loadAd(adRequest);

        mainLinearLayout = findViewById(R.id.mainLinearLayout̥);
        details = new Details();

        setPermissionListeners();
        askPermissions();
    }

    private void askPermissions() {
        TedPermission.with(this)
                .setPermissionListener(readPhoneStatePermissionlistener)
                .setDeniedMessage("Permission needed for getting IMEI and other phone details")
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .check();
    }

    private void setPermissionListeners() {
        readPhoneStatePermissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                new Utils(MainActivity.this, details);
                setDetailsToView();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
    }

    private void setDetailsToView() {
        setView("Device Model", details.getDeviceModel(), null);
        setView("Android ID", details.getDeviceID(), null);
        setView("Processor Architecture", details.getProcessorArch(), null);
        if (details.getIMEI2() != null) {
            setView("IMEI", details.getIMEI1(), details.getIMEI2());
        } else {
            setView("IMEI", details.getIMEI1(), null);
        }
        setView("SIM Subscriber ID", details.getSimSubID(), null);
        setView("SIM Serial", details.getSimSerial(), null);
        setView("WiFi MAC Address", details.getWifiMac(), null);
        setView("Local IP Address", details.getLocalIPAdd(), null);
        setView("Device Build FingerPrints", details.getDeviceBuildFingerprints(), null);

    }

    private void setView(final String detailname, final String detailvalue, final String detailvalue2) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.rows, null, false);

        TextView detailName = view.findViewById(R.id.detailname);
        TextView detailValue = view.findViewById(R.id.detailvalue);
        TextView detailValue2 = view.findViewById(R.id.detailvalue2);

        detailName.setText(detailname);
        detailValue.setText(detailvalue);
        detailValue2.setText(detailvalue2);

        String clipboardText = detailvalue;
        if (detailvalue2 != null) {
            detailValue2.setVisibility(View.VISIBLE);
            clipboardText = detailvalue + "\n" + detailvalue2;
        }

        final String clipboardTextFinal = clipboardText;
        layout.addView(view);
        layout.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        mainLinearLayout.addView(layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", clipboardTextFinal);
                clipboard.setPrimaryClip(clip);

                Toast toast = Toast.makeText(MainActivity.this, detailname + " copied to clipboard", Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                TextView toastMessage = toast.getView().findViewById(android.R.id.message);
                toastMessage.setTextColor(Color.WHITE);
                toast.setView(view);
                toast.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.checkotherapps) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:MadRabbits")));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
