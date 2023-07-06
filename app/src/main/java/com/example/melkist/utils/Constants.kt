package com.example.melkist.utils

enum class ApiStatus { LOADING, ERROR, DONE }
enum class ItemType {CHOOSE, SEEKER, OWNER, ALL}

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
const val FILTER_RESULT_KEY = "filter_result"

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

const val TYPE_OPTIONS_TAG = "type_options"
enum class Cr {PROVINCE, CITY, REGION_1, REGION_2, REGION_3}
const val PROVINCE = 101
const val CITY = 102
const val REGION_1 = 103
const val REGION_2 = 104
const val REGION_3 = 105

const val INBOX = 0
const val OUTBOX = 1
