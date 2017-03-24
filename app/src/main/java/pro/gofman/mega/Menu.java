package pro.gofman.mega;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gofman on 20.08.15.
 */
public class Menu {
    protected static final int TYPE_UNKNOWN = 0;
    protected static final int TYPE_MEGADEVICE = 1;
    protected static final int TYPE_CALLBACK = 2;

    protected static final String MENUID = "MenuID";
    protected static final String MENUPOS = "MenuPos";


    // Наименование пункта Меню
    private String Item = "";

    // Идентификатор пункта Меню
    private int ID = 0;

    // Группа пункта Меню
    private int Type = 0;

    // Картинка пункта меню
    private int Img = 0;

    private SQLiteDatabase db = null;




    private void read() {
        // Выходим если не открыта DB
        if ( !this.db.isOpen() ) {
            return;
        }
        // Выходим если ID < 1
        if ( !(this.ID > 0) ) {
            return;
        }


        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_MENU + " WHERE "
                + DatabaseHandler.KEY_IDROW + "=" + String.valueOf(this.ID);
        Cursor c = this.db.rawQuery(sql, null);
        if ( c.moveToFirst() ) {
            this.Item = c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM));
            this.Type = c.getInt(c.getColumnIndex(DatabaseHandler.KEY_MENU_TYPE));
            this.Img = c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IMG));

        }
    }
    public Menu() {

    }
    public Menu(SQLiteDatabase db) {
        this.db = db;
    }
    public Menu(SQLiteDatabase db, int id) {
        this.db = db;
        this.ID = id;

        this.read();
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
    public String getItem(){
        return this.Item;
    }
    public void setItem(String Item){
        this.Item = Item;
    }
    public int getType() {
        return this.Type;
    }
    public void setType(int type) {
        this.Type = type;
    }
    public int getImg() {
        return this.Img;
    }
    public void setImg(int img) {
        this.Img = img;
    }

    public List<Menu> getAllMenuItems(){
        List<Menu> lm = new ArrayList<Menu>();

        Cursor c = this.db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_MENU, null);
        if ( c.moveToFirst() ) {
            do {

                Menu mn = new Menu();
                mn.setItem(c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)) );
                mn.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IDROW)));
                mn.setType(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_MENU_TYPE)));
                mn.setImg(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IMG)));

                lm.add( mn );

            } while ( c.moveToNext() );
            c.close();
        }


        return lm;
    }
    public List<String> getMenuItemsByType(int type) {
        List<String> lm = new ArrayList<String>();

        Cursor c = this.db.rawQuery(
                "SELECT " + DatabaseHandler.KEY_ITEM + " FROM " + DatabaseHandler.TABLE_MENU + " WHERE " + DatabaseHandler.KEY_MENU_TYPE
                + " = " + String.valueOf(type),
                null
        );
        if ( c.moveToFirst() ) {
            do {
                lm.add( c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)) );
            } while ( c.moveToNext() );
            c.close();
        }


        return lm;
    }
    public void getMenu() {
        if ( this.db == null ) {
            return;
        }
        if ( this.ID < 1 ) {
            return;
        }
        if ( this.db.isOpen() ) {
            Cursor c = this.db.rawQuery(
                    "SELECT * FROM " + DatabaseHandler.TABLE_MENU
                            + " WHERE " + DatabaseHandler.KEY_IDROW
                            + " = " + String.valueOf(this.ID),
                    null
            );

            if ( c.moveToFirst() ) {
                do {
                    this.setItem(c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)) );
                    this.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IDROW)));
                    this.setType(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_MENU_TYPE)));
                    this.setImg(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IMG)));

                } while ( c.moveToNext() );
                c.close();
            }
        }
    }
    public int updateMenu() {
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
            cv.put( DatabaseHandler.KEY_MENU_TYPE, this.Type );
            cv.put( DatabaseHandler.KEY_IMG, this.Img );


            r = db.update(
                    DatabaseHandler.TABLE_MENU,
                    cv,
                    DatabaseHandler.KEY_IDROW + " =? ",
                    new String[] { String.valueOf( this.ID ) }
            );
        }

        return r;
    }
    // Удаляет объект из базы данных
    public void deleteMenu() {
        Log.d("deleteMenu", "one second" + " " + String.valueOf( this.getID() ));
        if (this.db == null) {
            return;
        }
        if ( !this.db.isOpen() ) {
            return;
        }

        this.db.delete(
                DatabaseHandler.TABLE_MENU,
                DatabaseHandler.KEY_IDROW + " =? ",
                new String[] { String.valueOf( this.ID ) }
        );
        Log.d("deleteMenu", "yes");
    }
    public void addItem() {
        if ( this.db.isOpen() ) {
            ContentValues cv = new ContentValues();

            cv.put( DatabaseHandler.KEY_ITEM, this.Item );
            cv.put( DatabaseHandler.KEY_MENU_TYPE, this.Type );
            cv.put( DatabaseHandler.KEY_IMG, this.Img );

            this.ID = (int) db.insert( DatabaseHandler.TABLE_MENU, null, cv );
        }
    }

}
