package cardloader.icode.cardloader;

/**
 * Created by Icode on 10/31/2017.
 */


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cardloader.icode.cardloader.R;
import cardloader.icode.cardloader.google.OcrCaptureActivity;

import static android.Manifest.permission.CALL_PHONE;

public class StartDialog extends DialogFragment {

    private Bitmap bmp;
    boolean noDB=false;
    private String title, txt="";
    private static final int MY_PERMISSIONS_CALL_PHONE = 1005;
    private DBHelper dbHelper;
    Button single,multiple;
    public StartDialog(){}

    public static StartDialog New(){
        return new StartDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        getActivity().getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = inflater.inflate(R.layout.start_dialog, null);
        dbHelper=new DBHelper(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            ActivityCompat.requestPermissions(getActivity(), new String[]{CALL_PHONE}, MY_PERMISSIONS_CALL_PHONE);

        }

        single=(Button) view.findViewById(R.id.single);
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity("single");
            }
        });
        multiple=(Button) view.findViewById(R.id.multiple);
        multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity("multiple");
            }
        });
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        bmp = null;
//        System.gc();
        super.onDismiss(dialog);
    }

    public void startCameraActivity(String type){
        OcrCaptureActivity.type=type;
        startActivity(new Intent(getActivity(),OcrCaptureActivity.class));
        // startActivity(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE ));
    }
}


