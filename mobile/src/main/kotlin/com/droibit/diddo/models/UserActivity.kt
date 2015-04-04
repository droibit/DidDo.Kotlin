package com.droibit.diddo.models

import android.content.Context
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select
import com.droibit.diddo.R
import java.io.Serializable
import java.util.Comparator
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * ユーザ定義の活動情報を格納するクラス
 *
 * @auther kumagai
 * @since 15/03/07
 */
Table(name = UserActivity.TABLE)
public data class UserActivity(): Model(), Serializable {

    companion object {
        val TABLE = "activity";
        val NAME = "name";
        val RECENTLY_DATE = "recently_date";

        val SORT_NAME = 0
        val SORT_ACTIVITY_DATE = 1

        val DAYS_LIMIT = 99L

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

    /** 新規作成されたアクティビティかどうか */
    public val isNew: Boolean
        get() = getId() == null

    /** 活動の詳細情報を取得する */
    public val details: MutableList<ActivityDate>
        get() = getMany(javaClass<ActivityDate>(), ActivityDate.ACTIVITY)

    /**
     * 現在から活動日までの経過日を取得する
     */
    public fun getElapsedDateFromNow(context: Context): String {
        val duration = System.currentTimeMillis() - recentlyDate.getTime()
        val count = duration / TimeUnit.DAYS.toMillis(1)

        return if (count == 0L) {
            context.getString(R.string.text_elapsed_zero)
        } else {
            // 100日超えたら+表示にする。
            if (count > DAYS_LIMIT) {
                context.getString(R.string.text_over)
            } else {
                context.getString(R.string.text_elapsed_format_short, count.toString())
            }
        }
    }

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

/**
 * アクティビティのリストを取得する。
 */
public fun loadUserActivities(): MutableList<UserActivity> = Select().from(javaClass<UserActivity>()).execute()