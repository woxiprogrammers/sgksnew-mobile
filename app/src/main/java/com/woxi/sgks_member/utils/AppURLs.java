package com.woxi.sgks_member.utils;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Sharvari.
 */
public class AppURLs {
    //Test environment
//    private static String SGKS_URL = "http://sgksapi.woxi.co.in";

    //Production Environment
    private static String SGKS_URL = "http://sgksapi.woxisoftware.com/api/";

    private static String VERSION = "v1/";
    private static String BASE_URL = SGKS_URL + VERSION;

    public static final String API_MEMBERS_LISTING=BASE_URL + "sgks-member/listing";
    public static final String API_EVENT_LISTING=BASE_URL + "sgks-event/listing";
    public static final String API_COMMITTEE_LISTING=BASE_URL + "sgks-committee/listing";
    public static final String API_MESSAGE_LISTING=BASE_URL + "sgks-message/listing";
    public static final String API_CLASSIFIED_LISTING=BASE_URL + "sgks-classified/listing";
    public static final String API_ACCOUNT_LISTING=BASE_URL + "sgks-account/listing";
    public static final String API_MISCELLANEOUS_WEBVIEW = BASE_URL + "sgks-public/";
    public static final String API_MASTER_LIST=BASE_URL + "sgks-public/master-list";
    public static final String API_ADDME_TO_SGKS=BASE_URL + "sgks-public/addmetosgks";
    public static final String API_SGKS_SUGGESTIONS=BASE_URL+"sgks-public/sgks-suggestion";
    public static final String API_SKS_OFFLINE=BASE_URL + "sgks-offline/local-storage-offline";




























    public static final String API_MEMBER_LAZY_LOADING_SEARCH = BASE_URL + "/sgksmain/membersearch/master_lazy_localstorage?page=1";
    public static final String API_MESSAGE_LAZY_LOADING_LIST = BASE_URL + "/sgksmain/messages/messages_lazy?page=1";
    //sgksmain/localstorage/offline
    public static final String API_LOCAL_DATA_SYNC = BASE_URL + "/sgksmain/localstorage/offline";
    public static final String API_EVENT_YEAR_LIST = BASE_URL + "/masters/sgks_event_master?city=";
    public static final String API_Classified_LAZY_LOADING_LIST = BASE_URL + "/sgksmain/classified/classified_list?page=1";
}
