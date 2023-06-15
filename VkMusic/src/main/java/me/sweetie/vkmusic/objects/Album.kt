package me.sweetie.vkmusic.objects

import org.json.JSONObject

class Album(payload:JSONObject) {
    var id = -1
    var title:String
    var ownerId = -1
    var accessKey:String
    var thumb: Thumb? = null

    init {
        id = payload.getInt("id")
        title = payload.getString("title")
        ownerId = payload.getInt("owner_id")
        accessKey = payload.getString("access_key")
        if(payload.has("thumb"))
            thumb = Thumb(payload.getJSONObject("thumb"))
    }

}