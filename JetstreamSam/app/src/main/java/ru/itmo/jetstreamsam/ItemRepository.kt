package ru.itmo.jetstreamsam

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope

class ItemRepository(context: Context, scope: CoroutineScope) {
    private val itemDao : ItemDao
    val allItems : LiveData<List<Item>>

    init {
        val database: ItemDatabase = ItemDatabase.getDataBase(context, scope)
        itemDao = database.ItemDao()
        allItems = itemDao.allItems()
    }

    fun insert(item: Item) {
        itemDao.insert(item)
    }

    fun update(item: Item) {
        itemDao.update(item)
    }

    fun delete(item: Item) {
        itemDao.delete(item)
    }
}