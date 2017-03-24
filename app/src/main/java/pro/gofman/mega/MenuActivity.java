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

public class MenuActivity extends AppCompatActivity {

    protected static final int EDIT_MENU = 1;

    pro.gofman.toolbar.Menu mn;
    private RecyclerView rvMenu;
    private MenuAdapter adMenu;
    private RecyclerView.LayoutManager lmMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        rvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        lmMenu = new LinearLayoutManager(this);
        rvMenu.setLayoutManager(lmMenu);


        mn = new pro.gofman.toolbar.Menu(MainActivity.db);



        adMenu = new MenuAdapter( mn.getAllMenuItems() );
        adMenu.setActivity(this);
        rvMenu.setAdapter(adMenu);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_menu);
        fab.attachToRecyclerView(rvMenu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddMenu();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Меню");

    }


    private void clickAddMenu() {
        mn.setType( pro.gofman.toolbar.Menu.TYPE_MEGADEVICE );
        mn.setItem(getResources().getString(R.string.new_menu));

        mn.addItem();
        adMenu.addItem( adMenu.getItemCount(), mn );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_MENU: {
                if (resultCode == RESULT_OK && null != data) {

                    int save = data.getIntExtra(MenuEditActivity.SAVE_MENU, 0);
                    int pos = data.getIntExtra(pro.gofman.toolbar.Menu.MENUPOS, 0);
                    int id = data.getIntExtra(pro.gofman.toolbar.Menu.MENUID, 0);
                    Log.d("onActivityResult", String.valueOf(save) + " : " + String.valueOf(pos));

                    mn.setID(id);
                    mn.getMenu();

                    adMenu.updateItem(pos, mn);

                }
            }
        }
    }
}
