package com.bandirma.entity

data class Book(
    val id : Int,
    val name : String,
    val barcode : String,
    val year : String,
    val pageCount : Int,
    val PublishingHouseId : Int?,
    val abstract : String,
    val coverPhoto : String,
    val isTranslated : Boolean,
    val categories : List<Category>?,
    val publishingHouse: PublishingHouse?
)