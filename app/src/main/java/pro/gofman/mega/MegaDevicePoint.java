package pro.gofman.mega;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;





public class MegaDevicePoint {
    public int id;
    public int menu;
    public String name;
    public int dev;
    public int port;
    public int type;

    // Типы портов на Мегадевайсе
    protected static final int TYPE_PORT_NC = 255;
    protected static final int TYPE_PORT_IN = 0;
    protected static final int TYPE_PORT_OUT = 1;
    protected static final int TYPE_PORT_ADC = 2;
    protected static final int TYPE_PORT_DSEN = 3;



    protected static final int TYPE_LIGHT = 0;
    protected static final int TYPE_LOCK = 1;
    protected static final int TYPE_SENSOR = 2;

    protected static final String STATUS_ON = "ON";
    protected static final String STATUS_OFF = "OFF";



    public int getID() { return this.id; }
    public void setID(int id) { this.id = id; }
    public int getMenu() { return this.menu; }
    public void setMenu(int menu) { this.menu = menu; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public int getDev() { return this.dev; }
    public void setDev(int dev) { this.dev = dev; }
    public int getPort() { return this.port; }
    public void setPort(int port) { this.port = port; }
    public int getType() { return this.type; }
    public void setType(int type) { this.type = type; }

    public void addPoint ( SQLiteDatabase db ) {
        if ( db.isOpen() ) {
            ContentValues cv = new ContentValues();
            cv.put( DatabaseHandler.KEY_ITEM, this.name );
            cv.put( DatabaseHandler.KEY_ELEMENTS_IDMENU, this.menu );
            cv.put( DatabaseHandler.KEY_ELEMENTS_DEV, this.dev );
            cv.put( DatabaseHandler.KEY_ELEMENTS_PORT, this.port );
            cv.put( DatabaseHandler.KEY_ELEMENTS_TYPE, this.type );

            this.id = (int) db.insert( DatabaseHandler.TABLE_ELEMENTS, null, cv );
        }
    }

    public int updatePoint( SQLiteDatabase db ) {
        int r = 0;
        if ( db.isOpen() ) {
            ContentValues cv = new ContentValues();

            cv.put( DatabaseHandler.KEY_ITEM, this.name );
            cv.put( DatabaseHandler.KEY_ELEMENTS_IDMENU, this.menu );
            cv.put( DatabaseHandler.KEY_ELEMENTS_DEV, this.dev );
            cv.put( DatabaseHandler.KEY_ELEMENTS_PORT, this.port );
            cv.put( DatabaseHandler.KEY_ELEMENTS_TYPE, this.type );

            r = db.update(
                    DatabaseHandler.TABLE_ELEMENTS,
                    cv,
                    DatabaseHandler.KEY_IDROW + " =? ",
                    new String[] { String.valueOf( this.id ) }
            );
        }
        return r;
    }

    public void getPoint( SQLiteDatabase db, int id) {

        if ( db.isOpen() ) {
            String sql = "SELECT * FROM " + DatabaseHandler.TABLE_ELEMENTS
                    + " WHERE " + DatabaseHandler.KEY_IDROW + " = " + String.valueOf(id);


            Cursor c = db.rawQuery(sql, null);

            if (c != null) {

                c.moveToFirst();

                this.setName( c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)) );
                this.setMenu( c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_IDMENU)) );
                this.setDev( c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_DEV)) );
                this.setPort( c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_PORT)) );
                this.setType( c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_TYPE)) );
                this.setID( id );

                c.close();

            }
        }
    }

    public void deletePoint( SQLiteDatabase db ) {
        if ( db.isOpen() ) {
            db.delete(
                    DatabaseHandler.TABLE_ELEMENTS,
                    DatabaseHandler.KEY_IDROW + " =? ",
                    new String[] { String.valueOf( this.id ) }
            );
        }
    }


    public List<MegaDevicePoint> getAllPoints( SQLiteDatabase db ){

        List<MegaDevicePoint> lm = new ArrayList<MegaDevicePoint>();
        if ( db.isOpen() ) {

            String sql = "SELECT * FROM " + DatabaseHandler.TABLE_ELEMENTS;
            Cursor c = db.rawQuery(sql, null);

            if ( c.moveToFirst() ) {
                do {

                    MegaDevicePoint mp = new MegaDevicePoint();
                    mp.setName( c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)) );
                    mp.setMenu(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_IDMENU)));
                    mp.setDev(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_DEV)));
                    mp.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IDROW)));
                    mp.setPort(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_PORT)));
                    mp.setType(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_TYPE)));

                    lm.add(mp);

                    Log.d("Элементы", String.valueOf( mp.getID() ) + " " + String.valueOf( mp.getDev() ) + " " + String.valueOf( mp.getPort() ) + " " + mp.getName() + " " + String.valueOf(mp.getType()));

                } while ( c.moveToNext() );

                c.close();
            }

        }
        return lm;
    }
    public List<MegaDevicePoint> getMegadevicePoints( SQLiteDatabase db ){

        List<MegaDevicePoint> lm = new ArrayList<MegaDevicePoint>();
        if ( this.dev < 0 ) {
            return lm;
        }

        if ( db.isOpen() ) {

            String sql = "SELECT * FROM " + DatabaseHandler.TABLE_ELEMENTS + " WHERE " + DatabaseHandler.KEY_ELEMENTS_DEV
                    + " = " + String.valueOf( this.dev );
            Cursor c = db.rawQuery(sql, null);

            if ( c.moveToFirst() ) {
                do {

                    MegaDevicePoint mp = new MegaDevicePoint();
                    mp.setName( c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)) );
                    mp.setMenu(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_IDMENU)));
                    mp.setDev(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_DEV)));
                    mp.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IDROW)));
                    mp.setPort(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_PORT)));
                    mp.setType(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_ELEMENTS_TYPE)));

                    lm.add(mp);

                    Log.d("Элементы", String.valueOf( mp.getID() ) + " " + String.valueOf( mp.getDev() ) + " " + String.valueOf( mp.getPort() ) + " " + mp.getName() + " " + String.valueOf(mp.getType()));

                } while ( c.moveToNext() );

                c.close();
            }

        }
        return lm;
    }



}
