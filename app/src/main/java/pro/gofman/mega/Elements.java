package pro.gofman.mega;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Роман on 20.08.2015.
 */
public class Elements {
    protected static final int TYPE_LIGHT = 0;
    protected static final int TYPE_LOCK = 1;
    protected static final int TYPE_OUTLET = 2;
    protected static final String[] TYPE_ARRAY = {
        "Освещение",
        "Замок",
        "Розетки"
    };


    protected static final String STATUS_ON = "ON";
    protected static final String STATUS_OFF = "OFF";

    protected static final String ELID = "ElID";
    protected static final String ELPOS = "ElPos";


    // Идетификатор Элемента управления
    private int ID = 0;
    // Наименование элемента управления
    private String Item = "";
    // Описание элемента управления
    private String Desc = "";
    // Принадлежность элемента управления к пункту Меню
    private int MenuID = 0;
    private String MenuItem = "";
    // Принадлежность элемента управления к Мегадевайсу
    private int DevID = 0;
    // Порт в указанном Мегадевайсе
    private int Port = 0;
    // Тип элемента управления
    private int Type = -1;

    private SQLiteDatabase db = null;

    public Elements() {

    }
    public Elements(SQLiteDatabase db, int id) {
        this.db = db;
        this.ID = id;
    }
    public Elements(SQLiteDatabase db) {
        this.db = db;
    }
    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    public void setID(int id) {
        this.ID = id;
    }
    public int getID() {
        return this.ID;
    }
    public void setItem(String item) {
        this.Item = item;
    }
    public String getItem() {
        return this.Item;
    }
    public void setDesc(String desc) {
        this.Desc = desc;
    }
    public String getDesc() {
        return this.Desc;
    }
    public void setMenuID(int menu){
        this.MenuID = menu;
    }
    public int getMenuID() {
        return this.MenuID;
    }

    public void setMenuItem(String menu){
        this.MenuItem = menu;
    }
    public String getMenuItem() {
        return this.MenuItem;
    }

    public void setDevID(int dev){
        this.DevID = dev;
    }


    public int getDevID() {
        return this.DevID;
    }
    public MegaDevices getDev() {
        MegaDevices md = new MegaDevices();
        md.getMegadevice(this.db, this.DevID );
        return md;
    }
    public void setPort(int port){
        this.Port = port;
    }
    public int getPort() {
        return this.Port;
    }
    public void setType(int type){
        this.Type = type;
    }
    public int getType() {
        return this.Type;
    }

