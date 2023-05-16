package com.exzell.composenote.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
        val id: Long = -1,

        val title: String = "",

        val body: String = "",

        val createdAt: Long = System.currentTimeMillis(),

        val updatedAt: Long = System.currentTimeMillis(),

        val colorFirst: Long = -1,

        val colorSecond: Long = -1
) : Parcelable
