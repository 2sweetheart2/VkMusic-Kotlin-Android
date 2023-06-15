package me.sweetie.vkmusic.objects

import org.json.JSONObject

class Audio(payload:JSONObject) {
    var artist:String
    var id:Long = -1
    var title:String
    var duration:Int = -1
    var accessKey:String
    var isExplicit = false
    var isLicensed:Boolean = false
    var trackCode:String
    var url:String
    var date:Long = -1
    var album:Album? = null
    init {
        artist = payload.getString("artist")
        id = payload.getLong("owner_id")
        title = payload.getString("title")
        duration = payload.getInt("duration")
        accessKey = payload.getString("access_key")
        isExplicit = payload.getBoolean("is_explicit")
        isLicensed = payload.getBoolean("is_licensed")
        trackCode = payload.getString("track_code")
        url = payload.getString("url")
        date = payload.getLong("date")
        if(payload.has("album"))
            album = Album(payload.getJSONObject("album"))
    }

}