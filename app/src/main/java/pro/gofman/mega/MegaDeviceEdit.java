package pro.gofman.mega;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Button;


public class MegaDeviceEdit extends AppCompatActivity {

    private MegaDevices md;
    private int pos = 0;
    private EditText mdItem;
    private EditText mdDesc;
    private EditText mdIPAddr;
    private EditText mdPort;
    private EditText mdPdw;
    private Button btnOK;
    private Button btnCancel;

    public static String mdID = "MegaDeviceID";
    protected static final String SAVE_MEGADEVICE = "SaveMegadevice";

    @Override
    protected void onResume() {
        Log.d("et_megadevice_item", "2");
        super.onResume();
        Log.d("et_megadevice_item", "3");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("et_megadevice_item", "1");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megadevice_edit);

        mdItem = (EditText) findViewById(R.id.et_megadevice_item);
        mdItem.applyStyle(R.style.LightEditText);
        mdDesc = (EditText) findViewById(R.id.et_megadevice_desc);
        mdDesc.applyStyle(R.style.LightEditText);
        mdIPAddr = (EditText) findViewById(R.id.et_megadevice_ipaddr);
        mdIPAddr.applyStyle(R.style.LightEditText);
        mdPort = (EditText) findViewById(R.id.et_megadevice_port);
        mdPort.applyStyle(R.style.LightEditText);
        mdPdw = (EditText) findViewById(R.id.et_megadevice_pwd);
        mdPdw.applyStyle(R.style.LightEditText);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.applyStyle(R.style.LightFlatButtonRippleStyle);
        btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.applyStyle(R.style.LightFlatButtonRippleStyle);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(0);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                md.setName( mdItem.getText().toString() );
                md.setDesc( mdDesc.getText().toString() );
                md.setIPAddr( mdIPAddr.getText().toString() );
                md.setPassword( mdPdw.getText().toString() );
                md.setPort( Integer.valueOf( mdPort.getText().toString() ) );


                if ( md.getID() > 0 ) {
                    md.updateMegadevice(MainActivity.db);
                } else {
                    md.addMegadevices(MainActivity.db);
                }

                closeActivity(1);
            }
        });


        md = new MegaDevices();

        Intent intent = getIntent();
        md.setID(intent.getIntExtra(mdID, 0));
        pos = intent.getIntExtra( MegaDevices.DEVPOS, 0);


        if ( md.getID() > 0 ) {
            md.getMegadevice( MainActivity.db, md.getID() );

            mdIPAddr.setText(md.getIPAddr());
            mdPort.setText( String.valueOf(md.getPort()) );
            mdPdw.setText(md.getPassword());
            mdItem.setText( md.getName() );
            mdDesc.setText( md.getDesc() );



        } else {
            mdItem.setText("привет");
        }



    }
    public void closeActivity(int r){
        if (r == 1) {
            Intent intent = new Intent();
            intent.putExtra( MegaDevices.DEVID, md.getID() );
            intent.putExtra( MegaDevices.DEVPOS, pos );

            setResult(RESULT_OK, intent);


        }
        this.finish();
    }
}
