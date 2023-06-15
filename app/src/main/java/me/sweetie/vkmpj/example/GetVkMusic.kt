package me.sweetie.vkmpj.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import me.sweetie.vkmpj.R
import me.sweetie.vkmusic.VkMusic
import me.sweetie.vkmusic.https.HTTPSRequests
import me.sweetie.vkmusic.interfaces.Callback
import me.sweetie.vkmusic.interfaces.GenerateMD5
import me.sweetie.vkmusic.interfaces.IPlayList
import me.sweetie.vkmusic.interfaces.IPutMD5
import me.sweetie.vkmusic.objects.PlayList
import org.json.JSONException
import org.json.JSONObject

class GetVkMusic: AppCompatActivity() {

    /**
     * create activity and get PlayList
     */
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // device id need for vk api
        val deviceId = Settings.Secure.getString(
            contentResolver, Settings.Secure.ANDROID_ID
        )

        val vk = VkMusic(deviceId)
        // set auth params for get token and secret
        vk.setAuthParams("user login", "user password")
        // getting a token and secret processing actions when we receive them
        vk.loginUser(object : Callback {
            //this json unusable, because we only need to handle event when we got token and secret
            override fun getJson(jsonObject_: JSONObject) {
                // call method for get audio

                // GenerateMD5 called when the Class VkMusic needed MD5. You need to create MD5 from first parametr and
                // return MD5 by interface IPutMD5
                vk.getAudio(object : GenerateMD5 {
                    override fun generateURLMD5(parametrs: String, param: IPutMD5) {
                        //GENERATE MD5
                        val md5 = "your md5"
                        param.putMD5(md5)
                    }

                }, //this interface is called when we get the playlist
                    object : IPlayList {
                    override fun getPlayList(playList: PlayList?, jsonObject: JSONObject?) {
                        println("PLAY LIST\nCOUNT: ${playList!!.count} ${playList.audios[0].artist}")
                    }
                },//there you can set any parameters
                "count=6000")
            }
        })
    }

}