package cardloader.icode.cardloader;

/**
 * Created by Icode on 10/31/2017.
 */


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cardloader.icode.cardloader.R;
import cardloader.icode.cardloader.google.OcrCaptureActivity;

import static android.Manifest.permission.CALL_PHONE;

public class ImageDialog extends DialogFragment {

    private Bitmap bmp;

    private String title, txt="";
    private static final int MY_PERMISSIONS_CALL_PHONE = 1007;
     EditText textEdit;
    Button btnRecharge;
    static final String TAG = "DBG_" + ImageDialog.class.getName();
    public ImageDialog(){}

    public static ImageDialog New(){
        return new ImageDialog();
    }

    public ImageDialog addBitmap(Bitmap bmp) {
        if (bmp != null)
            this.bmp = bmp;
        return this;
    }

    public ImageDialog addTitle(String title) {
        if (title != null)
            this.title = title;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_dialog, null);

//        ImageView imageView = (ImageView) view.findViewById(R.id.image_dialog_imageView);
//        TextView textView = (TextView) view.findViewById(R.id.image_dialog_textView);
         textEdit = (EditText) view.findViewById(R.id.image_dialog_textEdit);
        btnRecharge=(Button) view.findViewById(R.id.btnRecharge);
        if (ActivityCompat.checkSelfPermission(getActivity(), CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            ActivityCompat.requestPermissions(getActivity(), new String[]{CALL_PHONE}, MY_PERMISSIONS_CALL_PHONE);

        }
        if (bmp != null)
//            imageView.setImageBitmap(bmp);

        if(title!=null) {
//            textView.setText(this.title);
            textEdit.setText(this.title.trim().replace(" ",""));
//            txt=this.title;
        }
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              recharge();
            }
        });
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        bmp.recycle();
        bmp = null;
        System.gc();
//        startActivity(new Intent(getActivity(),OcrCaptureActivity.class));
        super.onDismiss(dialog);
    }

    public void recharge(){

        txt=textEdit.getText().toString().trim().replace(" ","");

        if(txt=="" || txt==null || txt.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("CardLoader");
            builder.setMessage("number empty, try again")
                    .setCancelable(true)
                    .setPositiveButton("OK", null);
            AlertDialog alert = builder.create();
            alert.show();
        }else{
//                    Toast.makeText(getActivity(), "TXT: "+txt, Toast.LENGTH_SHORT).show();
            try{
                int num=Integer.parseInt(txt);
                CardDialog.New().addTitle(txt)
                        .show(getFragmentManager(), TAG);
            }
            catch (Exception re){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("CardLoader");
                builder.setMessage("number invalid, try again")
                        .setCancelable(true)
                        .setPositiveButton("OK", null);
                AlertDialog alert = builder.create();
                alert.show();

            }
//

        }
    }

}


