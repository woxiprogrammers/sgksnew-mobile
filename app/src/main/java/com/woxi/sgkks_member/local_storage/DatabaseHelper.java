package com.woxi.sgkks_member.local_storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.woxi.sgkks_member.interfaces.DatabaseConstants;
import com.woxi.sgkks_member.utils.AppCommonMethods;

/**
 * <b><b>class DatabaseHelper extends SQLiteOpenHelper implements DatabaseConstants </b></b>
 * <p>This class is used as helper to create and upgrade  </p>
 * Created by Rohit.
 */
class DatabaseHelper extends SQLiteOpenHelper implements DatabaseConstants {
    private static DatabaseHelper sInstance;
    private String TAG = "DatabaseHelper";

//    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MEMBER_DETAILS_TABLE_QUERY_EN = "CREATE TABLE " + TABLE_MEMBER_DETAILS_EN
                + " ("
                + COLUMN_MEMBER_ID_PRIMARY_EN + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_MEMBER_FIRST_NAME + " VARCHAR(500), "
                + COLUMN_MEMBER_MIDDLE_NAME + " VARCHAR(500), "
                + COLUMN_MEMBER_LAST_NAME + " VARCHAR(500), "
                + COLUMN_MEMBER_ADDRESS + " VARCHAR(500), "
                + COLUMN_MEMBER_SGKS_CITY + " VARCHAR(500), "
                + COLUMN_MEMBER_SGKS_CITY_ID + " VARCHAR(500), "
                + COLUMN_MEMBER_GENDER + " VARCHAR(500), "
                + COLUMN_MEMBER_MOBILE + " VARCHAR(500), "
                + COLUMN_DATE_OF_BIRTH + " VARCHAR(500), "
                + COLUMN_MEMBER_EMAIL + " VARCHAR(500), "
                + COLUMN_MEMBER_BLOOD_GROUP + " VARCHAR(500), "
                + COLUMN_MEMBER_BLOOD_GROUP_ID + " VARCHAR(500), "
                + COLUMN_MEMBER_LATITUDE + " VARCHAR(500), "
                + COLUMN_MEMBER_LONGITUDE + " VARCHAR(500), "
                + COLUMN_MEMBER_IMAGE_URL + " VARCHAR(500), "
                + COLUMN_MEMBER_IS_ACTIVE + " BOOLEAN"
                + ")";
        new AppCommonMethods().LOG(0, TAG, CREATE_MEMBER_DETAILS_TABLE_QUERY_EN);

        String CREATE_MEMBER_DETAILS_TABLE_QUERY_GJ = "CREATE TABLE " + TABLE_MEMBER_DETAILS_GJ
                + " ("
                + COLUMN_MEMBER_ID_PRIMARY_GJ + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_MEMBER_FIRST_NAME + " VARCHAR(500), "
                + COLUMN_MEMBER_MIDDLE_NAME + " VARCHAR(500), "
                + COLUMN_MEMBER_LAST_NAME + " VARCHAR(500), "
                + COLUMN_MEMBER_ADDRESS + " VARCHAR(500), "
                + COLUMN_LANGUAGE_ID + " VARCHAR(500), "
                + COLUMN_MEMBER_ID_FOREIGN_KEY + " VARCHAR(500), "
                + "FOREIGN KEY(" + COLUMN_MEMBER_ID_FOREIGN_KEY + " ) REFERENCES "
                + TABLE_MEMBER_DETAILS_EN + " (" + COLUMN_MEMBER_ID_PRIMARY_EN + ")"
                + ")";
        new AppCommonMethods().LOG(0, TAG, CREATE_MEMBER_DETAILS_TABLE_QUERY_GJ);

