package com.droibit.diddo.fragments

import android.app.Activity
import android.os.Build
import android.support.v4.app.Fragment
import com.droibit.diddo.models.dummy.DummyContent
import android.widget.TextView
import com.melnykov.fab.FloatingActionButton
import android.widget.ListView
import android.view.View
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewCompat
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.droibit.diddo.R
import com.droibit.diddo.views.adapters.UserActivityAdapter
import android.widget.AdapterView
import android.widget.Adapter
import com.droibit.diddo.views.adapters.ActivityDateAdapter
import com.droibit.diddo.fragments.dialogs.ActivityMemoDialogFragment
import com.droibit.diddo.models.ActivityDate
import android.widget.Toast
import android.view.ContextMenu
import android.view.animation.AnimationUtils
import com.activeandroid.Model
import com.droibit.diddo.models.UserActivity
import com.droibit.easycreator.compat.fragment
import com.droibit.easycreator.compat.show
import com.droibit.easycreator.showToast
import com.droibit.diddo.extension.bindView
import com.droibit.diddo.fragments.dialogs.CalendarDialogFragment
import com.droibit.diddo.utils.ViewAnimationUtils
import java.util.ArrayList

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ActivityDetailFragment : Fragment(), ActivityMemoDialogFragment.Callbacks, View.OnLayoutChangeListener {

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        val ARG_ITEM_ID = "item_id"
        val ARG_ITEM_TITLE = "item_title"

        /**
         * 新しいインスタンスを作成する。
         */
        fun newInstance(id: Long): ActivityDetailFragment {
           return fragment() { args ->
                args.putLong(ARG_ITEM_ID, id)
            }
        }
    }

    /**
     * The dummy content this fragment is presenting.
     */
    private var mUserActivity: UserActivity? = null

    private val mElapsedDateText: TextView by bindView(R.id.elapsed_date)
    private val mDateText: TextView by bindView(R.id.date)
    private val mAddActionButton: FloatingActionButton by bindView(R.id.fab_add)
    private val mCalendarActionButton: FloatingActionButton by bindView(R.id.fab_calendar)
    private val mListView: ListView by bindView(android.R.id.list)

    /** {@inheritDoc} */
    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mUserActivity = Model.load(javaClass<UserActivity>(), getArguments().getLong(ARG_ITEM_ID))
        }
        setRetainInstance(true)
    }

    /** {@inheritDoc} */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_item_detail, container, false)

        // Android5.0以上の場合はヘッダにリップルエフェクトを適用する。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootView.addOnLayoutChangeListener(this)
        }
        return rootView
    }

    /** {@inheritDoc} */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super<Fragment>.onViewCreated(view, savedInstanceState)

        // 日付テキストをアニメーション表示する。
        ViewCompat.setTransitionName(mDateText, getString(R.string.transition_date))

        val adapter = ActivityDateAdapter(getActivity())
        mListView.setAdapter(adapter)
        mListView.setEmptyView(view.findViewById(android.R.id.empty))

        mListView.setOnItemClickListener { adapterView, view, position, l ->
            // クリックされたらメモの内容をトーストで表示する。
            showMemoToast(adapter.getItem(position))
        }
        mListView.setOnItemLongClickListener { adapterView, view, position, l ->
            // 長押しで編集用のダイアログを表示する。
            showActivityMemoDialog(adapter.getItem(position))
        }

        mAddActionButton.setOnClickListener { showActivityMemoDialog(null) }
        mCalendarActionButton.setOnClickListener { showActivityDateCalendar() }
    }

    /** {@inheritDoc} */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super<Fragment>.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            val activityDates = mUserActivity!!.details
            if (activityDates != null && activityDates.isNotEmpty()) {
                (mListView.getAdapter() as ActivityDateAdapter).addAll(activityDates)
            }
        }
        updateElapsedViews()
    }

    /** {@inheritDoc} */
    override fun onActivityDateEnterd(activityDate: ActivityDate) {
        val adapter = mListView.getAdapter() as ActivityDateAdapter
        if (activityDate.isNew) {
            adapter.add(activityDate)
        } else {
            adapter.notifyDataSetChanged();
        }

        val messageRes = if (activityDate.isNew)
                            R.string.toast_create_activity_date
                        else
                            R.string.toast_modify_activity_memo
        // アクティビティの日付も更新しておく。
        mUserActivity!!.recentlyDate = activityDate.date
        mUserActivity!!.save()
        // メッセージの都合、このタイミングでDBに保存する。
        activityDate.activity = mUserActivity
        activityDate.save()

        showToast(getActivity(), messageRes, Toast.LENGTH_SHORT)
        // 日をまたいで活動日を追加した場合のために画面を更新する。
        updateElapsedViews()

        // TODO: 削除した時も
        getActivity()?.setResult(Activity.RESULT_OK)
    }

    /** {@inheritDoc} */
    override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        v.removeOnLayoutChangeListener(this)

        val res = getResources()
        val header = v.findViewById(R.id.header)
        ViewAnimationUtils.animationCircularReveal(header, res.getInteger(R.integer.medium_animation_millis).toLong())
        ViewAnimationUtils.animationScaleUp(mAddActionButton, res.getInteger(R.integer.short_animation_millis).toLong())
        ViewAnimationUtils.animationScaleUp(mCalendarActionButton, res.getInteger(R.integer.short_animation_millis).toLong())
    }

    // アクティビティのメモ編集用のダイアログを表示する。
    private fun showActivityMemoDialog(srcActivityDate: ActivityDate?): Boolean {
        ActivityMemoDialogFragment.newInstance(srcActivityDate).show(this)
        return true
    }

    // アクティビティのメモをトーストで表示する。
    private fun showMemoToast(activityDate: ActivityDate) {
        if (activityDate.memo != null) {
            showToast(getActivity(), activityDate.memo!!, Toast.LENGTH_LONG)
        }
    }

    private fun updateElapsedViews() {
        if (mListView.getAdapter().isEmpty()) {
            mElapsedDateText.setText(R.string.empty_text)
            mDateText.setText(R.string.empty_text)
            return
        }

        val recentlyActivityDate = (mListView.getAdapter() as ActivityDateAdapter).getLastItem()!!
        mElapsedDateText.setText(recentlyActivityDate.getElapsedDateFromNow(getActivity()))
        mDateText.setText(DateFormat.getDateFormat(getActivity()).format(recentlyActivityDate.date))
    }

    private fun showActivityDateCalendar() {
        if (mListView.getAdapter().isEmpty()) {
            showToast(getActivity(), R.string.text_no_activity_date, Toast.LENGTH_SHORT)
            return
        }

        val activityDates = mUserActivity!!.details
        CalendarDialogFragment.newInstance(ArrayList(activityDates))
                .show(this)
    }
}