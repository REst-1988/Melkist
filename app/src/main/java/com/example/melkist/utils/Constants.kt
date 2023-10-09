package com.example.melkist.utils

enum class ApiStatus { LOADING, ERROR, NO_DATA, DONE }
enum class ItemType { CHOOSE, SEEKER, OWNER, ALL }

const val SHIRAZ_CITY_ID = 733
const val USER_AVATAR = "USER_AVATAR"

const val ONE_QUADRILLION = 1_000_000_000_000_000
const val HUNDRED_TRILLION = 100_000_000_000_000
const val TEN_TRILLION = 10_000_000_000_000
const val ONE_TRILLION = 1_000_000_000_000
const val HUNDRED_BILLION = 100_000_000_000
const val TEN_BILLION = 10_000_000_000
const val ONE_BILLION = 1_000_000_000
const val HUNDRED_MILLION = 100_000_000
const val TEN_MILLION = 10_000_000
const val ONE_MILLION = 1_000_000
const val HUNDRED_THOUSAND = 100_000
const val TEN_THOUSAND = 10_000
public const val GalleryPick = 1
public const val CAMERA_REQUEST = 100
public const val STORAGE_REQUEST = 200
public const val IMAGE_PICK_GALLERY_REQUEST = 300
public const val IMAGE_PICK_CAMERA_REQUEST = 400
const val DATA = "data"
const val ITEM_TYPE_KEY = "item_type"
const val ITEM_TYPE_ID_KEY = "item_type_id"
const val CAT_ID_KEY = "csck"
const val CAT_RESULT_KEY = "cat_result"
const val SUB_CAT_RESULT_KEY = "sub_cat_result"
const val CR_KEY = "cr"
const val FILTER_KEY = "filter"
const val FILTER_RESULT_KEY = "filter_result"
const val IS_IN_FAV_LIST_KEY = "is_in_fav_list"
const val CONDITION_FILTER_FAV_LIST = true

const val SEEKER_ITEM_TYPE = 0
const val OWNER_ITEM_TYPE = 1
const val CAT_TYPE = 0
const val SUB_CAT_TYPE = 1

const val EMPTY_CATEGORY_ID = -1
const val AGE_FROM_TAG = "age_from"
const val AGE_TO_TAG = "age_to"
const val SIZE_FROM_TAG = "size_from"
const val SIZE_TO_TAG = "size_to"
const val ROOM_FROM_TAG = "room_from"
const val ROOM_TO_TAG = "room_to1"
const val PRICE_FROM_TAG = "price_from"
const val PRICE_TO_TAG = "price_to"

const val MORTGAGE_FROM_TAG = "mortgage_from"
const val MORTGAGE_TO_TAG = "mortgage_to"
const val RENT_FROM_TAG = "rent_from"
const val RENT_TO_TAG = "rent_to"
const val FLOOR_FROM_TAG = "floor_from"
const val FLOOR_TO_TAG = "floor_to"

const val AGE_TAG = "age"
const val SIZE_TAG = "size"
const val ROOM_TAG = "room"
const val FLOOR_TAG = "room"
const val UNITS_IN_FLOOR = "u in f"
const val UNITS_TAG = "units"
const val ALLAY_TAG = "allay"
const val DIRECTION_TAG = "direction"
const val LIGHTS_TAG = "lights"
const val PRICE_TAG = "price"
const val SUITABLE_FOR_TAG = "suitable_for"
const val PARKING_TAG = "parking"
const val STORE_ROOM_TAG = "store_room"
const val BALCONY_TAG = "balcony"
const val ELEVATOR_TAG = "elevator"
const val ADMIN_DEED_TAG = "administrative_deed"
const val DEED_TYPE_TAG = "deed_type"

const val TYPE_OPTIONS_TAG = "type_options"

enum class Cr { PROVINCE, CITY, REGION_1, REGION_2, REGION_3 }

const val PROVINCE = 101
const val CITY = 102
const val REGION_1 = 103
const val REGION_2 = 104
const val REGION_3 = 105

const val INBOX = 0
const val OUTBOX = 1
const val RECEIVED = 0
const val SENT = 1

const val STATUS_APPROVED = 1
const val STATUS_DENY = 0
const val STATUS_NO_ACTION = -1

val UNKNOWN_ERRORS_LIST = mutableListOf("خطای نامشخص")
const val EXTRA_FILE_DETAIL = "extra_file_detail"
const val MAX_IMAGE_NUMBER_FOR_SAVING_FIVE = 5




