package com.example.lesson80taskermvc.database.categories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "category_name")
    var categoryName: String,
    @ColumnInfo(name = "category_color")
    var categoryColor: Int
)