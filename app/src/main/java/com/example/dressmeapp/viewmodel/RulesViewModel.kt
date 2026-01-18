package com.example.dressmeapp.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dressmeapp.data.AppDatabase
import com.example.dressmeapp.data.RulesRepository
import com.example.dressmeapp.enums.RulesEnum
import com.example.dressmeapp.model.Rule
import kotlinx.coroutines.launch



class RulesViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val dao = db.rulesDao()

    private val repo: RulesRepository

    val allRules by lazy { repo.allRules }

    init {
        val db = AppDatabase.getInstance(application)
        repo = RulesRepository(db.rulesDao())
    }

    val colorRules: LiveData<List<Rule>> = dao.getByType(RulesEnum.COLORS.label)
    val clothesRules: LiveData<List<Rule>> = dao.getByType(RulesEnum.CLOTHES.label)

    fun addColorRule(color1: String, color2: String) {
        viewModelScope.launch {
            dao.insert(Rule(type = RulesEnum.COLORS.label, first = color1, second = color2))
        }
    }

    fun addClothesRule(item1: String, item2: String) {
        viewModelScope.launch {
            dao.insert(Rule(type = RulesEnum.CLOTHES.label, first = item1, second = item2))
        }
    }

    fun deleteRule(rule: Rule) {
        viewModelScope.launch {
            dao.delete(rule)
        }
    }
}
