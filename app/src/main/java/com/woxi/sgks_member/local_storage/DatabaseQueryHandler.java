package com.woxi.sgks_member.local_storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.woxi.sgks_member.interfaces.DatabaseConstants;
import com.woxi.sgks_member.models.CommitteeDetailsItem;
import com.woxi.sgks_member.models.FamilyDetailsItem;
import com.woxi.sgks_member.models.MemberAddressItem;
import com.woxi.sgks_member.models.MemberDetailsItem;
import com.woxi.sgks_member.models.MessageDetailsItem;
import com.woxi.sgks_member.utils.AppCommonMethods;

import java.util.ArrayList;

import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_LAST_COMMITTEE_ID;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_LAST_FAMILY_ID;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_LAST_MESSAGE_ID;

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

    public boolean insertOrUpdateAllMembers(ArrayList<MemberDetailsItem> arrMemDetails) {
        //SQL Prepared Statement
        String insertMemberPreparedStatement = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_MEMBER_DETAILS + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
                if (memberDetailsItem.getMemID() != null) {
                    memberStatement.bindString(1, memberDetailsItem.getMemID());
                }
                if (memberDetailsItem.getMemFamilyId() != null) {
                    memberStatement.bindString(2, memberDetailsItem.getMemFamilyId());
                }
                if (memberDetailsItem.getMemSgksMemberId() != null) {
                    memberStatement.bindString(3, memberDetailsItem.getMemSgksMemberId());
                }
                if (memberDetailsItem.getMemSgksFamilyId() != null) {
                    memberStatement.bindString(4, memberDetailsItem.getMemSgksFamilyId());
                }
                if (memberDetailsItem.getMemFirstName() != null) {
                    memberStatement.bindString(5, memberDetailsItem.getMemFirstName());
                }
                if (memberDetailsItem.getMemMidName() != null) {
                    memberStatement.bindString(6, memberDetailsItem.getMemMidName());
                }
                if (memberDetailsItem.getMemSurname() != null) {
                    memberStatement.bindString(7, memberDetailsItem.getMemSurname());
                }
                if (memberDetailsItem.getMemGender() != null) {
                    memberStatement.bindString(8, memberDetailsItem.getMemGender());
                }
                if (memberDetailsItem.getMemMobile() != null) {
                    memberStatement.bindString(9, memberDetailsItem.getMemMobile());
                }
                if (memberDetailsItem.getMemEmail() != null) {
                    memberStatement.bindString(10, memberDetailsItem.getMemEmail());
                }
                if (memberDetailsItem.getMemBloodGroup() != null) {
                    memberStatement.bindString(11, memberDetailsItem.getMemBloodGroup());
                }
                if (memberDetailsItem.getMemMaritalStatus() != null) {
                    memberStatement.bindString(12, memberDetailsItem.getMemMaritalStatus());
                }
                if (memberDetailsItem.getMemSgksArea() != null) {
                    memberStatement.bindString(13, memberDetailsItem.getMemSgksArea());
                }
                if (memberDetailsItem.getMemLatitude() != null) {
                    memberStatement.bindString(14, memberDetailsItem.getMemLatitude());
                }
                if (memberDetailsItem.getMemLongitude() != null) {
                    memberStatement.bindString(15, memberDetailsItem.getMemLongitude());
                }
                if (memberDetailsItem.getMemSgksMainCity() != null) {
                    memberStatement.bindString(16, memberDetailsItem.getMemSgksMainCity());
                }
                if (memberDetailsItem.getAddress_id() != null) {
                    memberStatement.bindString(17, memberDetailsItem.getAddress_id());
                }
                if (memberDetailsItem.getMemberImageURL() != null) {
                    memberStatement.bindString(18, memberDetailsItem.getMemberImageURL());
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

    public ArrayList<MemberDetailsItem> queryMembers(String searchString) {
        ArrayList<MemberDetailsItem> arrMemDetails = new ArrayList<>();
        String whereClause = COLUMN_MEMBER_SGKS_CITY + " =?";
//        String[] whereArgs = new String[]{strCurrentCity};
        if (!searchString.equalsIgnoreCase("")) {
//            if (searchString.contains("-")) {
            if (searchString.matches("^[0-9]*-[0-9]*$")) {
                //TODO Search in member id column
                Log.d(TAG, "search by: Member ID");
//                whereClause = COLUMN_MEMBER_ID_PRIMARY + "=?";
//                whereArgs = new String[]{searchString};
                whereClause = COLUMN_MEMBER_SGKS_CITY + " =? AND " + COLUMN_MEMBER_SGKS_MEMBER_ID + " LIKE ?";
//                whereArgs = new String[]{strCurrentCity, searchString + "%"};
            } else if (searchString.matches("^[0-9]*$")) {
                //TODO Search in mobile no column
                Log.d(TAG, "search by: Mobile");
//              whereClause = COLUMN_MEMBER_MOBILE + "=?";
//              whereArgs = new String[]{searchString};
                whereClause = COLUMN_MEMBER_SGKS_CITY + " =? AND " + COLUMN_MEMBER_MOBILE + " LIKE ?";
//              whereArgs = new String[]{strCurrentCity, searchString + "%"};
            } else if (searchString.contains(" ")) {
                //TODO Search in name & surname column and vice-versa
                String[] splitStrings = searchString.split("\\s+");
                String firstString, secondString, thirdString;
                if (splitStrings.length == 2) {
                    Log.d(TAG, "search by: Name Surname");
                    firstString = splitStrings[0].trim();
                    secondString = splitStrings[1].trim();
//                  whereClause = "(" + COLUMN_MEMBER_FIRST_NAME + "=? AND " + COLUMN_MEMBER_LAST_NAME + "=?) OR (" + COLUMN_MEMBER_FIRST_NAME + "=? AND " + COLUMN_MEMBER_LAST_NAME + "=? )";
//                  whereArgs = new String[]{firstString, secondString, secondString, firstString};
                    whereClause = COLUMN_MEMBER_SGKS_CITY + " =? AND (" + "(" + COLUMN_MEMBER_FIRST_NAME + " LIKE ? AND " + COLUMN_MEMBER_LAST_NAME + " LIKE ?) OR (" + COLUMN_MEMBER_FIRST_NAME + " LIKE ? AND " + COLUMN_MEMBER_LAST_NAME + " LIKE ? ))";
//                    whereArgs = new String[]{strCurrentCity, firstString + "%", secondString + "%", secondString + "%", firstString + "%"};
                } else if (splitStrings.length == 3) {
                    Log.d(TAG, "search by: FullName");
                    firstString = splitStrings[0].trim();
                    secondString = splitStrings[1].trim();
                    thirdString = splitStrings[2].trim();
//                    whereClause = "(" + COLUMN_MEMBER_FIRST_NAME + "=? AND " + COLUMN_MEMBER_MIDDLE_NAME + "=? AND " + COLUMN_MEMBER_LAST_NAME + "=?) OR (" + COLUMN_MEMBER_FIRST_NAME + "=? AND " + COLUMN_MEMBER_MIDDLE_NAME + "=? AND " + COLUMN_MEMBER_LAST_NAME + "=?)";
//                    whereArgs = new String[]{firstString, secondString, thirdString, thirdString, secondString, firstString};
                    whereClause = COLUMN_MEMBER_SGKS_CITY + " =? AND (" + "(" + COLUMN_MEMBER_FIRST_NAME + " LIKE ? AND " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE ? AND " + COLUMN_MEMBER_LAST_NAME + " LIKE ?) OR (" + COLUMN_MEMBER_FIRST_NAME + " LIKE ? AND " + COLUMN_MEMBER_MIDDLE_NAME + " LIKE ? AND " + COLUMN_MEMBER_LAST_NAME + " LIKE ?))";
//                    whereArgs = new String[]{strCurrentCity, firstString + "%", secondString + "%", thirdString + "%", thirdString + "%", secondString + "%", firstString + "%"};
                }
            } else if (searchString.matches("^[A-Za-z]*$")) {
                //TODO Search in name OR surname column
                Log.d(TAG, "search by: Name AND Surname");
//                whereClause = COLUMN_MEMBER_FIRST_NAME + "=? OR " + COLUMN_MEMBER_LAST_NAME + "=?";
//                whereArgs = new String[]{searchString, searchString};
//                whereClause = "(" + COLUMN_MEMBER_FIRST_NAME + " LIKE ? OR " + COLUMN_MEMBER_LAST_NAME + " LIKE ?) AND (" + COLUMN_MEMBER_LAST_NAME + " LIKE ? OR " + COLUMN_MEMBER_FIRST_NAME + " LIKE ? )";
//                whereArgs = new String[]{searchString + "%", searchString + "%", searchString + "%", searchString + "%"};
                whereClause = COLUMN_MEMBER_SGKS_CITY + " =? AND (" + COLUMN_MEMBER_FIRST_NAME + " LIKE ? OR " + COLUMN_MEMBER_LAST_NAME + " LIKE ? )";
//                whereArgs = new String[]{strCurrentCity, searchString + "%", searchString + "%"};
            }
        }
        //Cursor cursor = sqLiteDatabase.query(tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
        Cursor cursorMember = mSqLiteDatabase.query(TABLE_MEMBER_DETAILS, null, null, null, null, null, null);
        if (cursorMember.moveToFirst()) {
            do {
//                if (isMemberActive.equalsIgnoreCase("true")) {
                    MemberDetailsItem memberDetailsItem = new MemberDetailsItem();
                    memberDetailsItem.setMemID(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_ID_PRIMARY)));
                    memberDetailsItem.setMemFamilyId(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_FAMILY_ID_FOREIGN_KEY)));
                    memberDetailsItem.setMemSgksMemberId(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_SGKS_MEMBER_ID)));
                    memberDetailsItem.setMemSgksFamilyId(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_SGKS_FAMILY_ID)));
                    memberDetailsItem.setMemFirstName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_FIRST_NAME)));
                    memberDetailsItem.setMemMidName(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_MIDDLE_NAME)));
                    memberDetailsItem.setMemSurname(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LAST_NAME)));
                    memberDetailsItem.setMemGender(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_GENDER)));
                    memberDetailsItem.setMemMobile(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_MOBILE)));
                    memberDetailsItem.setMemEmail(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_EMAIL)));
                    memberDetailsItem.setMemBloodGroup(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_BLOOD_GROUP)));
                    memberDetailsItem.setMemMaritalStatus(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_MARITAL_STATUS)));
                    memberDetailsItem.setMemSgksArea(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_SGKS_AREA)));
                    memberDetailsItem.setMemSgksMainCity(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_SGKS_CITY)));
                    memberDetailsItem.setMemLatitude(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LATITUDE)));
                    memberDetailsItem.setMemLongitude(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_LONGITUDE)));
                    memberDetailsItem.setMemberImageURL(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_IMAGE_URL)));
                    memberDetailsItem.setAddress_id(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS_ID)));
                    memberDetailsItem.setMemIsActive(cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_IS_ACTIVE)));
//////////////////
                    String strFamilyId = cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_FAMILY_ID_FOREIGN_KEY));
                    String strMemberAddressId = cursorMember.getString(cursorMember.getColumnIndexOrThrow(COLUMN_MEMBER_ADDRESS_ID));
                    /*MemberAddressItem memberAddressItem = queryAddress(strFamilyId, strMemberAddressId);
                    memberDetailsItem.setMemAddress(memberAddressItem);*/
/////////////////
                    arrMemDetails.add(memberDetailsItem);
//                }
            } while (cursorMember.moveToNext());
        }
        if (cursorMember != null && !cursorMember.isClosed()) {
            cursorMember.close();
        }
        return arrMemDetails;
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
