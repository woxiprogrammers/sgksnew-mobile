package com.woxi.sgks_member.utils;

import android.util.Log;

import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.models.AccountDetailsItem;
import com.woxi.sgks_member.models.AccountYearItem;
import com.woxi.sgks_member.models.ClassifiedDetailsItem;
import com.woxi.sgks_member.models.CommitteeDetailsItem;
import com.woxi.sgks_member.models.EventDataItem;
import com.woxi.sgks_member.models.FamilyDetailsItem;
import com.woxi.sgks_member.models.LocalDataSyncItem;
import com.woxi.sgks_member.models.MasterDataItem;
import com.woxi.sgks_member.models.MemberAddressItem;
import com.woxi.sgks_member.models.MemberDetailsItem;
import com.woxi.sgks_member.models.MemberSearchDataItem;
import com.woxi.sgks_member.models.MessageAndClassifiedResponseItem;
import com.woxi.sgks_member.models.MessageDetailsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <b>public class AppParser implements AppConstants</b>
 * <p>This class is used to parse JSON responses</p>
 * created by - Rohit
 */
public class AppParser implements AppConstants {
    /*{"data":[{"_id":"57e4c80272bdde4d5f37e164","name":"managing"},{"_id":"57ee08a472bdde3a2c635942","name":"annadan"}],"message":"success"}*/
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

