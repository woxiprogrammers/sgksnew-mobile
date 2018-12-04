package com.woxi.sgkks_member.utils;

import android.util.Log;

import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.models.AccountDetailsItem;
import com.woxi.sgkks_member.models.BloodGroupItems;
import com.woxi.sgkks_member.models.CityIteam;
import com.woxi.sgkks_member.models.ClassifiedDetailsItem;
import com.woxi.sgkks_member.models.CommitteeDetailsItem;
import com.woxi.sgkks_member.models.EventDataItem;
import com.woxi.sgkks_member.models.LocalDataSyncItem;
import com.woxi.sgkks_member.models.MasterDataItem;
import com.woxi.sgkks_member.models.MemberAddressItem;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.models.MemberSearchDataItem;
import com.woxi.sgkks_member.models.MessageAndClassifiedResponseItem;
import com.woxi.sgkks_member.models.MessageDetailsItem;
import com.woxi.sgkks_member.models.SGKSAreaItem;
import com.woxi.sgkks_member.models.SGKSCategory;
import com.woxi.sgkks_member.models.AccountImages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <b>public class AppParser implements AppConstants</b>
 * <p>This class is used to parse JSON responses</p>
 * created by - Rohit
 */
public class AppParser implements AppConstants {
    public static Object parseCommitteeListResponse(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        //
        ArrayList<CommitteeDetailsItem> arrMainCommList = new ArrayList<>();
        if (jsonResponseObject.has("data") && jsonResponseObject.getString("data") != null) {
            JSONArray jsonDataArray = jsonResponseObject.optJSONArray("data");
            if (jsonDataArray != null) {
                for (int arrIndexCom = 0; arrIndexCom < jsonDataArray.length(); arrIndexCom++) {
                    //
                    CommitteeDetailsItem mainCommDetailsItem = new CommitteeDetailsItem();
                    JSONObject jsonListObject = jsonDataArray.optJSONObject(arrIndexCom);
                    if (jsonListObject.has("name") && jsonListObject.getString("name") != null) {
                        mainCommDetailsItem.setCommitteeName(jsonListObject.getString("name"));
                    }
                    if (jsonListObject.has("id") && jsonListObject.getString("id") != null) {
                        mainCommDetailsItem.setCommitteeID(jsonListObject.getString("id"));
                    }
                    if (jsonListObject.has("description") && jsonListObject.getString("description") != null) {
                        mainCommDetailsItem.setCommitteeDescription(jsonListObject.getString("description"));
                    }
                    if (jsonListObject.has("city") && jsonListObject.getString("city") != null) {
                        mainCommDetailsItem.setCommitteeCity(jsonListObject.getString("city"));
                    }
                    if (jsonListObject.has("members") && jsonListObject.getString("members") != null) {
                        mainCommDetailsItem.setCommAllMembers(jsonListObject.getString("members"));
                    }
                    arrMainCommList.add(mainCommDetailsItem);
                }
            }
        }
        return arrMainCommList;
    }

