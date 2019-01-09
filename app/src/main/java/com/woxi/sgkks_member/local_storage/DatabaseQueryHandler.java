package com.woxi.sgkks_member.local_storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.interfaces.DatabaseConstants;
import com.woxi.sgkks_member.models.AccountDetailsItem;
import com.woxi.sgkks_member.models.AccountImages;
import com.woxi.sgkks_member.models.CityIteam;
import com.woxi.sgkks_member.models.ClassifiedDetailsItem;
import com.woxi.sgkks_member.models.CommMemberDetailsItem;
import com.woxi.sgkks_member.models.CommitteeDetailsItem;
import com.woxi.sgkks_member.models.CountItem;
import com.woxi.sgkks_member.models.EventDataItem;
import com.woxi.sgkks_member.models.FamilyDetailsItem;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.models.MessageDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LANGUAGE_APPLIED;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LAST_FAMILY_ID;

/**
 * <b><b>public class DatabaseQueryHandler implements DatabaseConstants </b></b>
 * <p>This class is used to create all Abstract methods related to local Database</p>
 * Created by Rohit.
 */
public class DatabaseQueryHandler implements DatabaseConstants {
    private SQLiteDatabase mSqLiteDatabase;
    private String TAG = "DatabaseQueryHandler";
    private Context mContext;

    public DatabaseQueryHandler() {
    }

