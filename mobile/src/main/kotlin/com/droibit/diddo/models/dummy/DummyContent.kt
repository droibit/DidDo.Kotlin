package com.droibit.diddo.models.dummy

import java.util.ArrayList
import java.util.HashMap
import com.droibit.diddo.models.UserActivity
import com.droibit.diddo.models.dummyActivity
import com.droibit.diddo.models.ActivityDate

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    class object {

        /**
         * An array of sample (dummy) items.
         */
        public val ITEMS: MutableList<UserActivity> = arrayListOf(
                dummyActivity("Item 1"),
                dummyActivity("Item 2"),
                dummyActivity("Item 3")
        )

        public val DETAIL_ITEMS: MutableList<ActivityDate> = arrayListOf(
                ActivityDate(),
                ActivityDate(),
                ActivityDate()
        )

        /**
         * A map of sample (dummy) items, by ID.
         */
        public var ITEM_MAP: MutableMap<String, UserActivity> = hashMapOf(
                "1" to dummyActivity("Item 1"),
                "2" to dummyActivity("Item 2"),
                "3" to dummyActivity("Item 3")
        )
    }
}
