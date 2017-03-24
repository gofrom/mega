package pro.gofman.mega;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gofman on 25.06.15.
 */
public class MegaDevices {
    protected static final int DEFAULT_PORT = 80;

    protected static final String DEVID = "devID";
    protected static final String DEVPOS = "DevPos";

    private int id;
    private String item;
    private String desc;
    private String ipaddr;
    private int port;
    private String pwd;
    private String ver;

    public MegaDevices() {
        this.id = 0;
        this.port = DEFAULT_PORT;
    }
    public MegaDevices(int id, String name, String ipaddr, String pwd) {
        this.id = id;
        this.item = name;
        this.ipaddr = ipaddr;
        this.port = DEFAULT_PORT;
        this.pwd = pwd;
    }
    public MegaDevices(String name, String ipaddr, String pwd) {
        this.item = name;
        this.ipaddr = ipaddr;
        this.port = DEFAULT_PORT;
        this.pwd = pwd;
        this.id = 0;
    }
    public int getPort() { return this.port; }
    public void setPort(int port) { this.port = port; }
    public int getID(){
        return this.id;
    }
    public void setID(int id){
        this.id = id;
    }
    public String getName(){
        return this.item;
    }
    public void setName(String name){
        this.item = name;
    }
    public String getDesc(){
        return this.desc;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getIPAddr(){
        return this.ipaddr;
    }
    public void setIPAddr(String ipaddr){
        this.ipaddr = ipaddr;
    }
    public String getPassword(){
        return this.pwd;
    }
    public void setPassword(String pwd){
        this.pwd = pwd;
    }
    public String getVersion(){
        return this.ver;
    }
    public void setVersion(String ver){
        this.ver = ver;
    }

    public List<MegaDevices> getAllMegadevices( SQLiteDatabase db ){

        List<MegaDevices> lm = new ArrayList<MegaDevices>();
        if ( db.isOpen() ) {

            String sql = "SELECT * FROM " + DatabaseHandler.TABLE_MEGADEVICES;
            Cursor c = db.rawQuery(sql, null);

            if ( c.moveToFirst() ) {
                do {

                    MegaDevices md = new MegaDevices();
                    md.setID( c.getInt( c.getColumnIndex(DatabaseHandler.KEY_IDROW)) );
                    md.setName(c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)));
                    md.setDesc(c.getString(c.getColumnIndex(DatabaseHandler.KEY_DESC)));
                    md.setIPAddr(c.getString(c.getColumnIndex(DatabaseHandler.KEY_MEGADEVICES_IPADDR)));
                    md.setPort(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_MEGADEVICES_PORT)));
                    md.setPassword(c.getString(c.getColumnIndex(DatabaseHandler.KEY_MEGADEVICES_PASSWORD)));
                    md.setVersion(c.getString(c.getColumnIndex(DatabaseHandler.KEY_MEGADEVICES_VER)));

                    lm.add(md);

                } while ( c.moveToNext() );

                c.close();
            }

        }
        return lm;
    }

    public void getMegadevice( SQLiteDatabase db, int id) {
        if ( id < 1 ) {
            return;
        }
        if ( db.isOpen() ) {
            String sql = "SELECT * FROM " + DatabaseHandler.TABLE_MEGADEVICES
                    + " WHERE " + DatabaseHandler.KEY_IDROW + " = " + String.valueOf( id );
            Cursor c = db.rawQuery(sql, null);

            if ( c != null ) {

                c.moveToFirst();

                this.setName(c.getString(c.getColumnIndex(DatabaseHandler.KEY_ITEM)));
                this.setDesc(c.getString(c.getColumnIndex(DatabaseHandler.KEY_DESC)));
                this.setIPAddr(c.getString(c.getColumnIndex(DatabaseHandler.KEY_MEGADEVICES_IPADDR)));
                this.setPort(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_MEGADEVICES_PORT)));
                this.setPassword(c.getString(c.getColumnIndex(DatabaseHandler.KEY_MEGADEVICES_PASSWORD)));
                this.setID(c.getInt(c.getColumnIndex(DatabaseHandler.KEY_IDROW)));
                this.setVersion(c.getString(c.getColumnIndex(DatabaseHandler.KEY_MEGADEVICES_VER)));

                c.close();

            }
        }
    }

    public int updateMegadevice( SQLiteDatabase db ) {
        int r = 0;
        if ( db.isOpen() ) {
            ContentValues cv = new ContentValues();

            cv.put( DatabaseHandler.KEY_ITEM, this.item );
            cv.put( DatabaseHandler.KEY_DESC, this.desc );
            cv.put( DatabaseHandler.KEY_MEGADEVICES_IPADDR, this.ipaddr );
            cv.put( DatabaseHandler.KEY_MEGADEVICES_PORT, this.port );
            cv.put( DatabaseHandler.KEY_MEGADEVICES_PASSWORD, this.pwd );
            cv.put( DatabaseHandler.KEY_MEGADEVICES_VER, this.ver );

            r = db.update(
                    DatabaseHandler.TABLE_MEGADEVICES,
                    cv,
                    DatabaseHandler.KEY_IDROW + " =? ",
                    new String[] { String.valueOf( this.id ) }
            );
        }
        return r;
    }

    public void deleteMegadevice( SQLiteDatabase db ) {
        if ( db.isOpen() ) {
            Log.d("2", "Deleted: " + String.valueOf(this.id) + " " + this.getName());

            db.delete(
                    DatabaseHandler.TABLE_ELEMENTS,
                    DatabaseHandler.KEY_ELEMENTS_DEV + "=" + String.valueOf( this.id ),
                    null
            );


            db.delete(
                    DatabaseHandler.TABLE_MEGADEVICES,
                    DatabaseHandler.KEY_IDROW + "=" + String.valueOf(this.id),
                    null
            );

        }
    }

    public void addMegadevices ( SQLiteDatabase db ) {
        if ( db.isOpen() ) {
            ContentValues cv = new ContentValues();
            cv.put( DatabaseHandler.KEY_ITEM, this.item );
            cv.put( DatabaseHandler.KEY_DESC, this.desc );
            cv.put( DatabaseHandler.KEY_MEGADEVICES_IPADDR, this.ipaddr );
            cv.put( DatabaseHandler.KEY_MEGADEVICES_PORT, this.port );
            cv.put( DatabaseHandler.KEY_MEGADEVICES_PASSWORD, this.pwd );
            cv.put(DatabaseHandler.KEY_MEGADEVICES_VER, this.ver);

            this.id = (int) db.insert( DatabaseHandler.TABLE_MEGADEVICES, null, cv );
        }
    }
    public int getCount( SQLiteDatabase db ) {
        int res = 0;

        if ( db.isOpen() ) {
            String sql = "SELECT COUNT(*) FROM " + DatabaseHandler.TABLE_MEGADEVICES;
            Cursor c = db.rawQuery(sql, null);

            if ( c.moveToFirst() ) {
                res = c.getInt( 0 );
                c.close();
            }
        }

        return res;
    }


}
