package com.example.fragmentapp.utils

import android.content.Context
import android.content.SharedPreferences

object FavoriteManager {
    private const val PREF_NAME = "crypto_favorites"
    private const val KEY_FAVORITES = "favorite_ids"
    private const val KEY_MEMO_PREFIX = "memo_"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getFavorites(context: Context): Set<String> {
        return getPreferences(context).getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    fun addFavorite(context: Context, cryptoId: String) {
        val favorites = getFavorites(context).toMutableSet()
        favorites.add(cryptoId)
        getPreferences(context).edit().putStringSet(KEY_FAVORITES, favorites).apply()
    }

    fun removeFavorite(context: Context, cryptoId: String) {
        val favorites = getFavorites(context).toMutableSet()
        favorites.remove(cryptoId)
        getPreferences(context).edit().putStringSet(KEY_FAVORITES, favorites).apply()
    }

    fun isFavorite(context: Context, cryptoId: String): Boolean {
        return getFavorites(context).contains(cryptoId)
    }

    fun toggleFavorite(context: Context, cryptoId: String): Boolean {
        return if (isFavorite(context, cryptoId)) {
            removeFavorite(context, cryptoId)
            false
        } else {
            addFavorite(context, cryptoId)
            true
        }
    }

    fun saveMemo(context: Context, cryptoId: String, memo: String) {
        getPreferences(context).edit()
            .putString(KEY_MEMO_PREFIX + cryptoId, memo)
            .apply()
    }

    fun getMemo(context: Context, cryptoId: String): String {
        return getPreferences(context).getString(KEY_MEMO_PREFIX + cryptoId, "") ?: ""
    }

    fun deleteMemo(context: Context, cryptoId: String) {
        getPreferences(context).edit()
            .remove(KEY_MEMO_PREFIX + cryptoId)
            .apply()
    }

    fun hasMemo(context: Context, cryptoId: String): Boolean {
        return getMemo(context, cryptoId).isNotEmpty()
    }
}
