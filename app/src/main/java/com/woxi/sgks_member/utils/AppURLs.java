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
    public static final String API_COMMITTEE_ID = "&id=";
    public static final String API_CITY = "?city=";

    public static final String API_MEMBERS_LISTING=SGKS_URL + VERSION + "sgks-member/listing";
























    //sgksmain/committee/lists?city=PUNE
    public static final String API_COMMITTEE_LIST = BASE_URL + "/sgksmain/committee/lists?city=";

    //http://temp.sgks.co.in/v1/sgksmain/committee/lists_short?city=PUNE
    public static final String API_COMMITTEE_LAZY_LOADING_LIST = BASE_URL + "/sgksmain/committee/lists_short?city=";

    ///sgksmain/miscellaneous/sgkshealthplus?city=PUNE
    public static final String API_MISCELLANEOUS_WEBVIEW = BASE_URL + "/sgksmain/miscellaneous/";

    ///sgksmain/membersearch/master
    //http://temp.sgks.co.in/v1/sgksmain/membersearch/master_lazy?page=1    //For version <= v1.9
    //http://temp.sgks.co.in/v1/sgksmain/membersearch/master_lazy_localstorage?page=1    //For version > v1.9
    public static final String API_MEMBER_LAZY_LOADING_SEARCH = BASE_URL + "/sgksmain/membersearch/master_lazy_localstorage?page=1";

    public static final String API_ADD_ME_TO_SGKS = BASE_URL + "/sgksmain/miscellaneous/addmetosgks";
    public static final String API_SUGGESTION = BASE_URL + "/sgksmain/miscellaneous/sgkssuggestionbox";

    //http://sgksapi.woxi.co.in/v1/masters/sgks_app_master?city=PUNE
    public static final String API_MASTERS_LIST = BASE_URL + "/masters/sgks_app_master" + API_CITY; // + current city

    //http://sgksapi.woxi.co.in/v1/sgksmain/committee/memberlists?city=PUNE&id=57ee08a472bdde3a2c635942
    public static final String API_COMMITTEE_DETAILS = BASE_URL + "/sgksmain/committee/memberlists?city="; //add city and committee ID

    ///sgksmain/messages/messages_lazy?page=1
    public static final String API_MESSAGE_LAZY_LOADING_LIST = BASE_URL + "/sgksmain/messages/messages_lazy?page=1";

    ///sgksmain/localstorage/offline
    public static final String API_LOCAL_DATA_SYNC = BASE_URL + "/sgksmain/localstorage/offline";

    //sgksmain/accounts/lists?city=PUNE
    public static final String API_ACCOUNT_DEATILS = BASE_URL + "/sgksmain/accounts/lists?city=";

    ///masters/sgks_event_master?city=PUNE
    public static final String API_EVENT_YEAR_LIST = BASE_URL + "/masters/sgks_event_master?city=";
    ///sgksmain/events/lists?city=PUNE
    public static final String API_EVENT_DETAILS_LIST = BASE_URL + "/sgksmain/events/lists?city=";  //current year

    //http://api.sgks.co.in/v1/sgksmain/classified/classified_list?page=1
    public static final String API_Classified_LAZY_LOADING_LIST = BASE_URL + "/sgksmain/classified/classified_list?page=1";
}
