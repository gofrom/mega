package pro.gofman.mega;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;


public class Settings_Megadevices extends AppCompatActivity {

    protected static final int EDIT_MEGADEVICE = 1;

    private RecyclerView rvMegadevices;
    private MegadevicesAdapter adMegadevices;
    private RecyclerView.LayoutManager lmMegadevices;
    //SwipeToDismissTouchListener swipeToDismissTouchListener;


    private MegaDevices md;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Settings_Megadevices.EDIT_MEGADEVICE: {
                if (resultCode == RESULT_OK && null != data) {

                    int save = data.getIntExtra( MegaDeviceEdit.SAVE_MEGADEVICE, 0 );
                    int pos = data.getIntExtra(MegaDevices.DEVPOS, 0);
                    int id = data.getIntExtra(MegaDevices.DEVID, 0);

                    Log.d("onActivityResult", String.valueOf(save) + " : " + String.valueOf(pos));

                    md.getMegadevice(MainActivity.db, id);
                    adMegadevices.updateItem(pos, md);

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_megadevices);

        md = new MegaDevices();

        rvMegadevices = (RecyclerView) findViewById(R.id.rv_megadevices);
        //rvMegadevices.setHasFixedSize(true);

        lmMegadevices = new LinearLayoutManager(this);
        rvMegadevices.setLayoutManager(lmMegadevices);

        adMegadevices = new MegadevicesAdapter( md.getAllMegadevices( MainActivity.db )  );
        adMegadevices.setmActivity(this);
        rvMegadevices.setAdapter( adMegadevices );

        //rvMegadevices.
        /*
        swipeToDismissTouchListener = new SwipeToDismissTouchListener(rvMegadevices, new SwipeToDismissTouchListener.DismissCallbacks() {
            @Override
            public SwipeToDismissTouchListener.SwipeDirection canDismiss(int position) {
                return SwipeToDismissTouchListener.SwipeDirection.LEFT;
            }

            @Override
            public void onDismiss(RecyclerView view, List<SwipeToDismissTouchListener.PendingDismissData> dismissData) {
                for (SwipeToDismissTouchListener.PendingDismissData data: dismissData) {
                    adMegadevices.removeItem(MainActivity.db, data.position);
                    adMegadevices.notifyItemRemoved(data.position);
                }
            }
        });

        rvMegadevices.addOnItemTouchListener(swipeToDismissTouchListener);
        */

        /*rvMegadevices.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(rvMegadevices);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddMegadevice();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings_megadevice);
         /*
        toolbar.inflateMenu(R.menu.menu_settings_megadevices);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.add_megadevice) {
                    clickAddMegadevice();
                }
                if (id == R.id.del_megadevice) {
                    clickDelMegadevice();
                }


                return true;
            }
        });
        */
    }
    public void clickAddMegadevice() {
        MegaDevices md = new MegaDevices("Новое устройство", "192.168.1.250", "sec");
        md.addMegadevices(MainActivity.db);

        int pos = adMegadevices.getItemCount();

        adMegadevices.addItem( pos, md );


    }
    public void clickDelMegadevice() {

    }
}
