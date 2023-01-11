package com.example.todoapp.model.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var isCompleted: Boolean,
    var text: String,
    var isFavorite: Boolean
) : Parcelable