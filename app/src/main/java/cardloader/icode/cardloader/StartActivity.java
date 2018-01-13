package cardloader.icode.cardloader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import cardloader.icode.cardloader.google.OcrCaptureActivity;

/**
 * Created by Icode on 11/9/2017.
 */

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    public static boolean welcomeScreenIsShown = true;
    Button btnStart,btnSetting,btnAbt, btnRate;
    private AdView adView;

    private static final String TAG = "StartActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        welcomeScreenIsShown=false;
        setContentView(R.layout.start_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("  CardLoader");
        // toolbar.setBackground(new ColorDrawable(Color.parseColor("#0000ff")));
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        btnAbt=(Button)findViewById(R.id.abt);
        btnSetting=(Button)findViewById(R.id.btnSetting);
        btnStart=(Button)findViewById(R.id.btnStart);
        btnRate=(Button)findViewById(R.id.btnRate);
        btnAbt.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnRate.setOnClickListener(this);

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        MobileAds.initialize(this, "ca-app-pub-5527620381143347~7505048258");
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnStart:
                //If authorisation not granted for camera
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    //ask for authorisation
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
                else

                StartDialog.New().show(getFragmentManager(), TAG);
                return;
            case R.id.btnSetting:
                startActivity(new Intent(this,Setting.class));
                return;
            case R.id.abt:
                startActivity(new Intent(this,About.class));
                return;
            case R.id.btnRate:
                rating();
                return;
        }
    }



    public void rating(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        Intent intent=null;
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));

        } catch (android.content.ActivityNotFoundException anfe) {
            intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
        }
        if(intent!=null)
            startActivity(intent);
    }
}