    public DatabaseQueryHandler(Context context, boolean isWritable) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        mContext = context;
        if (isWritable) {
            mSqLiteDatabase = databaseHelper.getWritableDatabase();
        } else {
            mSqLiteDatabase = databaseHelper.getReadableDatabase();
        }
    }

    /*-------------------------------------------MEMBER LISTING-------------------------------------------*/

    public boolean insertOrUpdateAllMembersEnglish(ArrayList<MemberDetailsItem> arrMemDetails) {
        //SQL Prepared Statement
        String insertMemberPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_MEMBER_DETAILS_EN + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        /*INSERT INTO used to insert data/row in table*/
            /*"INSERT OR REPLACE INTO" used to insert/replace data/row in table using primary key only.
            doesn't allow to use WHERE clause. It works only on primary key
            * If primary key exists replace row (first delete and then insert row) else insert row directly*/
        /* UPDATE TABLE_NAME SET COLUMN_1=?, COLUMN_2=? WHERE COLUMN_3=?       used to replace selected values/columns from table using WHERE clause
         * It is helpful when table doesn't have a primary key column
         * OR It is helpful when table's primary key column if not useful*/

            /*UPDATE Example
            insertMemberPreparedStatement = "UPDATE " + DatabaseHelper.TABLE_MEMBER_DETAILS + " SET " + COLUMN_MEMBER_ID_PRIMARY + "=?, " + COLUMN_MEMBER_FAMILY_ID_FOREIGN_KEY + "=?, "
                    + COLUMN_MEMBER_SGKS_MEMBER_ID + "=?, " + COLUMN_MEMBER_SGKS_FAMILY_ID + "=?, " + COLUMN_MEMBER_FIRST_NAME + "=?, " + COLUMN_MEMBER_MIDDLE_NAME + "=?, "
                    + COLUMN_MEMBER_LAST_NAME + "=?, " + COLUMN_MEMBER_GENDER + "=?, " + COLUMN_MEMBER_MOBILE + "=?, " + COLUMN_MEMBER_EMAIL + "=?, " + COLUMN_MEMBER_BLOOD_GROUP + "=?, "
                    + COLUMN_MEMBER_MARITAL_STATUS + "=?, " + COLUMN_MEMBER_SGKS_AREA + "=?, " + COLUMN_MEMBER_LATITUDE + "=?, " + COLUMN_MEMBER_LONGITUDE + "=?, "
                    + COLUMN_MEMBER_SGKS_CITY + "=?, " + COLUMN_MEMBER_ADDRESS_ID + "=?, " + COLUMN_MEMBER_IMAGE_URL + "=? "
                    + " WHERE " + COLUMN_MEMBER_ID_PRIMARY + "=?";*/

        SQLiteStatement memberStatement = mSqLiteDatabase.compileStatement(insertMemberPreparedStatement);
        mSqLiteDatabase.beginTransaction();
        MemberDetailsItem memberDetailsItem;
        try {
            for (int arrMemberIndex = 0; arrMemberIndex < arrMemDetails.size(); arrMemberIndex++) {
                memberDetailsItem = arrMemDetails.get(arrMemberIndex);
                memberStatement.clearBindings();
                if (memberDetailsItem.getStrId() != null) {
                    memberStatement.bindString(1, memberDetailsItem.getStrId());
                }
                if (memberDetailsItem.getStrFirstName() != null) {
                    memberStatement.bindString(2, memberDetailsItem.getStrFirstName());
                }
                if (memberDetailsItem.getStrMiddleName() != null) {
                    memberStatement.bindString(3, memberDetailsItem.getStrMiddleName());
                }
                if (memberDetailsItem.getStrLastName() != null) {
                    memberStatement.bindString(4, memberDetailsItem.getStrLastName());
                }
                if (memberDetailsItem.getStrAddress() != null) {
                    memberStatement.bindString(5, memberDetailsItem.getStrAddress());
                }
                if (memberDetailsItem.getStrCity() != null) {
                    memberStatement.bindString(6, memberDetailsItem.getStrCity());
                }
                if (memberDetailsItem.getStrCityId() != null) {
                    memberStatement.bindString(7, memberDetailsItem.getStrCityId());
                }
                if (memberDetailsItem.getStrGender() != null) {
                    memberStatement.bindString(8, memberDetailsItem.getStrGender());
                }
                if (memberDetailsItem.getStrMobileNumber() != null) {
                    memberStatement.bindString(9, memberDetailsItem.getStrMobileNumber());
                }
                if (memberDetailsItem.getStrDateOfBirth() != null) {
                    memberStatement.bindString(10, memberDetailsItem.getStrDateOfBirth());
                }
                if (memberDetailsItem.getStrEmail() != null) {
                    memberStatement.bindString(11, memberDetailsItem.getStrEmail());
                }
                if (memberDetailsItem.getStrBloodGroup() != null) {
                    memberStatement.bindString(12, memberDetailsItem.getStrBloodGroup());
                }
                if (memberDetailsItem.getStrBloodGroupId() != null) {
                    memberStatement.bindString(13, memberDetailsItem.getStrBloodGroupId());
                }
                if (memberDetailsItem.getStrLatitude() != null) {
                    memberStatement.bindString(14, memberDetailsItem.getStrLatitude());
                }
                if (memberDetailsItem.getStrLongitude() != null) {
                    memberStatement.bindString(15, memberDetailsItem.getStrLongitude());
                }
                if (memberDetailsItem.getStrMemberImageUrl() != null) {
                    memberStatement.bindString(16, memberDetailsItem.getStrMemberImageUrl());
                }
                if (memberDetailsItem.getBolIsActive() != null) {
                    memberStatement.bindString(17, memberDetailsItem.getBolIsActive());
                }
                memberStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean insertOrUpdateAllMembersGujarati(ArrayList<MemberDetailsItem> arrMemDetails) {
        //SQL Prepared Statement
        String insertMemberPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_MEMBER_DETAILS_GJ + " VALUES (?,?,?,?,?,?,?)";

        /*INSERT INTO used to insert data/row in table*/
            /*"INSERT OR REPLACE INTO" used to insert/replace data/row in table using primary key only.
            doesn't allow to use WHERE clause. It works only on primary key
            * If primary key exists replace row (first delete and then insert row) else insert row directly*/
        /* UPDATE TABLE_NAME SET COLUMN_1=?, COLUMN_2=? WHERE COLUMN_3=?       used to replace selected values/columns from table using WHERE clause
         * It is helpful when table doesn't have a primary key column
         * OR It is helpful when table's primary key column if not useful*/

            /*UPDATE Example
            insertMemberPreparedStatement = "UPDATE " + DatabaseHelper.TABLE_MEMBER_DETAILS + " SET " + COLUMN_MEMBER_ID_PRIMARY + "=?, " + COLUMN_MEMBER_FAMILY_ID_FOREIGN_KEY + "=?, "
                    + COLUMN_MEMBER_SGKS_MEMBER_ID + "=?, " + COLUMN_MEMBER_SGKS_FAMILY_ID + "=?, " + COLUMN_MEMBER_FIRST_NAME + "=?, " + COLUMN_MEMBER_MIDDLE_NAME + "=?, "
                    + COLUMN_MEMBER_LAST_NAME + "=?, " + COLUMN_MEMBER_GENDER + "=?, " + COLUMN_MEMBER_MOBILE + "=?, " + COLUMN_MEMBER_EMAIL + "=?, " + COLUMN_MEMBER_BLOOD_GROUP + "=?, "
                    + COLUMN_MEMBER_MARITAL_STATUS + "=?, " + COLUMN_MEMBER_SGKS_AREA + "=?, " + COLUMN_MEMBER_LATITUDE + "=?, " + COLUMN_MEMBER_LONGITUDE + "=?, "
                    + COLUMN_MEMBER_SGKS_CITY + "=?, " + COLUMN_MEMBER_ADDRESS_ID + "=?, " + COLUMN_MEMBER_IMAGE_URL + "=? "
                    + " WHERE " + COLUMN_MEMBER_ID_PRIMARY + "=?";*/

        SQLiteStatement memberStatement = mSqLiteDatabase.compileStatement(insertMemberPreparedStatement);
        mSqLiteDatabase.beginTransaction();
        MemberDetailsItem memberDetailsItem;
        try {
            for (int arrMemberIndex = 0; arrMemberIndex < arrMemDetails.size(); arrMemberIndex++) {
                memberDetailsItem = arrMemDetails.get(arrMemberIndex);
                memberStatement.clearBindings();
                if (memberDetailsItem.getStrId() != null) {
                    memberStatement.bindString(1, memberDetailsItem.getStrId());
                }
                if (memberDetailsItem.getStrFirstName() != null) {
                    memberStatement.bindString(2, memberDetailsItem.getStrFirstName());
                }
                if (memberDetailsItem.getStrMiddleName() != null) {
                    memberStatement.bindString(3, memberDetailsItem.getStrMiddleName());
                }
                if (memberDetailsItem.getStrLastName() != null) {
                    memberStatement.bindString(4, memberDetailsItem.getStrLastName());
                }
                if (memberDetailsItem.getStrAddress() != null) {
                    memberStatement.bindString(5, memberDetailsItem.getStrAddress());
                }
                if (memberDetailsItem.getStrLanguageId() != null) {
                    memberStatement.bindString(6, memberDetailsItem.getStrLanguageId());
                }
                if (memberDetailsItem.getStrMemberId() != null) {
                    memberStatement.bindString(7, memberDetailsItem.getStrMemberId());
                }
                memberStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public ArrayList<MemberDetailsItem> queryMembers(String searchString , int pageNumber) {
        int intLimit = 10;
        int intOffset = intLimit * pageNumber;
        ArrayList<MemberDetailsItem> arrMemDetails = new ArrayList<>();
        String sqlQuery = "";
        //FETCH ACTIVE MEMBER ID's
        String fetchMemberId = "";
        Cursor cursorMemberId = null;
        if (searchString.equalsIgnoreCase("")){
            Log.i(TAG, "Display all records");
            sqlQuery = "SELECT * FROM "
                    +TABLE_MEMBER_DETAILS_EN
                    + " WHERE "+COLUMN_MEMBER_SGKS_CITY_ID+"="+AppCommonMethods.getStringPref(AppConstants.PREFS_CURRENT_CITY,mContext)
                    + " AND "+COLUMN_MEMBER_IS_ACTIVE+"='true'"
                    
                    + " LIMIT " + intLimit + " OFFSET "+ intOffset;
            new AppCommonMethods(mContext).LOG(0,TAG,sqlQuery);
        }  else if (searchString.matches("^[A-Za-z]*$")) {
            //TODO Search in name OR surname column
            Log.d(TAG, "search by: Name AND Surname");
            sqlQuery = "SELECT * FROM "
                    +TABLE_MEMBER_DETAILS_EN
                    + " WHERE "+COLUMN_MEMBER_SGKS_CITY_ID+"="+AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext)
                    + " AND "+ COLUMN_MEMBER_FIRST_NAME +" LIKE " + "'%" + searchString + "%'"
                    + " OR " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE " + "'%" + searchString + "%'"
                    + " OR " + COLUMN_MEMBER_LAST_NAME + " LIKE " + "'%" + searchString + "%'"
                    + " AND "+COLUMN_MEMBER_IS_ACTIVE+"='true'"
                    
                    + " LIMIT " + intLimit + " OFFSET "+ intOffset;

            new AppCommonMethods(mContext).LOG(0,TAG,sqlQuery);
        } else if (searchString.contains(" ")) {
            //TODO Search in name & surname column and vice-versa
            String[] splitStrings = searchString.split("\\s+");
            String firstString, secondString, thirdString;
            if (splitStrings.length == 1) {
                Log.d(TAG, "search by: Name");
                firstString = splitStrings[0].trim();
                sqlQuery = "SELECT * FROM "
                        + TABLE_MEMBER_DETAILS_EN
                        + " WHERE " + COLUMN_MEMBER_FIRST_NAME + " like '%" + firstString + "%'"
                        + " AND "+COLUMN_MEMBER_IS_ACTIVE+"='true'"
                        
                        + " LIMIT " + intLimit + " OFFSET "+ intOffset;
                new AppCommonMethods(mContext).LOG(0, TAG, sqlQuery);
            }
            if (splitStrings.length == 2) {
                Log.d(TAG, "search by: Name Surname");
                firstString = splitStrings[0].trim();
                secondString = splitStrings[1].trim();
                sqlQuery = "SELECT * FROM "
                        + TABLE_MEMBER_DETAILS_EN
                        + " WHERE " + COLUMN_MEMBER_FIRST_NAME + " like '%" + firstString + "%'"
                        + " OR " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE " + "'%" + secondString + "%'"
                        + " AND "+COLUMN_MEMBER_IS_ACTIVE+"='true'"
                        
                        + " LIMIT " + intLimit + " OFFSET "+ intOffset;
                new AppCommonMethods(mContext).LOG(0, TAG, sqlQuery);
            }
            if (splitStrings.length == 3) {
                Log.d(TAG, "search by: FullName");
                firstString = splitStrings[0].trim();
                secondString = splitStrings[1].trim();
                thirdString = splitStrings[2].trim();
                sqlQuery = "SELECT * FROM "
                        + TABLE_MEMBER_DETAILS_EN
                        + " WHERE " + COLUMN_MEMBER_FIRST_NAME + " LIKE " + "'%" + firstString + "%'"
                        + " OR " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE " + "'%" + secondString + "%'"
                        + " OR " + COLUMN_MEMBER_LAST_NAME + " LIKE " + "'%" + thirdString + "%'"
                        + " AND "+COLUMN_MEMBER_IS_ACTIVE+"='true'"
                        
                        + " LIMIT " + intLimit + " OFFSET "+ intOffset;
                new AppCommonMethods(mContext).LOG(0, TAG, sqlQuery);
            }
        }
        Cursor cursorMember = mSqLiteDatabase.rawQuery(sqlQuery,null);
        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")){
            if (cursorMember.moveToFirst()) {
                do {
                    MemberDetailsItem memberDetailsItem = new MemberDetailsItem();
                    memberDetailsItem.setStrId(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_ID_PRIMARY_EN)));
                    memberDetailsItem.setStrFirstName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_FIRST_NAME)));
                    memberDetailsItem.setStrMiddleName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_MIDDLE_NAME)));
                    memberDetailsItem.setStrLastName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LAST_NAME)));
                    memberDetailsItem.setStrAddress(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)));
                    memberDetailsItem.setStrCity(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_SGKS_CITY)));
                    memberDetailsItem.setStrCityId(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_SGKS_CITY_ID)));
                    memberDetailsItem.setStrGender(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_GENDER)));
                    memberDetailsItem.setStrMobileNumber(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_MOBILE)));
                    memberDetailsItem.setStrDateOfBirth(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_DATE_OF_BIRTH)));
                    memberDetailsItem.setStrEmail(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_EMAIL)));
                    memberDetailsItem.setStrBloodGroup(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_BLOOD_GROUP)));
                    memberDetailsItem.setStrBloodGroupId(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_BLOOD_GROUP_ID)));
                    memberDetailsItem.setStrLatitude(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LATITUDE)));
                    memberDetailsItem.setStrLongitude(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LONGITUDE)));
                    memberDetailsItem.setStrMemberImageUrl(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_IMAGE_URL)));
                    arrMemDetails.add(memberDetailsItem);
                } while (cursorMember.moveToNext());
            }
        } else if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2")){
            if (searchString.equalsIgnoreCase("")){
                Log.i(TAG, "Display all records");
                fetchMemberId = "SELECT " + COLUMN_MEMBER_ID_PRIMARY_EN + " FROM " + TABLE_MEMBER_DETAILS_EN
                        + " WHERE "+ COLUMN_MEMBER_IS_ACTIVE +"='true'"
                        + " AND " + COLUMN_MEMBER_SGKS_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) +"'"
                        
                        + " LIMIT " + intLimit + " OFFSET " + intOffset ;
                new AppCommonMethods(mContext).LOG(0,TAG,sqlQuery);
            }  else if (searchString.matches("^[A-Za-z]*$")) {
                //TODO Search in name OR surname column
                Log.d(TAG, "search by: Name AND Surname");
                fetchMemberId = "SELECT " + COLUMN_MEMBER_ID_PRIMARY_EN + " FROM " + TABLE_MEMBER_DETAILS_EN
                        + " WHERE "+ COLUMN_MEMBER_IS_ACTIVE +"='true'"
                        + " AND " + COLUMN_MEMBER_SGKS_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) +"'"
                        + " AND "+ COLUMN_MEMBER_FIRST_NAME +" LIKE " + "'%" + searchString + "%'"
                        + " OR " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE " + "'%" + searchString + "%'"
                        + " OR " + COLUMN_MEMBER_LAST_NAME + " LIKE " + "'%" + searchString + "%'"
                        
                        + " LIMIT " + intLimit + " OFFSET " + intOffset ;
                new AppCommonMethods(mContext).LOG(0,TAG,sqlQuery);
            } else if (searchString.contains(" ")) {
                //TODO Search in name & surname column and vice-versa
                String[] splitStrings = searchString.split("\\s+");
                String firstString, secondString, thirdString;
                if (splitStrings.length == 1) {
                    Log.d(TAG, "search by: Name");
                    firstString = splitStrings[0].trim();
                    fetchMemberId = "SELECT " + COLUMN_MEMBER_ID_PRIMARY_EN + " FROM " + TABLE_MEMBER_DETAILS_EN
                            + " WHERE "+ COLUMN_MEMBER_IS_ACTIVE +"='true'"
                            + " AND " + COLUMN_MEMBER_SGKS_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) +"'"
                            + " AND "+ COLUMN_MEMBER_FIRST_NAME +" LIKE " + "'%" + firstString + "%'"
                            + " OR " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE " + "'%" + firstString + "%'"
                            + " OR " + COLUMN_MEMBER_LAST_NAME + " LIKE " + "'%" + firstString + "%'"
                            
                            + " LIMIT " + intLimit + " OFFSET " + intOffset ;
                }
                if (splitStrings.length == 2) {
                    Log.d(TAG, "search by: Name Surname");
                    firstString = splitStrings[0].trim();
                    secondString = splitStrings[1].trim();
                    fetchMemberId = "SELECT " + COLUMN_MEMBER_ID_PRIMARY_EN + " FROM " + TABLE_MEMBER_DETAILS_EN
                            + " WHERE "+ COLUMN_MEMBER_IS_ACTIVE +"='true'"
                            + " AND " + COLUMN_MEMBER_SGKS_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) +"'"
                            + " WHERE " + COLUMN_MEMBER_FIRST_NAME + " like '%" + firstString + "%'"
                            + " OR " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE " + "'%" + secondString + "%'"
                            
                            + " LIMIT " + intLimit + " OFFSET " + intOffset ;
                }
                if (splitStrings.length == 3) {
                    Log.d(TAG, "search by: FullName");
                    firstString = splitStrings[0].trim();
                    secondString = splitStrings[1].trim();
                    thirdString = splitStrings[2].trim();
                    fetchMemberId = "SELECT " + COLUMN_MEMBER_ID_PRIMARY_EN + " FROM " + TABLE_MEMBER_DETAILS_EN
                            + " WHERE "+ COLUMN_MEMBER_IS_ACTIVE +"='true'"
                            + " AND " + COLUMN_MEMBER_SGKS_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) +"'"
                            + " AND " + COLUMN_MEMBER_FIRST_NAME + " LIKE " + "'%" + firstString + "%'"
                            + " OR " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE " + "'%" + secondString + "%'"
                            + " OR " + COLUMN_MEMBER_LAST_NAME + " LIKE " + "'%" + thirdString + "%'"
                            
                            + " LIMIT " + intLimit + " OFFSET " + intOffset ;
                    sqlQuery = "SELECT * FROM "
                            + TABLE_MEMBER_DETAILS_EN
                            + " WHERE " + COLUMN_MEMBER_FIRST_NAME + " LIKE " + "'%" + firstString + "%'"
                            + " OR " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE " + "'%" + secondString + "%'"
                            + " OR " + COLUMN_MEMBER_LAST_NAME + " LIKE " + "'%" + thirdString + "%'"
                            + " AND "+COLUMN_MEMBER_IS_ACTIVE+"='true'"
                            
                            + " LIMIT " + intLimit + " OFFSET "+ intOffset;
                    new AppCommonMethods(mContext).LOG(0, TAG, sqlQuery);
                }
            }

            cursorMemberId = mSqLiteDatabase.rawQuery(fetchMemberId,null);
            if (cursorMemberId.moveToFirst()){
                do {
                    sqlQuery = "SELECT * FROM "
                            + TABLE_MEMBER_DETAILS_EN
                            + " WHERE " + COLUMN_MEMBER_ID_PRIMARY_EN + "='" + cursorMemberId.getString(cursorMemberId.getColumnIndexOrThrow(COLUMN_MEMBER_ID_PRIMARY_EN)) +"'"
                            ;
                    cursorMember = mSqLiteDatabase.rawQuery(sqlQuery,null);
                    String sqlMemberGujarati = "SELECT * FROM " + DatabaseHelper.TABLE_MEMBER_DETAILS_GJ
                            + " WHERE " + COLUMN_MEMBER_ID_FOREIGN_KEY + "='" + cursorMemberId.getString(cursorMemberId.getColumnIndexOrThrow(COLUMN_MEMBER_ID_PRIMARY_EN)) +"'"
                            + " ORDER BY " + COLUMN_MEMBER_ID_FOREIGN_KEY + " DESC ";
                    new AppCommonMethods(mContext).LOG(0,TAG,sqlMemberGujarati);
                    Cursor cursorGujarati = mSqLiteDatabase.rawQuery(sqlMemberGujarati,null);
                    if ((cursorGujarati.moveToFirst() && cursorMember.moveToFirst()) || (cursorGujarati.getCount() == 0 && cursorMember.moveToFirst())){
                        //do {
                        MemberDetailsItem memberDetailsItem = new MemberDetailsItem();
                        //Check if first name is present in Gujarati
                        if (cursorGujarati.getCount() != 0) {
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_FIRST_NAME)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_FIRST_NAME)).equalsIgnoreCase("null")) {
                                memberDetailsItem.setStrFirstName(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_FIRST_NAME)));
                            } else {
                                memberDetailsItem.setStrFirstName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_FIRST_NAME)));
                            }
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_MIDDLE_NAME)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_MIDDLE_NAME)).equalsIgnoreCase("null")) {
                                memberDetailsItem.setStrMiddleName(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_MIDDLE_NAME)));
                            } else {
                                memberDetailsItem.setStrMiddleName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_MIDDLE_NAME)));
                            }
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_LAST_NAME)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_LAST_NAME)).equalsIgnoreCase("null")) {
                                memberDetailsItem.setStrLastName(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_LAST_NAME)));
                            } else {
                                memberDetailsItem.setStrLastName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LAST_NAME)));
                            }
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)).equalsIgnoreCase("null")) {
                                memberDetailsItem.setStrAddress(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)));
                            } else {
                                memberDetailsItem.setStrAddress(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)));
                            }
                        } else {
                            memberDetailsItem.setStrFirstName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_FIRST_NAME)));
                            memberDetailsItem.setStrMiddleName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_MIDDLE_NAME)));
                            memberDetailsItem.setStrLastName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LAST_NAME)));
                            memberDetailsItem.setStrAddress(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS)));
                        }
                        memberDetailsItem.setStrCity(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_SGKS_CITY)));
                        memberDetailsItem.setStrCityId(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_SGKS_CITY_ID)));
                        memberDetailsItem.setStrGender(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_GENDER)));
                        memberDetailsItem.setStrMobileNumber(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_MOBILE)));
                        memberDetailsItem.setStrDateOfBirth(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_DATE_OF_BIRTH)));
                        memberDetailsItem.setStrEmail(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_EMAIL)));
                        memberDetailsItem.setStrBloodGroup(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_BLOOD_GROUP)));
                        memberDetailsItem.setStrBloodGroupId(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_BLOOD_GROUP_ID)));
                        memberDetailsItem.setStrLatitude(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LATITUDE)));
                        memberDetailsItem.setStrLongitude(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LONGITUDE)));
                        memberDetailsItem.setStrMemberImageUrl(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_IMAGE_URL)));
                        arrMemDetails.add(memberDetailsItem);
                    } while ((cursorGujarati.moveToNext() && cursorMember.moveToNext()) || (cursorMember.moveToNext()));

                    if (cursorGujarati != null && !cursorGujarati.isClosed()) {
                        cursorGujarati.close();
                    }
                } while (cursorMemberId.moveToNext());
            }
        }
        if (cursorMember != null && !cursorMember.isClosed()) {
            cursorMember.close();
        }
        if (cursorMemberId != null && !cursorMemberId.isClosed()) {
            cursorMemberId.close();
        }
        return arrMemDetails;
    }

    /*-------------------------------------------MEMBER LISTING-------------------------------------------*/

    /*-------------------------------------------CITY LISTING-------------------------------------------*/

    public boolean insertOrUpdateCitiesEnglish(ArrayList<CityIteam> arrCityItems){
        String inserCityPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_CITIES_EN + " VALUES (?,?,?,?,?)";
        SQLiteStatement cityStatement = mSqLiteDatabase.compileStatement(inserCityPreparedStatement);
        mSqLiteDatabase.beginTransaction();
        CityIteam cityIteam;
        try{
            for (int arrCityIndex = 0; arrCityIndex < arrCityItems.size(); arrCityIndex++){
                cityIteam = arrCityItems.get(arrCityIndex);
                cityStatement.clearBindings();
                if (cityIteam.getIntCityId() != -1){
                    cityStatement.bindString(1,String.valueOf(cityIteam.getIntCityId()));
                }
                if (cityIteam.getStrCityName() != null){
                    cityStatement.bindString(2,cityIteam.getStrCityName());
                }
                if (cityIteam.getStrStateId() != null){
                    cityStatement.bindString(3,cityIteam.getStrStateId());
                }
                if (cityIteam.getIs_active() != null){
                    cityStatement.bindString(4,cityIteam.getIs_active());
                }
                if (cityIteam.getStrMemberCount() != null){
                    cityStatement.bindString(5,cityIteam.getStrMemberCount());
                }
                cityStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean insertOrUpdateCitiesGujarati(ArrayList<CityIteam> arrCityItems){
        String insertCityPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_CITIES_GJ + " VALUES (?,?,?,?)";
        SQLiteStatement cityStatement = mSqLiteDatabase.compileStatement(insertCityPreparedStatement);
        mSqLiteDatabase.beginTransaction();
        CityIteam cityIteam;
        try{
            for (int arrCityIndex = 0; arrCityIndex < arrCityItems.size(); arrCityIndex++){
                cityIteam = arrCityItems.get(arrCityIndex);
                cityStatement.clearBindings();
                if (cityIteam.getId() != null){
                    cityStatement.bindString(1,cityIteam.getId());
                }
                if (cityIteam.getStrCityName() != null){
                    cityStatement.bindString(2,cityIteam.getStrCityName());
                }
                if (cityIteam.getIntCityId() != -1){
                    cityStatement.bindString(3,String.valueOf(cityIteam.getIntCityId()));
                }
                if (cityIteam.getLanguageId() != null){
                    cityStatement.bindString(4,cityIteam.getLanguageId());
                }
                cityStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public ArrayList<CityIteam> queryCities(String strSearchKey){
        ArrayList<CityIteam> arrCityIteam = new ArrayList<>();

        //FETCH CITY ENGLISH NAMES


        //FETCH CITY ID
        String fetchCityId = "";
        if (strSearchKey.equalsIgnoreCase("")) {
            fetchCityId = "SELECT " + COLUMN_CITY_ID_PRIMARY_EN + " FROM " + TABLE_CITIES_EN
                    + " WHERE " + COLUMN_CITY_IS_ACTIVE + "='true'"
                    + " ORDER BY " + COLUMN_CITY_NAME + " ASC ";;
        } else {
            fetchCityId = "SELECT " + COLUMN_CITY_ID_PRIMARY_EN + " FROM " + TABLE_CITIES_EN
                    + " WHERE " + COLUMN_CITY_IS_ACTIVE + "='true'"
                    + " AND " + COLUMN_CITY_NAME + " LIKE '%" + strSearchKey + "%'"
                    + " ORDER BY " + COLUMN_CITY_NAME + " ASC ";
        }
        new AppCommonMethods(mContext).LOG(0, TAG, fetchCityId);
        Cursor cursorCityId = mSqLiteDatabase.rawQuery(fetchCityId,null);

        //SET arrCityIteam WITH RESPECTIVE LANGUAGE
        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1") /*&& strSearchKey.equalsIgnoreCase("")*/){
            if (cursorCityId.moveToFirst()) {
                do {
                    String sqlQueryEnglish = "";
                    if (strSearchKey.equalsIgnoreCase("")){
                        sqlQueryEnglish = "SELECT * FROM "+ TABLE_CITIES_EN
                                + " WHERE " + COLUMN_CITY_IS_ACTIVE + "='true'"
                                + " AND " + COLUMN_CITY_ID_PRIMARY_EN + "='" + cursorCityId.getString(cursorCityId.getColumnIndexOrThrow(COLUMN_CITY_ID_PRIMARY_EN)) + "'"
                                + " ORDER BY " + COLUMN_CITY_NAME + " ASC ";
                    } else {
                        sqlQueryEnglish = "SELECT * FROM "+ TABLE_CITIES_EN
                                + " WHERE " + COLUMN_CITY_NAME + " LIKE '%" + strSearchKey + "%'"
                                + " AND " + COLUMN_CITY_ID_PRIMARY_EN + "='" + cursorCityId.getString(cursorCityId.getColumnIndexOrThrow(COLUMN_CITY_ID_PRIMARY_EN)) + "'"
                                + " AND " + COLUMN_CITY_IS_ACTIVE + "='true'"
                                + " ORDER BY " + COLUMN_CITY_NAME + " ASC ";;
                    }

                    new AppCommonMethods(mContext).LOG(0, TAG, sqlQueryEnglish);
                    Cursor cityCursor = mSqLiteDatabase.rawQuery(sqlQueryEnglish,null);
                    String sqlQueryGujarati = "SELECT " + COLUMN_CITY_NAME + " FROM " + TABLE_CITIES_GJ
                            + " WHERE " + COLUMN_CITY_ID_FOREIGN + "='" + cursorCityId.getString(cursorCityId.getColumnIndexOrThrow(COLUMN_CITY_ID_PRIMARY_EN)) + "'";
                    Cursor cursorGujarati = mSqLiteDatabase.rawQuery(sqlQueryGujarati,null);
                    if ((cityCursor.moveToFirst() && cursorGujarati.moveToFirst())) {
                        do {
                            CityIteam cityIteam = new CityIteam();
                            cityIteam.setIntCityId(Integer.parseInt(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_ID_PRIMARY_EN))));
                            cityIteam.setStrCityName(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                            cityIteam.setStrCityNameEnglish(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                            cityIteam.setStrMemberCount(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_MEMBER_COUNT)));
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CITY_NAME)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CITY_NAME)).equalsIgnoreCase("null")) {
                                cityIteam.setStrCityNameGujarati(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                            } else {
                                cityIteam.setStrCityNameGujarati(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                            }
                            arrCityIteam.add(cityIteam);
                        } while ((cityCursor.moveToNext() && cursorGujarati.moveToNext()));
                    }
                    if (cursorGujarati != null && !cursorGujarati.isClosed()) {
                        cursorGujarati.close();
                    }
                    if (cityCursor != null && !cityCursor.isClosed()) {
                        cityCursor.close();
                    }
                } while (cursorCityId.moveToNext());
            }
        } else if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2") /*&& strSearchKey.equalsIgnoreCase("")*/){
            if (cursorCityId.moveToFirst()) {
                do {
                    String sqlQueryEnglish="SELECT * FROM "+ TABLE_CITIES_EN
                            + " WHERE " + COLUMN_CITY_IS_ACTIVE + "='true'"
                            + " AND " + COLUMN_CITY_ID_PRIMARY_EN + "='" + cursorCityId.getString(cursorCityId.getColumnIndexOrThrow(COLUMN_CITY_ID_PRIMARY_EN)) + "'";
                    new AppCommonMethods(mContext).LOG(0, TAG, sqlQueryEnglish);
                    Cursor cityCursor = mSqLiteDatabase.rawQuery(sqlQueryEnglish,null);

                    String sqlQueryGujarati = "SELECT " + COLUMN_CITY_NAME + " FROM " + TABLE_CITIES_GJ
                            + " WHERE " + COLUMN_CITY_ID_FOREIGN + "='" + cursorCityId.getString(cursorCityId.getColumnIndexOrThrow(COLUMN_CITY_ID_PRIMARY_EN)) + "'";
                    Cursor cursorGujarati = mSqLiteDatabase.rawQuery(sqlQueryGujarati,null);
                    if (cursorGujarati.moveToFirst() && cityCursor.moveToFirst()) {
                        do {
                            CityIteam cityIteam = new CityIteam();
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CITY_NAME)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CITY_NAME)).equalsIgnoreCase("null")) {
                                cityIteam.setStrCityName(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                                cityIteam.setStrCityNameGujarati(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                            } else {
                                cityIteam.setStrCityName(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                            }
                            cityIteam.setIntCityId(Integer.parseInt(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_ID_PRIMARY_EN))));
                            cityIteam.setStrCityNameEnglish(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                            cityIteam.setStrMemberCount(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_MEMBER_COUNT)));
                            arrCityIteam.add(cityIteam);
                        } while (cursorGujarati.moveToNext() && cityCursor.moveToNext());
                    }
                    if (cursorGujarati != null && !cursorGujarati.isClosed()) {
                        cursorGujarati.close();
                    }
                } while (cursorCityId.moveToNext());
            }
        }
        if (cursorCityId !=null && !cursorCityId.isClosed()){
            cursorCityId.close();
        }
        return  arrCityIteam;
    }

    /*-------------------------------------------CITY LISTING-------------------------------------------*/

    /*-------------------------------------------EVENTS LISTING-------------------------------------------*/

    public boolean insertOrUpdateEventsEnglish(ArrayList<EventDataItem> arrayListEvent){
        String insertEventPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_EVENTS_EN + " VALUES (?,?,?,?,?,?,?,?,?)";
        SQLiteStatement eventStatement = mSqLiteDatabase.compileStatement(insertEventPreparedStatement);
        mSqLiteDatabase.beginTransaction();
        EventDataItem eventDataItem;
        try{
            for (int arrEventIndex = 0; arrEventIndex < arrayListEvent.size(); arrEventIndex++) {
                eventDataItem = arrayListEvent.get(arrEventIndex);
                if(eventDataItem.getStrEventId() != null){
                    eventStatement.bindString(1,eventDataItem.getStrEventId());
                }
                if (eventDataItem.getEventName() != null){
                    eventStatement.bindString(2,eventDataItem.getEventName());
                }
                if (eventDataItem.getEventDescription() != null){
                    eventStatement.bindString(3, eventDataItem.getEventDescription());
                }
                if (eventDataItem.getVenue() != null){
                    eventStatement.bindString(4, eventDataItem.getVenue());
                }
                if (eventDataItem.getEventDate() != null){
                    eventStatement.bindString(5, eventDataItem.getEventDate());
                }
                if (eventDataItem.getCity() != null){
                    eventStatement.bindString(6, eventDataItem.getCity());
                }
                if (eventDataItem.getCitryId() != null) {
                    eventStatement.bindString(7,eventDataItem.getCitryId());
                }
                if (eventDataItem.getStrIsActive() != null) {
                    eventStatement.bindString(8, eventDataItem.getStrIsActive());
                }
                if (eventDataItem.getEventYear() != null){
                    eventStatement.bindString(9,eventDataItem.getEventYear());
                }
                eventStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean insertOrUpdateEventGujarati(ArrayList<EventDataItem> arrayListEvent) {
        String insertEventPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_EVENTS_GJ + " VALUES (?,?,?,?,?,?)";
        SQLiteStatement eventStatement = mSqLiteDatabase.compileStatement(insertEventPreparedStatement);
        mSqLiteDatabase.beginTransaction();
        EventDataItem eventDataItem;
        try{
            for (int arrEventIndex = 0; arrEventIndex < arrayListEvent.size(); arrEventIndex++) {
                eventDataItem = arrayListEvent.get(arrEventIndex);
                if(eventDataItem.getStrId() != null){
                    eventStatement.bindString(1,eventDataItem.getStrId());
                }
                if (eventDataItem.getEventName() != null){
                    eventStatement.bindString(2,eventDataItem.getEventName());
                }
                if (eventDataItem.getEventDescription() != null){
                    eventStatement.bindString(3, eventDataItem.getEventDescription());
                }
                if (eventDataItem.getVenue() != null){
                    eventStatement.bindString(4, eventDataItem.getVenue());
                }
                if (eventDataItem.getStrlanguageId() != null){
                    eventStatement.bindString(5, eventDataItem.getStrlanguageId());
                }
                if (eventDataItem.getStrEventId() != null){
                    eventStatement.bindString(6, eventDataItem.getStrEventId());
                }
                eventStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public ArrayList<EventDataItem> queryEvents(String year){
        ArrayList<EventDataItem> arrayListEvents = new ArrayList<>();
        ArrayList<String> arrImage = new ArrayList<>();
        String sqlEventEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_EVENTS_EN
                + " WHERE " + COLUMN_EVENT_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                + " AND " + COLUMN_EVENT_IS_ACTIVE + "='true'"
                + " AND " + COLUMN_EVENT_YEAR + "='" + year + "'"
                + " ORDER BY " + COLUMN_EVENT_ID_PRIMARY_KEY + " DESC ";
        Cursor cursorEnglish = mSqLiteDatabase.rawQuery(sqlEventEnglish,null);
        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")) {
            if (cursorEnglish.moveToFirst()) {
                do {
                    EventDataItem eventDataItem = new EventDataItem();
                    eventDataItem.setEventName(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_NAME)));
                    eventDataItem.setEventDescription(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)));
                    eventDataItem.setVenue(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_VENUE)));
                    eventDataItem.setEventDate(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_DATE)));
                    eventDataItem.setCity(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_CITY)));
                    arrImage.add("");
                    eventDataItem.setArrEventImageURLs(arrImage);
                    arrayListEvents.add(eventDataItem);
                } while (cursorEnglish.moveToNext());
            }
            if (cursorEnglish !=null && !cursorEnglish.isClosed()){
                cursorEnglish.close();
            }
        } else if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2")) {
            String fetchEventId = "SELECT " + COLUMN_EVENT_ID_PRIMARY_KEY + " FROM " + TABLE_EVENTS_EN
                    + " WHERE " + COLUMN_EVENT_IS_ACTIVE + "='true'"
                    + " AND " + COLUMN_EVENT_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                    + " AND " + COLUMN_EVENT_YEAR + "='" + year + "'"
                    + " ORDER BY " + COLUMN_EVENT_ID_PRIMARY_KEY + " DESC ";
            Cursor cursorEventId = mSqLiteDatabase.rawQuery(fetchEventId,null);
            if (cursorEventId.moveToFirst()) {
                do {
                    String sqlEventGujarati = "SELECT * FROM " + DatabaseHelper.TABLE_EVENTS_GJ
                            + " WHERE " + COLUMN_EVENTS_ID_FOREIGN_KEY + "='" + cursorEventId.getString(cursorEventId.getColumnIndexOrThrow(COLUMN_EVENT_ID_PRIMARY_KEY)) + "'"
                            + " ORDER BY " + COLUMN_EVENTS_ID_FOREIGN_KEY + " DESC ";
                    Cursor cursorGujarati = mSqLiteDatabase.rawQuery(sqlEventGujarati,null);
                    sqlEventEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_EVENTS_EN
                            + " WHERE " + COLUMN_EVENT_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                            + " AND " + COLUMN_EVENT_IS_ACTIVE + "='true'"
                            + " AND " + COLUMN_EVENT_YEAR + "='" + year + "'"
                            + " AND " + COLUMN_EVENT_ID_PRIMARY_KEY + "='" + cursorEventId.getString(cursorEventId.getColumnIndexOrThrow(COLUMN_EVENT_ID_PRIMARY_KEY)) + "'"
                            + " ORDER BY " + COLUMN_EVENT_ID_PRIMARY_KEY + " DESC ";
                    cursorEnglish = mSqLiteDatabase.rawQuery(sqlEventEnglish,null);
                    if ((cursorEnglish.moveToFirst() && cursorGujarati.moveToFirst()) || (cursorGujarati.getCount() == 0 && cursorEnglish.moveToFirst())){
                        do {
                            EventDataItem eventDataItem = new EventDataItem();
                            //Check if title is present in Gujarati
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_NAME)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_NAME)).equalsIgnoreCase("null")) {
                                eventDataItem.setEventName(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_NAME)));
                            } else {
                                eventDataItem.setEventName(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_NAME)));
                            }
                            //CHECK IF DESCRIPTION IS PRESENT IN GUJARATI
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)).equalsIgnoreCase("null")) {
                                eventDataItem.setEventDescription(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)));
                            } else {
                                eventDataItem.setEventDescription(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)));
                            }
                            //check if venue is present in Gujarati
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_VENUE)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_VENUE)).equalsIgnoreCase("null")){
                                eventDataItem.setVenue(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_EVENT_VENUE)));
                            } else {
                                eventDataItem.setVenue(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_VENUE)));
                            }
                            eventDataItem.setEventDate(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_DATE)));
                            eventDataItem.setCity(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_EVENT_CITY)));
                            arrImage.add("");
                            eventDataItem.setArrEventImageURLs(arrImage);
                            arrayListEvents.add(eventDataItem);
                        } while ((cursorEnglish.moveToNext() && cursorGujarati.moveToNext()) && cursorEnglish.moveToNext());
                    }
                } while (cursorEventId.moveToNext());
            }
            if (cursorEnglish !=null && !cursorEnglish.isClosed()){
                cursorEnglish.close();
            }
            if (cursorEventId !=null && !cursorEventId.isClosed()){
                cursorEventId.close();
            }
        }
        return arrayListEvents;
    }

    /*-------------------------------------------EVENTS LISTING-------------------------------------------*/

    /*-------------------------------------------MESSAGE LISTING-------------------------------------------*/

    public boolean insertOrUpdateMessageEnglish(ArrayList<MessageDetailsItem> arrayListMessage) {
        String insetMessageQuerry = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_MESSAGE_NEWS_DETAILS + " VALUES (?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement sqLiteStatementMessage = mSqLiteDatabase.compileStatement(insetMessageQuerry);
        mSqLiteDatabase.beginTransaction();
        MessageDetailsItem messageDetailsItem;
        try {
            for (int arrIndex = 0; arrIndex < arrayListMessage.size(); arrIndex++){
                messageDetailsItem = arrayListMessage.get(arrIndex);
                if (messageDetailsItem.getId() != null){
                    sqLiteStatementMessage.bindString(1,messageDetailsItem.getId());
                }
                if (messageDetailsItem.getMessageTitle() != null){
                    sqLiteStatementMessage.bindString(2,messageDetailsItem.getMessageTitle());
                }
                if (messageDetailsItem.getMessageDescription() != null){
                    sqLiteStatementMessage.bindString(3,messageDetailsItem.getMessageDescription());
                }
                if (messageDetailsItem.getMessageType() != null){
                    sqLiteStatementMessage.bindString(4, messageDetailsItem.getMessageType());
                }
                if (messageDetailsItem.getMessageTypeId() != null){
                    sqLiteStatementMessage.bindString(5,messageDetailsItem.getMessageTypeId());
                }
                if (messageDetailsItem.getIsActive() != null){
                    sqLiteStatementMessage.bindString(6,messageDetailsItem.getIsActive());
                }
                if (messageDetailsItem.getMessageCity() != null){
                    sqLiteStatementMessage.bindString(7,messageDetailsItem.getMessageCity());
                }
                if (messageDetailsItem.getMessageCityId() != null){
                    sqLiteStatementMessage.bindString(8,messageDetailsItem.getMessageCityId());
                }
                if (messageDetailsItem.getMessageDate() != null){
                    sqLiteStatementMessage.bindString(9,messageDetailsItem.getMessageDate());
                }
                if (messageDetailsItem.getMessageImage() != null){
                    sqLiteStatementMessage.bindString(10, messageDetailsItem.getMessageImage());
                }
                sqLiteStatementMessage.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean insertOrUpdateMessageGujarati(ArrayList<MessageDetailsItem> arrayListMessage) {
        String insetMessageQuerry = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_MESSAGE_NEWS_DETAILS_GJ + " VALUES (?,?,?,?,?)";
        SQLiteStatement sqLiteStatementMessage = mSqLiteDatabase.compileStatement(insetMessageQuerry);
        mSqLiteDatabase.beginTransaction();
        MessageDetailsItem messageDetailsItem;
        try {
            for (int arrIndex = 0; arrIndex < arrayListMessage.size(); arrIndex++){
                messageDetailsItem = arrayListMessage.get(arrIndex);
                Log.i(TAG, "insertOrUpdateMessageGujarati: "+(messageDetailsItem.getId() != null));
                if (messageDetailsItem.getId() != null){
                    sqLiteStatementMessage.bindString(1,messageDetailsItem.getId());
                }
                if (messageDetailsItem.getMessageTitle() != null){
                    sqLiteStatementMessage.bindString(2,messageDetailsItem.getMessageTitle());
                }
                if (messageDetailsItem.getMessageDescription() != null){
                    sqLiteStatementMessage.bindString(3,messageDetailsItem.getMessageDescription());
                }
                if (messageDetailsItem.getMessageID() != null) {
                    sqLiteStatementMessage.bindString(4,messageDetailsItem.getMessageID());
                    Log.i(TAG, "insertOrUpdateMessageGujarati: FOREIGN KEY---------"+messageDetailsItem.getMessageID());
                }
                if (messageDetailsItem.getLangugeId() != null){
                    sqLiteStatementMessage.bindString(5,messageDetailsItem.getLangugeId());
                }
                sqLiteStatementMessage.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public ArrayList<MessageDetailsItem> querryMessages(int pageNumber) {
        int intLimit = 10;
        int intOffset = intLimit * pageNumber;
        ArrayList<MessageDetailsItem> arrayListMessages = new ArrayList<>();

        String sqlQueryEnglish = "SELECT * FROM "+ TABLE_MESSAGE_NEWS_DETAILS
                + " WHERE " + COLUMN_MESSAGES_IS_ACTIVE + "='true'"
                + " AND " + COLUMN_MESSAGES_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                + " ORDER BY " + COLUMN_MESSAGES_ID_PRIMARY + " DESC"
                + " LIMIT " + intLimit + " OFFSET " + intOffset;

        Cursor cursorMessages =mSqLiteDatabase.rawQuery(sqlQueryEnglish,null);
        Cursor cursorMessagesGujarati ;
        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")){
            if (cursorMessages.moveToFirst()){
                do {
                    Log.i(TAG, "querryMessages: MESSAGE ID---"+(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_ID_PRIMARY))));
                    MessageDetailsItem messageDetailsItem = new MessageDetailsItem();
                    messageDetailsItem.setId(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_ID_PRIMARY)));
                    messageDetailsItem.setMessageTitle(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_TITLE)));
                    messageDetailsItem.setMessageDescription(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_DESCRIPTION)));
                    messageDetailsItem.setMessageType(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_TYPE)));
                    messageDetailsItem.setMessageTypeId(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_TYPE_ID)));
                    messageDetailsItem.setMessageIsActive(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_IS_ACTIVE)));
                    messageDetailsItem.setMessageCity(cursorMessages.getColumnName(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_CITY)));
                    messageDetailsItem.setMessageCityId(cursorMessages.getColumnName(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_CITY_ID)));
                    messageDetailsItem.setMessageDate(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_CREATED_DATE)));
                    messageDetailsItem.setMessageImage(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_IMAGE_URL)));
                    arrayListMessages.add(messageDetailsItem);
                } while (cursorMessages.moveToNext());
            }

        } else if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2")){
            String fetchMessageId = "SELECT " + COLUMN_MESSAGES_ID_PRIMARY + " FROM " + TABLE_MESSAGE_NEWS_DETAILS
                    + " WHERE " + COLUMN_MESSAGES_IS_ACTIVE + "='true'"
                    + " AND " + COLUMN_MESSAGES_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                    + " ORDER BY " + COLUMN_MESSAGES_ID_PRIMARY + " DESC";
            Cursor cursorMessageId = mSqLiteDatabase.rawQuery(fetchMessageId,null);
            Log.i(TAG, "querryMessages: "+cursorMessageId.getCount());
            if (cursorMessageId.moveToFirst()) {
                do {
                    String sqlQueryGujarati = "SELECT * FROM "+TABLE_MESSAGE_NEWS_DETAILS_GJ
                            + " WHERE " + COLUMN_MESSAGES_ID_FOREIGN + "='" + cursorMessageId.getString(cursorMessageId.getColumnIndexOrThrow(COLUMN_MESSAGES_ID_PRIMARY)) + "'"
                            + " ORDER BY " + COLUMN_MESSAGES_ID_FOREIGN + " DESC"
                            + " LIMIT " + intLimit + " OFFSET " + intOffset;
                    cursorMessagesGujarati = mSqLiteDatabase.rawQuery(sqlQueryGujarati,null);

                    sqlQueryEnglish = "SELECT * FROM "+ TABLE_MESSAGE_NEWS_DETAILS
                            + " WHERE " + COLUMN_MESSAGES_IS_ACTIVE + "='true'"
                            + " AND " + COLUMN_MESSAGES_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                            + " AND " + COLUMN_MESSAGES_ID_PRIMARY + "='" + cursorMessageId.getString(cursorMessageId.getColumnIndexOrThrow(COLUMN_MESSAGES_ID_PRIMARY)) + "'"
                            + " ORDER BY " + COLUMN_MESSAGES_ID_PRIMARY + " DESC"
                            + " LIMIT " + intLimit + " OFFSET " + intOffset;

                    cursorMessages = mSqLiteDatabase.rawQuery(sqlQueryEnglish,null);;
                    if (cursorMessagesGujarati.moveToFirst() && cursorMessages.moveToFirst()){
                        do {
                            MessageDetailsItem messageDetailsItem = new MessageDetailsItem();
                            messageDetailsItem.setId(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_ID_PRIMARY_GJ)));
                            if (cursorMessagesGujarati.getString(cursorMessagesGujarati.getColumnIndexOrThrow(COLUMN_MESSAGES_TITLE)) != null && !cursorMessagesGujarati.getString(cursorMessagesGujarati.getColumnIndexOrThrow(COLUMN_MESSAGES_TITLE)).equalsIgnoreCase("null")){
                                messageDetailsItem.setMessageTitle(cursorMessagesGujarati.getString(cursorMessagesGujarati.getColumnIndexOrThrow(COLUMN_MESSAGES_TITLE)));
                            } else {
                                messageDetailsItem.setMessageTitle(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_TITLE)));
                            }
                            if (cursorMessagesGujarati.getString(cursorMessagesGujarati.getColumnIndexOrThrow(COLUMN_MESSAGES_DESCRIPTION)) != null && !cursorMessagesGujarati.getString(cursorMessagesGujarati.getColumnIndexOrThrow(COLUMN_MESSAGES_DESCRIPTION)).equalsIgnoreCase("null")){
                                messageDetailsItem.setMessageDescription(cursorMessagesGujarati.getString(cursorMessagesGujarati.getColumnIndexOrThrow(COLUMN_MESSAGES_DESCRIPTION)));
                            } else {
                                messageDetailsItem.setMessageDescription(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_DESCRIPTION)));
                            }
                            messageDetailsItem.setMessageDescription(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_DESCRIPTION)));
                            messageDetailsItem.setMessageType(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_TYPE)));
                            messageDetailsItem.setMessageTypeId(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_TYPE_ID)));
                            messageDetailsItem.setMessageIsActive(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_IS_ACTIVE)));
                            messageDetailsItem.setMessageCity(cursorMessages.getColumnName(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_CITY)));
                            messageDetailsItem.setMessageCityId(cursorMessages.getColumnName(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_CITY_ID)));
                            messageDetailsItem.setMessageDate(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_CREATED_DATE)));
                            messageDetailsItem.setMessageImage(cursorMessages.getString(cursorMessages.getColumnIndexOrThrow(COLUMN_MESSAGES_IMAGE_URL)));
                            arrayListMessages.add(messageDetailsItem);
                        } while (cursorMessagesGujarati.moveToNext() && cursorMessages.moveToNext());
                    }
                    if (cursorMessagesGujarati != null && !cursorMessagesGujarati.isClosed()) {
                        cursorMessagesGujarati.close();
                    }
                } while (cursorMessageId.moveToNext());
            }
            if (cursorMessageId != null && !cursorMessageId.isClosed()) {
                cursorMessageId.close();
            }
            if (cursorMessages != null && !cursorMessages.isClosed()) {
                cursorMessages.close();
            }
        }
        return arrayListMessages;
    }

    /*-------------------------------------------MESSAGE LISTING-------------------------------------------*/

    /*-------------------------------------------CLASSIFIED LISTING-------------------------------------------*/

    public boolean inserOrUpdateClassifiedEnglish(ArrayList<ClassifiedDetailsItem> arrayListClassified) {
        try{
            String insertQuery = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_CLASSIFIED_EN + " VALUES (?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(insertQuery);
            mSqLiteDatabase.beginTransaction();
            ClassifiedDetailsItem classifiedDetailsItem;
            for (int arrIndex = 0; arrIndex < arrayListClassified.size(); arrIndex++) {
                classifiedDetailsItem = arrayListClassified.get(arrIndex);
                if (classifiedDetailsItem.getClassifiedID() != null){
                    sqLiteStatement.bindString(1,classifiedDetailsItem.getClassifiedID());
                }
                if (classifiedDetailsItem.getClassifiedTitle() != null){
                    sqLiteStatement.bindString(2, classifiedDetailsItem.getClassifiedTitle());
                }
                if (classifiedDetailsItem.getClassifiedDescription() != null) {
                    sqLiteStatement.bindString(3, classifiedDetailsItem.getClassifiedDescription());
                }
                if (classifiedDetailsItem.getClassifiedCity() != null){
                    sqLiteStatement.bindString(4, classifiedDetailsItem.getClassifiedCity());
                }
                if (classifiedDetailsItem.getIsActive() != null) {
                    sqLiteStatement.bindString(5, classifiedDetailsItem.getIsActive());
                }
                if (classifiedDetailsItem.getClassifiedCreateDate() != null) {
                    sqLiteStatement.bindString(6, classifiedDetailsItem.getClassifiedCreateDate());
                }
                if (classifiedDetailsItem.getCityId() != null){
                    sqLiteStatement.bindString(7, classifiedDetailsItem.getCityId());
                }
                sqLiteStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean insertOrUpdateClassifiedGujarati(ArrayList<ClassifiedDetailsItem> arrayListClassified) {
        try {
            String insertClassified = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_CLASSIFIED_GJ + " VALUES (?,?,?,?)";
            SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(insertClassified);
            mSqLiteDatabase.beginTransaction();
            ClassifiedDetailsItem classifiedDetailsItem;
            for (int arrIndex = 0; arrIndex < arrayListClassified.size(); arrIndex++){
                classifiedDetailsItem = arrayListClassified.get(arrIndex);
                if (classifiedDetailsItem.getId() != null){
                    sqLiteStatement.bindString(1, classifiedDetailsItem.getId());
                }
                Log.i(TAG, "insertOrUpdateClassifiedGujarati: "+(classifiedDetailsItem.getClassifiedTitle() != null));
                if (classifiedDetailsItem.getClassifiedTitle() != null) {
                    Log.i(TAG, "insertOrUpdateClassifiedGujarati: "+classifiedDetailsItem.getClassifiedTitle());
                    sqLiteStatement.bindString(2,classifiedDetailsItem.getClassifiedTitle());
                }
                if (classifiedDetailsItem.getClassifiedDescription() != null) {
                    sqLiteStatement.bindString(3,classifiedDetailsItem.getClassifiedDescription());
                }
                if (classifiedDetailsItem.getClassifiedID() != null) {
                    sqLiteStatement.bindString(4,classifiedDetailsItem.getClassifiedID());
                }
                sqLiteStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public  ArrayList<ClassifiedDetailsItem> queryClassified (int pageNumber) {
        int intLimit = 10;
        int intOffset = intLimit * pageNumber;
        String sqlQueryEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_CLASSIFIED_EN
                + " WHERE " + COLUMN_CLASSIFIED_IS_ACTIVE + "='true'"
                + " AND " + COLUMN_CLASSIFIED_CITY_ID + "='"+ AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) +"'"
                + " LIMIT " + intLimit + " OFFSET " + intOffset;

        ArrayList<ClassifiedDetailsItem> arrayListClassified = new ArrayList<>();
        ArrayList<String> arrImage = new ArrayList<>();
        Cursor cursorEnglish = mSqLiteDatabase.rawQuery(sqlQueryEnglish,null);
        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")) {
            new AppCommonMethods(mContext).LOG(0,TAG,sqlQueryEnglish);
            if (cursorEnglish.moveToFirst()) {
                do {
                    ClassifiedDetailsItem classifiedDetailsItem = new ClassifiedDetailsItem();
                    classifiedDetailsItem.setClassifiedID(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_ID_PRIMARY_KEY)));
                    classifiedDetailsItem.setClassifiedTitle(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_TITLE)));
                    classifiedDetailsItem.setClassifiedDescription(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_DESCRIPTION)));
                    classifiedDetailsItem.setClassifiedCity(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_CITY)));
                    classifiedDetailsItem.setIsActive(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_IS_ACTIVE)));
                    classifiedDetailsItem.setClassifiedCreateDate(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_CREATED_AT)));
                    arrImage.add("");
                    classifiedDetailsItem.setArrClassifiedImages(arrImage);
                    arrayListClassified.add(classifiedDetailsItem);
                } while (cursorEnglish.moveToNext());
            }

        } else if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2")) {
            String fetchClassifiedId = "SELECT " + COLUMN_CLASSIFIED_ID_PRIMARY_KEY + " FROM " + TABLE_CLASSIFIED_EN
                    + " WHERE " + COLUMN_CLASSIFIED_IS_ACTIVE + "='true'"
                    + " AND " + COLUMN_CLASSIFIED_CITY_ID + "='"+ AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) +"'" ;
            Cursor cursorClassifiedId = mSqLiteDatabase.rawQuery(fetchClassifiedId,null);
            if (cursorClassifiedId.moveToFirst()) {
                do {
                    String sqlQueryGujarati = "SELECT * FROM " + DatabaseHelper.TABLE_CLASSIFIED_GJ
                            + " WHERE " + COLUMN_CLASSIFIED_ID_FOREIGN_KEY + "='" + cursorClassifiedId.getString(cursorClassifiedId.getColumnIndexOrThrow(COLUMN_CLASSIFIED_ID_PRIMARY_KEY)) +  "'"
                            + " LIMIT " + intLimit + " OFFSET " + intOffset;
                    Cursor cursorGujarati = mSqLiteDatabase.rawQuery(sqlQueryGujarati,null);
                    sqlQueryEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_CLASSIFIED_EN
                            + " WHERE " + COLUMN_CLASSIFIED_IS_ACTIVE + "='true'"
                            + " AND " + COLUMN_CLASSIFIED_CITY_ID + "='"+ AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) +"'"
                            + " AND " + COLUMN_CLASSIFIED_ID_PRIMARY_KEY + "='" + cursorClassifiedId.getString(cursorClassifiedId.getColumnIndexOrThrow(COLUMN_CLASSIFIED_ID_PRIMARY_KEY)) +  "'"
                            + " LIMIT " + intLimit + " OFFSET " + intOffset;
                    cursorEnglish = mSqLiteDatabase.rawQuery(sqlQueryEnglish,null);
                    if (cursorEnglish.moveToFirst() && cursorGujarati.moveToFirst()){
                        do {
                            ClassifiedDetailsItem classifiedDetailsItem = new ClassifiedDetailsItem();
                            // Check if title is present in Gujarati
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CLASSIFIED_TITLE)) != null && cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CLASSIFIED_TITLE)).equalsIgnoreCase("null")){
                                classifiedDetailsItem.setClassifiedTitle(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CLASSIFIED_TITLE)));
                            } else {
                                classifiedDetailsItem.setClassifiedTitle(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_TITLE)));
                            }
                            //Check if description is present in Gujarati
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CLASSIFIED_DESCRIPTION)) != null && cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CLASSIFIED_DESCRIPTION)).equalsIgnoreCase("null")) {
                                classifiedDetailsItem.setClassifiedDescription(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_CLASSIFIED_DESCRIPTION)));
                            } else {
                                classifiedDetailsItem.setClassifiedDescription(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_DESCRIPTION)));
                            }
                            classifiedDetailsItem.setClassifiedCity(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_CITY)));
                            classifiedDetailsItem.setIsActive(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_IS_ACTIVE)));
                            classifiedDetailsItem.setClassifiedCreateDate(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_CLASSIFIED_CREATED_AT)));
                            arrImage.add("");
                            classifiedDetailsItem.setArrClassifiedImages(arrImage);
                            arrayListClassified.add(classifiedDetailsItem);
                        } while (cursorEnglish.moveToNext() && cursorGujarati.moveToNext());
                    }
                } while (cursorClassifiedId.moveToNext());
            }
            if ( cursorClassifiedId != null && !cursorClassifiedId.isClosed()) {
                cursorClassifiedId.close();
            }
            if (cursorEnglish != null && !cursorEnglish.isClosed()) {
                cursorEnglish.close();
            }
        }
        if (cursorEnglish != null && !cursorEnglish.isClosed()) {
            cursorEnglish.close();
        }
        return arrayListClassified;
    }

    /*-------------------------------------------CLASSIFIED LISTING-------------------------------------------*/

    /*-------------------------------------------ACCOUNT LISTING-------------------------------------------*/

    public boolean insertOrUpdateAccountsEnglish (ArrayList<AccountDetailsItem> arrayListAccount) {
        try {
            String sqlInsert = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_ACCOUNT_EN + " VALUES (?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(sqlInsert);
            mSqLiteDatabase.beginTransaction();
            AccountDetailsItem accountDetailsItem;
            for (int arrIndex = 0; arrIndex < arrayListAccount.size(); arrIndex++) {
                accountDetailsItem = arrayListAccount.get(arrIndex);
                if (accountDetailsItem.getStrAccountId() != null) {
                    sqLiteStatement.bindString(1,accountDetailsItem.getStrAccountId());
                }
                if (accountDetailsItem.getStrAccountName() != null) {
                    sqLiteStatement.bindString(2,accountDetailsItem.getStrAccountName());
                }
                if (accountDetailsItem.getStrAccountDescription() != null) {
                    sqLiteStatement.bindString(3, accountDetailsItem.getStrAccountDescription());
                }
                if (accountDetailsItem.getStrIsActive() != null) {
                    sqLiteStatement.bindString(4,accountDetailsItem.getStrIsActive());
                }
                if (accountDetailsItem.getStrCity() != null) {
                    sqLiteStatement.bindString(5,accountDetailsItem.getStrCity());
                }
                if (accountDetailsItem.getStrCityId() != null) {
                    sqLiteStatement.bindString(6, accountDetailsItem.getStrCityId());
                }
                if (accountDetailsItem.getStrYear() != null) {
                    sqLiteStatement.bindString(7,accountDetailsItem.getStrYear());
                }
                sqLiteStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean insertOrUpdateAccountGujarati (ArrayList<AccountDetailsItem> arrayListAccount) {
        try {
            String sqlInsert = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_ACCOUNT_GJ + " VALUES (?, ?, ?, ?)";
            SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(sqlInsert);
            mSqLiteDatabase.beginTransaction();
            AccountDetailsItem accountDetailsItem;
            for (int arrIndex = 0; arrIndex < arrayListAccount.size(); arrIndex++) {
                accountDetailsItem = arrayListAccount.get(arrIndex);
                if (accountDetailsItem.getStrId() != null) {
                    sqLiteStatement.bindString(1,accountDetailsItem.getStrId());
                }
                if (accountDetailsItem.getStrAccountName() != null) {
                    sqLiteStatement.bindString(2, accountDetailsItem.getStrAccountName());
                }
                if (accountDetailsItem.getStrAccountDescription() != null) {
                    sqLiteStatement.bindString(3,accountDetailsItem.getStrAccountDescription());
                }
                if (accountDetailsItem.getStrAccountId() != null) {
                    sqLiteStatement.bindString(4,accountDetailsItem.getStrAccountId());
                }
                sqLiteStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public ArrayList<AccountDetailsItem> queryAccounts ( String year) {
        ArrayList<AccountDetailsItem> arrAccount = new ArrayList<>();
        String sqlEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_ACCOUNT_EN
                + " WHERE " + COLUMN_ACCOUNT_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                + " AND " + COLUMN_ACCOUNT_IS_ACTIVE + "='true'"
                + " AND " + COLUMN_ACCOUNT_YEAR + "='" + year + "'";

        Cursor cursorEnglish = mSqLiteDatabase.rawQuery(sqlEnglish,null);

        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")){
            if (cursorEnglish.moveToFirst()) {
                do {
                    AccountDetailsItem accountDetailsItem = new AccountDetailsItem();
                    accountDetailsItem.setStrAccountId(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_ACCOUNT_ID_PRIMARY)));
                    accountDetailsItem.setStrAccountName(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_ACCOUNT_NAME)));
                    accountDetailsItem.setStrAccountDescription(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_ACCOUNT_DESCRIPTION)));
                    accountDetailsItem.setStrCity(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_ACCOUNT_CITY)));
                    ArrayList<AccountImages> arrImages = new ArrayList<>();
                    AccountImages img = new AccountImages();
                    img.setImagePath("");
                    arrImages.add(img);
                    accountDetailsItem.setImagesList(arrImages);
                    arrAccount.add(accountDetailsItem);
                } while (cursorEnglish.moveToNext());
            }
        } else if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2")){
            String fetchAccountId = "SELECT " + COLUMN_ACCOUNT_ID_PRIMARY + " FROM " + TABLE_ACCOUNT_EN
                    + " WHERE " + COLUMN_ACCOUNT_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                    + " AND " + COLUMN_ACCOUNT_IS_ACTIVE + "='true'"
                    + " AND " + COLUMN_ACCOUNT_YEAR + "='" + year + "'";
            Cursor cursorAccountId = mSqLiteDatabase.rawQuery(fetchAccountId,null);
            if (cursorAccountId.moveToFirst()) {
                do {
                    String sqlGujarati = "SELECT * FROM " + DatabaseHelper.TABLE_ACCOUNT_GJ
                            + " WHERE " + COLUMN_ACCOUNT_ID_FOREIGN + "='" + cursorAccountId.getString(cursorAccountId.getColumnIndexOrThrow(COLUMN_ACCOUNT_ID_PRIMARY)) + "'";
                    Cursor cursorGujarati = mSqLiteDatabase.rawQuery(sqlGujarati,null);
                    if (cursorEnglish.moveToFirst() && cursorGujarati.moveToFirst()) {
                        do {
                            AccountDetailsItem accountDetailsItem = new AccountDetailsItem();
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_ACCOUNT_NAME)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_ACCOUNT_NAME)).equalsIgnoreCase("null")) {
                                accountDetailsItem.setStrAccountName(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_ACCOUNT_NAME)));
                            } else {
                                accountDetailsItem.setStrAccountName(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_ACCOUNT_NAME)));
                            }
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_ACCOUNT_DESCRIPTION)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_ACCOUNT_DESCRIPTION)).equalsIgnoreCase("null") ){
                                accountDetailsItem.setStrAccountDescription(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_ACCOUNT_DESCRIPTION)));
                            } else {
                                accountDetailsItem.setStrAccountDescription(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_ACCOUNT_DESCRIPTION)));
                            }
                            accountDetailsItem.setStrCity(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_ACCOUNT_CITY)));
                            ArrayList<AccountImages> arrImages = new ArrayList<>();
                            AccountImages img = new AccountImages();
                            img.setImagePath("");
                            arrImages.add(img);
                            accountDetailsItem.setImagesList(arrImages);
                            arrAccount.add(accountDetailsItem);
                        } while (cursorEnglish.moveToNext() && cursorGujarati.moveToNext());
                    }
                } while (cursorAccountId.moveToNext());
            }

        }
        return arrAccount;
    }

    /*-------------------------------------------ACCOUNT LISTING-------------------------------------------*/

    /*-------------------------------------------Committee LISTING-------------------------------------------*/

    public boolean insertOrUpdateCommitteeDetailsEnglish(ArrayList<CommitteeDetailsItem> arrCommittee) {
        ArrayList<CommitteeDetailsItem> arrayList = arrCommittee;
        String sqlInset = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_COMMITTEE_DETAILS + " VALUES (?,?,?,?,?,?,?,?)";
        SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(sqlInset);
        mSqLiteDatabase.beginTransaction();
        CommitteeDetailsItem committeeDetailsItem;
        try{
            for (int arrIndex = 0; arrIndex < arrCommittee.size(); arrIndex++) {
                committeeDetailsItem = arrCommittee.get(arrIndex);
                if (committeeDetailsItem.getCommitteeID() != null) {
                    sqLiteStatement.bindString(1,committeeDetailsItem.getCommitteeID());
                }
                if (committeeDetailsItem.getCommitteeName() != null) {
                    sqLiteStatement.bindString(2,committeeDetailsItem.getCommitteeName());
                }
                if (committeeDetailsItem.getCommitteeDescription() != null) {
                    sqLiteStatement.bindString(3, committeeDetailsItem.getCommitteeDescription());
                }
                if (committeeDetailsItem.getCommIsActive() != null) {
                    sqLiteStatement.bindString(4, committeeDetailsItem.getCommIsActive());
                }
                if (committeeDetailsItem.getCommitteeCity() != null ){
                    sqLiteStatement.bindString(5, committeeDetailsItem.getCommitteeCity());
                }
                if (committeeDetailsItem.getCityId() != null ) {
                    sqLiteStatement.bindString(6, committeeDetailsItem.getCityId());
                }
                if (committeeDetailsItem.getCommAllMembers() != null) {
                    sqLiteStatement.bindString(7, committeeDetailsItem.getCommAllMembers());
                }
                if (committeeDetailsItem.getCommAllMembersGujarati() != null) {
                    sqLiteStatement.bindString(8, committeeDetailsItem.getCommAllMembersGujarati());
                }
                sqLiteStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
            insertOrUpdateCommitteeMembers();
        }
    }

    public boolean insertOrUpdateCommitteeDetailsGuajrati ( ArrayList<CommitteeDetailsItem> arrCommittee) {
        try {
            String insert = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_COMMITTEE_DETAILS_GJ + " VALUES (?,?,?,?)";
            SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(insert);
            mSqLiteDatabase.beginTransaction();
            CommitteeDetailsItem committeeDetailsItem;
            for (int arrIndex = 0; arrIndex < arrCommittee.size(); arrIndex++) {
                committeeDetailsItem = arrCommittee.get(arrIndex);
                if (committeeDetailsItem.getId() != null) {
                    sqLiteStatement.bindString(1, committeeDetailsItem.getId());
                }
                if (committeeDetailsItem.getCommitteeName() != null) {
                    sqLiteStatement.bindString(2, committeeDetailsItem.getCommitteeName());
                }
                if (committeeDetailsItem.getCommitteeDescription() != null) {
                    sqLiteStatement.bindString(3, committeeDetailsItem.getCommitteeDescription());
                }
                if (committeeDetailsItem.getCommitteeID() != null) {
                    sqLiteStatement.bindString(4, committeeDetailsItem.getCommitteeID());
                }
                sqLiteStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void insertOrUpdateCommitteeMembers() {
        String fetchCommitteeId = "SELECT " + COLUMN_COMMITTEE_ID_PRIMARY + " FROM " + TABLE_COMMITTEE_DETAILS;
        Cursor cursorId = mSqLiteDatabase.rawQuery(fetchCommitteeId,null);

        if (cursorId.moveToFirst()){
            do {
                String fetchMemberEnglishDetails = "SELECT " + COLUMN_COMMITTEE_MEMBERS_EN + " FROM " + DatabaseHelper.TABLE_COMMITTEE_DETAILS
                        + " WHERE " + COLUMN_COMMITTEE_ID_PRIMARY + "='" + cursorId.getString(cursorId.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)) + "'";
                Cursor cursorEnglish = mSqLiteDatabase.rawQuery(fetchMemberEnglishDetails, null);
                if (cursorEnglish.moveToFirst()) {
                    String membersEnglish = cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBERS_EN));
                    try {
                        String insertMemebrsEnglish = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_COMMITTEE_MEMBERS_EN + " VALUES(?,?,?,?,?,?,?,?)";
                        SQLiteStatement sqLiteStatement= mSqLiteDatabase.compileStatement(insertMemebrsEnglish);
                        mSqLiteDatabase.beginTransaction();
                        JSONArray jsonMemberArray = new JSONArray(membersEnglish);
                        for (int arrIndexMem = 0; arrIndexMem < jsonMemberArray.length(); arrIndexMem++) {
                            JSONObject jsonObject = jsonMemberArray.optJSONObject(arrIndexMem);
                            if (jsonObject.has("id") && jsonObject.optString("id") != null) {
                                sqLiteStatement.bindString(1, jsonObject.optString("id"));
                            }
                            if (jsonObject.has("fullname") && jsonObject.getString("fullname") != null) {
                                sqLiteStatement.bindString(2,jsonObject.getString("fullname"));
                            }
                            if (jsonObject.has("designation") && jsonObject.getString("designation") != null) {
                                sqLiteStatement.bindString(3,jsonObject.getString("designation"));
                            }
                            if (jsonObject.has("cont_number") && jsonObject.getString("cont_number") != null) {
                                sqLiteStatement.bindString(4,jsonObject.getString("cont_number"));
                            }
                            if (jsonObject.has("email_id") && jsonObject.getString("email_id") != null) {
                                sqLiteStatement.bindString(5,jsonObject.getString("email_id"));
                            }
                            if (jsonObject.has("area") && jsonObject.getString("area") != null) {
                                sqLiteStatement.bindString(6,jsonObject.getString("area"));
                            }
                            if (jsonObject.has("is_active") && jsonObject.getString("is_active") != null) {
                                sqLiteStatement.bindString(7,jsonObject.getString("is_active"));
                            }
                            if (jsonObject.has("committee_id") && jsonObject.getString("committee_id") != null) {
                                sqLiteStatement.bindString(8, jsonObject.getString("committee_id"));
                            }
                            sqLiteStatement.execute();
                        }
                        mSqLiteDatabase.setTransactionSuccessful();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mSqLiteDatabase.endTransaction();
                    }
                }
                if (cursorEnglish != null && !cursorEnglish.isClosed()) {
                    cursorEnglish.close();
                }
            } while (cursorId.moveToNext());
        }


        if (cursorId.moveToFirst()) {
            do {
                String fetchMemberGujaratiDetails = "SELECT " + COLUMN_COMMITTEE_MEMBERS_GJ + " FROM " + DatabaseHelper.TABLE_COMMITTEE_DETAILS
                        + " WHERE " + COLUMN_COMMITTEE_ID_PRIMARY + "='" + cursorId.getString(cursorId.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)) + "'";
                Cursor cursorGujarati = mSqLiteDatabase.rawQuery(fetchMemberGujaratiDetails,null);

                if (cursorGujarati.moveToFirst()) {
                    String membersGuajrati = cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBERS_GJ));
                    try {
                        String insertMemebersGujarati = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_COMMITTEE_MEMBERS_GJ + " VALUES(?,?,?)";
                        SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(insertMemebersGujarati);
                        mSqLiteDatabase.beginTransaction();
                        JSONArray jsonMemberArray = new JSONArray(membersGuajrati);
                        for (int arrIndexMem = 0; arrIndexMem < jsonMemberArray.length(); arrIndexMem++) {
                            JSONObject jsonObject = jsonMemberArray.optJSONObject(arrIndexMem);
                            if (jsonObject.has("id") && jsonObject.optString("id") != null) {
                                sqLiteStatement.bindString(1, jsonObject.optString("id"));
                            }
                            if (jsonObject.has("fullname") && jsonObject.getString("fullname") != null) {
                                sqLiteStatement.bindString(2, jsonObject.getString("fullname"));
                            }
                            if (jsonObject.has("member_id") && jsonObject.getString("member_id") != null) {
                                sqLiteStatement.bindString(3, jsonObject.getString("member_id"));
                            }
                            sqLiteStatement.execute();
                        }
                        mSqLiteDatabase.setTransactionSuccessful();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mSqLiteDatabase.endTransaction();
                    }
                }

            } while (cursorId.moveToNext());
        }
        if (cursorId != null && !cursorId.isClosed()) {
            cursorId.close();
        }
    }

    public ArrayList<CommitteeDetailsItem> queryCommittees() {
        ArrayList<CommitteeDetailsItem> arrayList = new ArrayList<>();
        ArrayList<CommMemberDetailsItem> arrMemberEnglish = new ArrayList<>();
        ArrayList<CommMemberDetailsItem> arrMemberGujarati = new ArrayList<>();

        //FETCH ACTIVE COMMITTEES ID
        String fetchCommitteeId = "SELECT " + COLUMN_COMMITTEE_ID_PRIMARY + " FROM " + TABLE_COMMITTEE_DETAILS
                + " WHERE " + COLUMN_COMMITTEE_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                + " AND " + COLUMN_COMMITTEE_IS_ACTIVE + "='true'"
                + " ORDER BY "+ COLUMN_COMMITTEE_ID_PRIMARY + " DESC ";

        //Check Language selected
        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")) {
            Cursor cursorId = mSqLiteDatabase.rawQuery(fetchCommitteeId,null);
            if (cursorId.moveToFirst()){
                do {
                    String sqlEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_COMMITTEE_DETAILS
                            + " WHERE " + COLUMN_COMMITTEE_IS_ACTIVE + "='true'"
                            + " AND " + COLUMN_COMMITTEE_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                            + " AND " + COLUMN_COMMITTEE_ID_PRIMARY + "='" + cursorId.getString(cursorId.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)) + "'"
                            + " ORDER BY "+ COLUMN_COMMITTEE_ID_PRIMARY + " DESC ";
                    Cursor cursorEnglish = mSqLiteDatabase.rawQuery(sqlEnglish,null);
                    Log.i(TAG, "queryCommittees: sqlEnglish"+ sqlEnglish);

                    String sqlMembersEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_COMMITTEE_MEMBERS_EN
                            + " WHERE " + COLUMN_COMMITTEE_MEMBER_IS_ACTIVE + "='true'"
                            + " AND " + COLUMN_COMMITTEE_MEMBER_ID_FOREIGN + "='" + cursorId.getString(cursorId.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)) + "'";
                    //FETCH MEMBER WITH NAMES IN ENGLISH
                    Cursor cursorMemberEnglish = mSqLiteDatabase.rawQuery(sqlMembersEnglish,null);
                    if (cursorMemberEnglish.moveToFirst()) {
                        do {
                            CommMemberDetailsItem commMemberDetailItem = new CommMemberDetailsItem();
                            commMemberDetailItem.setCommitteeMemName(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_NAME)));
                            commMemberDetailItem.setCommitteeMemDesignation(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_DESIGNATION)));
                            commMemberDetailItem.setCommitteeMemAddress(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_AREA)));
                            commMemberDetailItem.setCommitteeMemContact(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_NUMBER)));
                            commMemberDetailItem.setCommitteeMemEmail(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_EMAIL)));
                            arrMemberEnglish.add(commMemberDetailItem);
                        } while (cursorMemberEnglish.moveToNext());
                    }
                    if (cursorMemberEnglish != null && cursorMemberEnglish.isClosed()) {
                        cursorMemberEnglish.close();
                    }
                    //Adding Member with details in ENglish to arrList
                    if (cursorEnglish.moveToFirst()) {
                        do {
                            CommitteeDetailsItem committeeDetailsItem = new CommitteeDetailsItem();
                            committeeDetailsItem.setCommitteeName(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_NAME)));
                            committeeDetailsItem.setCommitteeDescription(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_DESCRIPTION)));
                            committeeDetailsItem.setCommitteeCity(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_CITY)));
                            committeeDetailsItem.setMembersEnglish(arrMemberEnglish);
                            arrMemberEnglish = new ArrayList<>();
                            arrayList.add(committeeDetailsItem);
                        } while (cursorEnglish.moveToNext());
                    }
                    if (cursorEnglish != null && !cursorEnglish.isClosed()) {
                        cursorEnglish.close();
                    }

                } while (cursorId.moveToNext());
            }
            if (cursorId != null && !cursorId.isClosed()) {
                cursorId.close();
            }
        }

        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2")){
            Cursor cursorId = mSqLiteDatabase.rawQuery(fetchCommitteeId,null);
            if (cursorId.moveToFirst()) {
                do {
                    String sqlMembersEnglishId = "SELECT " + COLUMN_COMMITTEE_MEMBER_ID_PRIMARY_EN +" FROM " + DatabaseHelper.TABLE_COMMITTEE_MEMBERS_EN
                            + " WHERE " + COLUMN_COMMITTEE_MEMBER_IS_ACTIVE + "='true'"
                            + " AND " + COLUMN_COMMITTEE_MEMBER_ID_FOREIGN + "='" + cursorId.getString(cursorId.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)) + "'";
                    //FETCH MEMBER WITH NAMES IN ENGLISH
                    Cursor cursorMemberEnglishId = mSqLiteDatabase.rawQuery(sqlMembersEnglishId,null);

                    if (cursorMemberEnglishId.moveToFirst()) {
                        do {
                            String sqlMembersEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_COMMITTEE_MEMBERS_EN
                                    + " WHERE " + COLUMN_COMMITTEE_MEMBER_IS_ACTIVE + "='true'"
                                    + " AND " + COLUMN_COMMITTEE_MEMBER_ID_FOREIGN + "='" + cursorId.getString(cursorId.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)) + "'";
                            //FETCH MEMBER WITH NAMES IN ENGLISH
                            Cursor cursorMemberEnglish = mSqLiteDatabase.rawQuery(sqlMembersEnglish,null);
                            //FETCH MEMBERS WITH NAMES IN GUJARATI
                            String sqlMemberGujarati = "SELECT * FROM " + DatabaseHelper.TABLE_COMMITTEE_MEMBERS_GJ
                                    + " WHERE " + COLUMN_MEMBERS_COMMITTEE_ID_FOREIGN + "='" +cursorMemberEnglishId.getString(cursorMemberEnglishId.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_ID_PRIMARY_EN)) + "'";
                            Cursor cursorMemberGujarati = mSqLiteDatabase.rawQuery(sqlMemberGujarati,null);
                            if (cursorMemberGujarati.moveToFirst() && cursorMemberEnglish.moveToFirst()) {
                                do {
                                    CommMemberDetailsItem commMemberDetailItem = new CommMemberDetailsItem();
                                    if (cursorMemberGujarati.getString(cursorMemberGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_NAME)) != null && !cursorMemberGujarati.getString(cursorMemberGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_NAME)).equalsIgnoreCase("null"))   {
                                        commMemberDetailItem.setCommitteeMemName(cursorMemberGujarati.getString(cursorMemberGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_NAME)));
                                    } else {
                                        commMemberDetailItem.setCommitteeMemName(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_NAME)));
                                    }
                                    commMemberDetailItem.setCommitteeMemDesignation(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_DESIGNATION)));
                                    commMemberDetailItem.setCommitteeMemAddress(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_AREA)));
                                    commMemberDetailItem.setCommitteeMemContact(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_NUMBER)));
                                    commMemberDetailItem.setCommitteeMemEmail(cursorMemberEnglish.getString(cursorMemberEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBER_EMAIL)));
                                    arrMemberGujarati.add(commMemberDetailItem);
                                } while (cursorMemberGujarati.moveToNext() && cursorMemberEnglish.moveToNext());
                            }
                            Log.i(TAG, "queryCommittees: memebrs----"+ arrMemberGujarati.size());
                            if (cursorMemberGujarati != null && !cursorMemberGujarati.isClosed()) {
                                cursorMemberGujarati.close();
                            }
                            if (cursorMemberEnglish != null && !cursorMemberEnglish.isClosed()){
                                cursorMemberEnglish.close();
                            }
                        } while (cursorMemberEnglishId.moveToNext());
                    }
                    if (cursorMemberEnglishId != null && !cursorMemberEnglishId.isClosed()) {
                        cursorMemberEnglishId.close();
                    }

                    String sqlGujarati = "SELECT * FROM " + DatabaseHelper.TABLE_COMMITTEE_DETAILS_GJ
                            + " WHERE " + COLUMN_COMMITTEE_ID_FOREIGN + "='" + cursorId.getString(cursorId.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)) + "'"
                            + " ORDER BY "+ COLUMN_COMMITTEE_ID_FOREIGN + " DESC ";;
                    new AppCommonMethods(mContext).LOG(0,TAG,sqlGujarati);
                    Log.i(TAG, "queryCommittees: sqlGujarati Committee---"+sqlGujarati);
                    Cursor cursorGujarati = mSqLiteDatabase.rawQuery(sqlGujarati,null);


                    String sqlEnglish = "SELECT * FROM " + DatabaseHelper.TABLE_COMMITTEE_DETAILS
                            + " WHERE " + COLUMN_COMMITTEE_IS_ACTIVE + "='true'"
                            + " AND " + COLUMN_COMMITTEE_CITY_ID + "='" + AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext) + "'"
                            + " AND " + COLUMN_COMMITTEE_ID_PRIMARY + "='" + cursorId.getString(cursorId.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)) + "'";
                    Cursor cursorEnglish = mSqLiteDatabase.rawQuery(sqlEnglish,null);
                    new AppCommonMethods(mContext).LOG(0,TAG,sqlEnglish);

                    if (cursorGujarati.moveToFirst() && cursorEnglish.moveToFirst()) {
                        do {
                            CommitteeDetailsItem committeeDetailsItem = new CommitteeDetailsItem();
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_NAME)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_NAME)).equalsIgnoreCase("null")) {
                                committeeDetailsItem.setCommitteeName(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_NAME)));
                            } else {
                                committeeDetailsItem.setCommitteeName(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_NAME)));
                            }
                            if (cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_DESCRIPTION)) != null && !cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_DESCRIPTION)).equalsIgnoreCase("null")) {
                                committeeDetailsItem.setCommitteeDescription(cursorGujarati.getString(cursorGujarati.getColumnIndexOrThrow(COLUMN_COMMITTEE_DESCRIPTION)));
                            } else {
                                committeeDetailsItem.setCommitteeDescription(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_DESCRIPTION)));
                            }
                            committeeDetailsItem.setCommitteeCity(cursorEnglish.getString(cursorEnglish.getColumnIndexOrThrow(COLUMN_COMMITTEE_CITY)));
                            committeeDetailsItem.setMembersGujarati(arrMemberGujarati);
                            arrMemberGujarati = new ArrayList<>();
                            arrayList.add(committeeDetailsItem);
                        } while (cursorGujarati.moveToNext() && cursorEnglish.moveToNext());
                    }
                    Log.i(TAG, "queryCommittees: committees---------"+arrayList.size());
                    if (cursorEnglish != null && !cursorEnglish.isClosed()) {
                        cursorEnglish.close();
                    }
                    if (cursorGujarati != null && !cursorGujarati.isClosed()) {
                        cursorGujarati.close();
                    }
                } while (cursorId.moveToNext());
            }
            if (cursorId != null && !cursorId.isClosed()){
                cursorId.close();
            }
        }
        Log.i(TAG, "queryCommittees: arrMainCommList.size()---"+ arrayList.size());
        return arrayList;
    }

    /*-------------------------------------------Committee LISTING-------------------------------------------*/

    /*-------------------------------------------COUNT LISTING-------------------------------------------*/

    public void insertCount (ArrayList<CityIteam> arrCity, ArrayList<MessageDetailsItem> arrMessage, ArrayList<ClassifiedDetailsItem> arrClassified) {
        int messageCount=0, classifiedCount=0,cityID = 0;
        CountItem countItem;
        ArrayList<CountItem> arrayListCount = new ArrayList<>();
        //arrCityIndex = 1 as city ID starts from 1
        for (int arrCityIndex = 0; arrCityIndex < arrCity.size(); arrCityIndex++){
            messageCount = 0;
            classifiedCount = 0;
            CityIteam cityIteam = arrCity.get(arrCityIndex);
            cityID = cityIteam.getIntCityId();
            countItem = new CountItem();
            String sqlMessage ="SELECT * FROM " + DatabaseHelper.TABLE_MESSAGE_NEWS_DETAILS
                    + " WHERE " + COLUMN_MESSAGES_CITY_ID + "='" + cityID + "'"
                    + " AND " + COLUMN_MESSAGES_IS_ACTIVE + "='true'";

            Log.i(TAG, "insertCount: "+sqlMessage);

            String sqlClassified = "SELECT * FROM " + DatabaseHelper.TABLE_CLASSIFIED_EN
                    + " WHERE " + COLUMN_CLASSIFIED_CITY_ID + "='" + cityID + "'"
                    + " AND " + COLUMN_CLASSIFIED_IS_ACTIVE + "='true'";

            Log.i(TAG, "insertCount: "+sqlClassified);

            for (int arrMessageIndex = 0; arrMessageIndex < arrMessage.size(); arrMessageIndex++) {
                Cursor cursor = mSqLiteDatabase.rawQuery(sqlMessage,null);
                if (cursor.moveToFirst()){
                    messageCount = cursor.getCount();
                }
            }
            for (int arrClassifiedIndex = 0; arrClassifiedIndex < arrClassified.size(); arrClassifiedIndex++) {
                Cursor cursor = mSqLiteDatabase.rawQuery(sqlClassified,null);
                if (cursor.moveToFirst()) {
                    classifiedCount = cursor.getCount();
                }
            }
            Log.i(TAG, "insertCount: CityId------ "+cityID+" MessageCount---- " + messageCount + " ClassifiedCount-----"+classifiedCount );
            countItem.setStrCityId(String.valueOf(cityID));
            countItem.setIntMessageCount(messageCount);
            countItem.setIntClassifiedCount(classifiedCount);
            arrayListCount.add(countItem);
        }


        String insertCount = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_COUNTS + " VALUES (?,?,?)";
        SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(insertCount);
        mSqLiteDatabase.beginTransaction();
        try {
            for (int arrIndex = 0 ; arrIndex < arrayListCount.size(); arrIndex++) {
                countItem = arrayListCount.get(arrIndex);
                sqLiteStatement.bindString(1,countItem.getStrCityId());
                sqLiteStatement.bindString(2,String.valueOf(countItem.getIntMessageCount()));
                sqLiteStatement.bindString(3, String.valueOf(countItem.getIntClassifiedCount()));
                sqLiteStatement.execute();
            }
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean updateMessageCount (int intMessageCount, String cityId) {
        String updateMessageCount = "UPDATE " + TABLE_COUNTS
                + " SET " + COLUMN_COUNT_MESSAGE + "='" + intMessageCount + "'"
                + " WHERE " + COLUMN_COUNT_CITY_ID + "='" + cityId + "'";
        SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(updateMessageCount);
        mSqLiteDatabase.beginTransaction();
        try {
            sqLiteStatement.execute();
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean updateClassifiedCount (int intClassifiedCount, String cityId) {
        String updateMessageCount = "UPDATE " + TABLE_COUNTS
                + " SET " + COLUMN_COUNT_CLASSIFIED + "='" + intClassifiedCount + "'"
                + " WHERE " + COLUMN_COUNT_CITY_ID + "='" + cityId + "'";
        SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(updateMessageCount);
        mSqLiteDatabase.beginTransaction();
        try {
            sqLiteStatement.execute();
            mSqLiteDatabase.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public CountItem queryCount (String cityId) {
        CountItem countItem = new CountItem();
        String queryCounts = "SELECT * FROM " + DatabaseHelper.TABLE_COUNTS
                + " WHERE " + COLUMN_CITY_ID_PRIMARY_EN + "='" + cityId + "'";
        Cursor cursorCount = mSqLiteDatabase.rawQuery(queryCounts,null);
        if (cursorCount.moveToFirst()) {
            do {
                countItem.setStrCityId(cursorCount.getString(cursorCount.getColumnIndexOrThrow(COLUMN_COUNT_CITY_ID)));
                countItem.setIntClassifiedCount(Integer.parseInt(cursorCount.getString(cursorCount.getColumnIndexOrThrow(COLUMN_COUNT_CLASSIFIED))));
                countItem.setIntMessageCount(Integer.parseInt(cursorCount.getString(cursorCount.getColumnIndexOrThrow(COLUMN_COUNT_MESSAGE))));
            } while (cursorCount.moveToNext());
        }
        if (cursorCount != null && !cursorCount.isClosed()) {
            cursorCount.close();
        }
        return  countItem;
    }

    /*-------------------------------------------COUNT LISTING-------------------------------------------*/

    public ArrayList<MessageDetailsItem> queryMessages(String strNewsMessageID, String strCurrentCity) {
        ArrayList<MessageDetailsItem> arrMessagesList = new ArrayList<>();
        String whereClause = COLUMN_MESSAGES_CITY + " =?";
        String[] whereArgs = new String[]{strCurrentCity};
        String orderBy = COLUMN_MESSAGES_CREATED_DATE + " DESC";
        if (!strNewsMessageID.equalsIgnoreCase("")) {
            whereClause = COLUMN_MESSAGES_CITY + " =? AND " + COLUMN_MESSAGES_ID_PRIMARY + "=?";
            whereArgs = new String[]{strCurrentCity, strNewsMessageID};
        }
        //Cursor cursor = sqLiteDatabase.query(tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursorCommittee = mSqLiteDatabase.query(TABLE_MESSAGE_NEWS_DETAILS, null, whereClause, whereArgs, null, null, orderBy);
        if (cursorCommittee.moveToFirst()) {
            do {
                String isActive = cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_IS_ACTIVE));
                if (isActive.equalsIgnoreCase("true")) {
                    MessageDetailsItem messageDetailsItem = new MessageDetailsItem();
                    messageDetailsItem.setMessageID(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_ID_PRIMARY)));
                    messageDetailsItem.setMessageTitle(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_TITLE)));
                    messageDetailsItem.setMessageDescription(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_DESCRIPTION)));
                    messageDetailsItem.setMessageImage(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_IMAGE_URL)));
                    messageDetailsItem.setMessageCreateDate(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_CREATED_DATE)));
                    messageDetailsItem.setMessageType(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_TYPE)));
                    messageDetailsItem.setMessageCity(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_CITY)));
                    messageDetailsItem.setMessageIsActive(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_MESSAGES_IS_ACTIVE)));
                    arrMessagesList.add(messageDetailsItem);
                }
            } while (cursorCommittee.moveToNext());
        }
        if (cursorCommittee != null && !cursorCommittee.isClosed()) {
            cursorCommittee.close();
        }
        return arrMessagesList;
    }

    public int getDataBaseVersion() {
        return mSqLiteDatabase.getVersion();
    }
}
