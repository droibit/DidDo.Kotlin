package com.droibit.diddo.models

import com.activeandroid.Model
import java.io.Serializable
import java.util.Date
import com.activeandroid.annotation.Table
import com.activeandroid.annotation.Column

/**
 * [[UserActivity]]の詳細情報。
 * 活動記録とそのコメント情報を格納する。
 *
 * @auther kumagai
 * @since 15/03/07
 */
Table(name = ActivityDate.TABLE)
public class ActivityDate : Model(), Serializable {

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