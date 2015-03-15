package com.droibit.diddo.fragments

import butterknife.bindView
import android.support.v4.app.Fragment
import android.widget.AdapterView
import android.widget.ListView
import com.melnykov.fab.FloatingActionButton
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Adapter
import com.droibit.diddo
import android.widget.ArrayAdapter
import android.widget.AbsListView
import com.droibit.diddo.models.dummy.DummyContent
import com.droibit.diddo.views.adapters.ActivityAdapter
import android.widget.Toast
import com.droibit.diddo.R
import com.droibit.diddo.fragments.dialogs.ActivityDialogFragment
import com.droibit.diddo.models.UserActivity
import android.view.ContextMenu

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ActivityListFragment : Fragment(), AdapterView.OnItemClickListener, ActivityDialogFragment.Callbacks {

    class object {

        /**
         * The serialization (saved instance state) Bundle key representing the
         * activated item position. Only used on tablets.
         */
        private val STATE_ACTIVATED_POSITION = "activated_position"
        private val INVALID_POSITION = -1
        private val CONTEXT_MENU_MODIFY_ACTIVITY = 0;
        private val CONTEXT_MENU_DELETE_ACTIVITY = 1;


        /**
         * A dummy implementation of the {@link Callbacks} interface that does
         * nothing. Used only when this fragment is not attached to an activity.
         */
        private val sDummyCallbacks = object : Callbacks {
            override fun onItemSelected(id: String) {
            }
        }
    }

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

    private val mListView: ListView by bindView(android.R.id.list)
    private val mActionButton: FloatingActionButton by bindView(diddo.R.id.fab)

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity) {
        super<Fragment>.onAttach(activity)

        // Activities containing this fragment must implement its callbacks.
        if (!(activity is Callbacks)) {
            throw IllegalStateException("Activity must implement fragment's callbacks.")
        }

        mCallbacks = activity
    }

    /** {@inheritDoc} */
    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        setRetainInstance(true)
    }

    /** {@inheritDoc} */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(diddo.R.layout.fragment_item, container, false)
    }

    /** {@inheritDoc} */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super<Fragment>.onViewCreated(view, savedInstanceState)

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION))
        }

        val adapter = ActivityAdapter(getActivity())
        adapter.addAll(DummyContent.ITEMS)
        mListView.setAdapter(adapter)
        mListView.setOnItemClickListener(this)
        // 項目長押しでコンテキストメニューを表示する。
        registerForContextMenu(mListView)

        mActionButton.setOnClickListener {
            showNewActivityDialog()
        }
    }

    /** {@inheritDoc} */
    override fun onDetach() {
        super<Fragment>.onDetach()

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks
    }

    /** {@inheritDoc} */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(diddo.R.menu.list, menu)
    }

    /** {@inheritDoc} */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super<Fragment>.onOptionsItemSelected(item)
    }

    /** {@inheritDoc} */
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super<Fragment>.onCreateContextMenu(menu, v, menuInfo)

        menu.add(Menu.NONE, CONTEXT_MENU_MODIFY_ACTIVITY, Menu.NONE, R.string.title_activity_modify)
        menu.add(Menu.NONE, CONTEXT_MENU_DELETE_ACTIVITY, Menu.NONE, R.string.title_delete_activity)
    }

    /** {@inheritDoc} */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val menuInfo = item.getMenuInfo() as AdapterView.AdapterContextMenuInfo
        val activity = (mListView.getAdapter() as ActivityAdapter).getItem(menuInfo.position)
        when (item.getItemId()) {
            CONTEXT_MENU_MODIFY_ACTIVITY -> showModifyActivityDialog(activity)
            CONTEXT_MENU_DELETE_ACTIVITY -> deleteActivity(activity)
        }
        return super<Fragment>.onContextItemSelected(item)
    }

    /** {@inheritDoc} */
    override fun onSaveInstanceState(outState: Bundle) {
        super<Fragment>.onSaveInstanceState(outState)
        if (mActivatedPosition != INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition)
        }
    }

    /** {@inheritDoc} */
    override fun onItemClick(parent: AdapterView<out Adapter>, view: View, position: Int, id: Long) {
        mCallbacks.onItemSelected(position.toString())
    }

    /** {@inheritDoc} */
    override fun onActivityNameEnterd(activity: UserActivity) {
        val adapter = mListView.getAdapter() as ActivityAdapter
        if (activity.isNew) {
            adapter.add(activity)
        } else {
            adapter.notifyDataSetChanged();
        }

        Toast.makeText(
                getActivity(),
                if (activity.isNew) R.string.toast_create_activity else R.string.toast_modify_activity,
                Toast.LENGTH_SHORT).show()
    }

    private fun showNewActivityDialog() {
        ActivityDialogFragment.newInstance(null).show(this)
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public fun setActivateOnItemClick(activateOnItemClick: Boolean) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mListView.setChoiceMode(if (activateOnItemClick)
                                    AbsListView.CHOICE_MODE_SINGLE
                                else
                                    AbsListView.CHOICE_MODE_NONE)
    }

    private fun setActivatedPosition(position: Int) {
        if (position == INVALID_POSITION) {
            mListView.setItemChecked(mActivatedPosition, false)
        } else {
            mListView.setItemChecked(position, true)
        }
        mActivatedPosition = position
    }

    // アクティビティ名を修正するためのダイアログを表示する。
    private fun showModifyActivityDialog(activity: UserActivity) {
        ActivityDialogFragment.newInstance(activity).show(this)
    }

    // 選択されたアクティビティを削除する。
    private fun deleteActivity(activity: UserActivity) {
        (mListView.getAdapter() as ActivityAdapter).remove(activity)

        // TODO: DBからも削除

        Toast.makeText(getActivity(), R.string.toast_delete_activity, Toast.LENGTH_SHORT).show()
    }
}