    public static Object parseMemberList(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        MemberDetailsItem globalMemberDetailsItem = new MemberDetailsItem();
        if (jsonResponseObject.has("data") && jsonResponseObject.optJSONArray("data") != null) {
            JSONArray jsonDataArray = jsonResponseObject.optJSONArray("data");
            ArrayList<MemberDetailsItem> arrMemberList = new ArrayList<>();
            if (jsonDataArray != null) {
                for (int arrIndexData = 0; arrIndexData < jsonDataArray.length(); arrIndexData++) {
                    MemberDetailsItem memberDetailsItem = new MemberDetailsItem();
                    JSONObject jsonMemberObject = jsonDataArray.optJSONObject(arrIndexData);
                    if (jsonMemberObject.has("id") && jsonMemberObject.optString("id") != null && !jsonMemberObject.optString("id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrId(jsonMemberObject.optString("id"));
                    }
                    if (jsonMemberObject.has("first_name") && jsonMemberObject.optString("first_name") != null && !jsonMemberObject.optString("first_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrFirstName(jsonMemberObject.optString("first_name"));
                    }
                    if (jsonMemberObject.has("middle_name") && jsonMemberObject.optString("middle_name") != null && !jsonMemberObject.optString("middle_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrMiddleName(jsonMemberObject.optString("middle_name"));
                    }
                    if (jsonMemberObject.has("last_name") && jsonMemberObject.optString("last_name") != null && !jsonMemberObject.optString("last_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrLastName(jsonMemberObject.optString("last_name"));
                    }
                    if (jsonMemberObject.has("city") && jsonMemberObject.optString("city") != null && !jsonMemberObject.optString("city").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrCity(jsonMemberObject.optString("city"));
                    }
                    if (jsonMemberObject.has("member_id") && jsonMemberObject.optString("member_id") != null && !jsonMemberObject.optString("member_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrMemberId(jsonMemberObject.optString("member_id"));
                    }
                    if (jsonMemberObject.has("gender") && jsonMemberObject.optString("gender") != null && !jsonMemberObject.optString("gender").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrGender(jsonMemberObject.optString("gender"));
                    }
                    if (jsonMemberObject.has("mobile") && jsonMemberObject.getString("mobile") != null) {
                        memberDetailsItem.setStrMobileNumber(jsonMemberObject.optString("mobile"));
                    }
                    if (jsonMemberObject.has("member_image_url") && jsonMemberObject.getString("member_image_url") != null) {
                        memberDetailsItem.setStrMemberImageUrl(jsonMemberObject.optString("member_image_url"));
                    }
                    if (jsonMemberObject.has("latitude") && jsonMemberObject.getString("latitude") != null) {
                        memberDetailsItem.setStrLatitude(jsonMemberObject.optString("latitude"));
                    }
                    if (jsonMemberObject.has("longitude") && jsonMemberObject.getString("longitude") != null) {
                        memberDetailsItem.setStrLongitude(jsonMemberObject.optString("longitude"));
                    }
                    if (jsonMemberObject.has("blood_group") && jsonMemberObject.optString("blood_group") != null && !jsonMemberObject.optString("blood_group").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrBloodGroup(jsonMemberObject.optString("blood_group"));
                    }
                    if (jsonMemberObject.has("address") && jsonMemberObject.optString("address") != null && !jsonMemberObject.optString("address").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrAddress(jsonMemberObject.optString("address"));
                    }
                    if (jsonMemberObject.has("date_of_birth") && jsonMemberObject.optString("date_of_birth") != null && !jsonMemberObject.optString("date_of_birth").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrDateOfBirth(jsonMemberObject.optString("date_of_birth"));
                    }
                    if (jsonMemberObject.has("email") && jsonMemberObject.getString("email") != null ) {
                        memberDetailsItem.setStrEmail(jsonMemberObject.getString("email"));
                    }
                    arrMemberList.add(memberDetailsItem);
                }
                globalMemberDetailsItem.setArrMemberList(arrMemberList);
            }
            return globalMemberDetailsItem;
        }
        return false;
    }

    public static Object parseMessageResponse(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        MessageAndClassifiedResponseItem messageAndClassifiedResponseItem = new MessageAndClassifiedResponseItem();
        /*if (jsonResponseObject.has("pagination") && jsonResponseObject.optJSONObject("pagination") != null) {
            JSONObject jsonPaginationObject = jsonResponseObject.optJSONObject("pagination");
            if (jsonPaginationObject != null) {
                if (jsonPaginationObject.has("next_page_url") && jsonPaginationObject.optString("next_page_url") != null) {
                    messageAndClassifiedResponseItem.setStrNextPageUrl(jsonPaginationObject.optString("next_page_url"));
                }
            }
        }*/
        if (jsonResponseObject.has("data") && jsonResponseObject.optJSONArray("data") != null) {
            JSONArray jsonDataArray = jsonResponseObject.optJSONArray("data");
            ArrayList<MessageDetailsItem> arrNewsDetails = new ArrayList<>();
            if (jsonDataArray != null) {
                for (int arrIndexData = 0; arrIndexData < jsonDataArray.length(); arrIndexData++) {
                    MessageDetailsItem messageDetailsItem = new MessageDetailsItem();
                    JSONObject jsonNewsObject = jsonDataArray.optJSONObject(arrIndexData);
                    if (jsonNewsObject.has("_id") && jsonNewsObject.optString("_id") != null && !jsonNewsObject.optString("_id").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageID(jsonNewsObject.optString("_id"));
                    }
                    if (jsonNewsObject.has("title") && jsonNewsObject.optString("title") != null && !jsonNewsObject.optString("title").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageTitle(jsonNewsObject.optString("title"));
                    }
                    if (jsonNewsObject.has("city") && jsonNewsObject.optString("city") != null && !jsonNewsObject.optString("city").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageCity(jsonNewsObject.optString("city"));
                    }
                    if (jsonNewsObject.has("msg_desc") && jsonNewsObject.optString("msg_desc") != null && !jsonNewsObject.optString("msg_desc").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageDescription(jsonNewsObject.optString("msg_desc"));
                    }
                    if (jsonNewsObject.has("msg_img") && jsonNewsObject.optString("msg_img") != null && !jsonNewsObject.optString("msg_img").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageImage(jsonNewsObject.optString("msg_img"));
                    }
                    if (jsonNewsObject.has("created_at") && jsonNewsObject.optString("created_at") != null && !jsonNewsObject.optString("created_at").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageCreateDate(jsonNewsObject.optString("created_at"));
                    }
                    if (jsonNewsObject.has("msg_type") && jsonNewsObject.optString("msg_type") != null && !jsonNewsObject.optString("msg_type").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageType(jsonNewsObject.optString("msg_type"));
                    }
                    arrNewsDetails.add(messageDetailsItem);
                }
                messageAndClassifiedResponseItem.setArrMessageList(arrNewsDetails);
            }
            return messageAndClassifiedResponseItem;
        }
        return false;
    }

    public static Object parseMessageNewResponse(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        MessageDetailsItem globalMessageDetailsItem = new MessageDetailsItem();
        if (jsonResponseObject.has("data") && jsonResponseObject.optJSONArray("data") != null) {
            JSONArray jsonDataArray = jsonResponseObject.optJSONArray("data");
            ArrayList<MessageDetailsItem> arrNewsDetails = new ArrayList<>();
            if (jsonDataArray != null) {
                for (int arrIndexData = 0; arrIndexData < jsonDataArray.length(); arrIndexData++) {
                    MessageDetailsItem messageDetailsItem = new MessageDetailsItem();
                    JSONObject jsonNewsObject = jsonDataArray.optJSONObject(arrIndexData);
                    if (jsonNewsObject.has("_id") && jsonNewsObject.optString("_id") != null && !jsonNewsObject.optString("_id").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageID(jsonNewsObject.optString("_id"));
                    }
                    if (jsonNewsObject.has("title") && jsonNewsObject.optString("title") != null && !jsonNewsObject.optString("title").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageTitle(jsonNewsObject.optString("title"));
                    }
                    if (jsonNewsObject.has("city") && jsonNewsObject.optString("city") != null && !jsonNewsObject.optString("city").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageCity(jsonNewsObject.optString("city"));
                    }
                    if (jsonNewsObject.has("msg_desc") && jsonNewsObject.optString("msg_desc") != null && !jsonNewsObject.optString("msg_desc").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageDescription(jsonNewsObject.optString("msg_desc"));
                    }
                    if (jsonNewsObject.has("msg_img") && jsonNewsObject.optString("msg_img") != null && !jsonNewsObject.optString("msg_img").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageImage(jsonNewsObject.optString("msg_img"));
                    }
                    if (jsonNewsObject.has("created_at") && jsonNewsObject.optString("created_at") != null && !jsonNewsObject.optString("created_at").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageCreateDate(jsonNewsObject.optString("created_at"));
                    }
                    if (jsonNewsObject.has("msg_type") && jsonNewsObject.optString("msg_type") != null && !jsonNewsObject.optString("msg_type").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageType(jsonNewsObject.optString("msg_type"));
                    }
                    if (jsonNewsObject.has("message_date") && jsonNewsObject.optString("message_date") != null && !jsonNewsObject.optString("message_date").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageDate(jsonNewsObject.optString("message_date"));
                    }
                    arrNewsDetails.add(messageDetailsItem);
                }
                globalMessageDetailsItem.setArrMessageList(arrNewsDetails);
            }
            return globalMessageDetailsItem;
        }
        return false;
    }

    public static Object parseNewMasterList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            MasterDataItem masterDataItem = new MasterDataItem();
            ArrayList<SGKSAreaItem> sgksAreaItemArrayList = new ArrayList<>();
            ArrayList<SGKSCategory> sgksCategoryArrayList = new ArrayList<>();

            if (jsonObject.has("data")) {
                JSONObject jsonObjectResponse = jsonObject.getJSONObject("data");
                if (jsonObjectResponse != null) {
                    JSONArray jsonAreaArray = jsonObjectResponse.getJSONArray("sgks_area");
                    if (jsonAreaArray.length() > 0) {
                        for (int i = 0; i < jsonAreaArray.length(); i++) {
                            SGKSAreaItem sgksAreaItem = new SGKSAreaItem();
                            JSONObject jsonObject1 = jsonAreaArray.getJSONObject(i);
                            if (jsonObject1.has("id")) {
                                sgksAreaItem.setId(jsonObject1.getInt("id"));
                            }
                            if (jsonObject1.has("area_name")) {
                                sgksAreaItem.setAreaName(jsonObject1.getString("area_name"));
                            }
                            sgksAreaItemArrayList.add(sgksAreaItem);
                        }
                        masterDataItem.setSgksAreaItems(sgksAreaItemArrayList);
                    }
                    JSONArray jsonCategoryArray = jsonObjectResponse.getJSONArray("suggestionbox_category");
                    if (jsonCategoryArray.length() > 0) {
                        for (int i = 0; i < jsonCategoryArray.length(); i++) {
                            JSONObject jsonObjectSuggestionCategory = jsonCategoryArray.getJSONObject(i);
                            SGKSCategory sgksCategory = new SGKSCategory();
                            if (jsonObjectSuggestionCategory.has("id")) {
                                sgksCategory.setId(jsonObjectSuggestionCategory.getInt("id"));
                            }
                            if (jsonObjectSuggestionCategory.has("name")) {
                                sgksCategory.setStrCategoryName(jsonObjectSuggestionCategory.getString("name"));
                            }
                            sgksCategoryArrayList.add(sgksCategory);
                        }
                        masterDataItem.setSgksCategoryArrayList(sgksCategoryArrayList);
                    }
                    if (jsonObjectResponse.has("sgks_messages") && jsonObjectResponse.optString("sgks_messages") != null) {
                        int strMessage = jsonObjectResponse.optInt("sgks_messages");
                        masterDataItem.setStrMessageIds(strMessage);
                    }
                    if (jsonObjectResponse.has("sgks_buzz") && jsonObjectResponse.optJSONObject("sgks_buzz") != null) {
                        JSONObject jsonBuzzObject = jsonObjectResponse.optJSONObject("sgks_buzz");
                        /*String strBuzzId = jsonBuzzObject.optString("id");
                        masterDataItem.setStrSgksBuzz_Id(strBuzzId);*/
                        //
                        String strBuzzUrl = jsonBuzzObject.optString("msg_img");
                        Log.i("@@1",strBuzzUrl);
                        masterDataItem.setStrSgksBuzz_ImgUrl(strBuzzUrl);
                    }

                    /*JSONArray jsonMessagesArray=jsonObjectResponse.getJSONArray("sgks_messages");
                    for (int i=0; i<jsonMessagesArray.length();i++){
                        masterDataItem.setIntegers((ArrayList<Integer>) jsonMessagesArray.get(i));
                    }*/
                }
            }
            return masterDataItem;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object parseLocalDataSyncResponse(String response) throws JSONException {
        JSONObject jsonResponseObjecResp = new JSONObject(response);
        LocalDataSyncItem localDataSyncItem = new LocalDataSyncItem();
        JSONObject jsonResponseObject=jsonResponseObjecResp.getJSONObject("data");
        if (jsonResponseObject.has("members") && jsonResponseObject.optString("members") != null) {
            ArrayList<MemberDetailsItem> arrMemberDetailsItems = new ArrayList<>();
            JSONArray jsonMemberArray = jsonResponseObject.optJSONArray("members");
            if (jsonMemberArray != null) {
                MemberDetailsItem memberDetailsItem;
                for (int arrIndex = 0; arrIndex < jsonMemberArray.length(); arrIndex++) {
                    memberDetailsItem = new MemberDetailsItem();
                    JSONObject jsonObject = jsonMemberArray.optJSONObject(arrIndex);
                    if (jsonObject.has("id") && jsonObject.getString("id") != null) {
                        memberDetailsItem.setStrId(String.valueOf(jsonObject.getInt("id")));

                    }
                    if (jsonObject.has("member_id") && jsonObject.getString("member_id") != null) {
                        memberDetailsItem.setStrMemberId(jsonObject.getString("member_id"));
                    }
                    if (jsonObject.has("first_name") && jsonObject.getString("first_name") != null) {
                        memberDetailsItem.setStrFirstName(jsonObject.getString("first_name"));
                    }
                    if (jsonObject.has("middle_name") && jsonObject.getString("middle_name") != null) {
                        memberDetailsItem.setStrMiddleName(jsonObject.getString("middle_name"));
                    }
                    if (jsonObject.has("last_name") && jsonObject.getString("last_name") != null) {
                        memberDetailsItem.setStrLastName(jsonObject.getString("last_name"));
                    }
                    if (jsonObject.has("gender") && jsonObject.getString("gender") != null) {
                        memberDetailsItem.setStrGender(jsonObject.getString("gender"));
                    }
                    if (jsonObject.has("mobile") && jsonObject.getString("mobile") != null) {
                        memberDetailsItem.setStrMobileNumber(jsonObject.getString("mobile"));
                    }
                    if (jsonObject.has("email") && jsonObject.getString("email") != null ) {
                        memberDetailsItem.setStrEmail(jsonObject.getString("email"));
                    }
                    if (jsonObject.has("city") && jsonObject.getString("city") != null) {
                        memberDetailsItem.setStrCity(jsonObject.getString("city"));
                    }
                    if (jsonObject.has("memLatitude") && jsonObject.getString("memLatitude") != null) {
                        memberDetailsItem.setStrLatitude(jsonObject.getString("memLatitude"));
                    }
                    if (jsonObject.has("memLongitude") && jsonObject.getString("memLongitude") != null) {
                        memberDetailsItem.setStrLongitude(jsonObject.getString("memLongitude"));
                    }
                    if (jsonObject.has("image_url") && jsonObject.getString("image_url") != null) {
                        memberDetailsItem.setStrMemberImageUrl(jsonObject.getString("image_url"));
                    }
                    arrMemberDetailsItems.add(memberDetailsItem);
                }
            }
            localDataSyncItem.setArrMemberDetailsItems(arrMemberDetailsItems);
        }
        if (jsonResponseObject.has("committees") && jsonResponseObject.optString("committees") != null) {
            ArrayList<CommitteeDetailsItem> arrCommitteeDetailsItems = new ArrayList<>();
            JSONArray jsonCommitteeArray = jsonResponseObject.optJSONArray("committees");
            if (jsonCommitteeArray != null) {
                CommitteeDetailsItem committeeDetailsItem;
                for (int arrIndex = 0; arrIndex < jsonCommitteeArray.length(); arrIndex++) {
                    committeeDetailsItem = new CommitteeDetailsItem();
                    JSONObject jsonObject = jsonCommitteeArray.optJSONObject(arrIndex);
                    if (jsonObject.has("name") && jsonObject.getString("name") != null) {
                        committeeDetailsItem.setCommitteeName(jsonObject.getString("name"));
                    }
                    if (jsonObject.has("description") && jsonObject.getString("description") != null) {
                        committeeDetailsItem.setCommitteeDescription(jsonObject.getString("description"));
                    }
                    if (jsonObject.has("city") && jsonObject.getString("city") != null) {
                        committeeDetailsItem.setCommitteeCity(jsonObject.getString("city"));
                    }
                    if (jsonObject.has("committee_id") && jsonObject.getString("committee_id") != null) {
                        committeeDetailsItem.setCommitteeID(jsonObject.getString("committee_id"));
                    }
                    if (jsonObject.has("comm_mem_details") && jsonObject.getString("comm_mem_details") != null) {
                        committeeDetailsItem.setCommAllMembers(jsonObject.getString("comm_mem_details"));
                    }
                    arrCommitteeDetailsItems.add(committeeDetailsItem);
                }
            }
            localDataSyncItem.setArrCommitteeDetailsItems(arrCommitteeDetailsItems);
        }
        if (jsonResponseObject.has("messages") && jsonResponseObject.optString("messages") != null) {
            ArrayList<MessageDetailsItem> arrMessageDetailsItems = new ArrayList<>();
            JSONArray jsonMessageArray = jsonResponseObject.optJSONArray("messages");
            if (jsonMessageArray != null) {
                MessageDetailsItem messageDetailsItem;
                for (int arrIndex = 0; arrIndex < jsonMessageArray.length(); arrIndex++) {
                    messageDetailsItem = new MessageDetailsItem();
                    JSONObject jsonObject = jsonMessageArray.optJSONObject(arrIndex);
                    if (jsonObject.has("id") && jsonObject.getString("id") != null) {
                        messageDetailsItem.setMessageID(jsonObject.getString("id"));
                    }
                    if (jsonObject.has("title") && jsonObject.getString("title") != null) {
                        messageDetailsItem.setMessageTitle(jsonObject.getString("title"));
                    }
                    if (jsonObject.has("msg_desc") && jsonObject.getString("msg_desc") != null) {
                        messageDetailsItem.setMessageDescription(jsonObject.getString("msg_desc"));
                    }
                    if (jsonObject.has("msg_url") && jsonObject.getString("msg_url") != null) {
                        messageDetailsItem.setMessageImage(jsonObject.getString("msg_url"));
                    }
                    if (jsonObject.has("created_at") && jsonObject.getString("created_at") != null) {
                        messageDetailsItem.setMessageCreateDate(jsonObject.getString("created_at"));
                    }
                    if (jsonObject.has("msg_type") && jsonObject.getString("msg_type") != null) {
                        messageDetailsItem.setMessageType(jsonObject.getString("msg_type"));
                    }
                    if (jsonObject.has("city") && jsonObject.getString("city") != null) {
                        messageDetailsItem.setMessageCity(jsonObject.getString("city"));
                    }
                    arrMessageDetailsItems.add(messageDetailsItem);
                }
            }
            localDataSyncItem.setArrMessageDetailsItems(arrMessageDetailsItems);
        }
        return localDataSyncItem;
    }

    public static Object parseAccountDetailsResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("data") && jsonObject.optJSONArray("data") != null) {
            JSONArray jsonDataArray = jsonObject.optJSONArray("data");
            ArrayList<AccountDetailsItem> arrAccountDetails = new ArrayList<>();
            if (jsonDataArray != null) {
                AccountDetailsItem accountDetailsItem;
                arrAccountDetails = new ArrayList<>();
                for (int indexDetails = 0; indexDetails < jsonDataArray.length(); indexDetails++) {
                    accountDetailsItem = new AccountDetailsItem();
                    JSONObject jsonDetailsObject = jsonDataArray.optJSONObject(indexDetails);
                    if (jsonDetailsObject.has("accounts_images") && jsonDetailsObject.optString("accounts_images") != null && !jsonDetailsObject.optString("accounts_images").equalsIgnoreCase("null")) {
                        JSONArray imagesArray = jsonDetailsObject.getJSONArray("accounts_images");
                        ArrayList<AccountImages> imageList = new ArrayList<>();
                        if (imagesArray.length() > 0) {
                            for (int imageIndex = 0; imageIndex < imagesArray.length(); imageIndex++) {
                                String temp = imagesArray.getString(imageIndex);
                                AccountImages img = new AccountImages();
                                img.setImagePath(temp);
                                imageList.add(img);
                            }
                        }
                        accountDetailsItem.setImagesList(imageList);
                    }
                    if (jsonDetailsObject.has("name") && jsonDetailsObject.optString("name") != null && !jsonDetailsObject.optString("name").equalsIgnoreCase("null")) {
                        accountDetailsItem.setStrAccountName(jsonDetailsObject.optString("name"));
                    }
                    if (jsonDetailsObject.has("description") && jsonDetailsObject.optString("description") != null && !jsonDetailsObject.optString("description").equalsIgnoreCase("null")) {
                        accountDetailsItem.setStrAccountDescription(jsonDetailsObject.optString("description"));
                    }
                    arrAccountDetails.add(accountDetailsItem);
                }
            }
            return arrAccountDetails;
        }
        /*if (jsonObject.has("data") && jsonObject.optJSONArray("data") != null) {
            ArrayList<AccountYearItem> arrAccountYear = new ArrayList<>();
            AccountYearItem accountYearItem;
            JSONArray jsonDataArray = jsonObject.optJSONArray("data");
            for (int indexYear = 0; indexYear < jsonDataArray.length(); indexYear++) {
                ArrayList<AccountDetailsItem> arrAccountDetails;
                accountYearItem = new AccountYearItem();
                JSONObject jsonYearObject = jsonDataArray.optJSONObject(indexYear);
                String currentKey = "";
                Iterator<String> iterator = jsonYearObject.keys();
                while (iterator.hasNext()) {
                    currentKey = iterator.next();
                }
                accountYearItem.setStrYear(currentKey);
                if (jsonYearObject.has(currentKey) && jsonYearObject.optJSONArray(currentKey) != null) {
                    AccountDetailsItem accountDetailsItem;
                    arrAccountDetails = new ArrayList<>();
                    JSONArray jsonDetailsArray = jsonYearObject.optJSONArray(currentKey);
                    for (int indexDetails = 0; indexDetails < jsonDetailsArray.length(); indexDetails++) {
                        accountDetailsItem = new AccountDetailsItem();
                        JSONObject jsonDetailsObject = jsonDetailsArray.optJSONObject(indexDetails);
                        if (jsonDetailsObject.has("account_image_url") && jsonDetailsObject.optString("account_image_url") != null && !jsonDetailsObject.optString("account_image_url").equalsIgnoreCase("null")) {
                            accountDetailsItem.setStrAccountImageUrl(jsonDetailsObject.optString("account_image_url"));
                        }
                        if (jsonDetailsObject.has("name") && jsonDetailsObject.optString("name") != null && !jsonDetailsObject.optString("name").equalsIgnoreCase("null")) {
                            accountDetailsItem.setStrAccountName(jsonDetailsObject.optString("name"));
                        }
                        arrAccountDetails.add(accountDetailsItem);
                    }
                    accountYearItem.setArrAccountDetails(arrAccountDetails);
                }
                arrAccountYear.add(accountYearItem);
            }
            return arrAccountYear;
        }*/
        return null;
    }

    public static Object parseEventDetailsResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("data") && jsonObject.optJSONArray("data") != null) {
            ArrayList<EventDataItem> mArrEventData = new ArrayList<>();
            EventDataItem eventDataItem;
            JSONArray jsonDataArray = jsonObject.optJSONArray("data");
            for (int indexYear = 0; indexYear < jsonDataArray.length(); indexYear++) {
                ArrayList<String> arrEventImages;
                eventDataItem = new EventDataItem();
                JSONObject jsonEventObject = jsonDataArray.optJSONObject(indexYear);
                if (jsonEventObject.has("event_name") && jsonEventObject.optString("event_name") != null) {
                    String strEventName = jsonEventObject.optString("event_name");
                    eventDataItem.setEventName(strEventName);
                }
                if (jsonEventObject.has("desc") && jsonEventObject.optString("desc") != null) {
                    String strEventDesc = jsonEventObject.optString("desc");
                    eventDataItem.setEventDescription(strEventDesc);
                }
                if (jsonEventObject.has("event_date") && jsonEventObject.optString("event_date") != null) {
                    String strEventDate = jsonEventObject.optString("event_date");
                    eventDataItem.setEventDate(strEventDate);
                }
                if (jsonEventObject.has("year") && jsonEventObject.optString("year") != null) {
                    String strEventYear = jsonEventObject.optString("year");
                    eventDataItem.setEventYear(strEventYear);
                }
                if(jsonEventObject.has("venue") && jsonEventObject.opt("venue") != null){
                    String strEventVenue = jsonEventObject.optString("venue");
                    eventDataItem.setVenue(strEventVenue);
                }
                if(jsonEventObject.has("city") && jsonEventObject.opt("city") != null){
                    String strEventCity = jsonEventObject.optString("city");
                    eventDataItem.setCity(strEventCity);
                }
                if (jsonEventObject.has("event_images") && jsonEventObject.optJSONArray("event_images") != null) {
                    arrEventImages = new ArrayList<>();
                    JSONArray jsonImageUrlArray = jsonEventObject.optJSONArray("event_images");
                    for (int yearIndex = 0; yearIndex < jsonImageUrlArray.length(); yearIndex++) {
                        String strImgUrl = jsonImageUrlArray.optString(yearIndex);
                        arrEventImages.add(strImgUrl);
                    }
                    eventDataItem.setArrEventImageURLs(arrEventImages);
                }
                mArrEventData.add(eventDataItem);
            }
            return mArrEventData;
        }
        return null;
    }

    public static Object parseClassifiedResponse(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        ClassifiedDetailsItem globalClassifiedDetailsItem = new ClassifiedDetailsItem();
        if (jsonResponseObject.has("data") && jsonResponseObject.optJSONArray("data") != null) {
            JSONArray jsonDataArray = jsonResponseObject.optJSONArray("data");
            ArrayList<ClassifiedDetailsItem> arrClassifiedDetails = new ArrayList<ClassifiedDetailsItem>();
            if (jsonDataArray != null) {
                for (int arrIndexData = 0; arrIndexData < jsonDataArray.length(); arrIndexData++) {
                    ClassifiedDetailsItem classifiedDetailsItem = new ClassifiedDetailsItem();
                    JSONObject jsonNewsObject = jsonDataArray.optJSONObject(arrIndexData);
                    if (jsonNewsObject.has("id") && jsonNewsObject.optString("id") != null && !jsonNewsObject.optString("_id").equalsIgnoreCase("null")) {
                        classifiedDetailsItem.setClassifiedID(jsonNewsObject.optString("id"));
                    }
                    if (jsonNewsObject.has("title") && jsonNewsObject.optString("title") != null && !jsonNewsObject.optString("title").equalsIgnoreCase("null")) {
                        classifiedDetailsItem.setClassifiedTitle(jsonNewsObject.optString("title"));
                    }
                    if (jsonNewsObject.has("city") && jsonNewsObject.optString("city") != null && !jsonNewsObject.optString("city").equalsIgnoreCase("null")) {
                        classifiedDetailsItem.setClassifiedCity(jsonNewsObject.optString("city"));
                    }
                    if (jsonNewsObject.has("class_desc") && jsonNewsObject.optString("class_desc") != null && !jsonNewsObject.optString("class_desc").equalsIgnoreCase("null")) {
                        classifiedDetailsItem.setClassifiedDescription(jsonNewsObject.optString("class_desc"));
                    }
                    if (jsonNewsObject.has("class_pkg") && jsonNewsObject.optString("class_pkg") != null && !jsonNewsObject.optString("class_pkg").equalsIgnoreCase("null")) {
                        classifiedDetailsItem.setClassifiedPackage(jsonNewsObject.optString("class_pkg"));
                    }
                    if (jsonNewsObject.has("created_at") && jsonNewsObject.optString("created_at") != null && !jsonNewsObject.optString("created_at").equalsIgnoreCase("null")) {
                        classifiedDetailsItem.setClassifiedCreateDate(jsonNewsObject.optString("created_at"));
                    }
                    if (jsonNewsObject.has("class_type") && jsonNewsObject.optString("class_type") != null && !jsonNewsObject.optString("class_type").equalsIgnoreCase("null")) {
                        classifiedDetailsItem.setClassifiedType(jsonNewsObject.optString("class_type"));
                    }
                    if (jsonNewsObject.has("class_images") && jsonNewsObject.optJSONArray("class_images") != null) {
                        JSONArray jsonImageArray = jsonNewsObject.optJSONArray("class_images");
                        ArrayList<String> arrClassifiedImages = new ArrayList<>();
                        for (int arrIndex = 0; arrIndex < jsonImageArray.length(); arrIndex++) {
                            String strImageUrl = jsonImageArray.getString(arrIndex);
                            arrClassifiedImages.add(strImageUrl);
                        }
                        classifiedDetailsItem.setArrClassifiedImages(arrClassifiedImages);
                    }
                    arrClassifiedDetails.add(classifiedDetailsItem);
                }
                globalClassifiedDetailsItem.setArrClassifiedList(arrClassifiedDetails);
            }
            return globalClassifiedDetailsItem;
        }
        return false;
    }

    public static Object parseCityResponse (String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("data") && jsonObject.optJSONArray("data") != null) {
            ArrayList<CityIteam> mArrCityData = new ArrayList<>();
            CityIteam cityIteam;
            JSONArray jsonDataArray = jsonObject.optJSONArray("data");
            for (int indexCity = 0; indexCity < jsonDataArray.length(); indexCity++) {
                cityIteam = new CityIteam();
                JSONObject jsonCityObject = jsonDataArray.optJSONObject(indexCity);
                if (jsonCityObject.has("city_name") && jsonCityObject.optString("city_name") != null) {
                    String strCityName = jsonCityObject.optString("city_name");
                    cityIteam.setStrCityName(strCityName);
                }
                if (jsonCityObject.has("city_id") && jsonCityObject.optString("city_id") != null) {
                    int intCity_Id = jsonCityObject.optInt("city_id");
                    cityIteam.setIntCityId(intCity_Id);
                }
                if (jsonCityObject.has("city_member_count") && jsonCityObject.optString("city_member_count") != null) {
                    String strMemberCount = jsonCityObject.optString("city_member_count");
                    cityIteam.setStrMemberCount(strMemberCount);
                }
                mArrCityData.add(cityIteam);
            }
            return mArrCityData;
        }
        return false;
    }

    public static Object parseBloodGroupResponse (String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("data") && jsonObject.optJSONArray("data") != null) {
            ArrayList<BloodGroupItems> mArrBloodGroupData = new ArrayList<>();
            BloodGroupItems bloodGroupItems;
            JSONArray jsonDataArray = jsonObject.optJSONArray("data");
            for (int indexCity = 0; indexCity < jsonDataArray.length(); indexCity++) {
                bloodGroupItems = new BloodGroupItems();
                JSONObject jsonBloodGroupObject = jsonDataArray.optJSONObject(indexCity);
                if (jsonBloodGroupObject.has("blood_group") && jsonBloodGroupObject.optString("blood_group") != null) {
                    String strBloodGroup = jsonBloodGroupObject.optString("blood_group");
                    bloodGroupItems.setStrBloodGroupName(strBloodGroup);;
                }
                if (jsonBloodGroupObject.has("blood_id") && jsonBloodGroupObject.optString("blood_id") != null) {
                    String strBloodGroupId = jsonBloodGroupObject.optString("blood_id");
                    bloodGroupItems.setStrBloodGroupId(strBloodGroupId);
                }
                mArrBloodGroupData.add(bloodGroupItems);
            }
            return mArrBloodGroupData;
        }
        return false;
    }

}
