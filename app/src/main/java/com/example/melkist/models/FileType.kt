package com.example.melkist.models

data class FileType(
    val id: Int,
    val title: String
)

data class FileTypes(
    val seeker: FileType = FileType(id = 1, title = "خواهان"),  // changes might effect layout_item_list_my_file  file
    val owner: FileType = FileType(id = 2, title = "مالک") // changes might effect layout_item_list_my_file  file
)