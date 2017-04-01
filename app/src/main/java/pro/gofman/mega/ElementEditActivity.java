package pro.gofman.mega;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;


import com.rey.material.widget.Spinner;

public class ElementEditActivity extends AppCompatActivity {

    protected static final String SAVE_ELEMENT = "SaveElement";

    private Elements el;
    private int pos = 0;

    private EditText mElItem;
    private EditText mElDesc;
    private EditText mElPort;
    private Button btn_ok;
    private Button btn_cancel;
    private Spinner mElType;
    private Spinner mElMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_edit);


        Intent intent = getIntent();
        pos = intent.getIntExtra( Elements.ELPOS, 0 );
        el = new Elements(MainActivity.db, intent.getIntExtra( Elements.ELID, 0));
        el.getElement();


        mElItem = (EditText) findViewById(R.id.et_element_item);
        mElItem.applyStyle(R.style.LightEditText);
        mElDesc = (EditText) findViewById(R.id.et_element_desc);
        mElDesc.applyStyle(R.style.LightEditText);
        mElPort = (EditText) findViewById(R.id.et_element_port);
        mElPort.applyStyle(R.style.LightEditText);
        mElType = (Spinner) findViewById(R.id.sp_element);
        mElType.applyStyle(R.style.LightEditText);
        mElMenu = (Spinner) findViewById(R.id.sp_element_menu);
        mElMenu.applyStyle(R.style.LightEditText);

        btn_ok = (Button) findViewById(R.id.btn_element_ok);
        btn_ok.applyStyle(R.style.LightFlatButtonRippleStyle);
        btn_cancel = (Button) findViewById(R.id.btn_element_cancel);
        btn_cancel.applyStyle(R.style.LightFlatButtonRippleStyle);


        mElItem.setText(el.getItem());
        mElDesc.setText(el.getDesc());
        mElPort.setText(String.valueOf(el.getPort()));

        ArrayAdapter<String> adElType = new ArrayAdapter<String>(this, R.layout.row_spinner, Elements.TYPE_ARRAY);
        adElType.setDropDownViewResource(R.layout.row_spinner_dropdown);

        mElType.applyStyle(R.style.LightSpinner);
        mElType.setSelection(el.getType());
        mElType.setAdapter(adElType);


        ArrayAdapter<String> adElMenu = new ArrayAdapter<String>(this, R.layout.row_spinner, new pro.gofman.mega.Menu(MainActivity.db).getMenuItemsByType( pro.gofman.mega.Menu.TYPE_MEGADEVICE ));
        mElMenu.setAdapter(adElMenu);
        mElMenu.applyStyle(R.style.LightSpinner);
        mElMenu.setSelection( el.getMenuID()-1 );



        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                el.setItem(mElItem.getText().toString());
                el.setDesc(mElDesc.getText().toString());
                el.setPort(Integer.valueOf(mElPort.getText().toString()));
                el.setType(mElType.getSelectedItemPosition());
                el.setMenuID( mElMenu.getSelectedItemPosition()+1 );
                Log.d("setOnClickListener", String.valueOf(mElMenu.getSelectedItemPosition()+1));



                if (el.getID() > 0) {
                    el.updateElement();
                } else {
                    // el.add
                }
                closeActivity(1);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(0);
            }
        });




        /*

        Spinner spinner = (Spinner) findViewById(R.id.sp_element);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.element_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        */

    }

    public void closeActivity(int r) {
        if ( r == 1 ) {
            Intent intent = new Intent();

            intent.putExtra(SAVE_ELEMENT, r);
            intent.putExtra(Elements.ELPOS, pos);
            intent.putExtra(Elements.ELID, el.getID() );

            setResult(RESULT_OK, intent);
        }
        this.finish();
    }




}
