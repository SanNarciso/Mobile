package com.example.todoapp.model.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Task(
    val id: UUID = UUID.randomUUID(),
    var isCompleted: Boolean,
    var text: String,
    var additionalInfo: String,
    var isFavorite: Boolean
) : Parcelable