package pro.gofman.mega;

import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FI0 = "TagFragmentItem0";
    private static final String DRAWLER_SELECTED = "DrawlerSelectedItem";


    protected static final int RESULT_SPEECH = 1;
    protected static final int REQUEST_CODE_PICK_CONTACT = 2;

    private Toolbar toolbar;
    public TextView textView;
    Drawer drawerResult = null;
    DrawerBuilder drawerBuilder = null;
    int DrawlerSelectedItem = -1;

    // protected static final

    protected static SQLiteDatabase db;
    FragmentTransaction ft;

    protected static WifiManager wifi;
    protected static String KEY_DEVICE = "";


    private Uri uriContact;
    private String contactID;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt( DRAWLER_SELECTED, (int)drawerResult.getCurrentSelection() );


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            DrawlerSelectedItem = savedInstanceState.getInt(DRAWLER_SELECTED);

        }



        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Подключаем базу данных
        db = new DatabaseHandler( this ).getWritableDatabase();

        // Включаем WIFI если выключен
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLED ) {
            wifi.setWifiEnabled(true);
        }
        // Узнаем IMEI
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        String number = telephonyManager.getLine1Number();
        KEY_DEVICE = GetDeviceId(this);

        Log.d("1", "Номер телефона: " + number);



        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if ( id == R.id.action_voice ) {
                    clickActionVoice();
                }
                if ( id == R.id.action_megadevice ) {
                    clickActionMegadevice();
                }
                if ( id == R.id.action_menu ) {
                    clickActionMenu();
                }
                if ( id == R.id.action_settings ) {
                    //
                }

                return true;
            }
        });
        //toolbar.setElevation(10);


        drawerBuilder = new DrawerBuilder(this);
        //drawerBuilder.withRootView(R.id.container);
        drawerBuilder.withToolbar(toolbar);
        //drawerBuilder.withDisplayBelowStatusBar(true);
        drawerBuilder.withActionBarDrawerToggleAnimated(true);


        List<Menu> lm = new Menu(db).getAllMenuItems();
        //Log.d("Menu", String.valueOf(lm.size()));

        // Заполняем Меню из базы данных
        for( int i=0; i < lm.size(); i++) {

            if ( lm.get(i).getType() == Menu.TYPE_MEGADEVICE ) {

                drawerBuilder.addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName( lm.get(i).getItem() )
                                .withIdentifier(lm.get(i).getID())
                                .withIcon( lm.get(i).getImg() )
                );

            } else if ( lm.get(i).getType() == Menu.TYPE_CALLBACK ) {
                drawerBuilder.addDrawerItems(
                        new SecondaryDrawerItem()
                                .withName( lm.get(i).getItem() )
                                .withIdentifier( lm.get(i).getID() )
                                .withSelectable( false )
                                .withIcon( lm.get(i).getImg() )


                );
            }
        }

        drawerBuilder.withSelectedItem(DrawlerSelectedItem);
        if (DrawlerSelectedItem > 0) {
            setTitleToolbar(DrawlerSelectedItem);
        }
        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                int id = (int)iDrawerItem.getIdentifier();
                setTitleToolbar( id );

                if ( iDrawerItem.isSelectable() ) {

                    Item7 fi7 = new Item7();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Item7.MENU_ID, id);
                    fi7.setArguments(bundle);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fi7)
                            .commit();
                } else {
                    // Пока только Callback не требует заполнения фрагмента
                    findContact();
                }
                return false;
            }
        });
        /*
        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {


                return false;
            }
        });
        */
        drawerResult = drawerBuilder.build();


        final Item0 rfi0 = (Item0) getFragmentManager().findFragmentByTag(TAG_FI0);

        if ( (rfi0 == null) & (DrawlerSelectedItem == -1) ) {

            Item0 fi0 = new Item0();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment, fi0, TAG_FI0)
                    .commit();

        }


        GetDeviceId(this);
    }
    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (drawerResult != null && drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void clickActionMegadevice(){
        Intent intent = new Intent(this, Settings_Megadevices.class);
        startActivity(intent);
    }

    public void clickActionVoice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ru-RU");

        try {

            startActivityForResult(intent, RESULT_SPEECH);
            // textView.setText("");

        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }
    public void clickActionMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    // textView.setText( text.get(0) );
                    parseVoiceAction( text.get(0) );
                }
                break;
            }
            case REQUEST_CODE_PICK_CONTACT: {
                if (resultCode == RESULT_OK && null != data) {
                    Log.d("1", "Response: " + data.toString());
                    uriContact = data.getData();

                    //retrieveContactName();
                    String[] numbers = null;
                    numbers = retrieveContactNumber();
                    if ( numbers != null ) {

                        PopupMenu m = new PopupMenu(this, this.toolbar);

                        for (int i=0; i < numbers.length; i++) {
                            m.getMenu().add( numbers[i] );
                        }

                        m.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Options op = new Options("callback_url", db);
                                StringBuilder sb = new StringBuilder();
                                sb.append(op.getValue());

                                sb.append("?key=" + KEY_DEVICE );
                                sb.append("&number=" + item.getTitle().toString() );
                                sb.append("&name=" + retrieveContactName() );

                                HttpClientRequest mr;
                                mr = new HttpClientRequest();

                                mr.execute(new RequestType(sb.toString()));
                                return false;
                            }
                        });

                        m.show();
                        // Log.d("1", "Номер: " + numbers[0]);
                    }

                    //retrieveContactPhoto();

                }
                break;
            }
        }
    }

    private String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString( cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) );
        }

        cursor.close();


        Log.d("1", "Contact Name: " + contactName);

        return contactName;

    }

    private String[] retrieveContactNumber() {

        String[] contactNumber = {};


        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d("1", "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                /* AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                */

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {

            contactNumber = new String[cursorPhone.getCount()];

            int i = 0;
            do {
                contactNumber[i] = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d("1", "Contact Phone Number: " + contactNumber[i] + " " + String.valueOf(i));
                i++;
            } while ( cursorPhone.moveToNext() );
        }

        cursorPhone.close();
        return contactNumber;
    }

    @Override
        protected void onDestroy() {

        db.close();
        super.onDestroy();

    }

    public void parseVoiceAction(String s) {

        Response res = null;

        String[][] cmds = {
                {"свет", "свят", "цвет"},       // 0
                {"двер","твер"}                 // 1

        };
        String[][] places = {
                {"дверьвходная"},               // 1
                {"коридор"},                    // 2
                {"зал", "гостин"},              // 3
                {"макс", "детск", "комнат" },   // 4
                {"спальн", "родител"},          // 5
                {"туалет", "ванна"},            // 6
                {"кухн", "столов"},             // 7
                {"везде", "всё", "все", "весь"} // 8
        };


        String[][] modifs = {
                {"выкл"},                       // 0
                {"вкл"},                        // 1
                {"закрыт","закро"},             // 2
                {"открыт","откро"}              // 3
        };


        // Список слов
        String[] voices = s.split(" ");

        // Команда
        int cmd = -1;

        // Модификатор
        int modif = -1;

        // Место
        int place = -1;

        for (int i = 0; i < voices.length; i++) {
            Log.d("Parse", "Voice: " + voices[i]);

            // Ищем комманду
            if ( cmd < 0 ) {
                // Перебираем возможные команды
                for (int j = 0; j < cmds.length; j++) {
                    for (int jj = 0; jj < cmds[j].length; jj++) {

                        Log.d("1", voices[i] + " " + "\"" + cmds[j][jj] + "(.*)" + "\""  + String.valueOf( ( voices[i].matches( "\"" + cmds[j][jj] + "(.*)" + "\"") | (voices[i].equalsIgnoreCase(cmds[j][jj]) ))  ));

                        if ( voices[i].indexOf(cmds[j][jj]) > -1 ) {
                            cmd = j+1;
                        }

                    }
                }

            }

            // Ищем место
            if ( place < 0 ) {
                // Перебираем возможные места
                for (int j = 0; j < places.length; j++) {
                    for (int jj = 0; jj < places[j].length; jj++) {

                        if ( voices[i].indexOf(places[j][jj]) > -1 ) {
                            place = j+1;
                        }

                    }
                }

            }

            // Ищем модификатор
            if ( modif < 0 ) {
                // Перебираем возможные команды
                for (int j = 0; j < modifs.length; j++) {
                    for (int jj = 0; jj < modifs[j].length; jj++) {

                        if ( voices[i].indexOf(modifs[j][jj]) > -1 ) {
                            modif = j;
                        }

                    }
                }
            }

        }

        // Команда на управление освещением
        if ( cmd == 1 && place > 0 ) {
            Log.d("Команда", "Команда: " + String.valueOf(cmd) + " Место: " + String.valueOf(place) + " Модификатор: " + String.valueOf(modif) );

            MegaDevicePoint lp = new MegaDevicePoint();
            lp.getPoint(db, place);

            if ( modif == -1 ) {
                LightRequest(lp, 2);
            } else if ( (modif == 0) | (modif == 1) ) {
                LightRequest(lp, modif );

            }
        }

        if ( cmd == 2 && ( modif == 2 || modif == 3 )) {
            MegaDevicePoint lc = new MegaDevicePoint();
            lc.getPoint(db, 1);

            Log.d("Дверь", lc.getName() + " " + String.valueOf(lc.getDev()) + " " + String.valueOf(lc.getPort()));

            LightRequest(lc, 2);
        }


        //
        // Log.d("Команда", "Команда: " + String.valueOf( cmd ) + " Место: " + String.valueOf(place) + " Модификатор: " + String.valueOf(modif) );
    }

    public void LightRequest(MegaDevicePoint lp, int action) {
        MegaDevices md;
        String url = "";


        md = new MegaDevices();

        // Получаем Мегадевайс
        md.getMegadevice( db, lp.getDev() );

        // Формируем запрос
        url = "http://" + md.getIPAddr() + "/" + md.getPassword() + "/?pt=" + String.valueOf( lp.getPort() ) + "&cmd=" + String.valueOf( lp.getPort() ) + ":" + String.valueOf(action);
        Log.d("Запрос", url);


        HttpClientRequest mr;
        mr = new HttpClientRequest();

        if ( lp.getType() == MegaDevicePoint.TYPE_LIGHT ) {
            mr.execute(new RequestType(url));
        } else if ( lp.getType() == MegaDevicePoint.TYPE_LOCK ) {
            // Выполняем два запроса, второй с задержкой 1,5 секунды
            mr.execute(new RequestType(url), new RequestType(url, 1000));
        }

        try {

            String res = mr.get();

            if ( md.getVersion().equals("3.02") ) {
                //String res = mr.get();
                // Log.d("Результат", String.valueOf(res.indexOf("Port:")));

                int p1 = res.indexOf("Port:");
                int p2 = res.indexOf("<br>", p1);
                String p3 = res.substring(p1 + 5, p2);
                String[] p4 = p3.split("/");

                Log.d("Статус", p4[1]);

                if (p4[1].equals(MegaDevicePoint.STATUS_ON)) {
                /*
                    Надо найти элемент в интерфейсе сделать его активным

                */

                } else if (p4[1].equals(MegaDevicePoint.STATUS_OFF)) {
                /*
                    Надо найти элемент в интерфейсе сделать его неактивным

                */
                }
            } // Version

            if ( md.getVersion().equals("3.34b7") ) {
                String ss = "Back</a><br>";

                int p1 = res.indexOf( ss );
                int p2 = res.indexOf("<br>", p1 + ss.length() );
                String p3 = res.substring(p1 + ss.length(), p2);
                String[] p4 = p3.split("/");

                Log.d("Статус", p4[1]);

                if (p4[1].equals(MegaDevicePoint.STATUS_ON)) {
                    /*
                     Надо найти элемент в интерфейсе сделать его активным

                    */

                } else if (p4[1].equals(MegaDevicePoint.STATUS_OFF)) {
                    /*
                    Надо найти элемент в интерфейсе сделать его неактивным

                    */
                }
            }

            // textView.setText( mr.get() );
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    public static String GetDeviceId(Context context)
    {
        // Custom String Hash 1
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("marie"); // Not really needed, but means the stringBuilders value won't ever be null

        // TM Device String
        final TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDeviceId = tm.getDeviceId(); // Could well be set to null!
        Log.d("0", "TM Device String [" + tmDeviceId + "]");

        // Custom String Hash 2
        stringBuilder.append(tmDeviceId);
        long customHash = stringBuilder.toString().hashCode();
        Log.d("0", "Custom String hash [" + customHash + "]");

        // Device ID String
        String androidIDString = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        Log.d("0", "Device ID String [" + androidIDString + "]");

        // Combined hashes as GUID
        UUID deviceUuid = new UUID(androidIDString.hashCode(), (customHash << 32));
        Log.d("0", "Combined hashes as GUID [" + deviceUuid.toString() + "]");

        return deviceUuid.toString();
    }

    // Открытие списка контактов
    public void findContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACT);

    }

    // Получение сети WIFI
    public static String getSSID(WifiManager wifi) {
        String bssid = null;

        if ( wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED ) {

            final WifiInfo wifiInfo = wifi.getConnectionInfo();
            if ( wifiInfo != null) {
                bssid = wifiInfo.getSSID();
            }
        }
        if ( bssid == null ) bssid = "none";
        return bssid;
    }

    // Проверка домашней WIFI
    public static boolean isAllowedWifi() {
        boolean r = false;
        Options op = new Options("wifi_allowed", db);

        Log.d("1", op.getValue() + " - " + getSSID(wifi) );

        if (  op.getValue() != null ) {


            if (op.getValue().equals( getSSID(wifi).replace("\"", "") ) ) {
                r = true;
            }
            Log.d("1", op.getValue() + " - " + String.valueOf(r));
            //r = getSSID(wifi).indexOf(op.getValue(), 1) > 0;

        }
        return r;
    }

    void setTitleToolbar(int pos) {
        Menu mn = new Menu(db, pos);

        // Меняем заголовок тулбара только там где заполняется фрагмент
        if ( mn.getType() == Menu.TYPE_MEGADEVICE ) {
            toolbar.setTitle(mn.getItem());
        }
    }

    String[][] fillWords() {
        String[][] r = null;

        int i = 0;
        int j = 0;

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_WORDS + " ORDER BY " + DatabaseHandler.KEY_WORDS_IDELEMENT, null);
        if ( c.moveToFirst() ) {
            do {
                if ( i == c.getInt(c.getColumnIndex(DatabaseHandler.KEY_WORDS_IDELEMENT))-1) {
                    j++;
                } else {
                    i = c.getInt(c.getColumnIndex(DatabaseHandler.KEY_WORDS_IDELEMENT))-1;
                    j = 0;
                }

                r[i][j] = c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM));


            } while ( c.moveToNext() );

            c.close();
        }

        return r;
    }

       /*
    class OptionsButtonMenuListener implements View.OnClickListener {
        private MegaDevices md;

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
        private void showPopupMenu(View v) {
            PopupMenu m = new PopupMenu(v.getContext(), v);

            m.inflate(R.menu.menu_card_megadevice);

            PopupItemClickListener popupItemClickListener;
            popupItemClickListener = new PopupItemClickListener(v.getContext());
            popupItemClickListener.setMd(this.md);
            m.setOnMenuItemClickListener(popupItemClickListener);

            m.show();
        }
        public void setMd(MegaDevices md) {
            this.md = md;
        }

    }
    class PopupItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private MegaDevices md;
        private Context context;

        public PopupItemClickListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (item.getItemId() == R.id.edit_megadevice ) {

                //Toast t = Toast.makeText(this.context, this.md.getName(), Toast.LENGTH_SHORT);
                //t.show();

                Intent intent = new Intent(this.context, MegaDeviceEdit.class);
                intent.putExtra( MegaDeviceEdit.mdID, this.md.getID() );

                this.context.startActivity(intent);


            }
            return false;
        }


        public void setMd(MegaDevices md) {
            this.md = md;
        }
    }

    */




}
