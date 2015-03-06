package com.droibit.diddo

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView


import com.droibit.diddo.dummy.DummyContent
import android.widget.AbsListView

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment : ListFragment() {


    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private var mCallbacks: Callbacks = sDummyCallbacks

    /**
     * The current activated item position. Only used on tablets.
     */
    private var mActivatedPosition = INVALID_POSITION

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public trait Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public fun onItemSelected(id: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: replace with a real list adapter.
        setListAdapter(ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                DummyContent.ITEMS))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION))
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        // Activities containing this fragment must implement its callbacks.
        if (!(activity is Callbacks)) {
            throw IllegalStateException("Activity must implement fragment's callbacks.")
        }

        mCallbacks = activity
    }

    override fun onDetach() {
        super.onDetach()

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks
    }

    override fun onListItemClick(listView: ListView, view: View, position: Int, id: Long) {
        super.onListItemClick(listView, view, position, id)

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mActivatedPosition != INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition)
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public fun setActivateOnItemClick(activateOnItemClick: Boolean) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(if (activateOnItemClick)
            AbsListView.CHOICE_MODE_SINGLE
        else
            AbsListView.CHOICE_MODE_NONE)
    }

    private fun setActivatedPosition(position: Int) {
        if (position == INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false)
        } else {
            getListView().setItemChecked(position, true)
        }

        mActivatedPosition = position
    }

    class object {

        /**
         * The serialization (saved instance state) Bundle key representing the
         * activated item position. Only used on tablets.
         */
        private val STATE_ACTIVATED_POSITION = "activated_position"

        private val INVALID_POSITION = -1

        /**
         * A dummy implementation of the {@link Callbacks} interface that does
         * nothing. Used only when this fragment is not attached to an activity.
         */
        private val sDummyCallbacks = object : Callbacks {
            override fun onItemSelected(id: String) {
            }
        }
    }
}
