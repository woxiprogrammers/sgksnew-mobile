package com.woxi.sgkks_member.local_storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.interfaces.DatabaseConstants;
import com.woxi.sgkks_member.models.CityIteam;
import com.woxi.sgkks_member.models.CommitteeDetailsItem;
import com.woxi.sgkks_member.models.EventDataItem;
import com.woxi.sgkks_member.models.FamilyDetailsItem;
import com.woxi.sgkks_member.models.MemberAddressItem;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.models.MessageDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;

import java.util.ArrayList;

import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LANGUAGE_APPLIED;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LAST_COMMITTEE_ID;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LAST_FAMILY_ID;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LAST_MESSAGE_ID;

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

    boolean insertOrUpdateAllFamilies(ArrayList<FamilyDetailsItem> arrFamilyDetailsItems, boolean isUpdate) {
        //SQL Prepared Statement
        String insertFamilyPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_FAMILY_DETAILS + " VALUES (?,?,?,?,?);";

        SQLiteStatement familyStatement = mSqLiteDatabase.compileStatement(insertFamilyPreparedStatement);
        mSqLiteDatabase.beginTransaction();
        FamilyDetailsItem familyDetailsItem;
        try {
            for (int arrIndex = 0; arrIndex < arrFamilyDetailsItems.size(); arrIndex++) {
                familyStatement.clearBindings();
                familyDetailsItem = arrFamilyDetailsItems.get(arrIndex);
                if (familyDetailsItem.getFamily_id() != null) {
                    familyStatement.bindString(1, familyDetailsItem.getFamily_id());

                    //Save last inserted id
                    if (!isUpdate && arrIndex == arrFamilyDetailsItems.size() - 1) {
                        String lastFamilyId = familyDetailsItem.getFamily_id();
                        AppCommonMethods.putStringPref(PREFS_LAST_FAMILY_ID, lastFamilyId, mContext);
                    }
                }
                if (familyDetailsItem.getSgks_family_id() != null) {
                    familyStatement.bindString(2, familyDetailsItem.getSgks_family_id());
                }
                if (familyDetailsItem.getSurname() != null) {
                    familyStatement.bindString(3, familyDetailsItem.getSurname());
                }
                if (familyDetailsItem.getNative_place() != null) {
                    familyStatement.bindString(4, familyDetailsItem.getNative_place());
                }
                if (familyDetailsItem.getSgks_city() != null) {
                    familyStatement.bindString(5, familyDetailsItem.getSgks_city());
                }
                familyStatement.executeInsert();
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

    public ArrayList<MemberDetailsItem> queryMembers(String searchString) {
        ArrayList<MemberDetailsItem> arrMemDetails = new ArrayList<>();
        String sqlQuery = "";
        if (searchString.equalsIgnoreCase("")){
            Log.i(TAG, "Display all records");
            sqlQuery = "select * from "
                    +TABLE_MEMBER_DETAILS_EN
                    + " where "+COLUMN_MEMBER_SGKS_CITY_ID+"="+AppCommonMethods.getStringPref(AppConstants.PREFS_CURRENT_CITY,mContext)
                    + " and "+COLUMN_MEMBER_IS_ACTIVE+"='true'";
            new AppCommonMethods(mContext).LOG(0,TAG,sqlQuery);
        }  else if (searchString.matches("^[A-Za-z]*$")) {
            //TODO Search in name OR surname column
            Log.d(TAG, "search by: Name AND Surname");
            sqlQuery = "select * from "
                    +TABLE_MEMBER_DETAILS_EN
                    + " where "+COLUMN_MEMBER_SGKS_CITY_ID+"="+AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext)
                    + " and "+ COLUMN_MEMBER_FIRST_NAME +"="+"'"+searchString+"'"
                    + " and "+COLUMN_MEMBER_IS_ACTIVE+"='true'";

            new AppCommonMethods(mContext).LOG(0,TAG,sqlQuery);
        } else if (searchString.contains(" ")) {
            //TODO Search in name & surname column and vice-versa
            String[] splitStrings = searchString.split("\\s+");
            String firstString, secondString, thirdString;
            if (splitStrings.length == 2) {
                Log.d(TAG, "search by: Name Surname");
                firstString = splitStrings[0].trim();
                secondString = splitStrings[1].trim();
                sqlQuery = "select * from "
                        + TABLE_MEMBER_DETAILS_EN
                        + " where " + COLUMN_MEMBER_FIRST_NAME + " like " + "'" + firstString + "'"
                        + " or " + COLUMN_MEMBER_MIDDLE_NAME + " like " + "'" + secondString + "'"
                        + " and "+COLUMN_MEMBER_IS_ACTIVE+"='true'";;
                new AppCommonMethods(mContext).LOG(0, TAG, sqlQuery);
            }
            if (splitStrings.length == 3) {
                Log.d(TAG, "search by: FullName");
                firstString = splitStrings[0].trim();
                secondString = splitStrings[1].trim();
                thirdString = splitStrings[2].trim();
                sqlQuery = "select * from "
                        + TABLE_MEMBER_DETAILS_EN
                        + " where " + COLUMN_MEMBER_FIRST_NAME + " like " + "'" + firstString + "'"
                        + " or " + COLUMN_MEMBER_MIDDLE_NAME + " like " + "'" + secondString + "'"
                        + " or " + COLUMN_MEMBER_LAST_NAME + " like " + "'" + thirdString + "'"
                        + " and "+COLUMN_MEMBER_IS_ACTIVE+"='true'";;
                new AppCommonMethods(mContext).LOG(0, TAG, sqlQuery);
            }
        }
//        Cursor cursor = sqLiteDatabase.query(tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        //   Cursor cursorMember = mSqLiteDatabase.query(TABLE_MEMBER_DETAILS_EN, null, null, null, null, null, null);
        Cursor cursorMember = mSqLiteDatabase.rawQuery(sqlQuery,null);
        Log.i(TAG, "queryMembers: cursorMember.moveToFirst(): "+cursorMember.moveToFirst());
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
                memberDetailsItem.setStrBloodGroup(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_BLOOD_GROUP_ID)));
                memberDetailsItem.setStrLatitude(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LATITUDE)));
                memberDetailsItem.setStrLongitude(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LONGITUDE)));
                memberDetailsItem.setStrMemberImageUrl(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_IMAGE_URL)));
                arrMemDetails.add(memberDetailsItem);
            } while (cursorMember.moveToNext());
        }

        if (cursorMember != null && !cursorMember.isClosed()) {
            cursorMember.close();
        }
        return arrMemDetails;
    }

    /*-------------------------------------------MEMBER LISTING-------------------------------------------*/

    /*-------------------------------------------CITY LISTING-------------------------------------------*/

    public boolean insertOrUpdateCitiesEnglish(ArrayList<CityIteam> arrCityItems){
        String inserCityPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_CITIES_EN + " VALUES (?,?,?,?)";
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
        String sqlQuery="";
        if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1") && strSearchKey.equalsIgnoreCase("")){
            sqlQuery = "SELECT * FROM "+TABLE_CITIES_EN;
            new AppCommonMethods(mContext).LOG(0, TAG, sqlQuery);

        } else if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2") && strSearchKey.equalsIgnoreCase("")){
            sqlQuery = "SELECT * FROM "+TABLE_CITIES_GJ;
            new AppCommonMethods(mContext).LOG(0, TAG, sqlQuery);
        }
        Cursor cityCursor = mSqLiteDatabase.rawQuery(sqlQuery,null);
        Log.i(TAG, "queryCities: "+cityCursor);
        Log.i(TAG, "queryCities: "+cityCursor.moveToFirst());
        if (cityCursor.moveToFirst()){
            do {
                CityIteam cityIteam = new CityIteam();
                cityIteam.setIntCityId(Integer.parseInt(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_ID_PRIMARY))));
                cityIteam.setStrCityName(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_NAME)));
                cityIteam.setStrStateId(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_STATE_ID)));
                cityIteam.setIs_active(cityCursor.getString(cityCursor.getColumnIndexOrThrow(COLUMN_CITY_IS_ACTIVE)));
                arrCityIteam.add(cityIteam);
            } while (cityCursor.moveToNext());
        }
        if (cityCursor !=null && !cityCursor.isClosed()){
            cityCursor.close();
        }
        return  arrCityIteam;
    }

    /*-------------------------------------------CITY LISTING-------------------------------------------*/

    /*-------------------------------------------EVENTS LISTING-------------------------------------------*/

    public boolean insertOrUpdateEventsEnglish(ArrayList<EventDataItem> arrayListEvent){
        String insertEventPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_EVENTS_EN + " VALUES (?,?,?,?,?,?)";
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

    public boolean insertOrUpdateEventImages(ArrayList<EventDataItem> arrayListEvents) {
        String insertImages = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_EVENT_IMAGES + " VALUES (?,?,?)";
        SQLiteStatement sqLiteStatement = mSqLiteDatabase.compileStatement(insertImages);
        mSqLiteDatabase.beginTransaction();
        EventDataItem eventDataItem;
        try {
            for (int arrIndex = 0; arrIndex < arrayListEvents.size(); arrIndex++) {
                eventDataItem = arrayListEvents.get(arrIndex);

            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    /*-------------------------------------------EVENTS LISTING-------------------------------------------*/

    boolean insertOrUpdateAllAddresses(ArrayList<MemberAddressItem> arrMemberAddressItems, boolean isUpdate) {
        //SQL Prepared Statement
        String insertAddressPreparedStatement;
        if (isUpdate) {
            insertAddressPreparedStatement = "UPDATE " + DatabaseHelper.TABLE_MEM_ADDRESS_DETAILS + " SET " + COLUMN_ADDRESS_ADDRESS_LINE + "=?, "
                    + COLUMN_ADDRESS_AREA + "=?, " + COLUMN_ADDRESS_LANDMARK + "=?, " + COLUMN_ADDRESS_CITY + "=?, " + COLUMN_ADDRESS_PINCODE + "=?, "
                    + COLUMN_ADDRESS_STATE + "=?, " + COLUMN_ADDRESS_COUNTRY + "=? "
                    + " WHERE " + COLUMN_ADDRESS_FAMILY_ID_FOREIGN_KEY + "=? AND " + COLUMN_ADDRESS_ADDRESS_ID + "=?";
        } else {
            insertAddressPreparedStatement = "INSERT INTO " + DatabaseHelper.TABLE_MEM_ADDRESS_DETAILS + " VALUES (?,?,?,?,?,?,?,?,?,?)";
        }
        SQLiteStatement addressStatement = mSqLiteDatabase.compileStatement(insertAddressPreparedStatement);
        mSqLiteDatabase.beginTransaction();
        MemberAddressItem memberAddressItem;
        try {
            for (int arrIndex = 0; arrIndex < arrMemberAddressItems.size(); arrIndex++) {
                addressStatement.clearBindings();
                memberAddressItem = arrMemberAddressItems.get(arrIndex);
                if (isUpdate) {
                    if (memberAddressItem.getAddAddressLine() != null) {
                        addressStatement.bindString(1, memberAddressItem.getAddAddressLine());
                    }
                    if (memberAddressItem.getAddArea() != null) {
                        addressStatement.bindString(2, memberAddressItem.getAddArea());
                    }
                    if (memberAddressItem.getAddLandMark() != null) {
                        addressStatement.bindString(3, memberAddressItem.getAddLandMark());
                    }
                    if (memberAddressItem.getAddCity() != null) {
                        addressStatement.bindString(4, memberAddressItem.getAddCity());
                    }
                    if (memberAddressItem.getAddPincode() != null) {
                        addressStatement.bindString(5, memberAddressItem.getAddPincode());
                    }
                    if (memberAddressItem.getAddState() != null) {
                        addressStatement.bindString(6, memberAddressItem.getAddState());
                    }
                    if (memberAddressItem.getAddCountry() != null) {
                        addressStatement.bindString(7, memberAddressItem.getAddCountry());
                    }
                    if (memberAddressItem.getAddFamilyId() != null) {
                        addressStatement.bindString(8, memberAddressItem.getAddFamilyId());
                    }
                    if (memberAddressItem.getAddAddressId() != null) {
                        addressStatement.bindString(9, memberAddressItem.getAddAddressId());
                    }
                } else {
//        addressStatement.bindString(1,memAddress); //Index
                    if (memberAddressItem.getAddFamilyId() != null) {
                        addressStatement.bindString(2, memberAddressItem.getAddFamilyId());
                    }
                    if (memberAddressItem.getAddAddressId() != null) {
                        addressStatement.bindString(3, memberAddressItem.getAddAddressId());
                    }
                    if (memberAddressItem.getAddAddressLine() != null) {
                        addressStatement.bindString(4, memberAddressItem.getAddAddressLine());
                    }
                    if (memberAddressItem.getAddArea() != null) {
                        addressStatement.bindString(5, memberAddressItem.getAddArea());
                    }
                    if (memberAddressItem.getAddLandMark() != null) {
                        addressStatement.bindString(6, memberAddressItem.getAddLandMark());
                    }
                    if (memberAddressItem.getAddCity() != null) {
                        addressStatement.bindString(7, memberAddressItem.getAddCity());
                    }
                    if (memberAddressItem.getAddPincode() != null) {
                        addressStatement.bindString(8, memberAddressItem.getAddPincode());
                    }
                    if (memberAddressItem.getAddState() != null) {
                        addressStatement.bindString(9, memberAddressItem.getAddState());
                    }
                    if (memberAddressItem.getAddCountry() != null) {
                        addressStatement.bindString(10, memberAddressItem.getAddCountry());
                    }
                }
                addressStatement.execute();
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

    boolean insertOrUpdateAllCommittees(ArrayList<CommitteeDetailsItem> arrMainCommList, boolean isUpdate) {
        //SQL Prepared Statement
        String insertCommitteePreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_COMMITTEE_DETAILS + " VALUES (?,?,?,?,?,?);";

        SQLiteStatement committeeStatement = mSqLiteDatabase.compileStatement(insertCommitteePreparedStatement);
        mSqLiteDatabase.beginTransaction();
        CommitteeDetailsItem committeeDetailsItem;
        try {
            for (int arrCommitteeIndex = 0; arrCommitteeIndex < arrMainCommList.size(); arrCommitteeIndex++) {
                committeeStatement.clearBindings();
                committeeDetailsItem = arrMainCommList.get(arrCommitteeIndex);
                if (committeeDetailsItem.getCommitteeID() != null) {
                    committeeStatement.bindString(1, committeeDetailsItem.getCommitteeID());

                    //Save last inserted id
                    if (!isUpdate && arrCommitteeIndex == arrMainCommList.size() - 1) {
                        String lastCommitteeId = committeeDetailsItem.getCommitteeID();
                        AppCommonMethods.putStringPref(PREFS_LAST_COMMITTEE_ID, lastCommitteeId, mContext);
                    }
                }
                if (committeeDetailsItem.getCommitteeName() != null) {
                    committeeStatement.bindString(2, committeeDetailsItem.getCommitteeName());
                }
                if (committeeDetailsItem.getCommitteeDescription() != null) {
                    committeeStatement.bindString(3, committeeDetailsItem.getCommitteeDescription());
                }
                if (committeeDetailsItem.getCommitteeCity() != null) {
                    committeeStatement.bindString(4, committeeDetailsItem.getCommitteeCity());
                }
                if (committeeDetailsItem.getCommAllMembers() != null) {
                    committeeStatement.bindString(5, committeeDetailsItem.getCommAllMembers());
                }
                if (committeeDetailsItem.getCommIsActive() != null) {
                    committeeStatement.bindString(6, committeeDetailsItem.getCommIsActive());
                }
                committeeStatement.execute();
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

    public boolean insertOrUpdateAllMessages(ArrayList<MessageDetailsItem> mArrNewsDetails, boolean isUpdate) {
        String insertMessagePreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_MESSAGE_NEWS_DETAILS + " VALUES (?,?,?,?,?,?,?,?);";

        SQLiteStatement messageStatement = mSqLiteDatabase.compileStatement(insertMessagePreparedStatement);
        mSqLiteDatabase.beginTransaction();
        MessageDetailsItem messageDetailsItem;
        try {
            for (int arrMessagesIndex = 0; arrMessagesIndex < mArrNewsDetails.size(); arrMessagesIndex++) {
                messageDetailsItem = mArrNewsDetails.get(arrMessagesIndex);
                messageStatement.clearBindings();
                if (messageDetailsItem.getMessageID() != null) {
                    messageStatement.bindString(1, messageDetailsItem.getMessageID());

                    //Save last inserted id
                    if (!isUpdate && arrMessagesIndex == mArrNewsDetails.size() - 1) {
                        String lastMessageId = messageDetailsItem.getMessageID();
                        AppCommonMethods.putStringPref(PREFS_LAST_MESSAGE_ID, lastMessageId, mContext);
                    }
                }
                if (messageDetailsItem.getMessageTitle() != null) {
                    messageStatement.bindString(2, messageDetailsItem.getMessageTitle());
                }
                if (messageDetailsItem.getMessageDescription() != null) {
                    messageStatement.bindString(3, messageDetailsItem.getMessageDescription());
                }
                if (messageDetailsItem.getMessageImage() != null) {
                    messageStatement.bindString(4, messageDetailsItem.getMessageImage());
                }
                if (messageDetailsItem.getMessageCreateDate() != null) {
                    messageStatement.bindString(5, messageDetailsItem.getMessageCreateDate());
                }
                if (messageDetailsItem.getMessageType() != null) {
                    messageStatement.bindString(6, messageDetailsItem.getMessageType());
                }
                if (messageDetailsItem.getMessageCity() != null) {
                    messageStatement.bindString(7, messageDetailsItem.getMessageCity());
                }
                if (messageDetailsItem.getMessageIsActive() != null) {
                    messageStatement.bindString(8, messageDetailsItem.getMessageIsActive());
                }
                messageStatement.execute();
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



    private MemberAddressItem queryAddress(String strFamilyId, String strMemberAddressId) {
        MemberAddressItem memberAddressItem = new MemberAddressItem();
        String whereClause = null;
        String[] whereArgs = null;
        whereClause = COLUMN_ADDRESS_FAMILY_ID_FOREIGN_KEY + "=? AND " + COLUMN_ADDRESS_ADDRESS_ID + "=?";
        whereArgs = new String[]{strFamilyId, strMemberAddressId};
        //Cursor cursor = sqLiteDatabase.query(tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursorAddress = mSqLiteDatabase.query(TABLE_MEM_ADDRESS_DETAILS, null, whereClause, whereArgs, null, null, null);
        if (cursorAddress.moveToFirst()) {
            do {
                memberAddressItem.setAddFamilyId(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_FAMILY_ID_FOREIGN_KEY)));
                memberAddressItem.setAddAddressLine(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_ADDRESS_LINE)));
                memberAddressItem.setAddArea(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_AREA)));
                memberAddressItem.setAddLandMark(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_LANDMARK)));
                memberAddressItem.setAddCity(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_CITY)));
                memberAddressItem.setAddPincode(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_PINCODE)));
                memberAddressItem.setAddState(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_STATE)));
                memberAddressItem.setAddCountry(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_COUNTRY)));
                memberAddressItem.setAddAddressId(cursorAddress.getString(cursorAddress.getColumnIndexOrThrow(COLUMN_ADDRESS_ADDRESS_ID)));
            } while (cursorAddress.moveToNext());
        }
        if (cursorAddress != null && !cursorAddress.isClosed()) {
            cursorAddress.close();
        }
        return memberAddressItem;
    }

    public ArrayList<CommitteeDetailsItem> queryCommittees(String strCommitteeID, String strCurrentCity) {
        ArrayList<CommitteeDetailsItem> arrCommitteeList = new ArrayList<>();

        String whereClause = COLUMN_COMMITTEE_CITY + " =?";
        String[] whereArgs = new String[]{strCurrentCity};

        if (!strCommitteeID.equalsIgnoreCase("")) {
            whereClause = COLUMN_COMMITTEE_CITY + " =? AND " + COLUMN_COMMITTEE_ID_PRIMARY + "=?";
            whereArgs = new String[]{strCurrentCity, strCommitteeID};
        }
        //Cursor cursor = sqLiteDatabase.query(tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursorCommittee = mSqLiteDatabase.query(TABLE_COMMITTEE_DETAILS, null, whereClause, whereArgs, null, null, null);
        if (cursorCommittee.moveToFirst()) {
            do {
                String isActive = cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_COMMITTEE_IS_ACTIVE));
                if (isActive.equalsIgnoreCase("true")) {
                    CommitteeDetailsItem committeeDetailsItem = new CommitteeDetailsItem();
                    committeeDetailsItem.setCommitteeID(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_COMMITTEE_ID_PRIMARY)));
                    committeeDetailsItem.setCommitteeName(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_COMMITTEE_NAME)));
                    committeeDetailsItem.setCommitteeDescription(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_COMMITTEE_DESCRIPTION)));
                    committeeDetailsItem.setCommitteeCity(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_COMMITTEE_CITY)));
                    committeeDetailsItem.setCommAllMembers(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_COMMITTEE_MEMBERS)));
                    committeeDetailsItem.setCommIsActive(cursorCommittee.getString(cursorCommittee.getColumnIndexOrThrow(COLUMN_COMMITTEE_IS_ACTIVE)));
                    arrCommitteeList.add(committeeDetailsItem);
                }
            } while (cursorCommittee.moveToNext());
        }
        if (cursorCommittee != null && !cursorCommittee.isClosed()) {
            cursorCommittee.close();
        }
        return arrCommitteeList;
    }

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
