package pro.gofman.mega;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

    // Опции базы данных
    protected static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "marie";

    protected static final int MENU_TYPE_MEGADEVICE = 1;
    protected static final int MENU_TYPE_CALLBACK = 2;

    // Таблицы в базе данных
    protected static final String TABLE_MEGADEVICES = "mega_devices";
    protected static final String TABLE_MENU = "menu_drawer";
    protected static final String TABLE_ELEMENTS = "fragment_items";
    protected static final String TABLE_WORDS = "words";
    protected static final String TABLE_OPTIONS = "options";

    // Общие поля для нескольких таблиц
    protected static final String KEY_IDROW = "_id";
    protected static final String KEY_ITEM = "item";
    protected static final String KEY_DESC = "desc";
    protected static final String KEY_IMG = "img";


    // Дополнительные поля для таблицы Мегадевайсы
    protected static final String KEY_MEGADEVICES_IPADDR = "ipaddr";
    protected static final String KEY_MEGADEVICES_PORT = "port";
    protected static final String KEY_MEGADEVICES_PASSWORD = "pwd";
    protected static final String KEY_MEGADEVICES_VER = "ver";

    // Дополнительные поля для таблицы Меню
    //protected static final String KEY_MENU_ID = "id";
    protected static final String KEY_MENU_TYPE = "type";

    // Дополнительные поля для таблицы Элементы управления
    protected static final String KEY_ELEMENTS_IDMENU = "menu";         // Ссылка на пункт меню
    protected static final String KEY_ELEMENTS_TYPE = "type";           // Тип Элемента управления (Освещение, Замок)
    protected static final String KEY_ELEMENTS_DEV = "dev";             // Ссылка на устройство (Мегадевайс)
    protected static final String KEY_ELEMENTS_PORT = "port";

    // Дополнительные поля для Голосового управления
    protected static final String KEY_WORDS_IDELEMENT = "el";


    // Допольнительные поля настроек
    protected static final String KEY_OPTIONS_KEY = "key";
    protected static final String KEY_OPTIONS_VALUE = "value";

    // Конструктор
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблцы Мегадевайсов
        String CREATE_MEGADEVICES_TABLE = "CREATE TABLE " + TABLE_MEGADEVICES + " ("
                + KEY_IDROW + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ITEM + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_MEGADEVICES_IPADDR + " TEXT,"
                + KEY_MEGADEVICES_PORT + " INTEGER,"
                + KEY_MEGADEVICES_PASSWORD + " TEXT,"
                + KEY_MEGADEVICES_VER + " TEXT"
                + ")";

        // Создание таблицы меню
        String CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + " ("
                + KEY_IDROW + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_IMG + " INTEGER,"
                + KEY_MENU_TYPE + " INTEGER,"
                + KEY_ITEM + " TEXT"
                + ")";

        String CREATE_ELEMENTS_TABLE = "CREATE TABLE " + TABLE_ELEMENTS + " ("
                + KEY_IDROW + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ELEMENTS_IDMENU + " INTEGER,"
                + KEY_ITEM + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_ELEMENTS_TYPE + " INTEGER,"
                + KEY_ELEMENTS_DEV + " INTEGER,"
                + KEY_ELEMENTS_PORT + " INTEGER"
                + ")";

        String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + " ("
                + KEY_IDROW + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_WORDS_IDELEMENT + " INTEGER,"
                + KEY_ITEM + " TEXT"
                + ")";

        String CREATE_OPTIONS_TABLE = "CREATE TABLE " + TABLE_OPTIONS + " ("
                + KEY_IDROW + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_OPTIONS_KEY + " TEXT,"
                + KEY_OPTIONS_VALUE + " TEXT"
                + ")";

        //db.beginTransaction();
        db.execSQL(CREATE_OPTIONS_TABLE);
        db.execSQL(CREATE_MEGADEVICES_TABLE);
        db.execSQL(CREATE_MENU_TABLE);
        db.execSQL(CREATE_ELEMENTS_TABLE);
        db.execSQL(CREATE_WORDS_TABLE);

        // Заполняем таблицу опций
        fillOptions(db);

        // Заполняем таблицу Устройств
        fillMegadevices(db);

        // Заполняем таблицу Меню
        fillMenu(db);

        // Заполняем таблицу Элементов управления
        fillElements(db);

        // Заполняем таблицу словами для Голосового управления
        fillWords(db);

        //db.setTransactionSuccessful();
        //db.endTransaction();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Надо обработать понижение версии базы данных или повышение
        // пока тупое удаленние

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEGADEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);

        onCreate(db);
    }

    public void fillOptions(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        // Собственный основной номер в домашнем регионе
        // в режиме callback-а будет подставляться в CallerID.
        cv.put( DatabaseHandler.KEY_OPTIONS_KEY, "main_number" );
        cv.put( DatabaseHandler.KEY_OPTIONS_VALUE, "" );
        db.insert(DatabaseHandler.TABLE_OPTIONS, null, cv);

        // Режим использования Callback
        //      0 - неиспользовать,
        //      1 - использовать при использовании не основной сим карты
        //      2 - использовать всегда, полезно при международных звонках
        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_OPTIONS_KEY, "callback" );
        cv.put( DatabaseHandler.KEY_OPTIONS_VALUE, "2" );
        db.insert(DatabaseHandler.TABLE_OPTIONS, null, cv);

        // Номер на который будет работать Callback
        // Указывается номер или основной или заграничной симкарты
        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_OPTIONS_KEY, "callback_number" );
        cv.put(DatabaseHandler.KEY_OPTIONS_VALUE, "");
        db.insert(DatabaseHandler.TABLE_OPTIONS, null, cv);

        // Callback url
        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_OPTIONS_KEY, "callback_url" );
        cv.put(DatabaseHandler.KEY_OPTIONS_VALUE, "http://gofman.pro/callnow/callback.php");
        db.insert(DatabaseHandler.TABLE_OPTIONS, null, cv);

        // Имя сети WIFI для работы с мегой
        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_OPTIONS_KEY, "wifi_allowed" );
        cv.put(DatabaseHandler.KEY_OPTIONS_VALUE, "114");
        db.insert(DatabaseHandler.TABLE_OPTIONS, null, cv);




    }

    void fillMegadevices(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ITEM, "Мегадевайс 328");
        cv.put(KEY_DESC, "Управляет освещением в комнатах: коридор, гостинная, ванная, десткая, спальня и кухня");
        cv.put(KEY_MEGADEVICES_IPADDR, "192.168.1.251");
        cv.put(KEY_MEGADEVICES_PORT, MegaDevices.DEFAULT_PORT);
        cv.put(KEY_MEGADEVICES_PASSWORD, "sec");
        cv.put(KEY_MEGADEVICES_VER, "3.34b7");

        db.insert(TABLE_MEGADEVICES, null, cv);
    }

    void fillMenu(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_IMG, R.drawable.ic_exit_to_app);
        cv.put( DatabaseHandler.KEY_MENU_TYPE, MENU_TYPE_MEGADEVICE);
        cv.put(DatabaseHandler.KEY_ITEM, "Коридор");
        db.insert(DatabaseHandler.TABLE_MENU, null, cv);

        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_IMG, R.drawable.ic_sofa);
        cv.put( DatabaseHandler.KEY_MENU_TYPE, MENU_TYPE_MEGADEVICE);
        cv.put(DatabaseHandler.KEY_ITEM, "Гостинная");
        db.insert(DatabaseHandler.TABLE_MENU, null, cv);

        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_IMG, R.drawable.ic_human_child);
        cv.put( DatabaseHandler.KEY_MENU_TYPE, MENU_TYPE_MEGADEVICE);
        cv.put(DatabaseHandler.KEY_ITEM, "Детская");
        db.insert(DatabaseHandler.TABLE_MENU, null, cv);

        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_IMG, R.drawable.ic_hotel);
        cv.put( DatabaseHandler.KEY_MENU_TYPE, MENU_TYPE_MEGADEVICE);
        cv.put(DatabaseHandler.KEY_ITEM, "Спальня");
        db.insert(DatabaseHandler.TABLE_MENU, null, cv);

        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_IMG, R.drawable.ic_human_male_female);
        cv.put( DatabaseHandler.KEY_MENU_TYPE, MENU_TYPE_MEGADEVICE);
        cv.put(DatabaseHandler.KEY_ITEM, "Ванная");
        db.insert(DatabaseHandler.TABLE_MENU, null, cv);

        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_IMG, R.drawable.ic_silverware_spoon);
        cv.put( DatabaseHandler.KEY_MENU_TYPE, MENU_TYPE_MEGADEVICE);
        cv.put(DatabaseHandler.KEY_ITEM, "Кухня");
        db.insert(DatabaseHandler.TABLE_MENU, null, cv);

        /*
        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_MENU_ID, 7 );
        cv.put( DatabaseHandler.KEY_MENU_TYPE, MENU_TYPE_MEGADEVICE);
        cv.put( DatabaseHandler.KEY_ITEM, "Балкон" );
        db.insert(DatabaseHandler.TABLE_MENU, null, cv);
        */

        cv = new ContentValues();
        cv.put( DatabaseHandler.KEY_IMG, R.drawable.ic_phone);
        cv.put( DatabaseHandler.KEY_MENU_TYPE, MENU_TYPE_CALLBACK);
        cv.put( DatabaseHandler.KEY_ITEM, "Заказ звонка" );
        db.insert(DatabaseHandler.TABLE_MENU, null, cv);

    }

    void fillElements(SQLiteDatabase db) {
        // Коридор
        ContentValues cv = new ContentValues();
        cv.put(KEY_ITEM, "Входная дверь");          // Наименование
        cv.put(KEY_DESC, "Открывает, закрывает входную дверь");
        cv.put(KEY_ELEMENTS_IDMENU, 1);             // Пункт меню
        cv.put(KEY_ELEMENTS_DEV, 1);                // Устройство
        cv.put(KEY_ELEMENTS_PORT, 7);               // Порт
        cv.put(KEY_ELEMENTS_TYPE, 1);               // Тип элемента 0 - Назрузка; 1 - Замок

        db.insert(TABLE_ELEMENTS, null, cv);

        cv = new ContentValues();
        cv.put(KEY_ITEM, "Основное освещение");     // Наименование
        cv.put(KEY_DESC, "Включает, выключает основное освещение в коридоре");
        cv.put(KEY_ELEMENTS_IDMENU, 1);             // Пункт меню
        cv.put(KEY_ELEMENTS_DEV, 1);                // Устройство
        cv.put(KEY_ELEMENTS_PORT, 8);               // Порт
        cv.put(KEY_ELEMENTS_TYPE, 0);               // Тип элемента 0 - Назрузка; 1 - Замок

        db.insert(TABLE_ELEMENTS, null, cv);

        // Гостинная
        cv = new ContentValues();
        cv.put(KEY_ITEM, "Основное освещение");     // Наименование
        cv.put(KEY_DESC, "Включает, выключает основное освещение в гостинной");
        cv.put(KEY_ELEMENTS_IDMENU, 2);             // Пункт меню
        cv.put(KEY_ELEMENTS_DEV, 1);                // Устройство
        cv.put(KEY_ELEMENTS_PORT, 13);              // Порт
        cv.put(KEY_ELEMENTS_TYPE, 0);               // Тип элемента 0 - Назрузка; 1 - Замок

        db.insert(TABLE_ELEMENTS, null, cv);

        // Детская
        cv = new ContentValues();
        cv.put(KEY_ITEM, "Основное освещение");     // Наименование
        cv.put(KEY_DESC, "Включает, выключает основное освещение в детской");
        cv.put(KEY_ELEMENTS_IDMENU, 3);             // Пункт меню
        cv.put(KEY_ELEMENTS_DEV, 1);                // Устройство
        cv.put(KEY_ELEMENTS_PORT, 10);              // Порт
        cv.put(KEY_ELEMENTS_TYPE, 0);               // Тип элемента 0 - Назрузка; 1 - Замок

        db.insert(TABLE_ELEMENTS, null, cv);

        // Спальня
        cv = new ContentValues();
        cv.put(KEY_ITEM, "Основное освещение");     // Наименование
        cv.put(KEY_DESC, "Включает, выключает основное освещение в спальне");
        cv.put(KEY_ELEMENTS_IDMENU, 4);             // Пункт меню
        cv.put(KEY_ELEMENTS_DEV, 1);                // Устройство
        cv.put(KEY_ELEMENTS_PORT, 12);              // Порт
        cv.put(KEY_ELEMENTS_TYPE, 0);               // Тип элемента 0 - Назрузка; 1 - Замок

        db.insert(TABLE_ELEMENTS, null, cv);

        // Ванная
        cv = new ContentValues();
        cv.put(KEY_ITEM, "Основное освещение");     // Наименование
        cv.put(KEY_DESC, "Включает, выключает основное освещение в ванной");
        cv.put(KEY_ELEMENTS_IDMENU, 5);             // Пункт меню
        cv.put(KEY_ELEMENTS_DEV, 1);                // Устройство
        cv.put(KEY_ELEMENTS_PORT, 9);               // Порт
        cv.put(KEY_ELEMENTS_TYPE, 0);               // Тип элемента 0 - Назрузка; 1 - Замок

        db.insert(TABLE_ELEMENTS, null, cv);

        // Кухня
        cv = new ContentValues();
        cv.put(KEY_ITEM, "Основное освещение");     // Наименование
        cv.put(KEY_DESC, "Включает, выключает основное освещение в кухне");
        cv.put(KEY_ELEMENTS_IDMENU, 6);             // Пункт меню
        cv.put(KEY_ELEMENTS_DEV, 1);                // Устройство
        cv.put(KEY_ELEMENTS_PORT, 11);              // Порт
        cv.put(KEY_ELEMENTS_TYPE, 0);               // Тип элемента 0 - Назрузка; 1 - Замок

        db.insert(TABLE_ELEMENTS, null, cv);

        /*
        // Балкон
        cv = new ContentValues();
        cv.put(KEY_ITEM, "Основное освещение");     // Наименование
        cv.put(KEY_ELEMENTS_IDMENU, 7);             // Пункт меню
        cv.put(KEY_ELEMENTS_DEV, 1);                // Устройство
        cv.put(KEY_ELEMENTS_PORT, 9);               // Порт
        cv.put(KEY_ELEMENTS_TYPE, 0);               // Тип элемента 0 - Назрузка; 1 - Замок

        db.insert(TABLE_ELEMENTS, null, cv);
        */
    }
    void fillWords(SQLiteDatabase db) {
        // Замок двери
        ContentValues cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 1);
        cv.put(KEY_ITEM, "входна");
        db.insert(TABLE_WORDS, null, cv);

        // Освещение в коридоре
        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 2);
        cv.put(KEY_ITEM, "коридор");
        db.insert(TABLE_WORDS, null, cv);

        // Освещение в гостинной
        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 3);
        cv.put(KEY_ITEM, "гостин");
        db.insert(TABLE_WORDS, null, cv);

        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 3);
        cv.put(KEY_ITEM, "зал");
        db.insert(TABLE_WORDS, null, cv);

        // Освещение в детской
        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 4);
        cv.put(KEY_ITEM, "макс");
        db.insert(TABLE_WORDS, null, cv);

        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 4);
        cv.put(KEY_ITEM, "детск");
        db.insert(TABLE_WORDS, null, cv);

        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 4);
        cv.put(KEY_ITEM, "комнат");
        db.insert(TABLE_WORDS, null, cv);

        // Освещение в спальне
        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 5);
        cv.put(KEY_ITEM, "спальн");
        db.insert(TABLE_WORDS, null, cv);

        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 5);
        cv.put(KEY_ITEM, "родит");
        db.insert(TABLE_WORDS, null, cv);

        // Освещение в ванной
        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 6);
        cv.put(KEY_ITEM, "ванн");
        db.insert(TABLE_WORDS, null, cv);

        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 6);
        cv.put(KEY_ITEM, "туалет");
        db.insert(TABLE_WORDS, null, cv);

        // Освещение в кухне
        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 7);
        cv.put(KEY_ITEM, "кухн");
        db.insert(TABLE_WORDS, null, cv);

        cv = new ContentValues();
        cv.put(KEY_WORDS_IDELEMENT, 7);
        cv.put(KEY_ITEM, "столов");
        db.insert(TABLE_WORDS, null, cv);
    }


}
