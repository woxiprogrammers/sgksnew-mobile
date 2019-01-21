package com.woxi.sgkks_member.utils;

import android.util.Log;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.models.AccountDetailsItem;
import com.woxi.sgkks_member.models.BloodGroupItems;
import com.woxi.sgkks_member.models.CityIteam;
import com.woxi.sgkks_member.models.ClassifiedDetailsItem;
import com.woxi.sgkks_member.models.CommitteeDetailsItem;
import com.woxi.sgkks_member.models.EventDataItem;
import com.woxi.sgkks_member.models.LocalDataSyncItem;
import com.woxi.sgkks_member.models.MasterDataItem;
import com.woxi.sgkks_member.models.MasterItem;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.models.MessageAndClassifiedResponseItem;
import com.woxi.sgkks_member.models.MessageDetailsItem;
import com.woxi.sgkks_member.models.SGKSAreaItem;
import com.woxi.sgkks_member.models.SGKSCategory;
import com.woxi.sgkks_member.models.AccountImages;
import com.woxi.sgkks_member.models.SuggestionCategoriesItem;

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
    private static String TAG = "###";
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
                    if (jsonMemberObject.has("city_id") && jsonMemberObject.optString("city_id") != null && !jsonMemberObject.optString("city_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrCityId(jsonMemberObject.optString("city_id"));
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
                    if (jsonMemberObject.has("blood_group_id") && jsonMemberObject.optString("blood_group_id") != null && !jsonMemberObject.optString("blood_group_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrBloodGroupId(jsonMemberObject.optString("blood_group_id"));
                    }
                    if (jsonMemberObject.has("address") && jsonMemberObject.optString("address") != null && !jsonMemberObject.optString("address").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrAddress(jsonMemberObject.optString("address"));
                    }
                    if (jsonMemberObject.has("date_of_birth") && jsonMemberObject.optString("date_of_birth") != null && !jsonMemberObject.optString("date_of_birth").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrDateOfBirth(jsonMemberObject.optString("date_of_birth"));
                    }
                    if (jsonMemberObject.has("email") && jsonMemberObject.getString("email") != null && !jsonMemberObject.optString("email").equalsIgnoreCase("null")) {
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
                    if (jsonObjectResponse.has("buzz") && jsonObjectResponse.optJSONObject("buzz") != null) {
                        JSONObject jsonBuzzObject = jsonObjectResponse.optJSONObject("buzz");
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
                        Log.i(TAG, "parseAccountDetailsResponse: "+imagesArray.length());
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
                if (jsonCityObject.has("city_name_gj") && jsonCityObject.optString("city_name_gj") != null) {
                    String strCityName = jsonCityObject.optString("city_name_gj");
                    cityIteam.setStrCityNameGujarati(strCityName);
                }
                if (jsonCityObject.has("city_name_en") && jsonCityObject.optString("city_name_en") != null) {
                    String strCityName = jsonCityObject.optString("city_name_en");
                    cityIteam.setStrCityNameEnglish(strCityName);
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

    public static Object parseSuggestionCategoryResponse (String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("data") && jsonObject.optJSONArray("data") != null) {
            ArrayList<SuggestionCategoriesItem> mArrSuggestionCategory = new ArrayList<>();
            SuggestionCategoriesItem suggestionCategoriesItem;
            JSONArray jsonDataArray = jsonObject.optJSONArray("data");
            for (int indexCity = 0; indexCity < jsonDataArray.length(); indexCity++) {
                suggestionCategoriesItem = new SuggestionCategoriesItem();
                JSONObject jsonSuggestionCategoryObject = jsonDataArray.optJSONObject(indexCity);
                if (jsonSuggestionCategoryObject.has("category_id") && jsonSuggestionCategoryObject.optString("category_id") != null) {
                    String strCategoryId = jsonSuggestionCategoryObject.optString("category_id");
                    suggestionCategoriesItem.setStrCategoryId(strCategoryId);
                }
                if (jsonSuggestionCategoryObject.has("category") && jsonSuggestionCategoryObject.optString("category") != null) {
                    String strCategory = jsonSuggestionCategoryObject.optString("category");
                    suggestionCategoriesItem.setStrCategoryName(strCategory);
                }
                mArrSuggestionCategory.add(suggestionCategoriesItem);
            }
            return mArrSuggestionCategory;
        }
        return false;
    }

    public static Object parseLocalDataSyncResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        LocalDataSyncItem localDataSyncItem = new LocalDataSyncItem();
        if (jsonObject.has("members") && jsonObject.optJSONObject("members") != null) {
            ArrayList<MemberDetailsItem> arrMemberDetailsItems = new ArrayList<>();
            ArrayList<MemberDetailsItem> arrMemberDetailsGujaratiItems = new ArrayList<>();
            JSONObject jsonObjectMembers = jsonObject.optJSONObject("members");
            MemberDetailsItem memberDetailsItem;
            if (jsonObjectMembers.has("member_en") && jsonObjectMembers.optJSONArray("member_en") != null) {
                JSONArray jsonArrayMember_en = jsonObjectMembers.optJSONArray("member_en");
                for (int arrIndex = 0; arrIndex < jsonArrayMember_en.length(); arrIndex++) {
                    memberDetailsItem = new MemberDetailsItem();
                    JSONObject jsonObjectMember_en = jsonArrayMember_en.getJSONObject(arrIndex);
                    if (jsonObjectMember_en.has("id") && jsonObjectMember_en.optString("id") != null && !jsonObjectMember_en.optString("id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrId(jsonObjectMember_en.optString("id"));
                    }
                    if (jsonObjectMember_en.has("first_name") && jsonObjectMember_en.optString("first_name") != null && !jsonObjectMember_en.optString("first_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrFirstName(jsonObjectMember_en.optString("first_name"));
                    }
                    if (jsonObjectMember_en.has("middle_name") && jsonObjectMember_en.optString("middle_name") != null && !jsonObjectMember_en.optString("middle_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrMiddleName(jsonObjectMember_en.optString("middle_name"));
                    }
                    if (jsonObjectMember_en.has("last_name") && jsonObjectMember_en.optString("last_name") != null && !jsonObjectMember_en.optString("last_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrLastName(jsonObjectMember_en.optString("last_name"));
                    }
                    if (jsonObjectMember_en.has("city") && jsonObjectMember_en.optString("city") != null && !jsonObjectMember_en.optString("city").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrCity(jsonObjectMember_en.optString("city"));
                    }
                    if (jsonObjectMember_en.has("city_id") && jsonObjectMember_en.optString("city_id") != null && !jsonObjectMember_en.optString("city_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrCityId(jsonObjectMember_en.optString("city_id"));
                    }
                    if (jsonObjectMember_en.has("member_id") && jsonObjectMember_en.optString("member_id") != null && !jsonObjectMember_en.optString("member_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrMemberId(jsonObjectMember_en.optString("member_id"));
                    }
                    if (jsonObjectMember_en.has("gender") && jsonObjectMember_en.optString("gender") != null && !jsonObjectMember_en.optString("gender").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrGender(jsonObjectMember_en.optString("gender"));
                    }
                    if (jsonObjectMember_en.has("mobile") && jsonObjectMember_en.getString("mobile") != null) {
                        memberDetailsItem.setStrMobileNumber(jsonObjectMember_en.optString("mobile"));
                    }
                    if (jsonObjectMember_en.has("profile_image") && jsonObjectMember_en.getString("profile_image") != null) {
                        memberDetailsItem.setStrMemberImageUrl(jsonObjectMember_en.optString("profile_image"));
                    }
                    if (jsonObjectMember_en.has("latitude") && jsonObjectMember_en.getString("latitude") != null) {
                        memberDetailsItem.setStrLatitude(jsonObjectMember_en.optString("latitude"));
                    }
                    if (jsonObjectMember_en.has("longitude") && jsonObjectMember_en.getString("longitude") != null) {
                        memberDetailsItem.setStrLongitude(jsonObjectMember_en.optString("longitude"));
                    }
                    if (jsonObjectMember_en.has("blood_group") && jsonObjectMember_en.optString("blood_group") != null && !jsonObjectMember_en.optString("blood_group").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrBloodGroup(jsonObjectMember_en.optString("blood_group"));
                    }
                    if (jsonObjectMember_en.has("blood_group_id") && jsonObjectMember_en.optString("blood_group_id") != null && !jsonObjectMember_en.optString("blood_group_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrBloodGroupId(jsonObjectMember_en.optString("blood_group_id"));
                    }
                    if (jsonObjectMember_en.has("address") && jsonObjectMember_en.optString("address") != null && !jsonObjectMember_en.optString("address").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrAddress(jsonObjectMember_en.optString("address"));
                    }
                    if (jsonObjectMember_en.has("date_of_birth") && jsonObjectMember_en.optString("date_of_birth") != null && !jsonObjectMember_en.optString("date_of_birth").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrDateOfBirth(jsonObjectMember_en.optString("date_of_birth"));
                    }
                    if (jsonObjectMember_en.has("email") && jsonObjectMember_en.getString("email") != null && !jsonObjectMember_en.optString("email").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrEmail(jsonObjectMember_en.getString("email"));
                    }
                    if (jsonObjectMember_en.has("is_active") && jsonObjectMember_en.getString("is_active") != null && !jsonObjectMember_en.optString("is_active").equalsIgnoreCase("null")) {
                        memberDetailsItem.setBolIsActive(jsonObjectMember_en.getString("is_active"));
                    }
                    arrMemberDetailsItems.add(memberDetailsItem);
                }
                localDataSyncItem.setArrMemberDetailsItems(arrMemberDetailsItems);

            }
            if (jsonObjectMembers.has("member_gj") && jsonObjectMembers.optJSONArray("member_gj") != null) {
                JSONArray jsonArrayMember_en = jsonObjectMembers.optJSONArray("member_gj");
                for (int arrIndex = 0; arrIndex < jsonArrayMember_en.length(); arrIndex++) {
                    memberDetailsItem = new MemberDetailsItem();
                    JSONObject jsonObjectMember_en = jsonArrayMember_en.getJSONObject(arrIndex);
                    if (jsonObjectMember_en.has("id") && jsonObjectMember_en.optString("id") != null && !jsonObjectMember_en.optString("id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrId(jsonObjectMember_en.optString("id"));
                    }
                    if (jsonObjectMember_en.has("first_name") && jsonObjectMember_en.optString("first_name") != null && !jsonObjectMember_en.optString("first_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrFirstName(jsonObjectMember_en.optString("first_name"));
                    }
                    if (jsonObjectMember_en.has("middle_name") && jsonObjectMember_en.optString("middle_name") != null && !jsonObjectMember_en.optString("middle_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrMiddleName(jsonObjectMember_en.optString("middle_name"));
                    }
                    if (jsonObjectMember_en.has("last_name") && jsonObjectMember_en.optString("last_name") != null && !jsonObjectMember_en.optString("last_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrLastName(jsonObjectMember_en.optString("last_name"));
                    }
                    if (jsonObjectMember_en.has("member_id") && jsonObjectMember_en.optString("member_id") != null && !jsonObjectMember_en.optString("member_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrMemberId(jsonObjectMember_en.optString("member_id"));
                    }
                    if (jsonObjectMember_en.has("address") && jsonObjectMember_en.optString("address") != null && !jsonObjectMember_en.optString("address").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrAddress(jsonObjectMember_en.optString("address"));
                    }
                    if (jsonObjectMember_en.has("language_id") && jsonObjectMember_en.optString("language_id") != null && !jsonObjectMember_en.optString("language_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setStrLanguageId(jsonObjectMember_en.optString("language_id"));
                    }
                    arrMemberDetailsGujaratiItems.add(memberDetailsItem);
                }
                localDataSyncItem.setArrMemberDetailsGujaratiItems(arrMemberDetailsGujaratiItems);

            }
        } else {
            Log.i(TAG, "parseLocalDataSyncResponse: members not present");
        }

        if (jsonObject.has("cities") && jsonObject.optJSONObject("cities") != null) {
            ArrayList<CityIteam> arrCityItem = new ArrayList<>();
            ArrayList<CityIteam> arrCityItemGujarati = new ArrayList<>();
            CityIteam cityIteam;
            JSONObject jsonObjectCity = jsonObject.optJSONObject("cities");
            if (jsonObjectCity.has("city_en") && jsonObjectCity.optJSONArray("city_en") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: city_en present");
                JSONArray jsonArray = jsonObjectCity.optJSONArray("city_en");
                for (int arrIndex = 0; arrIndex < jsonArray.length(); arrIndex++) {
                    cityIteam = new CityIteam();
                    JSONObject cityJsonObject = jsonArray.getJSONObject(arrIndex);
                    if (cityJsonObject.has("id") && cityJsonObject.optString("id") != null) {
                        cityIteam.setIntCityId(Integer.valueOf(cityJsonObject.optString("id")));
                    }
                    if (cityJsonObject.has("name") && cityJsonObject.optString("name") != null) {
                        cityIteam.setStrCityName(cityJsonObject.optString("name"));
                        Log.i(TAG, "parseLocalDataSyncResponse: " + (cityJsonObject.optString("name")));
                    }
                    if (cityJsonObject.has("state_id") && cityJsonObject.optString("state_id") != null) {
                        cityIteam.setStrStateId(cityJsonObject.optString("state_id"));
                    }
                    if (cityJsonObject.has("is_active") && cityJsonObject.optString("is_active") != null) {
                        cityIteam.setIs_active(cityJsonObject.optString("is_active"));
                    }
                    if (cityJsonObject.has("city_member_count") && cityJsonObject.optString("city_member_count") != null) {
                        cityIteam.setStrMemberCount(cityJsonObject.optString("city_member_count"));
                    }
                    arrCityItem.add(cityIteam);
                }
                localDataSyncItem.setArrCityItems(arrCityItem);
            }
            if (jsonObjectCity.has("city_gj") && jsonObjectCity.optJSONArray("city_gj") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: city_gj present");
                JSONArray jsonArray = jsonObjectCity.optJSONArray("city_gj");
                for (int arrIndex = 0; arrIndex < jsonArray.length(); arrIndex++) {
                    cityIteam = new CityIteam();
                    JSONObject cityJsonObject = jsonArray.getJSONObject(arrIndex);
                    if (cityJsonObject.has("id") && cityJsonObject.optString("id") != null && !cityJsonObject.optString("id").equalsIgnoreCase(null)) {
                        cityIteam.setId(cityJsonObject.optString("id"));
                    }
                    if (cityJsonObject.has("city_id") && cityJsonObject.optString("city_id") != null && !cityJsonObject.optString("city_id").equalsIgnoreCase(null)) {
                        cityIteam.setIntCityId(Integer.valueOf(cityJsonObject.optString("city_id")));
                    }
                    if (cityJsonObject.has("name") && cityJsonObject.optString("name") != null && !cityJsonObject.optString("name").equalsIgnoreCase(null)) {
                        cityIteam.setStrCityName(cityJsonObject.optString("name"));
                    }
                    if (cityJsonObject.has("language_id") && cityJsonObject.optString("language_id") != null && !cityJsonObject.optString("language_id").equalsIgnoreCase(null)) {
                        cityIteam.setLanguageId(cityJsonObject.optString("language_id"));
                    }
                    arrCityItemGujarati.add(cityIteam);
                }
                localDataSyncItem.setArrCityItemsGujarati(arrCityItemGujarati);
            }
        } else {
            Log.i(TAG, "parseLocalDataSyncResponse: Cities not present");
        }
        if (jsonObject.has("events") && jsonObject.optJSONObject("events") != null) {
            Log.i(TAG, "parseLocalDataSyncResponse: Events is present");
            ArrayList<EventDataItem> arrEventDataItems = new ArrayList<>();
            ArrayList<EventDataItem> arrEventGujaratiDataItems = new ArrayList<>();
            EventDataItem eventDataItem;
            JSONObject jsonObjectEvents = jsonObject.optJSONObject("events");
            if (jsonObjectEvents.has("event_en") && jsonObjectEvents.optJSONArray("event_en") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: event_en is present");
                JSONArray jsonDataArray = jsonObjectEvents.optJSONArray("event_en");
                for (int indexYear = 0; indexYear < jsonDataArray.length(); indexYear++) {
                    ArrayList<String> arrEventImages;
                    eventDataItem = new EventDataItem();
                    JSONObject jsonEventObject = jsonDataArray.optJSONObject(indexYear);
                    if (jsonEventObject.has("id") && jsonEventObject.optString("id") != null) {
                        eventDataItem.setStrEventId(jsonEventObject.optString("id"));
                    }
                    if (jsonEventObject.has("event_name") && jsonEventObject.optString("event_name") != null) {
                        String strEventName = jsonEventObject.optString("event_name");
                        eventDataItem.setEventName(strEventName);
                    }
                    if (jsonEventObject.has("description") && jsonEventObject.optString("description") != null) {
                        String strEventDesc = jsonEventObject.optString("description");
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
                    if (jsonEventObject.has("venue") && jsonEventObject.opt("venue") != null) {
                        String strEventVenue = jsonEventObject.optString("venue");
                        eventDataItem.setVenue(strEventVenue);
                    }
                    if (jsonEventObject.has("city") && jsonEventObject.opt("city") != null) {
                        String strEventCity = jsonEventObject.optString("city");
                        eventDataItem.setCity(strEventCity);
                    }
                    if (jsonEventObject.has("city_id") && jsonEventObject.optString("city_id") != null) {
                        eventDataItem.setCitryId(jsonEventObject.optString("city_id"));
                    }
                    if (jsonEventObject.has("is_active") && jsonEventObject.optString("is_active") != null) {
                        eventDataItem.setStrIsActive(jsonEventObject.optString("is_active"));
                    }
                    if (jsonEventObject.has("event_images") && jsonEventObject.optJSONArray("event_images") != null) {
                        arrEventImages = new ArrayList<>();
                        JSONArray jsonImageUrlArray = jsonEventObject.optJSONArray("event_images");
                        for (int yearIndex = 0; yearIndex < jsonImageUrlArray.length(); yearIndex++) {
                            String strImgUrl = jsonImageUrlArray.optString(yearIndex);
                            arrEventImages.add(strImgUrl);
                        }
                        eventDataItem.setArrEventImageURLs(arrEventImages);
                        localDataSyncItem.setArrEventImageURLs(arrEventImages);
                    }
                    arrEventDataItems.add(eventDataItem);
                }
                localDataSyncItem.setArrEventDataItem(arrEventDataItems);

            }
            if (jsonObjectEvents.has("event_gj") && jsonObjectEvents.optJSONArray("event_gj") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: event_gj is present");
                JSONArray jsonDataArray = jsonObjectEvents.optJSONArray("event_gj");
                for (int indexYear = 0; indexYear < jsonDataArray.length(); indexYear++) {
                    eventDataItem = new EventDataItem();
                    JSONObject jsonEventObject = jsonDataArray.optJSONObject(indexYear);
                    if (jsonEventObject.has("id") && jsonEventObject.optString("id") != null) {
                        eventDataItem.setStrId(jsonEventObject.optString("id"));
                    }
                    if (jsonEventObject.has("event_name") && jsonEventObject.optString("event_name") != null) {
                        String strEventName = jsonEventObject.optString("event_name");
                        eventDataItem.setEventName(strEventName);
                    }
                    if (jsonEventObject.has("description") && jsonEventObject.optString("description") != null) {
                        String strEventDesc = jsonEventObject.optString("description");
                        eventDataItem.setEventDescription(strEventDesc);
                    }
                    if (jsonEventObject.has("year") && jsonEventObject.optString("year") != null) {
                        String strEventYear = jsonEventObject.optString("year");
                        eventDataItem.setEventYear(strEventYear);
                    }
                    if (jsonEventObject.has("venue") && jsonEventObject.opt("venue") != null) {
                        String strEventVenue = jsonEventObject.optString("venue");
                        eventDataItem.setVenue(strEventVenue);
                    }
                    if (jsonEventObject.has("language_id") && jsonEventObject.opt("language_id") != null) {
                        eventDataItem.setStrlanguageId(jsonEventObject.optString("language_id"));
                    }
                    if (jsonEventObject.has("event_id") && jsonEventObject.opt("event_id") != null) {
                        eventDataItem.setStrEventId(jsonEventObject.optString("event_id"));
                    }
                    arrEventGujaratiDataItems.add(eventDataItem);
                }
                localDataSyncItem.setArrEventDataGujaratiItem(arrEventGujaratiDataItems);
            }
        } else {
            Log.i(TAG, "parseLocalDataSyncResponse: Events not present");
        }
        if (jsonObject.has("messages") && jsonObject.optJSONObject("messages") != null) {
            ArrayList<MessageDetailsItem> arrMessageDetailsItems = new ArrayList<>();
            ArrayList<MessageDetailsItem> arrMessageGujaratiDetailsItems = new ArrayList<>();
            JSONObject jsonObjectMessages = jsonObject.optJSONObject("messages");
            if (jsonObjectMessages.has("message_en") && jsonObjectMessages.optJSONArray("message_en") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: message_en present");
                JSONArray jsonArray = jsonObjectMessages.optJSONArray("message_en");
                for (int arrIndex = 0; arrIndex < jsonArray.length(); arrIndex++) {
                    MessageDetailsItem messageDetailsItem = new MessageDetailsItem();
                    JSONObject jsonObjectMessage = jsonArray.optJSONObject(arrIndex);
                    if (jsonObjectMessage.has("id") && jsonObjectMessage.optString("id") != null) {
                        messageDetailsItem.setId(jsonObjectMessage.optString("id"));
                    }
                    if (jsonObjectMessage.has("title") && jsonObjectMessage.optString("title") != null) {
                        messageDetailsItem.setMessageTitle(jsonObjectMessage.optString("title"));
                    }
                    if (jsonObjectMessage.has("description") && jsonObjectMessage.optString("description") != null) {
                        messageDetailsItem.setMessageDescription(jsonObjectMessage.optString("description"));
                    }
                    if (jsonObjectMessage.has("message_type") && jsonObjectMessage.optString("message_type") != null) {
                        messageDetailsItem.setMessageType(jsonObjectMessage.optString("message_type"));
                    }
                    if (jsonObjectMessage.has("message_type_id") && jsonObjectMessage.optString("message_type_id") != null) {
                        messageDetailsItem.setMessageTypeId(jsonObjectMessage.optString("message_type_id"));
                    }
                    if (jsonObjectMessage.has("is_active") && jsonObjectMessage.optString("is_active") != null) {
                        messageDetailsItem.setIsActive(jsonObjectMessage.optString("is_active"));
                    }
                    if (jsonObjectMessage.has("city") && jsonObjectMessage.optString("city") != null) {
                        messageDetailsItem.setMessageCity(jsonObjectMessage.optString("city"));
                    }
                    if (jsonObjectMessage.has("city_id") && jsonObjectMessage.optString("city_id") != null) {
                        messageDetailsItem.setMessageCityId(jsonObjectMessage.optString("city_id"));
                    }
                    if (jsonObjectMessage.has("message_date") && jsonObjectMessage.optString("message_date") != null) {
                        messageDetailsItem.setMessageDate(jsonObjectMessage.optString("message_date"));
                    }
                    if (jsonObjectMessage.has("image_url") && jsonObjectMessage.optString("image_url") != null) {
                        messageDetailsItem.setMessageImage(jsonObjectMessage.optString("image_url"));
                    }
                    arrMessageDetailsItems.add(messageDetailsItem);
                }
                localDataSyncItem.setArrMessageDetailsItems(arrMessageDetailsItems);

            }
            if (jsonObjectMessages.has("message_gj") && jsonObjectMessages.optJSONArray("message_gj") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: message_gj present");
                JSONArray jsonArray = jsonObjectMessages.optJSONArray("message_gj");
                for (int arrIndex = 0; arrIndex < jsonArray.length(); arrIndex++) {
                    MessageDetailsItem messageDetailsItem = new MessageDetailsItem();
                    JSONObject jsonObjectMessage = jsonArray.optJSONObject(arrIndex);
                    if (jsonObjectMessage.has("id") && jsonObjectMessage.optString("id") != null) {
                        messageDetailsItem.setId(jsonObjectMessage.optString("id"));
                    }
                    if (jsonObjectMessage.has("title") && jsonObjectMessage.optString("title") != null) {
                        messageDetailsItem.setMessageTitle(jsonObjectMessage.optString("title"));
                    }
                    if (jsonObjectMessage.has("description") && jsonObjectMessage.optString("description") != null) {
                        messageDetailsItem.setMessageDescription(jsonObjectMessage.optString("description"));
                    }
                    if (jsonObjectMessage.has("message_id") && jsonObjectMessage.optString("message_id") != null) {
                        messageDetailsItem.setMessageID(jsonObjectMessage.optString("message_id"));
                    }
                    if (jsonObjectMessage.has("language_id") && jsonObject.optString("language_id") != null) {
                        messageDetailsItem.setLangugeId(jsonObjectMessage.optString("language_id"));
                    }
                    arrMessageGujaratiDetailsItems.add(messageDetailsItem);
                }
                localDataSyncItem.setArrMessageDetailsGujaratiItems(arrMessageGujaratiDetailsItems);
            }
        } else {
            Log.i(TAG, "parseLocalDataSyncResponse: Messages not present");
        }
        if (jsonObject.has("classifieds") && jsonObject.optJSONObject("classifieds") != null) {
            ArrayList<ClassifiedDetailsItem> arrClassifiedDetailsItemsEnglish = new ArrayList<>();
            ArrayList<ClassifiedDetailsItem> arrClassifiedDetailsItemsGujarati = new ArrayList<>();
            JSONObject jsonObjectClassified = jsonObject.optJSONObject("classifieds");

            if (jsonObjectClassified.has("classified_en") && jsonObjectClassified.optJSONArray("classified_en") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: classified_en present");
                JSONArray jsonArray = jsonObjectClassified.optJSONArray("classified_en");
                for (int arrIndex = 0; arrIndex < jsonArray.length(); arrIndex++) {
                    JSONObject jsonObjectClassifiedEn = jsonArray.optJSONObject(arrIndex);
                    ClassifiedDetailsItem classifiedDetailsItem = new ClassifiedDetailsItem();
                    if (jsonObjectClassifiedEn.has("id") && jsonObjectClassifiedEn.optString("id") != null) {
                        classifiedDetailsItem.setClassifiedID(jsonObjectClassifiedEn.optString("id"));
                    }
                    if (jsonObjectClassifiedEn.has("title") && jsonObjectClassifiedEn.optString("title") != null) {
                        classifiedDetailsItem.setClassifiedTitle(jsonObjectClassifiedEn.optString("title"));
                    }
                    if (jsonObjectClassifiedEn.has("description") && jsonObjectClassifiedEn.optString("description") != null) {
                        classifiedDetailsItem.setClassifiedDescription(jsonObjectClassifiedEn.optString("description"));
                    }
                    if (jsonObjectClassifiedEn.has("is_active") && jsonObjectClassifiedEn.optString("is_active") != null) {
                        classifiedDetailsItem.setIsActive(jsonObjectClassifiedEn.optString("is_active"));
                    }
                    if (jsonObjectClassifiedEn.has("city") && jsonObjectClassifiedEn.optString("city") != null) {
                        classifiedDetailsItem.setClassifiedCity(jsonObjectClassifiedEn.optString("city"));
                    }
                    if (jsonObjectClassifiedEn.has("city_id") && jsonObjectClassifiedEn.optString("city_id") != null) {
                        classifiedDetailsItem.setCityId(jsonObjectClassifiedEn.optString("city_id"));
                    }
                    if (jsonObjectClassifiedEn.has("created_at") && jsonObjectClassifiedEn.optString("created_at") != null) {
                        classifiedDetailsItem.setClassifiedCreateDate(jsonObjectClassifiedEn.optString("created_at"));
                    }
                    arrClassifiedDetailsItemsEnglish.add(classifiedDetailsItem);
                }
                localDataSyncItem.setArrClassifiedItems(arrClassifiedDetailsItemsEnglish);
            }
            if (jsonObjectClassified.has("classified_gj") && jsonObjectClassified.optJSONArray("classified_gj") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: classified_gj present");
                JSONArray jsonArray = jsonObjectClassified.optJSONArray("classified_gj");
                for (int arrIndex = 0; arrIndex < jsonArray.length(); arrIndex++) {
                    JSONObject jsonObjectGujarati = jsonArray.optJSONObject(arrIndex);
                    ClassifiedDetailsItem classifiedDetailsItem = new ClassifiedDetailsItem();
                    if (jsonObjectGujarati.has("id") && jsonObjectGujarati.optString("id") != null) {
                        classifiedDetailsItem.setId(jsonObjectGujarati.optString("id"));
                    }
                    if (jsonObjectGujarati.has("title") && jsonObjectGujarati.optString("title") != null) {
                        classifiedDetailsItem.setClassifiedTitle(jsonObjectGujarati.optString("title"));
                    }
                    if (jsonObjectGujarati.has("classified_desc") && jsonObjectGujarati.optString("classified_desc") != null) {
                        classifiedDetailsItem.setClassifiedDescription(jsonObjectGujarati.optString("classified_desc"));
                    }
                    if (jsonObjectGujarati.has("classified_id") && jsonObjectGujarati.optString("classified_id") != null) {
                        classifiedDetailsItem.setClassifiedID(jsonObjectGujarati.optString("classified_id"));
                    }
                    arrClassifiedDetailsItemsGujarati.add(classifiedDetailsItem);
                }
                localDataSyncItem.setArrClassifiedGujaratiItems(arrClassifiedDetailsItemsGujarati);
            }
        }
        if (jsonObject.has("accounts") && jsonObject.optJSONObject("accounts") != null) {
            JSONObject jsonObjectAccounts = jsonObject.getJSONObject("accounts");
            ArrayList<AccountDetailsItem> arrAccountDetailsEnglish = new ArrayList<>();
            ArrayList<AccountDetailsItem> arrAccountDetailsGujarati = new ArrayList<>();
            if (jsonObjectAccounts.has("account_en") && jsonObjectAccounts.optJSONArray("account_en") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: account_en is present");
                JSONArray jsonArrayEnglish = jsonObjectAccounts.optJSONArray("account_en");
                for (int arrIndex = 0; arrIndex < jsonArrayEnglish.length(); arrIndex++) {
                    JSONObject jsonObjectEnglish = jsonArrayEnglish.optJSONObject(arrIndex);
                    AccountDetailsItem accountDetailsItem = new AccountDetailsItem();
                    if (jsonObjectEnglish.has("id") && jsonObjectEnglish.optString("id") != null) {
                        accountDetailsItem.setStrAccountId(jsonObjectEnglish.optString("id"));
                    }
                    if (jsonObjectEnglish.has("name") && jsonObjectEnglish.optString("name") != null) {
                        accountDetailsItem.setStrAccountName(jsonObjectEnglish.optString("name"));
                    }
                    if (jsonObjectEnglish.has("description") && jsonObjectEnglish.optString("description") != null) {
                        accountDetailsItem.setStrAccountDescription(jsonObjectEnglish.optString("description"));
                    }
                    if (jsonObjectEnglish.has("is_active") && jsonObjectEnglish.optString("is_active") != null) {
                        accountDetailsItem.setStrIsActive(jsonObjectEnglish.optString("is_active"));
                    }
                    if (jsonObjectEnglish.has("city") && jsonObjectEnglish.optString("city") != null) {
                        accountDetailsItem.setStrCity(jsonObjectEnglish.optString("city"));
                    }
                    if (jsonObjectEnglish.has("city_id") && jsonObjectEnglish.optString("city_id") != null) {
                        accountDetailsItem.setStrCityId(jsonObjectEnglish.optString("city_id"));
                    }
                    if (jsonObjectEnglish.has("year") && jsonObjectEnglish.optString("year") != null) {
                        accountDetailsItem.setStrYear(jsonObjectEnglish.optString("year"));
                    }
                    if (jsonObjectEnglish.has("accounts_images") && jsonObjectEnglish.optString("accounts_images") != null && !jsonObjectEnglish.optString("accounts_images").equalsIgnoreCase("null")) {
                        JSONArray imagesArray = jsonObjectEnglish.getJSONArray("accounts_images");
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
                    arrAccountDetailsEnglish.add(accountDetailsItem);
                }
                localDataSyncItem.setArrAccountItem(arrAccountDetailsEnglish);
            } else {
                Log.i(TAG, "parseLocalDataSyncResponse: account_en is absent");
            }
            if (jsonObjectAccounts.has("account_gj") && jsonObjectAccounts.optJSONArray("account_gj") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: account_gj is present");
                JSONArray jsonArrayGujarati = jsonObjectAccounts.optJSONArray("account_gj");
                for (int arrIndex = 0; arrIndex < jsonArrayGujarati.length(); arrIndex++) {
                    AccountDetailsItem accountDetailsItem = new AccountDetailsItem();
                    JSONObject jsonObjectGujarati = jsonArrayGujarati.optJSONObject(arrIndex);
                    if (jsonObjectGujarati.has("id") && jsonObjectGujarati.optString("id") != null) {
                        accountDetailsItem.setStrId(jsonObjectGujarati.optString("id"));
                    }
                    if (jsonObjectGujarati.has("name") && jsonObjectGujarati.optString("name") != null) {
                        accountDetailsItem.setStrAccountName(jsonObjectGujarati.optString("name"));
                    }
                    if (jsonObjectGujarati.has("description") && jsonObjectGujarati.optString("description") != null) {
                        accountDetailsItem.setStrAccountDescription(jsonObjectGujarati.optString("description"));
                    }
                    if (jsonObjectGujarati.has("account_id") && jsonObjectGujarati.optString("account_id") != null) {
                        accountDetailsItem.setStrAccountId(jsonObjectGujarati.optString("account_id"));
                    }
                    arrAccountDetailsGujarati.add(accountDetailsItem);
                }
                localDataSyncItem.setArrAccountGujaratiItem(arrAccountDetailsGujarati);
            } else {
                Log.i(TAG, "parseLocalDataSyncResponse: account_gj is absent");
            }
        }
        if (jsonObject.has("committees") && jsonObject.optString("committees") != null) {
            ArrayList<CommitteeDetailsItem> arrCommitteeDetailsItems = new ArrayList<>();
            ArrayList<CommitteeDetailsItem> arrCommitteeDetailsItemsGujarati = new ArrayList<>();

            JSONObject jsonCommitteeObj = jsonObject.optJSONObject("committees");
            if (jsonCommitteeObj.has("committee_en") && jsonCommitteeObj.optJSONArray("committee_en") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: committee is present");
                JSONArray jsonArrayCommitteeEnglish = jsonCommitteeObj.optJSONArray("committee_en");
                for (int arrIndex = 0; arrIndex < jsonArrayCommitteeEnglish.length(); arrIndex++) {
                    CommitteeDetailsItem committeeDetailsItem = new CommitteeDetailsItem();
                    JSONObject jsonObjectEnglish = jsonArrayCommitteeEnglish.optJSONObject(arrIndex);
                    if (jsonObjectEnglish.has("id") && jsonObjectEnglish.optString("id") != null) {
                        committeeDetailsItem.setCommitteeID(jsonObjectEnglish.optString("id"));
                    }
                    if (jsonObjectEnglish.has("name") && jsonObjectEnglish.optString("name") != null) {
                        committeeDetailsItem.setCommitteeName(jsonObjectEnglish.optString("name"));
                    }
                    if (jsonObjectEnglish.has("description") && jsonObjectEnglish.optString("description") != null) {
                        committeeDetailsItem.setCommitteeDescription(jsonObjectEnglish.optString("description"));
                    }
                    if (jsonObjectEnglish.has("is_active") && jsonObjectEnglish.optString("is_active") != null) {
                        committeeDetailsItem.setCommIsActive(jsonObjectEnglish.optString("is_active"));
                    }
                    if (jsonObjectEnglish.has("city") && jsonObjectEnglish.optString("city") != null) {
                        committeeDetailsItem.setCommitteeCity(jsonObjectEnglish.optString("city"));
                    }
                    if (jsonObjectEnglish.has("city_id") && jsonObjectEnglish.optString("city_id") != null) {
                        committeeDetailsItem.setCityId(jsonObjectEnglish.optString("city_id"));
                    }
                    if (jsonObjectEnglish.has("members") && jsonObjectEnglish.optJSONObject("members") != null) {
                        Log.i(TAG, "parseLocalDataSyncResponse: members present inside committii");
                        JSONObject jsonMember = jsonObjectEnglish.optJSONObject("members");
                        if (jsonMember.has("member_en") && jsonMember.optString("member_en") != null) {
                            committeeDetailsItem.setCommAllMembers(jsonMember.optString("member_en"));
                        }
                        if (jsonMember.has("member_gj") && jsonMember.optString("member_gj") != null) {
                            committeeDetailsItem.setCommAllMembersGujarati(jsonMember.optString("member_gj"));
                        }
                    } else {
                        Log.i(TAG, "parseLocalDataSyncResponse: members absent inside committii");
                    }
                    arrCommitteeDetailsItems.add(committeeDetailsItem);
                }
                localDataSyncItem.setArrCommitteeDetailsItems(arrCommitteeDetailsItems);
            } else {
                Log.i(TAG, "parseLocalDataSyncResponse: committee is absent");
            }
            if (jsonCommitteeObj.has("committee_gj") && jsonCommitteeObj.optJSONArray("committee_gj") != null) {
                Log.i(TAG, "parseLocalDataSyncResponse: committee_gj present");
                JSONArray jsonArray = jsonCommitteeObj.optJSONArray("committee_gj");
                for (int arrIndex = 0; arrIndex < jsonArray.length(); arrIndex++) {
                    CommitteeDetailsItem committeeDetailsItem = new CommitteeDetailsItem();
                    JSONObject jsonObjectGujarati = jsonArray.optJSONObject(arrIndex);
                    if (jsonObjectGujarati.has("id") && jsonObjectGujarati.optString("id") != null) {
                        committeeDetailsItem.setId(jsonObjectGujarati.optString("id"));
                    }
                    if (jsonObjectGujarati.has("committee_name") && jsonObjectGujarati.optString("committee_name") != null) {
                        committeeDetailsItem.setCommitteeName(jsonObjectGujarati.optString("committee_name"));
                    }
                    if (jsonObjectGujarati.has("description") && jsonObjectGujarati.optString("description") != null) {
                        committeeDetailsItem.setCommitteeDescription(jsonObjectGujarati.optString("description"));
                    }
                    if (jsonObjectGujarati.has("committee_id") && jsonObjectGujarati.optString("committee_id") != null) {
                        committeeDetailsItem.setCommitteeID(jsonObjectGujarati.optString("committee_id"));
                    }
                    arrCommitteeDetailsItemsGujarati.add(committeeDetailsItem);
                }
                localDataSyncItem.setArrCommitteeDetailsGujaratiItems(arrCommitteeDetailsItemsGujarati);
            } else {
                Log.i(TAG, "parseLocalDataSyncResponse: committee_gj is absent");
            }

        }
        return localDataSyncItem;
    }

    public static Object parseMasterResponse (String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("data") && jsonObject.optJSONArray("data") != null) {
            ArrayList<MasterItem> arrMasterItems = new ArrayList<>();
            MasterItem masterItem = new MasterItem();
            JSONArray jsonDataArray = jsonObject.optJSONArray("data");
            for (int index = 0; index < jsonDataArray.length(); index++) {
                JSONObject jsonMasterObject = jsonDataArray.optJSONObject(index);
                {
                    Log.i(TAG, "@@1"+(jsonMasterObject.optBoolean("is_add_edit_member_enable") || !jsonMasterObject.optBoolean("is_add_edit_member_enable")));
                    if (jsonMasterObject.has("is_add_edit_member_enable") && (jsonMasterObject.optBoolean("is_add_edit_member_enable") || !jsonMasterObject.optBoolean("is_add_edit_member_enable"))) {
                        masterItem.setAddEditEnabled(jsonMasterObject.optBoolean("is_add_edit_member_enable"));
                        Log.i(TAG, "parseMasterResponse: Add Edit is Enable?...."+jsonMasterObject.optBoolean("is_add_edit_member_enable"));
                    }
                    JSONObject jsonBuzzObject = jsonMasterObject.optJSONObject("buzz");
                    if (jsonBuzzObject != null) {
                        if(jsonBuzzObject.has("msg_img") && jsonBuzzObject.optString("msg_img") != null && !jsonBuzzObject.optString("msg_img").equalsIgnoreCase("null")){
                            String strBuzzUrl = jsonBuzzObject.optString("msg_img");
                            masterItem.setStrBuzzImageUrl(strBuzzUrl);
                            Log.i("@@1",strBuzzUrl);
                        }
                        if (jsonBuzzObject.has("id")&& jsonBuzzObject.optString("id") != null && !jsonBuzzObject.optString("msg_img").equalsIgnoreCase("null")){
                            int intId = jsonBuzzObject.optInt("id");
                            masterItem.setIntBuzzId(intId);
                        }
                    }
                }
            }
            return masterItem;
        }
        return false;
    }

}
