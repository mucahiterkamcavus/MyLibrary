package com.bandirma.entity

data class AuthorBooks(val id : Integer,
    val author: Author,
    val book : Book,
    val isTranslated: Boolean)
