package com.example.finpocket.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryItem(
    val category: String,
    val name: String,
    val amount: Int,
    val icon: Int,
    val date: String
) : Parcelable
