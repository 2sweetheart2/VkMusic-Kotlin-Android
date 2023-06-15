package me.sweetie.vkmusic.objects

import org.json.JSONObject

class PlayList(payload: JSONObject) {
    var count = 0
    var audios:ArrayList<Audio> = ArrayList()

    init {
        count = payload.getInt("count")
        val ar = payload.getJSONArray("items")
        for(i in 0 until ar.length()){
            audios.add(Audio(ar.getJSONObject(i)))
        }
    }
}