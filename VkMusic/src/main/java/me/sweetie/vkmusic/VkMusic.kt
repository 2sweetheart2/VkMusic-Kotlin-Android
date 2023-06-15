package me.sweetie.vkmusic

import me.sweetie.vkmusic.exceptions.EmptyLoginAndPassword
import me.sweetie.vkmusic.exceptions.EmptyTokenOrSecret
import me.sweetie.vkmusic.exceptions.VkApiError
import me.sweetie.vkmusic.https.HTTPSRequests
import me.sweetie.vkmusic.interfaces.Callback
import me.sweetie.vkmusic.interfaces.GenerateMD5
import me.sweetie.vkmusic.interfaces.IPlayList
import me.sweetie.vkmusic.interfaces.IPutMD5
import me.sweetie.vkmusic.objects.PlayList
import org.json.JSONObject

/**
 * класс для получения музыки из Вк без реклам и прочей фигни
 *
 * <a>
 *
 * a class for getting music from VK without ads and other stuff
 *
 * @author 2sweetheart2
 * @see <a href="https://github.com/2sweetheart2/VkMusic-Kotlin-Android">Git</a>
 */
open class VkMusic(deviceId: String) {
    var login: String? = null
    var password: String? = null

    var token: String? = null
    var secret: String? = null

    var clientSecret: String = "hHbZxrka2uZ6jB1inYsH"
    var clientId: Int = 2274003

    var deviceId: String

    private val version = "5.95"

    init {
        this.deviceId = deviceId
    }

    /**
     * Эту функцию нужно вызывать первой, после создания класс, а иначе как же
     * получать музыку пользователя без логина и пароля.
     * <a>
     *
     * This function should be called first, after creating the class, otherwise how
     * receive user's music without login and password.
     * @param login логин пользователя (телефон или почта) / user login (telephone or email)
     */
    fun setAuthParams(login: String, password: String) {
        this.login = login
        this.password = HTTPSRequests.iamsoscary(password)
    }

    /**
     * Если у вас уже есть токен и secret, то зачем их получать снова, если можно сразу получать музыку
     * <a>
     *
     * If you already have a token and secret, then why get them again if you can get music right away
     */
    fun setToken(token: String, secret: String) {
        this.token = token
        this.clientSecret = secret
    }

    /**
     * Можете создать своё Standalone приложение и передать ID приложения и его Защищённый ключ
     * смотрите подробнее Vk API.
     * <a>
     *
     * You can create your own Standalone application and transfer the application ID and its Secure Key
     * see Vk API details.
     * @see <a href="https://dev.vk.com/api/direct-auth">VK API direct-auth</a>
     */
    fun setCustomStandaloneInfo(secret: String, clientId: Int) {
        this.secret = secret
        this.clientId = clientId
    }

    /**
     * функция получения token и secret необходимые для обращения к Vk API
     * <a>
     *
     * the function of obtaining the token and secret required to access the Vk API
     * @param callback - вызывается, когда получен ответ от сервера вк./called when a response is received from the vk server.
     */
    fun loginUser(callback: Callback?) {
        if (token == null && secret == null)
            tryLoginByLogAndPas(callback)
    }

    /**
     * запрос к серверу вк для получения token и secret
     * <a>
     *
     * request to the vk server to get token and secret
     * @throws EmptyLoginAndPassword
     */
    private fun tryLoginByLogAndPas(callback: Callback?) {
        if (login == null && password == null)
            throw EmptyLoginAndPassword("login and password is null")
        val params = getAuthParams()
        HTTPSRequests.sendPost("https://oauth.vk.com/token?$params", JSONObject(), object : Callback {
            override fun getJson(jsonObject: JSONObject) {
                if (jsonObject.has("error"))
                    throw VkApiError(jsonObject.toString())
                this@VkMusic.secret = jsonObject.getString("secret")
                this@VkMusic.token = jsonObject.getString("access_token")

                if (callback != null)
                    callback.getJson(jsonObject)
            }
        })
    }


