package com.droibit.diddo.models

import android.content.Context
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.droibit.diddo.R
import java.io.Serializable
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * [[UserActivity]]の詳細情報。
 * 活動記録とそのコメント情報を格納する。
 *
 * @auther kumagai
 * @since 15/03/07
 */
Table(name = ActivityDate.TABLE)
public data class ActivityDate : Model(), Serializable {

    companion object {
        val TABLE = "activity_detail"
        val ACTIVITY = "activity"
        val DATE = "date"
        val MEMO = "memo"
    }

    /** 活動日 */
    Column(name = DATE)
    public var date: Date = Date()
    /** 活動のメモ */
    Column(name = MEMO)
    public var memo: String? = null
    /** 親のアクティビティ */
    Column(name = ACTIVITY)
    public var activity: UserActivity? = null

    /** 新規作成された活動日かどうか */
    public val isNew: Boolean
        get() = getId() == null

    /** {@inheritDoc} */
    override fun toString(): String {
        return memo ?: "---"
    }

    /**
     * 現在から活動日までの経過日を取得する
     */
    public fun getElapsedDateFromNow(context: Context): String {
        val now = Date(System.currentTimeMillis())
        val duration = now.getTime() - date.getTime()
        val count = duration / TimeUnit.DAYS.toMillis(1)

        return if (count == 0L) {
                    val nc = Calendar.getInstance()
                    val dc = Calendar.getInstance()
                    nc.setTime(now)
                    dc.setTime(date)
                    // 日をまたいでも1日経過していない場合
                    if (nc.get(Calendar.DAY_OF_WEEK) == dc.get(Calendar.DAY_OF_WEEK))
                        context.getString(R.string.text_today)
                    else
                        context.getString(R.string.text_next_day)
               } else {
                    // 100日超えたら+表示にする。
                    if (count > UserActivity.DAYS_LIMIT)
                        context.getString(R.string.text_elapsed_format, context.getString(R.string.text_over))
                    else
                        context.getString(R.string.text_elapsed_format, count.toString())
               }
    }
}

/**
 * 新しい[[ActivityDate]]インスタンスを作成するためのヘルパー関数。
 */
public fun newActivityDate(init: ActivityDate.() -> Unit): ActivityDate {
    val activityDate = ActivityDate()
    activityDate.init()
    return activityDate
}

/**
 * ダミー用の[[ActivityDate]]インスタンスを作成するためのヘルパー関数。
 */
public fun dummyActivityDate(date: Date): ActivityDate {
    return newActivityDate {
        this.date = date
    }
}