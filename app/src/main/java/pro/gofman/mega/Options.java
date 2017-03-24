package pro.gofman.mega;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Роман on 21.07.2015.
 */
public class Options {

    private String key = null;
    private String value = null;
    private SQLiteDatabase db = null;

    private void read() {
        // Выходим если не открыта DB
        if ( !this.db.isOpen() ) {
            return;
        }

        String sql = "SELECT value FROM " + DatabaseHandler.TABLE_OPTIONS + " WHERE "
                + DatabaseHandler.KEY_OPTIONS_KEY + "='" + key + "'";
        Cursor c = this.db.rawQuery(sql, null);
        if ( c.moveToFirst() ) {
            this.value = c.getString(c.getColumnIndex(DatabaseHandler.KEY_OPTIONS_VALUE));
        }
    }

    public Options(SQLiteDatabase db) {
        this.db = db;
    }
    public Options(String key, SQLiteDatabase db) {
        this.key = key;
        this.db = db;
        this.read();
    }

    public void setKey(String key) {
        this.key = key;
        this.read();
    }


    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        // Выходим если не указан ключ
        if ( this.key == null ) {
            return;
        }
        // Выходим если не открыта DB
        if ( !this.db.isOpen() ) {
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_OPTIONS_VALUE, value );

        if ( this.db.update(
                DatabaseHandler.TABLE_OPTIONS,
                cv,
                DatabaseHandler.KEY_OPTIONS_KEY + " =? ",
                new String[] { this.key }
        ) > 0 ) {
            this.value = value;
        }
    }
}
