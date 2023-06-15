package me.sweetie.vkmusic.interfaces

import org.json.JSONObject

interface Callback {
    fun getJson(jsonObject: JSONObject)
}