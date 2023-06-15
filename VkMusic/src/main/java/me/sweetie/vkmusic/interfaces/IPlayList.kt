package me.sweetie.vkmusic.interfaces

import me.sweetie.vkmusic.objects.PlayList
import org.json.JSONObject

interface IPlayList {
    fun getPlayList(playList: PlayList?,jsonObject: JSONObject?)
}