package com.example.lesson80taskermvc.database.categories

import androidx.room.*

@Dao
interface CategoryDao {
    @Insert
    fun addCategory(category: CategoryEntity)

    @Update
    fun updateCategory(category: CategoryEntity)

    @Delete
    fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM category_table")
    fun getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM category_table WHERE id = :id")
    fun getCategoryById(id: Long): CategoryEntity
}