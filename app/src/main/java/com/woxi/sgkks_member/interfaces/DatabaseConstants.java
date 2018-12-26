package com.woxi.sgkks_member.interfaces;

/**
 * <b><b>public interface DatabaseConstants </b></b>
 * <p>This class is used to store constants related to local database</p>
 * Created by Rohit.
 */

public interface DatabaseConstants {
    //Database Info
    String DATABASE_NAME = "sgks_database.db";
    int DATABASE_VERSION = 2;

    //Family Details Table
    String TABLE_FAMILY_DETAILS = "table_families";
    String COLUMN_FAMILY_ID_PRIMARY = "family_id_primary";
    String COLUMN_FAMILY_SGKS_FAMILY_ID = "sgks_family_id";
    String COLUMN_FAMILY_SURNAME = "surname";
    String COLUMN_FAMILY_NATIVE_PLACE = "native_place";
    String COLUMN_FAMILY_CITY = "family_city";

    //Member's Details Table
    String TABLE_MEMBER_DETAILS_EN = "table_members_en";
    String TABLE_MEMBER_DETAILS_GJ = "table_members_gj";

    //    String COLUMN_MEMBER_PRIMARY_INDEX = "member_index_primary";
    String COLUMN_MEMBER_ID_PRIMARY_EN = "member_id_primary_en";
    String COLUMN_MEMBER_ID_PRIMARY_GJ = "member_id_primary_gj";
    String COLUMN_MEMBER_ID_FOREIGN_KEY = "member_id_foreign";
    String COLUMN_MEMBER_FAMILY_ID_FOREIGN_KEY = "family_id_foreign";
    String COLUMN_MEMBER_SGKS_MEMBER_ID = "sgks_mem_id";
    String COLUMN_MEMBER_SGKS_FAMILY_ID = "sgks_family_id";
    String COLUMN_MEMBER_FIRST_NAME = "first_name";
    String COLUMN_MEMBER_MIDDLE_NAME = "middle_name";
    String COLUMN_MEMBER_LAST_NAME = "last_name";
    String COLUMN_MEMBER_GENDER = "gender";
    String COLUMN_MEMBER_MOBILE = "mobile_number";
    String COLUMN_MEMBER_EMAIL = "email_id";
    String COLUMN_MEMBER_BLOOD_GROUP = "blood_group";
    String COLUMN_MEMBER_BLOOD_GROUP_ID = "blood_group_id";
    String COLUMN_MEMBER_MARITAL_STATUS = "marital_status";
    String COLUMN_MEMBER_SGKS_AREA = "sgks_area";
    String COLUMN_MEMBER_LATITUDE = "latitude";
    String COLUMN_MEMBER_LONGITUDE = "longitude";
    String COLUMN_MEMBER_IMAGE_URL = "image_url";
    String COLUMN_MEMBER_SGKS_CITY = "sgks_city";
    String COLUMN_MEMBER_SGKS_CITY_ID = "sgks_city_id";
    String COLUMN_MEMBER_ADDRESS = "address";
    String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    String COLUMN_MEMBER_IS_ACTIVE = "is_active";
    String COLUMN_LANGUAGE_ID = "language_id";

    //Member's Address Table
    String TABLE_MEM_ADDRESS_DETAILS = "table_member_address";
    String COLUMN_ADDRESS_PRIMARY_INDEX = "address_primary_index";
    String COLUMN_ADDRESS_FAMILY_ID_FOREIGN_KEY = "family_id_foreign";
    String COLUMN_ADDRESS_ADDRESS_LINE = "address_line";
    String COLUMN_ADDRESS_AREA = "address_area";
    String COLUMN_ADDRESS_LANDMARK = "landmark";
    String COLUMN_ADDRESS_CITY = "address_city";
    String COLUMN_ADDRESS_PINCODE = "pincode";
    String COLUMN_ADDRESS_STATE = "state";
    String COLUMN_ADDRESS_COUNTRY = "country";
    String COLUMN_ADDRESS_ADDRESS_ID = "address_id";

