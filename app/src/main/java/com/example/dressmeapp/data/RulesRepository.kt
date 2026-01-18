package com.example.dressmeapp.data

import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Rule

class RulesRepository(private val dao: RulesDao) {
    val allRules = dao.getAllRules()
    suspend fun insert(item: Rule) = dao.insert(item)
    suspend fun delete(item: Rule) = dao.delete(item)
}