    public List<Elements> getAllElements() {
        List<Elements> le =  new ArrayList<Elements>();

        Cursor c = this.db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_ELEMENTS, null);
        if ( c.moveToFirst() ) {
            do {

                Elements el = new Elements();
                el.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IDROW)) );
                el.setItem(c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)));
                el.setDesc(c.getString(c.getColumnIndex(DatabaseHandler.KEY_DESC)));
                el.setDevID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_DEV)) );
                el.setPort(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_PORT)) );
                el.setType(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_TYPE)) );
                el.setMenuID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_IDMENU)) );

                le.add(el);

            } while ( c.moveToNext() );

            c.close();
        }

        return le;
    }

    public List<Elements> getElementsByMenuItem(int menu) {
        List<Elements> le =  new ArrayList<Elements>();

        Cursor c = this.db.rawQuery(
                "SELECT * FROM " + DatabaseHandler.TABLE_ELEMENTS
                        + " WHERE " + DatabaseHandler.KEY_ELEMENTS_IDMENU
                        + " = " + String.valueOf(menu),
                null
        );
        //Log.d("Create", " Кол-во элементов: " + String.valueOf(c.getCount()) );

        if ( c.moveToFirst() ) {
            do {

                //Log.d("Create", "getElementsByMenuItem " + c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)));
                Elements el = new Elements();
                el.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IDROW)));
                el.setItem(c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)));
                el.setDesc(c.getString(c.getColumnIndex(DatabaseHandler.KEY_DESC)));
                el.setDevID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_DEV)));
                el.setPort(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_PORT)));
                el.setType(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_TYPE)));
                el.setMenuID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_IDMENU)));

                le.add(el);

            } while ( c.moveToNext() );

            c.close();
        }

        return le;
    }
    public List<Elements> getElementsByDevID(int devID) {
        List<Elements> le =  new ArrayList<Elements>();

        Cursor c = this.db.rawQuery(
                "SELECT " + DatabaseHandler.TABLE_ELEMENTS + ".*, " + DatabaseHandler.TABLE_MENU + "." + DatabaseHandler.KEY_ITEM + " as MENU" + " FROM " + DatabaseHandler.TABLE_ELEMENTS
                        + " LEFT OUTER JOIN " + DatabaseHandler.TABLE_MENU
                        + " ON " + DatabaseHandler.TABLE_ELEMENTS + "." + DatabaseHandler.KEY_ELEMENTS_IDMENU
                        + " = " + DatabaseHandler.TABLE_MENU + "." + DatabaseHandler.KEY_IDROW
                        + " WHERE " + DatabaseHandler.KEY_ELEMENTS_DEV
                        + " = " + String.valueOf(devID),
                null
        );
        Log.d("getElementsByDevID", " Кол-во элементов: " + String.valueOf( c.getCount() ) + " DevID: " + String.valueOf(devID));

        if ( c.moveToFirst() ) {
            do {

                //Log.d("Create", "getElementsByDevID " + c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)));
                Elements el = new Elements();
                el.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IDROW)) );
                el.setItem(c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)));
                el.setDesc(c.getString(c.getColumnIndex(DatabaseHandler.KEY_DESC)));
                el.setDevID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_DEV)));
                el.setPort(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_PORT)));
                el.setType(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_TYPE)));
                el.setMenuID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_IDMENU)));
                el.setMenuItem(c.getString(c.getColumnIndex("MENU")));

                le.add(el);

            } while ( c.moveToNext() );

            c.close();
        }

        return le;
    }

    // Заполняет себя самого из базы данных
    public void getElement() {
        if (this.ID < 1) {
            return;
        }

        if ( this.db.isOpen() ) {
            /*
            String sql = "SELECT * FROM " + DatabaseHandler.TABLE_ELEMENTS
                    + " WHERE " + DatabaseHandler.KEY_IDROW + " = " + String.valueOf( this.ID );
            */

            String sql = "SELECT " + DatabaseHandler.TABLE_ELEMENTS + ".*, " + DatabaseHandler.TABLE_MENU + "." + DatabaseHandler.KEY_ITEM + " as MENU" + " FROM " + DatabaseHandler.TABLE_ELEMENTS
                    + " LEFT OUTER JOIN " + DatabaseHandler.TABLE_MENU
                    + " ON " + DatabaseHandler.TABLE_ELEMENTS + "." + DatabaseHandler.KEY_ELEMENTS_IDMENU
                    + " = " + DatabaseHandler.TABLE_MENU + "." + DatabaseHandler.KEY_IDROW
                    + " WHERE " + DatabaseHandler.TABLE_ELEMENTS + "." + DatabaseHandler.KEY_IDROW
                    + " = " + String.valueOf( this.ID );

            Cursor c = this.db.rawQuery(sql, null);

            if (c != null) {

                c.moveToFirst();

                this.setItem(c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)));
                this.setDesc(c.getString(c.getColumnIndex(DatabaseHandler.KEY_DESC)));
                this.setMenuID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_IDMENU)));
                this.setDevID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_DEV)));
                this.setPort(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_PORT)));
                this.setType(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_TYPE)));
                this.setMenuItem(c.getString(c.getColumnIndex("MENU")));

                c.close();

            }
        }
    }
    public void addItem() {
        if ( this.db.isOpen() ) {
            ContentValues cv = new ContentValues();

            cv.put( DatabaseHandler.KEY_ITEM, this.Item );
            cv.put( DatabaseHandler.KEY_DESC, this.Desc );
            cv.put( DatabaseHandler.KEY_ELEMENTS_DEV, this.DevID );
            cv.put( DatabaseHandler.KEY_ELEMENTS_IDMENU, this.MenuID );
            cv.put( DatabaseHandler.KEY_ELEMENTS_PORT, this.Port );
            cv.put( DatabaseHandler.KEY_ELEMENTS_TYPE, this.Type );

            this.ID = (int) db.insert( DatabaseHandler.TABLE_ELEMENTS, null, cv );
        }
    }

    // Изменяет данные в базе
    public int updateElement(  ) {
        int r = 0;

        if ( this.db == null ) {
            return r;
        }
        if ( this.ID < 1 ) {
            return r;
        }


        if ( this.db.isOpen() ) {
            ContentValues cv = new ContentValues();

            cv.put( DatabaseHandler.KEY_ITEM, this.Item );
            cv.put( DatabaseHandler.KEY_DESC, this.Desc );
            cv.put( DatabaseHandler.KEY_ELEMENTS_IDMENU, this.MenuID );
            cv.put( DatabaseHandler.KEY_ELEMENTS_PORT, this.Port );
            cv.put( DatabaseHandler.KEY_ELEMENTS_TYPE, this.Type );

            r = db.update(
                    DatabaseHandler.TABLE_ELEMENTS,
                    cv,
                    DatabaseHandler.KEY_IDROW + " =? ",
                    new String[] { String.valueOf( this.ID ) }
            );
        }
        return r;
    }

    // Удаляет объект из базы данных
    public void deleteElement() {
        if (this.db == null) {
            return;
        }
        if ( !this.db.isOpen() ) {
            return;
        }
        this.db.delete(
                DatabaseHandler.TABLE_ELEMENTS,
                DatabaseHandler.KEY_IDROW + " =? ",
                new String[] { String.valueOf( this.ID ) }
        );


    }

}
