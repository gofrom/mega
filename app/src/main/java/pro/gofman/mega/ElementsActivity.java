package pro.gofman.mega;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.melnykov.fab.FloatingActionButton;


public class ElementsActivity extends AppCompatActivity {

    protected static final int EDIT_ELEMENT = 1;

    private int devid = 0;
    private Elements el;
    private RecyclerView rvElements;
    private ElementsAdapter adElements;
    private RecyclerView.LayoutManager lmElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        rvElements = (RecyclerView) findViewById(R.id.rv_elements);
        lmElements = new LinearLayoutManager(this);
        rvElements.setLayoutManager(lmElements);

        el = new Elements(MainActivity.db);
        devid = getIntent().getIntExtra( MegaDevices.DEVID, 0 );


        adElements = new ElementsAdapter( el.getElementsByDevID( devid ) );
        adElements.setActivity( this );
                //MegadevicesAdapter( md.getAllMegadevices( MainActivity.db )  );
        rvElements.setAdapter(adElements);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_elements);
        fab.attachToRecyclerView(rvElements);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddElements();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Элементы");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case EDIT_ELEMENT: {
                if (resultCode == RESULT_OK && null != data) {

                    int save = data.getIntExtra( ElementEditActivity.SAVE_ELEMENT, 0 );
                    int pos = data.getIntExtra(Elements.ELPOS, 0);
                    int id = data.getIntExtra(Elements.ELID, 0);
                    Log.d("onActivityResult", String.valueOf( save ) + " : " + String.valueOf(pos) );

                    el.setID( id );
                    el.getElement();

                    adElements.updateItem( pos, el );

                    /*
                    Elements el = new Elements(MainActivity.db, this.elid);
                    el.getElement();
                    updateItem( pos, el );
                    */


                }

            }
        }

    }


    private void clickAddElements() {
        // Log.d("clickAddElements", "Устройство: " + String.valueOf(devid) );
        el.setItem(getResources().getString(R.string.new_element) );
        el.setDevID(devid);
        el.setMenuID( 1 );
        el.setPort(1);
        el.setDesc(getResources().getString(R.string.new_element_desc) );
        el.setType(Elements.TYPE_LIGHT);
        el.setMenuItem("");

        el.addItem();
        adElements.addItem( adElements.getItemCount(), el );


        Log.d("clickAddElements", "Устройство: " + String.valueOf(devid) + " Добавлено: " + String.valueOf(el.getID()) + " " + getResources().getString(R.string.new_element));
    }
}
