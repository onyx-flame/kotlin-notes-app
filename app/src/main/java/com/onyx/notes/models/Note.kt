package com.onyx.notes.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize


@Entity(tableName = "notes")
@Parcelize
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val noteTitle: String,
    val noteBody: String,
) : Parcelable

@Entity(tableName = "hashtags")
@Parcelize
data class Hashtag (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var noteId: Int,
    val text: String
) : Parcelable


data class NoteWithHashTags (
    @Embedded
    val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val hashtags: List<Hashtag>
)