    /*{"pagination":{"total":3,"current_page":1,"last_page":1,"next_page_url":null,"prev_page_url":null},
    "data":[{"surname":"Vaghela","native_place":"Sampar","sgks_city":"PUNE","member_id":"1-1","first_name":"Gokal","middle_name":"Rajabhai",
    "gender":"Male","email":null,"mobile":"9371277787","birth_date":"1942-05-20","blood_group":"B+","hobbies":["Family Time","Reading","others"],
    "marital_status":"married","marriage_date":"1964-05-20","education":{"degree":"7th std","specialization":"Gujarati"},"area":"Somwar Peth",
    "system_area":"somwar peth","city":"Pune","state":"MH","country":null,"family_head":true,"relations":{"children":["1-3"],"partner":"1-2"},
    "isapprove_member":null,"created_at":null,"updated_at":null,"image_url":"http://api.sgks.co.in/uploads/userdata/family/no_image.png",
    "income":"between 10 to 25 lakh","memLatitude":null,"memLongitude":null,"mosal_village":"Jiragardh","occupation":"Business","organ_donate":"Yes",
    "organs":["eye part sclera","eye part cornea","skin","liver","heart","lungs","kidneys"],"panth":"rampanthi","status":"Alive",
    "political_support":"BJP","address":{"address_line":"210, Somwar Peth, Shree Apartment","area":"Somwar Peth","city":"Pune","country":"IN",
    "id":"1","landmark":"near nageshwar temple","pincode":"411011","state":"MH"}}],"message":"success","stats":null}*/
    public static Object parseMemberSearchResponse(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        MemberSearchDataItem memberSearchDataItem = new MemberSearchDataItem();
        if (jsonResponseObject.has("pagination") && jsonResponseObject.optJSONObject("pagination") != null) {
            JSONObject jsonPaginationObject = jsonResponseObject.optJSONObject("pagination");
            if (jsonPaginationObject != null) {
                if (jsonPaginationObject.has("next_page_url") && jsonPaginationObject.optString("next_page_url") != null) {
                    memberSearchDataItem.setStrNextPageUrl(jsonPaginationObject.optString("next_page_url"));
                }
            }
        }
        if (jsonResponseObject.has("data") && jsonResponseObject.optJSONArray("data") != null) {
            JSONArray jsonDataArray = jsonResponseObject.optJSONArray("data");
            ArrayList<MemberDetailsItem> arrMemberList = new ArrayList<>();
            if (jsonDataArray != null) {
                for (int arrIndexData = 0; arrIndexData < jsonDataArray.length(); arrIndexData++) {
                    MemberDetailsItem memberDetailsItem = new MemberDetailsItem();
                    JSONObject jsonMemberObject = jsonDataArray.optJSONObject(arrIndexData);
                    if (jsonMemberObject.has("surname") && jsonMemberObject.optString("surname") != null && !jsonMemberObject.optString("surname").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemSurname(jsonMemberObject.optString("surname"));
                    }
                    if (jsonMemberObject.has("native_place") && jsonMemberObject.optString("native_place") != null && !jsonMemberObject.optString("native_place").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemNativePlace(jsonMemberObject.optString("native_place"));
                    }
                    if (jsonMemberObject.has("sgks_city") && jsonMemberObject.optString("sgks_city") != null && !jsonMemberObject.optString("sgks_city").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemSgksMainCity(jsonMemberObject.optString("sgks_city"));
                    }
                    if (jsonMemberObject.has("sgks_member_id") && jsonMemberObject.getString("sgks_member_id") != null) {
                        memberDetailsItem.setMemSgksMemberId(jsonMemberObject.getString("sgks_member_id"));
                    }
                    if (jsonMemberObject.has("member_id") && jsonMemberObject.optString("member_id") != null && !jsonMemberObject.optString("member_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemID(jsonMemberObject.optString("member_id"));
                    }
                    if (jsonMemberObject.has("first_name") && jsonMemberObject.optString("first_name") != null && !jsonMemberObject.optString("first_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemFirstName(jsonMemberObject.optString("first_name"));
                    }
                    if (jsonMemberObject.has("middle_name") && jsonMemberObject.optString("middle_name") != null && !jsonMemberObject.optString("middle_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemMidName(jsonMemberObject.optString("middle_name"));
                    }
                    if (jsonMemberObject.has("gender") && jsonMemberObject.optString("gender") != null && !jsonMemberObject.optString("gender").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemGender(jsonMemberObject.optString("gender"));
                    }
                    if (jsonMemberObject.has("email") && jsonMemberObject.optString("email") != null && !jsonMemberObject.optString("email").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemEmail(jsonMemberObject.optString("email"));
                    }
                    if (jsonMemberObject.has("mobile") && jsonMemberObject.optString("mobile") != null && !jsonMemberObject.optString("mobile").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemMobile(jsonMemberObject.optString("mobile"));
                    }
                    if (jsonMemberObject.has("birth_date") && jsonMemberObject.optString("birth_date") != null && !jsonMemberObject.optString("birth_date").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemBirthDate(jsonMemberObject.optString("birth_date"));
                    }
                    if (jsonMemberObject.has("blood_group") && jsonMemberObject.optString("blood_group") != null && !jsonMemberObject.optString("blood_group").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemBloodGroup(jsonMemberObject.optString("blood_group"));
                    }
                    /*if (jsonMemberObject.has("hobbies") && jsonMemberObject.optString("hobbies") != null && !jsonMemberObject.optString("hobbies").equalsIgnoreCase("null")) {
                        //
//                    memberDetailsItem.setArrMemHobbies(jsonMemberObject.optString("hobbies"));
                    }*/
                    if (jsonMemberObject.has("marital_status") && jsonMemberObject.optString("marital_status") != null && !jsonMemberObject.optString("marital_status").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemMaritalStatus(jsonMemberObject.optString("marital_status"));
                    }
                    if (jsonMemberObject.has("marriage_date") && jsonMemberObject.optString("marriage_date") != null && !jsonMemberObject.optString("marriage_date").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemMarriageDate(jsonMemberObject.optString("marriage_date"));
                    }
                    /*if (jsonMemberObject.has("education") && jsonMemberObject.optString("education") != null && !jsonMemberObject.optString("education").equalsIgnoreCase("null")) {
                        //
//                    memberDetailsItem.setArrMemEducation(jsonMemberObject.optString("education"));
                    }*/
                    if (jsonMemberObject.has("system_area") && jsonMemberObject.optString("system_area") != null && !jsonMemberObject.optString("system_area").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemSgksArea(jsonMemberObject.optString("system_area"));
                    }
                    if (jsonMemberObject.has("image_url") && jsonMemberObject.optString("image_url") != null && !jsonMemberObject.optString("image_url").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemberImageURL(jsonMemberObject.optString("image_url"));
                    }
                    if (jsonMemberObject.has("memLatitude") && jsonMemberObject.optString("memLatitude") != null && !jsonMemberObject.optString("memLatitude").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemLatitude(jsonMemberObject.optString("memLatitude"));
                    }
                    if (jsonMemberObject.has("memLongitude") && jsonMemberObject.optString("memLongitude") != null && !jsonMemberObject.optString("memLongitude").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemLongitude(jsonMemberObject.optString("memLongitude"));
                    }
                    if (jsonMemberObject.has("mosal_village") && jsonMemberObject.optString("mosal_village") != null && !jsonMemberObject.optString("mosal_village").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemMosalVillage(jsonMemberObject.optString("mosal_village"));
                    }
                    if (jsonMemberObject.has("occupation") && jsonMemberObject.optString("occupation") != null && !jsonMemberObject.optString("occupation").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemOccupation(jsonMemberObject.optString("occupation"));
                    }
                    if (jsonMemberObject.has("address") && jsonMemberObject.optString("address") != null && !jsonMemberObject.optString("address").equalsIgnoreCase("null")) {
                        MemberAddressItem memAddressItem = new MemberAddressItem();
                        JSONObject jsonAddressObject = jsonMemberObject.optJSONObject("address");
                        if (jsonAddressObject.has("address_line") && jsonAddressObject.optString("address_line") != null && !jsonAddressObject.optString("address_line").equalsIgnoreCase("null")) {
                            memAddressItem.setAddAddressLine(jsonAddressObject.optString("address_line"));
                        }
                        if (jsonAddressObject.has("area") && jsonAddressObject.optString("area") != null && !jsonAddressObject.optString("area").equalsIgnoreCase("null")) {
                            memAddressItem.setAddArea(jsonAddressObject.optString("area"));
                        }
                        if (jsonAddressObject.has("city") && jsonAddressObject.optString("city") != null && !jsonAddressObject.optString("city").equalsIgnoreCase("null")) {
                            memAddressItem.setAddCity(jsonAddressObject.optString("city"));
                        }
                        if (jsonAddressObject.has("state") && jsonAddressObject.optString("state") != null && !jsonAddressObject.optString("state").equalsIgnoreCase("null")) {
                            memAddressItem.setAddState(jsonAddressObject.optString("state"));
                        }
                        if (jsonAddressObject.has("country") && jsonAddressObject.optString("country") != null && !jsonAddressObject.optString("country").equalsIgnoreCase("null")) {
                            memAddressItem.setAddCountry(jsonAddressObject.optString("country"));
                        }
                        if (jsonAddressObject.has("landmark") && jsonAddressObject.optString("landmark") != null && !jsonAddressObject.optString("landmark").equalsIgnoreCase("null")) {
                            memAddressItem.setAddLandMark(jsonAddressObject.optString("landmark"));
                        }
                        if (jsonAddressObject.has("pincode") && jsonAddressObject.optString("pincode") != null && !jsonAddressObject.optString("pincode").equalsIgnoreCase("null")) {
                            memAddressItem.setAddPincode(jsonAddressObject.optString("pincode"));
                        }
                        memberDetailsItem.setMemAddress(memAddressItem);
                    }
                    arrMemberList.add(memberDetailsItem);
                }
                memberSearchDataItem.setArrMemberList(arrMemberList);
            }
            return memberSearchDataItem;
        }
        return false;
    }

    public static Object parseNewMemberList(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        MemberSearchDataItem memberSearchDataItem = new MemberSearchDataItem();
        if (jsonResponseObject.has("data") && jsonResponseObject.optJSONArray("data") != null) {
            JSONArray jsonDataArray = jsonResponseObject.optJSONArray("data");
            ArrayList<MemberDetailsItem> arrMemberList = new ArrayList<>();
            if (jsonDataArray != null) {
                for (int arrIndexData = 0; arrIndexData < jsonDataArray.length(); arrIndexData++) {
                    MemberDetailsItem memberDetailsItem = new MemberDetailsItem();
                    JSONObject jsonMemberObject = jsonDataArray.optJSONObject(arrIndexData);
                    if (jsonMemberObject.has("surname") && jsonMemberObject.optString("surname") != null && !jsonMemberObject.optString("surname").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemSurname(jsonMemberObject.optString("surname"));
                    }
                    if (jsonMemberObject.has("native_place") && jsonMemberObject.optString("native_place") != null && !jsonMemberObject.optString("native_place").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemNativePlace(jsonMemberObject.optString("native_place"));
                    }
                    if (jsonMemberObject.has("sgks_city") && jsonMemberObject.optString("sgks_city") != null && !jsonMemberObject.optString("sgks_city").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemSgksMainCity(jsonMemberObject.optString("sgks_city"));
                    }
                    if (jsonMemberObject.has("sgks_member_id") && jsonMemberObject.getString("sgks_member_id") != null) {
                        memberDetailsItem.setMemSgksMemberId(jsonMemberObject.getString("sgks_member_id"));
                    }
                    if (jsonMemberObject.has("member_id") && jsonMemberObject.optString("member_id") != null && !jsonMemberObject.optString("member_id").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemID(jsonMemberObject.optString("member_id"));
                    }
                    if (jsonMemberObject.has("first_name") && jsonMemberObject.optString("first_name") != null && !jsonMemberObject.optString("first_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemFirstName(jsonMemberObject.optString("first_name"));
                    }
                    if (jsonMemberObject.has("middle_name") && jsonMemberObject.optString("middle_name") != null && !jsonMemberObject.optString("middle_name").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemMidName(jsonMemberObject.optString("middle_name"));
                    }
                    if (jsonMemberObject.has("gender") && jsonMemberObject.optString("gender") != null && !jsonMemberObject.optString("gender").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemGender(jsonMemberObject.optString("gender"));
                    }
                    if (jsonMemberObject.has("area") && jsonMemberObject.getString("area") != null) {
                        memberDetailsItem.setMemSgksArea(jsonMemberObject.optString("area"));
                    }
                    if (jsonMemberObject.has("mobile") && jsonMemberObject.getString("mobile") != null) {
                        memberDetailsItem.setMemMobile(jsonMemberObject.optString("mobile"));
                    }
                    if (jsonMemberObject.has("member_image_url") && jsonMemberObject.getString("member_image_url") != null) {
                        memberDetailsItem.setMemberImageURL(jsonMemberObject.optString("member_image_url"));
                    }
                    if (jsonMemberObject.has("latitude") && jsonMemberObject.getString("latitude") != null) {
                        memberDetailsItem.setMemLatitude(jsonMemberObject.optString("latitude"));
                    }
                    if (jsonMemberObject.has("longitude") && jsonMemberObject.getString("longitude") != null) {
                        memberDetailsItem.setMemLongitude(jsonMemberObject.optString("longitude"));
                    }
                    if (jsonMemberObject.has("blood_group") && jsonMemberObject.optString("blood_group") != null && !jsonMemberObject.optString("blood_group").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemBloodGroup(jsonMemberObject.optString("blood_group"));
                    }
                    if (jsonMemberObject.has("marital_status") && jsonMemberObject.optString("marital_status") != null && !jsonMemberObject.optString("marital_status").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemMaritalStatus(jsonMemberObject.optString("marital_status"));
                    }
                    if (jsonMemberObject.has("address") && jsonMemberObject.optString("address") != null && !jsonMemberObject.optString("address").equalsIgnoreCase("null")) {
                        MemberAddressItem memAddressItem = new MemberAddressItem();
                        JSONObject jsonAddressObject = jsonMemberObject.optJSONObject("address");
                        if (jsonAddressObject.has("address_line") && jsonAddressObject.optString("address_line") != null && !jsonAddressObject.optString("address_line").equalsIgnoreCase("null")) {
                            memAddressItem.setAddAddressLine(jsonAddressObject.optString("address_line"));
                        }
                        if (jsonAddressObject.has("area") && jsonAddressObject.optString("area") != null && !jsonAddressObject.optString("area").equalsIgnoreCase("null")) {
                            memAddressItem.setAddArea(jsonAddressObject.optString("area"));
                        }
                        if (jsonAddressObject.has("city") && jsonAddressObject.optString("city") != null && !jsonAddressObject.optString("city").equalsIgnoreCase("null")) {
                            memAddressItem.setAddCity(jsonAddressObject.optString("city"));
                        }
                        if (jsonAddressObject.has("state") && jsonAddressObject.optString("state") != null && !jsonAddressObject.optString("state").equalsIgnoreCase("null")) {
                            memAddressItem.setAddState(jsonAddressObject.optString("state"));
                        }
                        if (jsonAddressObject.has("country") && jsonAddressObject.optString("country") != null && !jsonAddressObject.optString("country").equalsIgnoreCase("null")) {
                            memAddressItem.setAddCountry(jsonAddressObject.optString("country"));
                        }
                        if (jsonAddressObject.has("landmark") && jsonAddressObject.optString("landmark") != null && !jsonAddressObject.optString("landmark").equalsIgnoreCase("null")) {
                            memAddressItem.setAddLandMark(jsonAddressObject.optString("landmark"));
                        }
                        if (jsonAddressObject.has("pincode") && jsonAddressObject.optString("pincode") != null && !jsonAddressObject.optString("pincode").equalsIgnoreCase("null")) {
                            memAddressItem.setAddPincode(jsonAddressObject.optString("pincode"));
                        }
                        memberDetailsItem.setMemAddress(memAddressItem);
                    }
                    arrMemberList.add(memberDetailsItem);
                }
            }
            return arrMemberList;
        }
        return false;
    }

    //News Listing and Details Response
    /*{"pagination":{"current_page":1,"next_page_url":"http://sgksapi.woxi.co.in/v1/sgksmain/membersearch/master_lazy?page=2",
    "prev_page_url":null},"data":[{"_id":"58240e6c72bdde7d610c62e1","title":"Happy BD PQR!","msg_desc":"Happy BD PQR.",
    "msg_img":"http://sgksapi.woxi.co.in/uploads/messages/4e2e3bd0c0e54a24d0d5.jpg","msg_type":"birthday","sgks_city":"PUNE"},
    {"_id":"58240e5e72bdde33cd7baa12","title":"Happy BD ABC!","msg_desc":"Happy BD ABC.","msg_img":"http://sgksapi.woxi.co.in/uploads/messages/02d95faf4a3573dfbc32.jpg",
    "msg_type":"birthday","sgks_city":"PUNE"}],"message":"success"}*/
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
                    if (jsonNewsObject.has("sgks_city") && jsonNewsObject.optString("sgks_city") != null && !jsonNewsObject.optString("sgks_city").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageCity(jsonNewsObject.optString("sgks_city"));
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
                    if (jsonNewsObject.has("sgks_city") && jsonNewsObject.optString("sgks_city") != null && !jsonNewsObject.optString("sgks_city").equalsIgnoreCase("null")) {
                        messageDetailsItem.setMessageCity(jsonNewsObject.optString("sgks_city"));
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
            }
            return arrNewsDetails;
        }
        return false;
    }

    /*{"sgks_area":["karvenagar","somwar peth"],"suggestionbox_category":["Social","Cultural"],
    "sgks_messages":["58240e0772bdde7c884627b1","58240e5272bdde35e014e751"],
    "sgks_buzz":{"_id":"5825b89872bdde66511ec2d1","msg_img":"http://sgksapi.woxi.co.in/uploads/userdata/family/no_image.png"},
    "sgks_classified":null,"message":"success"}*/
    public static Object parseMasterListResponse(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        MasterDataItem masterDataItem = new MasterDataItem();
        if (jsonResponseObject.has("sgks_area") && jsonResponseObject.optString("sgks_area") != null) {
            String strSgksArea = jsonResponseObject.optString("sgks_area");
            masterDataItem.setStrSgksArea(strSgksArea);
        }
        if (jsonResponseObject.has("suggestionbox_category") && jsonResponseObject.optString("suggestionbox_category") != null) {
            String strSuggestionCategory = jsonResponseObject.optString("suggestionbox_category");
            masterDataItem.setStrSuggestionCategory(strSuggestionCategory);
        }
        if (jsonResponseObject.has("sgks_total_member") && jsonResponseObject.optString("sgks_total_member") != null && !jsonResponseObject.optString("sgks_total_member").equalsIgnoreCase("null")) {
            int intTotalMemberCount = Integer.parseInt(jsonResponseObject.optString("sgks_total_member"));
            masterDataItem.setIntTotalMemberCount(intTotalMemberCount);
        }
        if (jsonResponseObject.has("sgks_messages") && jsonResponseObject.optString("sgks_messages") != null) {
            String strMessage = jsonResponseObject.optString("sgks_messages");
            masterDataItem.setStrMessageIds(strMessage);
        }
        if (jsonResponseObject.has("sgks_buzz") && jsonResponseObject.optJSONObject("sgks_buzz") != null) {
            JSONObject jsonBuzzObject = jsonResponseObject.optJSONObject("sgks_buzz");
            String strBuzzId = jsonBuzzObject.optString("_id");
            masterDataItem.setStrSgksBuzz_Id(strBuzzId);
            //
            String strBuzzUrl = jsonBuzzObject.optString("msg_img");
            masterDataItem.setStrSgksBuzz_ImgUrl(strBuzzUrl);
        }
        if (jsonResponseObject.has("sgks_classified") && jsonResponseObject.optString("sgks_classified") != null) {
            String strClassifiedIDs = jsonResponseObject.optString("sgks_classified");
            masterDataItem.setStrClassifiedIds(strClassifiedIDs);
        }
        //Return final masterDataItem object
        return masterDataItem;
    }

    public static Object parseLocalDataSyncResponse(String response) throws JSONException {
        JSONObject jsonResponseObject = new JSONObject(response);
        LocalDataSyncItem localDataSyncItem = new LocalDataSyncItem();
        if (jsonResponseObject.has("families") && jsonResponseObject.optString("families") != null) {
            ArrayList<FamilyDetailsItem> arrFamilyDetailsItems = new ArrayList<>();
            JSONArray jsonFamilyArray = jsonResponseObject.optJSONArray("families");
            if (jsonFamilyArray != null) {
                FamilyDetailsItem familyDetailsItem;
                for (int arrIndex = 0; arrIndex < jsonFamilyArray.length(); arrIndex++) {
                    familyDetailsItem = new FamilyDetailsItem();
                    JSONObject jsonObject = jsonFamilyArray.optJSONObject(arrIndex);
                    if (jsonObject.has("family_id") && jsonObject.getString("family_id") != null) {
                        familyDetailsItem.setFamily_id(jsonObject.getString("family_id"));
                    }
                    if (jsonObject.has("sgks_family_id") && jsonObject.getString("sgks_family_id") != null) {
                        familyDetailsItem.setSgks_family_id(jsonObject.getString("sgks_family_id"));
                    }
                    if (jsonObject.has("surname") && jsonObject.getString("surname") != null) {
                        familyDetailsItem.setSurname(jsonObject.getString("surname"));
                    }
                    if (jsonObject.has("native_place") && jsonObject.getString("native_place") != null) {
                        familyDetailsItem.setNative_place(jsonObject.getString("native_place"));
                    }
                    if (jsonObject.has("sgks_city") && jsonObject.getString("sgks_city") != null) {
                        familyDetailsItem.setSgks_city(jsonObject.getString("sgks_city"));
                    }
                    arrFamilyDetailsItems.add(familyDetailsItem);
                }
            }
            localDataSyncItem.setArrFamilyDetailsItems(arrFamilyDetailsItems);
        }
        if (jsonResponseObject.has("members") && jsonResponseObject.optString("members") != null) {
            ArrayList<MemberDetailsItem> arrMemberDetailsItems = new ArrayList<>();
            JSONArray jsonMemberArray = jsonResponseObject.optJSONArray("members");
            if (jsonMemberArray != null) {
                MemberDetailsItem memberDetailsItem;
                for (int arrIndex = 0; arrIndex < jsonMemberArray.length(); arrIndex++) {
                    memberDetailsItem = new MemberDetailsItem();
                    JSONObject jsonObject = jsonMemberArray.optJSONObject(arrIndex);
                    if (jsonObject.has("member_id") && jsonObject.getString("member_id") != null) {
                        memberDetailsItem.setMemID(jsonObject.getString("member_id"));
                    }
                    if (jsonObject.has("sgks_member_id") && jsonObject.getString("sgks_member_id") != null) {
                        memberDetailsItem.setMemSgksMemberId(jsonObject.getString("sgks_member_id"));
                    }
                    if (jsonObject.has("family_id") && jsonObject.getString("family_id") != null) {
                        memberDetailsItem.setMemFamilyId(jsonObject.getString("family_id"));
                    }
                    if (jsonObject.has("sgks_family_id") && jsonObject.getString("sgks_family_id") != null) {
                        memberDetailsItem.setMemSgksFamilyId(jsonObject.getString("sgks_family_id"));
                    }
                    if (jsonObject.has("first_name") && jsonObject.getString("first_name") != null) {
                        memberDetailsItem.setMemFirstName(jsonObject.getString("first_name"));
                    }
                    if (jsonObject.has("middle_name") && jsonObject.getString("middle_name") != null) {
                        memberDetailsItem.setMemMidName(jsonObject.getString("middle_name"));
                    }
                    if (jsonObject.has("surname") && jsonObject.getString("surname") != null) {
                        memberDetailsItem.setMemSurname(jsonObject.getString("surname"));
                    }
                    if (jsonObject.has("gender") && jsonObject.getString("gender") != null) {
                        memberDetailsItem.setMemGender(jsonObject.getString("gender"));
                    }
                    if (jsonObject.has("mobile") && jsonObject.getString("mobile") != null) {
                        memberDetailsItem.setMemMobile(jsonObject.getString("mobile"));
                    }
                    if (jsonObject.has("email") && jsonObject.getString("email") != null && !jsonObject.getString("email").equalsIgnoreCase("null")) {
                        memberDetailsItem.setMemEmail(jsonObject.getString("email"));
                    }
                    if (jsonObject.has("blood_group") && jsonObject.getString("blood_group") != null) {
                        memberDetailsItem.setMemBloodGroup(jsonObject.getString("blood_group"));
                    }
                    if (jsonObject.has("marital_status") && jsonObject.getString("marital_status") != null) {
                        memberDetailsItem.setMemMaritalStatus(jsonObject.getString("marital_status"));
                    }
                    if (jsonObject.has("sgks_area") && jsonObject.getString("sgks_area") != null) {
                        memberDetailsItem.setMemSgksArea(jsonObject.getString("sgks_area"));
                    }
                    if (jsonObject.has("sgks_city") && jsonObject.getString("sgks_city") != null) {
                        memberDetailsItem.setMemSgksMainCity(jsonObject.getString("sgks_city"));
                    }
                    if (jsonObject.has("memLatitude") && jsonObject.getString("memLatitude") != null) {
                        memberDetailsItem.setMemLatitude(jsonObject.getString("memLatitude"));
                    }
                    if (jsonObject.has("memLongitude") && jsonObject.getString("memLongitude") != null) {
                        memberDetailsItem.setMemLongitude(jsonObject.getString("memLongitude"));
                    }
                    if (jsonObject.has("image_url") && jsonObject.getString("image_url") != null) {
                        memberDetailsItem.setMemberImageURL(jsonObject.getString("image_url"));
                    }
                    if (jsonObject.has("address_id") && jsonObject.getString("address_id") != null) {
                        memberDetailsItem.setAddress_id(jsonObject.getString("address_id"));
                    }
                    if (jsonObject.has("isapprove_member") && jsonObject.getString("isapprove_member") != null) {
                        memberDetailsItem.setMemIsActive(jsonObject.getString("isapprove_member"));
                    }
                    arrMemberDetailsItems.add(memberDetailsItem);
                }
            }
            localDataSyncItem.setArrMemberDetailsItems(arrMemberDetailsItems);
        }
        if (jsonResponseObject.has("address") && jsonResponseObject.optString("address") != null) {
            ArrayList<MemberAddressItem> arrMemberAddressItems = new ArrayList<>();
            JSONArray jsonAddressArray = jsonResponseObject.optJSONArray("address");
            if (jsonAddressArray != null) {
                MemberAddressItem memberAddressItem;
                for (int arrIndex = 0; arrIndex < jsonAddressArray.length(); arrIndex++) {
                    memberAddressItem = new MemberAddressItem();
                    JSONObject jsonObject = jsonAddressArray.optJSONObject(arrIndex);
                    if (jsonObject.has("address_line") && jsonObject.getString("address_line") != null) {
                        memberAddressItem.setAddAddressLine(jsonObject.getString("address_line"));
                    }
                    if (jsonObject.has("area") && jsonObject.getString("area") != null) {
                        memberAddressItem.setAddArea(jsonObject.getString("area"));
                    }
                    if (jsonObject.has("landmark") && jsonObject.getString("landmark") != null) {
                        memberAddressItem.setAddLandMark(jsonObject.getString("landmark"));
                    }
                    if (jsonObject.has("city") && jsonObject.getString("city") != null) {
                        memberAddressItem.setAddCity(jsonObject.getString("city"));
                    }
                    if (jsonObject.has("pincode") && jsonObject.getString("pincode") != null) {
                        memberAddressItem.setAddPincode(jsonObject.getString("pincode"));
                    }
                    if (jsonObject.has("state") && jsonObject.getString("state") != null) {
                        memberAddressItem.setAddState(jsonObject.getString("state"));
                    }
                    if (jsonObject.has("country") && jsonObject.getString("country") != null) {
                        memberAddressItem.setAddCountry(jsonObject.getString("country"));
                    }
                    if (jsonObject.has("address_id") && jsonObject.getString("address_id") != null) {
                        memberAddressItem.setAddAddressId(jsonObject.getString("address_id"));
                    }
                    if (jsonObject.has("family_id") && jsonObject.getString("family_id") != null) {
                        memberAddressItem.setAddFamilyId(jsonObject.getString("family_id"));
                    }
                    arrMemberAddressItems.add(memberAddressItem);
                }
            }
            localDataSyncItem.setArrMemberAddressItems(arrMemberAddressItems);
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
                    if (jsonObject.has("sgks_city") && jsonObject.getString("sgks_city") != null) {
                        committeeDetailsItem.setCommitteeCity(jsonObject.getString("sgks_city"));
                    }
                    if (jsonObject.has("committee_id") && jsonObject.getString("committee_id") != null) {
                        committeeDetailsItem.setCommitteeID(jsonObject.getString("committee_id"));
                    }
                    if (jsonObject.has("comm_mem_details") && jsonObject.getString("comm_mem_details") != null) {
                        committeeDetailsItem.setCommAllMembers(jsonObject.getString("comm_mem_details"));
                    }
                    if (jsonObject.has("is_active") && jsonObject.getString("is_active") != null) {
                        committeeDetailsItem.setCommIsActive(jsonObject.getString("is_active"));
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
                    if (jsonObject.has("message_id") && jsonObject.getString("message_id") != null) {
                        messageDetailsItem.setMessageID(jsonObject.getString("message_id"));
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
                    if (jsonObject.has("sgks_city") && jsonObject.getString("sgks_city") != null) {
                        messageDetailsItem.setMessageCity(jsonObject.getString("sgks_city"));
                    }
                    if (jsonObject.has("is_active") && jsonObject.getString("is_active") != null) {
                        messageDetailsItem.setMessageIsActive(jsonObject.getString("is_active"));
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
                    if (jsonDetailsObject.has("account_image_url") && jsonDetailsObject.optString("account_image_url") != null && !jsonDetailsObject.optString("account_image_url").equalsIgnoreCase("null")) {
                        accountDetailsItem.setStrAccountImageUrl(jsonDetailsObject.optString("account_image_url"));
                    }
                    if (jsonDetailsObject.has("name") && jsonDetailsObject.optString("name") != null && !jsonDetailsObject.optString("name").equalsIgnoreCase("null")) {
                        accountDetailsItem.setStrAccountName(jsonDetailsObject.optString("name"));
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

    /*{"data":[{"_id":"58d36b6b3e84e4041f201979","event_name":"hello world","desc":"hi hello","event_date":"29th feb 2016 to 30th feb 2017",
    "year":"2017","event_images":["http:\/\/api.sgks.co.in\/uploads\/events\/c256c6333dcb32253025.jpg"]}],"message":"success"}*/
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
        MessageAndClassifiedResponseItem classifiedResponseItem = new MessageAndClassifiedResponseItem();
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
                    if (jsonNewsObject.has("sgks_city") && jsonNewsObject.optString("sgks_city") != null && !jsonNewsObject.optString("sgks_city").equalsIgnoreCase("null")) {
                        classifiedDetailsItem.setClassifiedCity(jsonNewsObject.optString("sgks_city"));
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
                classifiedResponseItem.setArrClassifiedList(arrClassifiedDetails);
            }
            return arrClassifiedDetails;
        }
        return false;
    }
}
