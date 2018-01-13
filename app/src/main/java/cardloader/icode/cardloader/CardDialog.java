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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CardDialog extends DialogFragment {

    private Bitmap bmp;
boolean noDB=false;
    private String title, txt="";
    static String[] multiTxt;
    private static final int MY_PERMISSIONS_CALL_PHONE = 1005;
private DBHelper dbHelper;
    public CardDialog(){}
    private String[] settingArray = {"Network 1","Network 2","Network 3","Network 4"};
     private String[] NetworkArray= {"Network 1","Network 2","Network 3","Network 4"};
    private String[] NetworkArrayId= {"1","2","3","4"};
    ListView listView;
    static String dbrechargCode=null;
static int clickPosition=5,ni;
    private TelephonyManager mTelephonyManager;
    static int ii;
    static int cardCount=0;
    public static CardDialog New(){
        return new CardDialog();
    }

    public CardDialog addTitle(String title) {
        if (title != null)
            this.title = title;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_dialog, null);


dbHelper=new DBHelper(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            ActivityCompat.requestPermissions(getActivity(), new String[]{CALL_PHONE}, MY_PERMISSIONS_CALL_PHONE);

        }

            PhoneCallListener phoneListener = new PhoneCallListener();
            mTelephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
            mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        ProgressDialog process=new ProgressDialog(getActivity());
        process.setMessage("loaging...");
        if(dbHelper.numberOfRows()<1){
             NetworkArray[0]= "MTN";
            NetworkArray[1]= "GLO";
            NetworkArray[2]= "AIRTEL";
            NetworkArray[3]= "ETISALAT";
            noDB=true;
        }else {
            try {
                NetworkArray[0] = dbHelper.getByNetwork(settingArray[0]).split("-")[0].toUpperCase();
            } catch (Exception r1) {
                NetworkArray[0] = settingArray[0];
            }
            try {
                NetworkArray[1] = dbHelper.getByNetwork(settingArray[1]).split("-")[0].toUpperCase();
            } catch (Exception r1) {
                NetworkArray[1] = settingArray[1];
            }
            try {
                NetworkArray[2] = dbHelper.getByNetwork(settingArray[2]).split("-")[0].toUpperCase();
            } catch (Exception r1) {
                NetworkArray[2] = settingArray[2];
            }
            try {
                NetworkArray[3] = dbHelper.getByNetwork(settingArray[3]).split("-")[0].toUpperCase();
            } catch (Exception r1) {
                NetworkArray[3] = settingArray[3];
            }
            try {
                NetworkArrayId[0] = dbHelper.getByNetwork(settingArray[0]).split("-")[2];
            } catch (Exception r1) {
                NetworkArrayId[0] = "";
            }
            try {
                NetworkArrayId[1] = dbHelper.getByNetwork(settingArray[1]).split("-")[2];
            } catch (Exception r1) {
                NetworkArrayId[1] = "";
            }
            try {
                NetworkArrayId[2] = dbHelper.getByNetwork(settingArray[2]).split("-")[2];
            } catch (Exception r1) {
                NetworkArrayId[2] = "";
            }
            try {
                NetworkArrayId[3] = dbHelper.getByNetwork(settingArray[3]).split("-")[2];
            } catch (Exception r1) {
                NetworkArrayId[3] = "";
            }
        }
//            if (i==3){
//                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
//                        R.layout.listview, NetworkArray);
//
//                listView = (ListView) view.findViewById(R.id.card_list);
//                listView.setAdapter(adapter);
//            }


//        }

        process.dismiss();
        if(title!=null) {
//            textView.setText(this.title);
            txt=this.title;
//            txt=this.title;
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.listview, NetworkArray);

        listView = (ListView) view.findViewById(R.id.card_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                int networkid=0;
                try {

                    networkid=Integer.parseInt(NetworkArrayId[position]);
                    if(NetworkArrayId[position]=="" || NetworkArrayId[position].isEmpty()){networkid=0;}
                }catch (Exception er){
                    networkid=0;
                }
              if(networkid!=0 && !noDB){
                String network=item;
                String rechargCode;
                  rechargCode=dbHelper.getDataById(networkid).trim().replace(" ","");

                String[] splitCode=rechargCode.split("");
                if(!splitCode[1].equals("*") ){rechargCode="*"+rechargCode;}
                if(!splitCode[splitCode.length-1].equals("*")){rechargCode=rechargCode+"*";}
                try{
                   String[] rechargC=rechargCode.split("\\*");
                    for(int i=0;i<rechargC.length;i++){
                        if(rechargC[i].trim().length()>0){

                            Toast.makeText(getActivity(),rechargC[i],Toast.LENGTH_LONG).show();
                            int num=Integer.parseInt(rechargC[i]);
                        }
                    }
                    multiTxt = txt.split(",");
                    if(OcrCaptureActivity.type.equals("multiple") || multiTxt.length>1 ) {
                        for (int i = 0; i < multiTxt.length; i++) {
                            if (!multiTxt[i].isEmpty() || !multiTxt[i].equals("")) {
                                cardCount=cardCount+1;
                            }
                        }
                        dbrechargCode=rechargCode;
                        for (int i = 0; i < multiTxt.length; i++) {
                            if(!multiTxt[i].isEmpty() || !multiTxt[i].equals("")) {
                                txt = multiTxt[i];
                                multiTxt[i] = "";
                                String number = rechargCode + txt + "#";
                                rechargeCard(number);
                                break;
                            }
                        }
                    }else{
                        String number = rechargCode + txt + "#";
                        rechargeCard(number);
                    }
                }
                catch (Exception re){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("CardLoader");
                    builder.setMessage("code enter in setting is invalid, try again")
                            .setCancelable(true)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getActivity(),Setting.class));
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }else{
                  if(dbHelper.numberOfRows()<1){
                String[] NetworkCod={"*555*","*123*","*126*","*222*"};
                     String rechargCode= NetworkCod[position];
                     clickPosition=position;
                      String number = rechargCode+ txt+"#";
                      multiTxt = txt.split(",");
                      if(OcrCaptureActivity.type.equals("multiple") || multiTxt.length>1 ) {
                          for (int i = 0; i < multiTxt.length; i++) {
                              if (!multiTxt[i].isEmpty() || !multiTxt[i].equals("")) {
                                  cardCount=cardCount+1;
                              }
                          }
                          for (int i = 0; i < multiTxt.length; i++) {
                              if(!multiTxt[i].isEmpty() || !multiTxt[i].equals("")) {
                                  txt = multiTxt[i];
                                  multiTxt[i] = "";
                                  ni=i;
                                  number = rechargCode+ txt+"#";
                                  rechargeCard(number);
                                  break;
                              }
                          }
                      }else{
                          rechargeCard(number);
                      }
                  }
                  else{
                  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                  builder.setTitle("CardLoader");

                  builder.setMessage(item+" is not set,go to settings to set "+item+" and try again")
                          .setCancelable(true)
                          .setPositiveButton("OK", null);
                  AlertDialog alert = builder.create();
                  alert.show();

              }
              }
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

    void rechargeCard(String numb){
       String number = numb.replace("**", "*").replace("*", Uri.encode("*")).replace("#", Uri.encode("#"));
        Intent callIntent= new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        cardCount=cardCount-1;
        startActivity(callIntent);
        Toast.makeText(getActivity(), number, Toast.LENGTH_LONG).show();
        try {

            Thread.sleep(2000);
        } catch (Exception e) {
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");
                if(TelephonyManager.CALL_STATE_RINGING == state){
                    //Ring but no pickup-  a miss
//                    onMissedCall(context, savedNumber, callStartTime);
                }else if (isPhoneCalling) {
                    Log.i(LOG_TAG, "restart app");

                    // restart app
//                    Intent i = getContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);


                    isPhoneCalling = false;
//                    MISSEDcall()
                }else{
//                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                    try {
                        if (OcrCaptureActivity.type.equals("multiple") || multiTxt.length > 1 && (clickPosition != 5 && cardCount>0)) {
                            String[] NetworkCod = {"*555*", "*123*", "*126*", "*222*"};
                            String rechargCode;
                            rechargCode = NetworkCod[clickPosition];

                            if(dbrechargCode!=null || !dbrechargCode.isEmpty() || !dbrechargCode.equals("")){
                                rechargCode=dbrechargCode;
                            }

//                            for (int i = ni; i < multiTxt.length; i++) {
                            int lni=ni+1;
                            ii=lni;
                            if (!multiTxt[ii].isEmpty() || !multiTxt[ii].equals("")) {
                                txt = multiTxt[ii];
                                multiTxt[ii] = "";
                                ni=ii;

                                String number = rechargCode + txt + "#";
//                                Toast.makeText(getActivity(),"PhoneCall "+number,Toast.LENGTH_LONG).show();
                                rechargeCard(number);
                            }
//                            }
                        }
                    }catch (ArrayIndexOutOfBoundsException ar){}
                    catch (Exception er) {}
//                    Toast.makeText(getActivity(),"PhoneCall End",Toast.LENGTH_LONG).show();

                }

            }
        }
    }

    @Override
    public void onResume() {

            PhoneCallListener phoneListener = new PhoneCallListener();
            mTelephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
            mTelephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onResume();
    }
}


