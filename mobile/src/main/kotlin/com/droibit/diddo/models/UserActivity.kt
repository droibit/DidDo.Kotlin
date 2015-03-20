package com.droibit.diddo.models

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import java.util.Date
import java.io.Serializable
import java.util.Comparator

/**
 * ユーザ定義の活動情報を格納するクラス
 *
 * @auther kumagai
 * @since 15/03/07
 */
Table(name = UserActivity.TABLE)
public class UserActivity(): Model(), Serializable {

    companion object {
        val TABLE = "activity";
        val NAME = "name";
        val RECENTLY_DATE = "recently_date";

        val SORT_NAME = 0
        val SORT_ACTIVITY_DATE = 1

        /**
         * アクティビティ名でソートする
         */
        class NameComarator: Comparator<UserActivity> {
            override fun compare(lhs: UserActivity, rhs: UserActivity): Int = lhs.name!!.compareTo(rhs.name!!)
        }

        /**
         * 日付でソートする
         */
        class DateComarator: Comparator<UserActivity> {
            override fun compare(lhs: UserActivity, rhs: UserActivity): Int = lhs.recentlyDate.compareTo(rhs.recentlyDate)
        }

        /**
         * ソートするための[Comparator]インスタンスを取得する
         */
        fun getComparator(type: Int): Comparator<UserActivity>? {
            when (type) {
                SORT_NAME          -> return NameComarator()
                SORT_ACTIVITY_DATE -> return DateComarator()
                else               -> return null
            }
        }
    }

    /** 活動名 */
    Column(name = NAME)
    public var name: String? = null
    /** 最新の活動日 */
    Column(name = RECENTLY_DATE)
    public var recentlyDate: Date = Date()
    /** 最新の活動日をミリ秒で取得する */
    public val recentlyDateMillis: Long
    get() = recentlyDate.getTime()

    /** 新規作成されたアクティビティかどうか */
    public val isNew: Boolean
        get() = getId() == null

    /** 活動の詳細情報を取得する */
    public val details: List<ActivityDate>
        get() = getMany(javaClass<ActivityDate>(), ActivityDate.ACTIVITY)

    /** {@inheritDoc} */
    override fun toString(): String {
        return name ?: ""
    }
}

/**
 * 新しい[ActivityDate]インスタンスを作成するためのヘルパー関数。
 */
public fun newUserActivity(init: UserActivity.() -> Unit): UserActivity {
    val userActivity = UserActivity()
    userActivity.init()
    return userActivity
}

/**
 * ダミー用の[ActivityDate]インスタンスを作成する。
 */
public fun dummyActivity(name: String, date: Date = Date()): UserActivity {
    return newUserActivity {
        this.name = name
        this.recentlyDate = date
    }
}