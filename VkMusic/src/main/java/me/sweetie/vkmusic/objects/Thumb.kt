package me.sweetie.vkmusic.objects

import org.json.JSONObject

class Thumb(payload:JSONObject) {
    var width = 0
    var height = 0
    var photo_34:String? = null
    var photo_68:String? = null
    var photo_135:String? = null
    var photo_270:String? = null
    var photo_300:String? = null
    var photo_600:String? = null
    var photo_1200:String? = null

    init {
        width = payload.getInt("width")
        height= payload.getInt("height")
        if(payload.has("photo_34"))
            photo_34 = payload.getString("photo_34")
        if(payload.has("photo_68"))
            photo_68 = payload.getString("photo_68")
        if(payload.has("photo_135"))
            photo_135 = payload.getString("photo_135")
        if(payload.has("photo_270"))
            photo_270 = payload.getString("photo_270")
        if(payload.has("photo_300"))
            photo_300 = payload.getString("photo_300")
        if(payload.has("photo_600"))
            photo_600 = payload.getString("photo_600")
        if(payload.has("photo_1200"))
            photo_1200 = payload.getString("photo_1200")
    }
}