    //Committee Details Table
    String TABLE_COMMITTEE_DETAILS = "table_committee";
    //    String COLUMN_COMMITTEE_PRIMARY_INDEX = "committee_index_primary";
    String COLUMN_COMMITTEE_ID_PRIMARY = "committee_id_primary";
    String COLUMN_COMMITTEE_NAME = "committee_name";
    String COLUMN_COMMITTEE_DESCRIPTION = "committee_description";
    String COLUMN_COMMITTEE_CITY = "committee_city";
    String COLUMN_COMMITTEE_MEMBERS = "committee_members";
    String COLUMN_COMMITTEE_IS_ACTIVE = "is_active";

    //Message/News Details Table
    String TABLE_MESSAGE_NEWS_DETAILS = "table_messages_en";
    String TABLE_MESSAGE_NEWS_DETAILS_GJ = "table_messages_gj";
    //    String COLUMN_MESSAGES_PRIMARY_INDEX = "message_index_primary";
    String COLUMN_MESSAGES_ID_PRIMARY = "message_id_primary";
    String COLUMN_MESSAGES_ID_PRIMARY_GJ = "message_id_primary";
    String COLUMN_MESSAGES_ID_FOREIGN = "message_id_foreign";
    String COLUMN_MESSAGES_TITLE = "message_title";
    String COLUMN_MESSAGES_DESCRIPTION = "message_description";
    String COLUMN_MESSAGES_IMAGE_URL = "message_image_url";
    String COLUMN_MESSAGES_CREATED_DATE = "message_date";
    String COLUMN_MESSAGES_TYPE = "message_type";
    String COLUMN_MESSAGES_TYPE_ID = "message_type_id";
    String COLUMN_MESSAGES_CITY = "message_city";
    String COLUMN_MESSAGES_CITY_ID = "message_city_id";
    String COLUMN_MESSAGES_IS_ACTIVE = "is_active";
    String COLUMN_MESSAGE_LANGUAGE_ID = "langauge_id";


    //TABLES CITIES
    String TABLE_CITIES_GJ = "table_cities_gj";
    String TABLE_CITIES_EN = "table_cities_en";

    //COLUMN CITIES TABLE
    String COLUMN_CITY_ID_PRIMARY = "city_id";
    String COLUMN_CITY_NAME = "city_name";
    String COLUMN_STATE_ID = "state_id";
    String COLUMN_CITY_IS_ACTIVE = "is_active";
    String COLUMN_CITY_ID_FOREIGN = "city_id_foreign";
    String COLUMN_CITY_LANGUAGE_ID = "language_id";

    //TABLE EVENTS
    String TABLE_EVENTS_EN = "table_events_en";
    String TABLE_EVENTS_GJ = "table_events_gj";
    String TABLE_EVENT_IMAGES = "table_events_images";

    //COLUMN EVENTS TABLE
    String COLUMN_EVENT_ID_PRIMARY_KEY = "id_primary_key";
    String COLUMN_EVENTS_ID_FOREIGN_KEY = "id_foreign_key";
    String COLUMN_EVENT_NAME = "event_name";
    String COLUMN_EVENT_DESCRIPTION = "event_descp";
    String COLUMN_EVENT_VENUE = "event_venue";
    String COLUMN_EVENT_DATE = "event_date";
    String COLUMN_EVENT_IS_ACTIVE = "is_active";
    String COLUMN_EVENT_CITY = "city";
    String COLUMN_EVENT_CITY_ID = "city_id";
    String COLUMN_EVENT_IMAGE_URL = "image_url";
    String COLUMN_IMAGE_ID_PRIMARY_KEY = "image_id";
    String COLUMN_EVENT_LANGUAGE_ID = "language_id";
    String COLUMN_EVENT_YEAR = "year";


    //TABLE CLASSIFIED
    String TABLE_CLASSIFIED_EN = "table_classified_en";
    String TABLE_CLASSIFIED_GJ = "table_classified_gj";
    String COLUMN_CLASSIFIED_ID_PRIMARY_KEY = "id";
    String COLUMN_CLASSIFIED_ID_PRIMARY_KEY_GJ = "id";
    String COLUMN_CLASSIFIED_ID_FOREIGN_KEY = "foreign_id";
    String COLUMN_CLASSIFIED_TITLE = "title";
    String COLUMN_CLASSIFIED_DESCRIPTION = "description";
    String COLUMN_CLASSIFIED_IS_ACTIVE = "is_active";
    String COLUMN_CLASSIFIED_CITY = "city";
    String COLUMN_CLASSIFIED_CITY_ID = "city_id";
    String COLUMN_CLASSIFIED_CREATED_AT = "created_at";

}