    /**
     * основная функция для получения аудиозапсей и остальных взаимодействий с Vk APi Music
     * <a>
     *
     * the main function for receiving audio recordings and other interactions with Vk APi Music
     *
     * @param callbackMD5 нужнен, чтобы получить MD5, необоходимую для вк / this is necessary to get MD5, what is needed for vkontakte. Called when you need to send a request that needs MD5
     * @param callback Callback, который возвращает объект PlayList / Callback who return PlayList object
     * @param params доп. параметры для запроса. / additional parameters for the request. For example: "count=6000","owner_id=123...." etc.
     *
     * @see GenerateMD5
     * @see IPlayList
     */
    fun getAudio(callbackMD5: GenerateMD5,callback: IPlayList,vararg params: String) {
        sendMethod("audio.get",callbackMD5,callback,*params)
    }

    /**
     * Если хотите ещё по взаимодействовать с Vk API
     * <a>
     *
     * If you want more software to interact with Vk API
     *
     * @param method имя метода, подробнее смотрите VK API Methods
     * @see <a href="https://dev.vk.com/method">Vk API Methods</a>
     *
     * @param callbackMD5 нужнен, чтобы получить MD5, необоходимую для вк / this is necessary to get MD5, what is needed for vkontakte. Called when you need to send a request that needs MD5
     * @param callback Callback, который возвращает объект PlayList / Callback who return PlayList object
     * @param params доп. параметры для запроса. / additional parameters for the request. For example: "count=6000","owner_id=123...." etc.
     *
     * @see GenerateMD5
     * @see IPlayList
     */
    fun sendMethod(method: String, callbackMD5: GenerateMD5,callback: IPlayList, vararg params: String) {
        if(token == null || secret == null)
            throw EmptyTokenOrSecret("token or secret is null")
        val url = "/method/$method?"
        send(url, callbackMD5, callback, *params)
    }


    /**
     * отправка запроса на сервер вк, получение ответа (Если был вызван метод audio.get, то IPlayList вернёт объект
     * класс PlayList и JSONObject, который пришёл, Если был вызван другой метод, то IPlayList вернёт null в playList и
     * JSONObject, который пришёл
     * <a>
     *
     * sending a request to the vk server, receiving a response (If the audio.get method was called, the Playlist will return an object
     * the PlayList class and the JSONObject that came in, If another method was called, the IPlayList will return null to the playList and
     * JSONObject that came in
     *
     * @see IPlayList
     * @see <a href="https://dev.vk.com/method">Vk API</a>
     */
    private fun send(url: String, callbackMD5: GenerateMD5, callback: IPlayList,vararg params: String) {
        var url_ = url
        url_ += "v=$version&device_id=$deviceId&access_token=$token"
        for (s in params) {
            url_ += "&$s"
        }
        callbackMD5.generateURLMD5(url_+secret,object : IPutMD5{
            override fun putMD5(md5: String) {
                val mainURL = "$url_&sig=$md5"
                HTTPSRequests.sendPost(
                    "https://api.vk.com$mainURL",
                    JSONObject(),
                    object : Callback {
                        override fun getJson(jsonObject: JSONObject) {
                            jsonObject.put("token", token)
                            jsonObject.put("secret", secret)
                            if(url.contains("https://api.vk.com/method/audio.get")) {
                                val pl = PlayList(jsonObject.getJSONObject("response"))
                                callback.getPlayList(pl,jsonObject)
                            }
                            else
                                callback.getPlayList(null,jsonObject)
                        }
                    })
            }
        })


    }


    /**
     * эта функция нужна просто чтобы создать строку параметров URL для получения токена и секрета
     * <a>
     *
     * this function is needed simply to create a string of URL parameters to get a token and a secret
     */
    fun getAuthParams(): String {
        return "grant_type=password&scope=nohttps,audio,offline&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH&validate_token=true&username=$login&password=$password"
    }

}