package com.droibit.diddo.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.droibit.diddo.ItemListActivity
import com.droibit.diddo.R
import com.droibit.diddo.SettingsActivity
import com.droibit.diddo.extension.bindView
import com.droibit.diddo.fragments.dialogs.ActivityDialogFragment
import com.droibit.diddo.fragments.dialogs.SortActivityDialogFragment
import com.droibit.diddo.models.RefreshEvent
import com.droibit.diddo.models.UserActivity
import com.droibit.diddo.models.loadUserActivities
import com.droibit.diddo.utils.PauseHandler
import com.droibit.diddo.utils.SettingsUtils
import com.droibit.diddo.views.adapters.UserActivityAdapter
import com.droibit.easycreator.compat.show
import com.droibit.easycreator.sendMessage
import com.droibit.easycreator.showToast
import com.droibit.easycreator.startActivity
import com.melnykov.fab.FloatingActionButton
import kotlin.properties.Delegates

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a [ItemDetailFragment].
 * <p/>
 * Activities containing this fragment MUST implement the [Callbacks]
 * interface.
 */
public class ActivityListFragment : Fragment(),
        ActivityDialogFragment.Callbacks, SortActivityDialogFragment.Callbacks {

    companion object {

        /**
         * The serialization (saved instance state) Bundle key representing the
         * activated item position. Only used on tablets.
         */
        const private val STATE_ACTIVATED_POSITION = "activated_position"
        const private val CONTEXT_MENU_MODIFY_ACTIVITY = 0
        const private val CONTEXT_MENU_DELETE_ACTIVITY = 1

        /**
         * A dummy implementation of the [Callbacks] interface that does
         * nothing. Used only when this fragment is not attached to an activity.
         */
        private val sDummyCallbacks = object: Callbacks {
            override fun onItemSelected(activity: UserActivity?, sharedView: View?) {
            }
        }
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public fun onItemSelected(activity: UserActivity?, sharedView: View?)
    }

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private var mCallbacks: Callbacks = sDummyCallbacks

    /**
     * The current activated item position. Only used on tablets.
     */
    private var mActivatedPosition = AdapterView.INVALID_POSITION

    private val mListView: ListView by bindView(android.R.id.list)
    private val mActionButton: FloatingActionButton by bindView(R.id.fab)
    private val mPauseHandler = PauseHandler()
    private var mAdapter: UserActivityAdapter by Delegates.notNull()

    /** {@inheritDoc} */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        // Activities containing this fragment must implement its callbacks.
        if (activity !is Callbacks) {
            throw IllegalStateException("Activity must implement fragment's callbacks.")
        }
        mCallbacks = activity
    }

    /** {@inheritDoc} */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    /** {@inheritDoc} */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    /** {@inheritDoc} */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = UserActivityAdapter(getActivity())
        mListView.adapter = mAdapter
        mListView.emptyView = view.findViewById(android.R.id.empty)

        mListView.setOnItemClickListener { adapterView, view, position, l ->
            mCallbacks.onItemSelected(mAdapter.getItem(position), view.findViewById(android.R.id.text2))
            mActivatedPosition = position
        }
        // 項目長押しでコンテキストメニューを表示する。
        registerForContextMenu(mListView)

        // アクションボタン押下で新規にアクティビティを作成する。
        mActionButton.setOnClickListener {
            showNewActivityDialog()
        }

    }

    /** {@inheritDoc} */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // DB内にアクティビティが存在すれば表示する
        val userActivities = loadUserActivities()
        if (userActivities.isNotEmpty()) {
            mAdapter.addAll(userActivities)
        }

        // アクティビティが存在する場合は以前の並び順で表示する。
        if (!mListView.adapter.isEmpty) {
            sortActivity(SettingsUtils.getOrder(activity))
        }

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION))
        }
    }

    /** {@inheritDoc} */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != ItemListActivity.REQUEST_ACTIVITY ||
            resultCode  != Activity.RESULT_OK) {
            return
        }

        mPauseHandler.sendMessage() {
            obj = Runnable { mAdapter.notifyDataSetChanged() }
        }
    }

    /** {@inheritDoc} */
    override fun onResume() {
        super.onResume()

        mPauseHandler.resume()
    }

    /** {@inheritDoc} */
    override fun onPause() {
        super.onPause()

        mPauseHandler.pause()
    }

    /** {@inheritDoc} */
    override fun onDetach() {
        super.onDetach()

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks
    }

    /** {@inheritDoc} */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.list, menu)

    /** {@inheritDoc} */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort     -> showSortDialog()
            R.id.action_settings -> showSettings()
        }
        return true
    }

    /** {@inheritDoc} */
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu.add(Menu.NONE, CONTEXT_MENU_MODIFY_ACTIVITY, Menu.NONE, R.string.title_activity_modify)
        menu.add(Menu.NONE, CONTEXT_MENU_DELETE_ACTIVITY, Menu.NONE, R.string.title_delete_activity)
    }

    /** {@inheritDoc} */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val menuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val activity = mAdapter.getItem(menuInfo.position)
        when (item.itemId) {
            CONTEXT_MENU_MODIFY_ACTIVITY -> showModifyActivityDialog(activity)
            CONTEXT_MENU_DELETE_ACTIVITY -> deleteActivity(activity)
        }
        return super.onContextItemSelected(item)
    }

    /** {@inheritDoc} */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Serialize and persist the activated item position.
        outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition)
    }

    /** {@inheritDoc} */
    override fun onActivityNameEntered(activity: UserActivity) {
        if (activity.isNew) {
            mAdapter.add(activity)
        } else {
            mAdapter.notifyDataSetChanged()
        }

        val messageRes = if (activity.isNew)
                            R.string.toast_create_activity
                        else
                            R.string.toast_modify_activity
        // メッセージの都合、このタイミングで値を保存する
        activity.save()

        showToast(getActivity(), messageRes, Toast.LENGTH_SHORT)
    }

    /** {@inheritDoc} */
    override fun onSortChose(order: Int) {
        sortActivity(order)
        // 並び順を復元できるように保存しておく
        SettingsUtils.setOrder(context, order)
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public fun setActivateOnItemClick(activateOnItemClick: Boolean) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mListView.choiceMode = if (activateOnItemClick)
                                    AbsListView.CHOICE_MODE_SINGLE
                                else
                                    AbsListView.CHOICE_MODE_NONE
    }

    private fun setActivatedPosition(position: Int) {
        if (position == AdapterView.INVALID_POSITION) {
            mListView.setItemChecked(mActivatedPosition, false)
        } else {
            mListView.setItemChecked(position, true)
        }
        mActivatedPosition = position
    }

    // アクティビティを作成する為のダイアログを表示する。
    private fun showNewActivityDialog() = ActivityDialogFragment.newInstance(null).show(this)

    // アクティビティ名を修正するためのダイアログを表示する。
    private fun showModifyActivityDialog(activity: UserActivity) = ActivityDialogFragment.newInstance(activity).show(this)

    // 選択されたアクティビティを削除する。
    private fun deleteActivity(activity: UserActivity) {
        // 2画面表示されている状態で鮮太されているアクティビティを削除した場合（※スマホは呼ばれない）
        if (mListView.choiceMode == AbsListView.CHOICE_MODE_SINGLE) {
            if (mAdapter.getPosition(activity) == mListView.checkedItemPosition) {
                // 詳細画面の表示をクリアしておく
                mCallbacks.onItemSelected(null, null)
            }
        }
        mAdapter.remove(activity)

        // トランザクションはまぁいっか…。量もそんなに多くないだろう…。
        val activityDates = activity.details
        activityDates.forEach { it.delete() }
        activity.delete()

        showToast(getActivity(), R.string.toast_delete_activity, Toast.LENGTH_SHORT)
    }

    // アクティビティのソート用ダイアログを表示する。
    private fun showSortDialog() = SortActivityDialogFragment.newInstance(SettingsUtils.getOrder(activity)).show(this)

    // 設定画面を表示する
    private fun showSettings() = activity.startActivity<SettingsActivity>()

    // アクティビティリストをソートする。
    private fun sortActivity(order: Int) {
        if (!mAdapter.isEmpty) {
            mAdapter.sort(UserActivity.getComparator(order))
        }
    }

    // 更新イベントが呼ばれた時の処理
    public fun onRefreshEvent(event: RefreshEvent) {
        mAdapter.updateRow(mListView, event.activity)
    }
}
