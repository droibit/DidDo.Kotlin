package com.droibit.diddo.models.dummy

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * A dummy item representing a piece of content.
     */
    public class DummyItem(public var id: String, public var content: String) {

        override fun toString(): String {
            return content
        }
    }

    class object {

        /**
         * An array of sample (dummy) items.
         */
        public val ITEMS: MutableList<DummyItem> = arrayListOf(
                DummyItem("1", "Item 1"),
                DummyItem("2", "Item 2"),
                DummyItem("3", "Item 3")
        )

        /**
         * A map of sample (dummy) items, by ID.
         */
        public var ITEM_MAP: MutableMap<String, DummyItem> = hashMapOf(
                "1" to DummyItem("1", "Item 1"),
                "2" to DummyItem("2", "Item 2"),
                "3" to DummyItem("3", "Item 3")
        )
    }
}
