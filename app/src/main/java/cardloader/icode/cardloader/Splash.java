package cardloader.icode.cardloader;

/**
 * Created by Icode on 11/9/2017.
 */


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by charles on 06-May-16.
 */
public class Splash extends AppCompatActivity {



    protected LocationManager locationManager;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1001;
    private static final int MY_PERMISSIONS_CALL_PHONE = 1005;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (StartActivity.welcomeScreenIsShown) {
            // Open your Main Activity

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Thread timer = new Thread() {
                public void run() {
                    try {
                        //If authorisation not granted for camera
                        if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            //ask for authorisation
                            ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.CAMERA}, 50);}
                        sleep(3000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent spl = new Intent(Splash.this, StartActivity.class);
                        startActivity(spl);
                        finish();
                    }
                }
            };
            timer.start();

            setContentView(R.layout.splash);
        }else {
            Intent spl = new Intent(Splash.this, StartActivity.class);
            startActivity(spl);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag == 0) {
            flag = 1 ;
        }
        else {
            flag=1;
            Intent spl = new Intent(Splash.this, StartActivity.class);
            startActivity(spl);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


}