        String CREATE_CITY_TABLE_QUERY_EN = "CREATE TABLE "+ TABLE_CITIES_EN
                + " ("
                + COLUMN_CITY_ID_PRIMARY_EN + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_CITY_NAME+ " VARCHAR(500), "
                + COLUMN_STATE_ID+ " VARCHAR(500), "
                + COLUMN_CITY_IS_ACTIVE+" BOOLEAN, "
                + COLUMN_CITY_MEMBER_COUNT + " VARCHAR(500)"
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_CITY_TABLE_QUERY_EN);

        String CREATE_CITY_TABLE_QUERY_GJ = "CREATE TABLE "+ TABLE_CITIES_GJ
                + "("
                + COLUMN_CITY_ID_PRIMARY_GJ + " VARCAHR(500) PRIMARY KEY,"
                + COLUMN_CITY_NAME+ " VARCHAR(500), "
                + COLUMN_CITY_ID_FOREIGN + " VARCHAR(500), "
                + COLUMN_CITY_LANGUAGE_ID+ " VARCHAR(500), "
                + "FOREIGN KEY(" + COLUMN_CITY_ID_FOREIGN + ") REFERENCES "
                + TABLE_CITIES_EN + " ("+ COLUMN_CITY_ID_PRIMARY_EN + ")"
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_CITY_TABLE_QUERY_GJ);

        String CREATE_EVENTS_TABLE_QUERY_EN = "CREATE TABLE " + TABLE_EVENTS_EN
                + " ("
                + COLUMN_EVENT_ID_PRIMARY_KEY + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_EVENT_NAME + " VARCHAR(500), "
                + COLUMN_EVENT_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_EVENT_VENUE + " VARCHAR(500), "
                + COLUMN_EVENT_DATE + " VARCHAR(500), "
                + COLUMN_EVENT_CITY + " VARCHAR(500), "
                + COLUMN_EVENT_CITY_ID + " VARCHAR(500), "
                + COLUMN_EVENT_IS_ACTIVE + " VARCHAR(500),"
                + COLUMN_EVENT_YEAR + " VARCHAR(500)"
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_EVENTS_TABLE_QUERY_EN);

        String CREATE_EVENTS_TABLE_QUERY_GJ = "CREATE TABLE " + TABLE_EVENTS_GJ
                + " ("
                + COLUMN_EVENT_ID_PRIMARY_KEY + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_EVENT_NAME + " VARCHAR(500), "
                + COLUMN_EVENT_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_EVENT_VENUE + " VARCHAR(500), "
                + COLUMN_EVENT_LANGUAGE_ID + " VARCHAR(500), "
                + COLUMN_EVENTS_ID_FOREIGN_KEY + " VARCHAR(500), "
                + " FOREIGN KEY("+ COLUMN_EVENTS_ID_FOREIGN_KEY + ") REFERENCES "
                + TABLE_EVENTS_EN + "(" + COLUMN_EVENT_ID_PRIMARY_KEY + ")"
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_EVENTS_TABLE_QUERY_GJ);

        String CREATE_EVENTS_IMAGES = "CREATE TABLE " + TABLE_EVENT_IMAGES
                + " ("
                + COLUMN_IMAGE_ID_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EVENT_IMAGE_URL + " VARCHAR(500), "
                + COLUMN_EVENTS_ID_FOREIGN_KEY + " VARCHAR(500), "
                + " FOREIGN KEY("+COLUMN_EVENTS_ID_FOREIGN_KEY + ") REFERENCES "
                + TABLE_EVENTS_EN + "(" + COLUMN_EVENT_ID_PRIMARY_KEY + "))";
        new AppCommonMethods().LOG(0,TAG,CREATE_EVENTS_IMAGES);

        String CREATE_MESSAGE_TABLE_QUERY_EN = "CREATE TABLE " + TABLE_MESSAGE_NEWS_DETAILS
                + " ("
                + COLUMN_MESSAGES_ID_PRIMARY + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_MESSAGES_TITLE + " VARCHAR(500), "
                + COLUMN_MESSAGES_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_MESSAGES_TYPE + " VARCHAR(500), "
                + COLUMN_MESSAGES_TYPE_ID + " VARCHAR(500), "
                + COLUMN_MESSAGES_IS_ACTIVE + " VARCHAR(500), "
                + COLUMN_MESSAGES_CITY + " VARCAHR(500), "
                + COLUMN_MESSAGES_CITY_ID + " VARCAHR(500), "
                + COLUMN_MESSAGES_CREATED_DATE + " VARCHAR(500), "
                + COLUMN_MESSAGES_IMAGE_URL + " VARCHAR(500)"
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_MESSAGE_TABLE_QUERY_EN);

        String CREATE_MESSAGE_TABLE_QUERY_GJ = "CREATE TABLE " + TABLE_MESSAGE_NEWS_DETAILS_GJ
                + " ("
                + COLUMN_MESSAGES_ID_PRIMARY_GJ + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_MESSAGES_TITLE + " VARCHAR(500), "
                + COLUMN_MESSAGES_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_MESSAGES_ID_FOREIGN + " VARCHAR(500), "
                + COLUMN_MESSAGE_LANGUAGE_ID + " VARCHAR(500), "
                + " FOREIGN KEY("+COLUMN_MESSAGES_ID_FOREIGN + ") REFERENCES "
                + TABLE_MESSAGE_NEWS_DETAILS + "(" + COLUMN_MESSAGES_ID_PRIMARY + "))";
        new AppCommonMethods().LOG(0,TAG,CREATE_MESSAGE_TABLE_QUERY_EN);

        String CREATE_CLASSIFIED_TABLE_QUERY_EN = "CREATE TABLE " + TABLE_CLASSIFIED_EN
                + "("
                + COLUMN_CLASSIFIED_ID_PRIMARY_KEY + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_CLASSIFIED_TITLE + " VARCHAR(500), "
                + COLUMN_CLASSIFIED_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_CLASSIFIED_CITY + " VARCHAR(500), "
                + COLUMN_CLASSIFIED_IS_ACTIVE + " VARCHAR(500), "
                + COLUMN_CLASSIFIED_CREATED_AT + " VARCHAR(500), "
                + COLUMN_CLASSIFIED_CITY_ID + " VARCHAR(500) "
                +")";
        new AppCommonMethods().LOG(0,TAG,CREATE_MESSAGE_TABLE_QUERY_EN);

        String CREATE_CLASSIFIED_TABLE_QUERY_GJ = "CREATE TABLE " + TABLE_CLASSIFIED_GJ
                + "("
                + COLUMN_CLASSIFIED_ID_PRIMARY_KEY_GJ + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_CLASSIFIED_TITLE + " VARCHAR(500), "
                + COLUMN_CLASSIFIED_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_CLASSIFIED_ID_FOREIGN_KEY + " VARCHAR(500), "
                + " FOREIGN KEY(" + COLUMN_CLASSIFIED_ID_FOREIGN_KEY + ")REFERENCES "
                + TABLE_CLASSIFIED_EN + "(" + COLUMN_CLASSIFIED_ID_PRIMARY_KEY + "))";
        new AppCommonMethods().LOG(0,TAG,CREATE_CLASSIFIED_TABLE_QUERY_GJ);

        String CREATE_ACCOUNT_TABLE_QUERY_EN = "CREATE TABLE " + TABLE_ACCOUNT_EN
                + "("
                + COLUMN_ACCOUNT_ID_PRIMARY + " VARCAHR(500) PRIMARY KEY, "
                + COLUMN_ACCOUNT_NAME + " VARCHAR(500), "
                + COLUMN_ACCOUNT_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_ACCOUNT_IS_ACTIVE + " VARCHAR(500), "
                + COLUMN_ACCOUNT_CITY + " VARCHAR(500), "
                + COLUMN_ACCOUNT_CITY_ID + " VARCHAR(500), "
                + COLUMN_ACCOUNT_YEAR + " VARCHAR(500)"
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_ACCOUNT_TABLE_QUERY_EN);

        String CREATE_ACCOUNT_TABLE_QUERY_GJ = "CREATE TABLE "+ TABLE_ACCOUNT_GJ
                +"("
                + COLUMN_ACCOUNT_ID_PRIMARY_GJ + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_ACCOUNT_NAME + " VARCHAR(500), "
                + COLUMN_ACCOUNT_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_ACCOUNT_ID_FOREIGN + " VARCHAR(500), "
                + " FOREIGN KEY(" + COLUMN_ACCOUNT_ID_FOREIGN + ") REFERENCES "
                + TABLE_ACCOUNT_EN + "(" + COLUMN_ACCOUNT_ID_PRIMARY +")"
                +")";
        new AppCommonMethods().LOG(0,TAG,CREATE_ACCOUNT_TABLE_QUERY_GJ);

        String CREATE_COUNT_TABLE = "CREATE TABLE " + TABLE_COUNTS
                +"("
                + COLUMN_COUNT_CITY_ID + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_COUNT_MESSAGE + " VARCHAR(500), "
                + COLUMN_COUNT_CLASSIFIED + " VARCHAR(500)"
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_COUNT_TABLE);

        String CREATE_TABLE_COMMITTEE_QUERY_EN = "CREATE TABLE " + TABLE_COMMITTEE_DETAILS
                + "("
                + COLUMN_COMMITTEE_ID_PRIMARY + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_COMMITTEE_NAME + " VARCHAR(500), "
                + COLUMN_COMMITTEE_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_COMMITTEE_IS_ACTIVE + " VARCHAR(500), "
                + COLUMN_COMMITTEE_CITY + " VARCHAR(500), "
                + COLUMN_COMMITTEE_CITY_ID + " VARCAHR(500), "
                + COLUMN_COMMITTEE_MEMBERS_EN + " TEXT, "
                + COLUMN_COMMITTEE_MEMBERS_GJ + " TEXT "
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_TABLE_COMMITTEE_QUERY_EN);

        String CREATE_TABLE_COMMITTEE_QUERY_GJ = "CREATE TABLE " + TABLE_COMMITTEE_DETAILS_GJ
                + "("
                + COLUMN_COMMITTEE_ID_PRIMARY_GJ + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_COMMITTEE_NAME + " VARCHAR(500), "
                + COLUMN_COMMITTEE_DESCRIPTION + " VARCHAR(500), "
                + COLUMN_COMMITTEE_ID_FOREIGN + " VARCHAR(500), "
                + " FOREIGN KEY(" + COLUMN_COMMITTEE_ID_FOREIGN + ") REFERENCES "
                + TABLE_COMMITTEE_DETAILS + "(" + COLUMN_COMMITTEE_ID_PRIMARY + ")"
                + ")";

        String CREATE_TABLE_COMMITTEE_MEMBERS_QUERY_EN = "CREATE TABLE " + TABLE_COMMITTEE_MEMBERS_EN
                + "("
                + COLUMN_COMMITTEE_MEMBER_ID_PRIMARY_EN + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_COMMITTEE_MEMBER_NAME + " VARCHAR(500), "
                + COLUMN_COMMITTEE_MEMBER_DESIGNATION + " VARCHAR(500), "
                + COLUMN_COMMITTEE_MEMBER_NUMBER + " VARCAHR(500), "
                + COLUMN_COMMITTEE_MEMBER_EMAIL + " VARCHAR(500), "
                + COLUMN_COMMITTEE_MEMBER_AREA + " VARCHAR(500), "
                + COLUMN_COMMITTEE_MEMBER_IS_ACTIVE + " VARCHAR(500), "
                + COLUMN_COMMITTEE_MEMBER_ID_FOREIGN + " VARCHAR(500), "
                + " FOREIGN KEY(" + COLUMN_COMMITTEE_MEMBER_ID_FOREIGN + ")REFERENCES "
                + TABLE_COMMITTEE_DETAILS + "(" + COLUMN_COMMITTEE_ID_PRIMARY + ")"
                + ")";

        new AppCommonMethods().LOG(0,TAG,CREATE_TABLE_COMMITTEE_MEMBERS_QUERY_EN);

        String CREATE_TABLE_COMMITTEE_MEMBERS_QUERY_GJ = "CREATE TABLE " + TABLE_COMMITTEE_MEMBERS_GJ
                + "("
                + COLUMN_COMMITTEE_MEMBER_ID_PRIMARY_GJ + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_COMMITTEE_MEMBER_NAME + " VARCHAR(500), "
                + COLUMN_MEMBERS_COMMITTEE_ID_FOREIGN + " VARCAHR(500), "
                + "FOREIGN KEY(" + COLUMN_MEMBERS_COMMITTEE_ID_FOREIGN +")REFERENCES "
                + TABLE_COMMITTEE_MEMBERS_EN + "(" + COLUMN_COMMITTEE_MEMBER_ID_PRIMARY_EN + ")"
                + ")";
        new AppCommonMethods().LOG(0,TAG,CREATE_TABLE_COMMITTEE_MEMBERS_QUERY_GJ);

        db.execSQL(CREATE_MEMBER_DETAILS_TABLE_QUERY_EN);
        db.execSQL(CREATE_MEMBER_DETAILS_TABLE_QUERY_GJ);
        db.execSQL(CREATE_CITY_TABLE_QUERY_EN);
        db.execSQL(CREATE_CITY_TABLE_QUERY_GJ);
        db.execSQL(CREATE_EVENTS_TABLE_QUERY_EN);
        db.execSQL(CREATE_EVENTS_TABLE_QUERY_GJ);
        db.execSQL(CREATE_EVENTS_IMAGES);
        db.execSQL(CREATE_MESSAGE_TABLE_QUERY_EN);
        db.execSQL(CREATE_MESSAGE_TABLE_QUERY_GJ);
        db.execSQL(CREATE_CLASSIFIED_TABLE_QUERY_EN);
        db.execSQL(CREATE_CLASSIFIED_TABLE_QUERY_GJ);
        db.execSQL(CREATE_ACCOUNT_TABLE_QUERY_EN);
        db.execSQL(CREATE_ACCOUNT_TABLE_QUERY_GJ);
        db.execSQL(CREATE_COUNT_TABLE);
        db.execSQL(CREATE_TABLE_COMMITTEE_QUERY_EN);
        db.execSQL(CREATE_TABLE_COMMITTEE_QUERY_GJ);
        db.execSQL(CREATE_TABLE_COMMITTEE_MEMBERS_QUERY_GJ);
        db.execSQL(CREATE_TABLE_COMMITTEE_MEMBERS_QUERY_EN);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_DETAILS_EN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_DETAILS_GJ);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES_EN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES_GJ);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_EN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_GJ);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_IMAGES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_DETAILS_EN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_DETAILS_GJ);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSIFIED_EN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSIFIED_GJ);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT_EN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT_GJ);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMITTEE_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMITTEE_DETAILS_GJ);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMITTEE_MEMBERS_EN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMITTEE_MEMBERS_GJ);
            onCreate(db);
        }
    }
}