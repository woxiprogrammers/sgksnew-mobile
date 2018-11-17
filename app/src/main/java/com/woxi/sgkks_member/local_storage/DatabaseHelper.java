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
        String CREATE_FAMILY_DETAILS_TABLE_QUERY = "CREATE TABLE " + TABLE_FAMILY_DETAILS + " (" + COLUMN_FAMILY_ID_PRIMARY + " INTEGER PRIMARY KEY, "
                + COLUMN_FAMILY_SGKS_FAMILY_ID + " VARCHAR(500), " + COLUMN_FAMILY_SURNAME + " VARCHAR(500), " + COLUMN_FAMILY_NATIVE_PLACE + " VARCHAR(500), "
                + COLUMN_FAMILY_CITY + " VARCHAR(500))";
        new AppCommonMethods().LOG(0, TAG, CREATE_FAMILY_DETAILS_TABLE_QUERY);

        //+ COLUMN_MEMBER_PRIMARY_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        String CREATE_MEMBER_DETAILS_TABLE_QUERY = "CREATE TABLE " + TABLE_MEMBER_DETAILS
                + " (" + COLUMN_MEMBER_ID_PRIMARY + " VARCHAR(500) PRIMARY KEY, " + COLUMN_MEMBER_FAMILY_ID_FOREIGN_KEY + " VARCHAR(500), " + COLUMN_MEMBER_SGKS_MEMBER_ID
                + " VARCHAR(500), " + COLUMN_MEMBER_SGKS_FAMILY_ID + " VARCHAR(500), " + COLUMN_MEMBER_FIRST_NAME + " VARCHAR(500), " +
                COLUMN_MEMBER_MIDDLE_NAME + " VARCHAR(500), " + COLUMN_MEMBER_LAST_NAME + " VARCHAR(500), " + COLUMN_MEMBER_GENDER + " VARCHAR(500), "
                + COLUMN_MEMBER_MOBILE + " VARCHAR(500), " + COLUMN_MEMBER_EMAIL + " VARCHAR(500), " + COLUMN_MEMBER_BLOOD_GROUP + " VARCHAR(500), " +
                COLUMN_MEMBER_MARITAL_STATUS + " VARCHAR(500), " + COLUMN_MEMBER_SGKS_AREA + " VARCHAR(500), " + COLUMN_MEMBER_LATITUDE + " VARCHAR(500), "
                + COLUMN_MEMBER_LONGITUDE + " VARCHAR(500), " + COLUMN_MEMBER_SGKS_CITY + " VARCHAR(500), " + COLUMN_MEMBER_ADDRESS_ID + " VARCHAR(500), " + COLUMN_MEMBER_IMAGE_URL + " VARCHAR(500), "
                + COLUMN_MEMBER_IS_ACTIVE + " VARCHAR(500) )";
//                + "FOREIGN KEY(" + COLUMN_MEMBER_FAMILY_ID_FOREIGN_KEY + ") REFERENCES "
//                + TABLE_FAMILY_DETAILS + " (" + COLUMN_FAMILY_ID_PRIMARY + "))";
        new AppCommonMethods().LOG(0, TAG, CREATE_MEMBER_DETAILS_TABLE_QUERY);

        String CREATE_MEMBER_ADDRESS_TABLE_QUERY = "CREATE TABLE " + TABLE_MEM_ADDRESS_DETAILS + " (" + COLUMN_ADDRESS_PRIMARY_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ADDRESS_FAMILY_ID_FOREIGN_KEY + " VARCHAR(500), " + COLUMN_ADDRESS_ADDRESS_ID + " VARCHAR(500), " + COLUMN_ADDRESS_ADDRESS_LINE + " VARCHAR(500), " + COLUMN_ADDRESS_AREA + " VARCHAR(500), " +
                COLUMN_ADDRESS_LANDMARK + " VARCHAR(500), " + COLUMN_ADDRESS_CITY + " VARCHAR(500), " + COLUMN_ADDRESS_PINCODE + " VARCHAR(500), "
                + COLUMN_ADDRESS_STATE + " VARCHAR(500), " + COLUMN_ADDRESS_COUNTRY + " VARCHAR(500), " + "FOREIGN KEY(" + COLUMN_ADDRESS_FAMILY_ID_FOREIGN_KEY + ") REFERENCES " + TABLE_FAMILY_DETAILS + " (" + COLUMN_FAMILY_ID_PRIMARY + "))";
        new AppCommonMethods().LOG(0, TAG, CREATE_MEMBER_ADDRESS_TABLE_QUERY);

        //+ COLUMN_COMMITTEE_PRIMARY_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        String CREATE_COMMITTEE_DETAILS_TABLE_QUERY = "CREATE TABLE " + TABLE_COMMITTEE_DETAILS + " (" + COLUMN_COMMITTEE_ID_PRIMARY + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_COMMITTEE_NAME + " VARCHAR(500), " + COLUMN_COMMITTEE_DESCRIPTION + " TEXT, " + COLUMN_COMMITTEE_CITY + " VARCHAR(500), "
                + COLUMN_COMMITTEE_MEMBERS + "  TEXT, " + COLUMN_COMMITTEE_IS_ACTIVE + " VARCHAR(500))";
        new AppCommonMethods().LOG(0, TAG, CREATE_COMMITTEE_DETAILS_TABLE_QUERY);

        //+ COLUMN_MESSAGES_PRIMARY_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        String CREATE_MESSAGE_NEWS_DETAILS_TABLE_QUERY = "CREATE TABLE " + TABLE_MESSAGE_NEWS_DETAILS + " (" + COLUMN_MESSAGES_ID_PRIMARY + " VARCHAR(500) PRIMARY KEY, "
                + COLUMN_MESSAGES_TITLE + " VARCHAR(500), " + COLUMN_MESSAGES_DESCRIPTION + " TEXT, " + COLUMN_MESSAGES_IMAGE_URL + " VARCHAR(500), "
                + COLUMN_MESSAGES_CREATED_DATE + " VARCHAR(500), " + COLUMN_MESSAGES_TYPE + " VARCHAR(500), "
                + COLUMN_MESSAGES_CITY + " VARCHAR(500), " + COLUMN_MESSAGES_IS_ACTIVE + " VARCHAR(500))";
        new AppCommonMethods().LOG(0, TAG, CREATE_MESSAGE_NEWS_DETAILS_TABLE_QUERY);

        db.execSQL(CREATE_FAMILY_DETAILS_TABLE_QUERY);
        db.execSQL(CREATE_MEMBER_DETAILS_TABLE_QUERY);
        db.execSQL(CREATE_MEMBER_ADDRESS_TABLE_QUERY);
        db.execSQL(CREATE_COMMITTEE_DETAILS_TABLE_QUERY);
        db.execSQL(CREATE_MESSAGE_NEWS_DETAILS_TABLE_QUERY);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEM_ADDRESS_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILY_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMITTEE_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_NEWS_DETAILS);
            onCreate(db);
        }
    }
}