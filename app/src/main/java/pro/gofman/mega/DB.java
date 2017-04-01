package pro.gofman.mega;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;


/**
 * Created by roman on 10.07.16.
 */

public class DB {

    private dbHelper mHelper;
    public SQLiteDatabase mDatabase;
    private SimpleDateFormat df;

    protected static final int OPTION_CONNECTION = 1;
    protected static final int OPTION_AUTH = 2;
    protected static final int OPTION_COORD = 3;

    public DB(Context context) {
        mHelper = new dbHelper(context);
        mDatabase = mHelper.getWritableDatabase();

        df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    }

    public String getOptions(int o_id) {
        String r = "";
        Cursor c = mDatabase.rawQuery("SELECT o_data FROM options WHERE o_id = " + String.valueOf( o_id ), null );
        if ( c != null ) {
            if ( c.moveToFirst() ) {
                r = c.getString(0);
                c.close();
            }
        }
        return r;
    }
    public boolean setOptions(int o_id, String json) {

        ContentValues cv = new ContentValues();
        cv.put( "o_data", json );

        return 1 == mDatabase.update( "options", cv, "o_id = " + String.valueOf(o_id), null );
    }

    public void execSQL(String sql) {
        mDatabase.execSQL(sql);
    }
    public Cursor rawQuery(String sql, String[] arg) { return mDatabase.rawQuery(sql, arg); }

    public long insert(String tn, ContentValues cv) {
        return mDatabase.insert(tn, null, cv);
    }

    public long replace(String tn, ContentValues cv) {
        return mDatabase.replace(tn, null, cv);
    }


    private static class dbHelper extends SQLiteOpenHelper {

        protected static final int DATABASE_VERSION = 1;
        protected static final String DATABASE_NAME = "mega";

        private Context context;

        public dbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase s) {

            String sql = "";
            InputStream is = this.context.getResources().openRawResource(R.raw.database);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;

            try {
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                //Log.i("JSON", baos.toString("UTF-8") );
                JSONObject database = new JSONObject(baos.toString("UTF-8"));
                Log.i("JSON", database.getString("database"));

                JSONArray tables = database.getJSONArray("tables");
                JSONObject table;

                for (int i = 0; i < tables.length(); i++) {
                    table = tables.getJSONObject(i);

                    sql = SQLBuilderCreateTable( table.getString("table"), table.getJSONArray("fields"), table.optBoolean("fts3") );

                    s.execSQL(sql);
                    Log.i("SQLCREATE", sql);

                    if ( table.optJSONArray("indexes") != null ) {
                        CreateIndexes(table.getString("table"), table.optJSONArray("indexes"), s);
                    }

                    if ( table.optJSONArray("values") != null ) {
                        FillTable(table.getString("table"), table.optJSONArray("values"), s);
                        //s.execSQL(sql);
                        //Log.i("SQLFILL", sql);
                    }

                }







            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase s, int i, int i1) {

        }

        // t - Наименование таблицы
        // a - Столбцы
        // f - Свойства таблицы (пока одно)
        private String SQLBuilderCreateTable(String t, JSONArray a, Boolean f) throws JSONException {
            int length = a.length();

            String result = "";
            String d;
            Boolean pk, uk, wr;

            if (length > 0) {
                wr = false;

                if ( f ) {
                    result = "CREATE VIRTUAL TABLE " + t + " USING fts3 ( ";
                } else {
                    result = "CREATE TABLE " + t + " ( ";
                }

                for (int i = 0; i < length; i++) {

                    pk = a.getJSONObject(i).optBoolean("primary_key", false);
                    uk = a.getJSONObject(i).optBoolean("unique_key", false);
                    result += a.getJSONObject(i).getString("field") + " ";
                    d = i == length - 1 ? " " : ", ";
                    result += a.getJSONObject(i).getString("type");
                    if ( pk ) {
                        if ( !f ) result += " PRIMARY KEY";
                        wr = true;
                    }
                    if ( uk ) {
                        result += " UNIQUE KEY";
                    }
                    result += d;
                }
                result += ")";
                if (wr && !f) result += " WITHOUT ROWID";
                result += ";";
            }

            return result;
        }
        private void FillTable(String t, JSONArray a, SQLiteDatabase s) throws JSONException {
            int length = a.length();
            JSONArray f;


            if (length > 0) {
                f = a.getJSONObject(0).names();
                ContentValues cv;

                for (int i=0; i < length; i++) {
                    cv = new ContentValues();
                    for (int j=0; j < f.length(); j++) {
                        cv.put( f.getString(j), a.getJSONObject(i).getString(f.getString(j)) );
                        Log.i("SQLVALUES", f.getString(j) + " : " + a.getJSONObject(i).getString(f.getString(j)) );
                    }
                    s.insert(t, null, cv);

                }

            }

        }
        private void CreateIndexes(String t, JSONArray a, SQLiteDatabase s ) throws JSONException {
            int length = a.length();
            JSONObject f;
            JSONArray c;
            String sql;

            if ( length > 0 ) {

                for (int i=0; i < length; i++ ) {

                    f = a.getJSONObject(i);

                    sql = "CREATE ";
                    if ( f.optBoolean("unique") ) {
                        sql += "UNIQUE ";
                    }
                    sql += "INDEX IF NOT EXISTS ";
                    sql += f.getString("index") + " ON " + t + " ( ";

                    c = f.getJSONArray("fields");

                    for (int j=0; j < c.length(); j++ ) {
                        sql += c.getString(j);
                        sql += j == c.length() - 1 ? " " : ", ";
                    }

                    sql += " );";


                    s.execSQL( sql );
                    Log.i("SQLINDEX", sql);
                }


            }
        }

    } // dbHelper
}
