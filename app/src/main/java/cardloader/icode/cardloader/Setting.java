package cardloader.icode.cardloader;

/**
 * Created by Icode on 11/9/2017.
 */


import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends AppCompatActivity {
    boolean boolVal;
    TextView abtRemote;
    String networkCode,networkName;
    String[] settingArray = {"Network 1","Network 2","Network 3","Network 4"};
   private DBHelper dbhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelp=new DBHelper(this);
        setContentView(R.layout.setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("  Settings");
        // toolbar.setBackground(new ColorDrawable(Color.parseColor("#0000ff")));
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.listview, settingArray);

        ListView listView = (ListView) findViewById(R.id.setting_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                popEdit(item);
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void popEdit(final String network){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);

//text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.setting_edit_text, null);

        final EditText inputName = (EditText) textEntryView.findViewById(R.id.edit_name);
        final EditText inputCode = (EditText) textEntryView.findViewById(R.id.edit_code);


        alert.setTitle(network);
// Set an EditText view to get user input

//        final EditText inputName = new EditText(this);
        try {
            String networkCN= dbhelp.getByNetwork(network);
            networkName=networkCN.split("-")[0];
            networkCode=networkCN.split("-")[1];
            inputCode.setText(networkCode);
            inputName.setText(networkName);
            Toast.makeText(Setting.this,network+"- "+networkCode,Toast.LENGTH_LONG).show();
        }catch (Exception er){
            er.printStackTrace();
//            Toast.makeText(Setting.this,"setting failed , try again",Toast.LENGTH_LONG).show();
            Log.d("TAG INPUT",er.toString());
        }
        alert.setView(textEntryView);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
             networkCode= inputCode.getText().toString();
                networkName= inputName.getText().toString();
                try{
                    if(dbhelp.numberOfRowsByNetwork(network)==0) {
                        dbhelp.insertNetwork(network,networkName ,networkCode);
                    }else{
                        dbhelp.updateNetwork(network,networkName,networkCode);
                    }

                }catch (Exception er){
                    Toast.makeText(Setting.this,"Error occured, try again ",Toast.LENGTH_LONG).show();
                }
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}
