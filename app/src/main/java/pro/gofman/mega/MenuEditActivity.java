package pro.gofman.mega;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.Button;
import com.rey.material.widget.Button;
// import android.widget.EditText;
import com.rey.material.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.rey.material.widget.Spinner;

public class MenuEditActivity extends AppCompatActivity {

    protected static final String SAVE_MENU = "SaveMenu";

    int icon_array[] = {
            R.drawable.ic_exit_to_app,  R.drawable.ic_hotel, R.drawable.ic_lock, R.drawable.ic_human_child, R.drawable.ic_silverware_spoon,
            R.drawable.ic_phone, R.drawable.ic_sofa, R.drawable.ic_human_male_female
    };

    String[] icon_name = {
            "ic_exit_to_app", "ic_hotel", "ic_lock", "ic_human_child", "ic_silverware_spoon", "ic_phone", "ic_sofa", "ic_human_male_female"
    };

    String[] menu_type = {
            "Неопределено", "Устройство", "Заказ звонка"
    };

    private pro.gofman.toolbar.Menu mn = null;
    private int pos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);

        Intent intent = getIntent();
        pos = intent.getIntExtra( pro.gofman.toolbar.Menu.MENUPOS, 0 );
        mn = new pro.gofman.toolbar.Menu(MainActivity.db, intent.getIntExtra( pro.gofman.toolbar.Menu.MENUID, 0));

        final EditText et_item = (EditText) findViewById(R.id.et_menu_item);
        et_item.applyStyle(R.style.LightEditText);
        et_item.setText(mn.getItem());

        final Spinner sp_pic = (Spinner) findViewById(R.id.sp_menu_pic);
        sp_pic.applyStyle(R.style.LightSpinner);
        sp_pic.setAdapter(new PicturesAdapter(this, R.layout.row_spinner_pictures, icon_name));
        sp_pic.setSelection(findPositionByImg(mn.getImg()));


        final Spinner sp_type = (Spinner) findViewById(R.id.sp_menu_type);
        sp_type.applyStyle(R.style.LightSpinner);
        sp_type.setAdapter(new ArrayAdapter<String>(this, R.layout.row_spinner, menu_type));
        sp_type.setSelection(mn.getType());

        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.applyStyle(R.style.LightFlatButtonRippleStyle);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mn.setItem( et_item.getText().toString() );
                mn.setType(sp_type.getSelectedItemPosition());
                mn.setImg( findImgbyPosition( sp_pic.getSelectedItemPosition() ) );

                if ( mn.getID() > 0 ) {
                    mn.updateMenu();
                } else {
                    // Добавить пункт меню
                }
                closeActivity(1);
            }
        });

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.applyStyle(R.style.LightFlatButtonRippleStyle);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class PicturesAdapter extends ArrayAdapter<String> {



        public PicturesAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }
        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }
        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.row_spinner_pictures, parent, false);

            TextView tv_icon_name = (TextView) mySpinner.findViewById(R.id.row_icon_name);
            tv_icon_name.setText( icon_name[position] );


            ImageView iv_icon = (ImageView) mySpinner.findViewById(R.id.row_icon);
            iv_icon.setImageResource( icon_array[position]);

            return mySpinner;
        }

    }
    private int findPositionByImg(int img) {
        int r = 0;

        for (int i = 0; i < icon_array.length; i++) {
            if ( icon_array[i] == img ) {
                return i;
            }
        }

        return r;
    }

    private int findImgbyPosition(int pos) {
        int r = 0;

        if ( pos < 0 ) {
            return r;
        }
        if ( pos >= icon_array.length ) {
            return r;
        }

        r = icon_array[pos];

        return r;
    }
    private void closeActivity(int r) {
        if ( r == 1 ) {
            Log.d("closeActivity", "pos: " + String.valueOf(pos) );
            Intent intent = new Intent();

            intent.putExtra(SAVE_MENU, r);
            intent.putExtra(pro.gofman.toolbar.Menu.MENUPOS, pos);
            intent.putExtra(pro.gofman.toolbar.Menu.MENUID, mn.getID());

            setResult(RESULT_OK, intent);
        }

        this.finish();
    }

}
