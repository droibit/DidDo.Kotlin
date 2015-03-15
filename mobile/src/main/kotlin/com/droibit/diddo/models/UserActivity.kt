package com.droibit.diddo.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.Date
import java.io.Serializable

/**
 * ユーザ定義の活動情報を格納するクラス
 *
 * @auther kumagai
 * @since 15/03/07
 */
Table(name = UserActivity.TABLE)
public class UserActivity(): Model(), Serializable {

    class object {
        val TABLE = "activity";
        val NAME = "name";
        val RECENTLY_DATE = "recently_date";
    }

    /** 活動名 */
    Column(name = NAME)
    public var name: String? = null
    /** 最新の活動日 */
    Column(name = RECENTLY_DATE)
    public var recentlyDate: Date? = null

    /** 活動の詳細情報を取得する */
    public val details: List<ActivityDetail>
    get() = getMany(javaClass<ActivityDetail>(), ActivityDetail.ACTIVITY)
}