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

const val SEEKER_ITEM_TYPE = 0
const val OWNER_ITEM_TYPE = 1
const val CAT_TYPE = 0
const val SUB_CAT_TYPE = 1

const val EMPTY_CATEGORY_ID = -1
const val AGE_FROM_TAG = "age from"

