package com.example.cloudmusic.utils.webs

object WebConstant {

    /**
     * Api根地址
     */
    const val BASE_URL = "http://10.36.65.63:3000"

    /**
     * 登录的接口地址
     */
    object Login{
        const val API_LOGIN = "login/cellphone"
    }

    /**
     * 登出的接口地址
     */
    object Logout{
        const val API_LOGOUT = "logout"
    }

    /**
     * 获取音乐Url
     */
    object MusicUrl{
        const val API_MUSIC_URL = "song/url"
    }

    /**
     * 每日推荐歌曲
     */
    object DailyRecommendSongs{
        const val API_DAILY_RECOMMEND_SONGS = "recommend/songs"
    }

    /**
     * Banner
     */
    object Banner{
        const val API_BANNER = "banner"
    }

    /**
     * Personalized
     */
    object Personalized{
        const val API_PERSONALIZED = "personalized"
    }
}