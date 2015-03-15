package com.droibit.diddo.fragments

import butterknife.bindView
import android.support.v4.app.Fragment
import com.droibit.diddo.models.dummy.DummyContent
import android.widget.TextView
import com.melnykov.fab.FloatingActionButton
import android.widget.ListView
import android.view.View
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.droibit.diddo.R
import com.droibit.diddo.views.adapters.ActivityAdapter
import android.widget.AdapterView
import android.widget.Adapter
import com.droibit.diddo.views.adapters.ActivityDateAdapter
import com.droibit.diddo.fragments.dialogs.ActivityDateDialogFragment
import com.droibit.diddo.models.ActivityDate
import android.widget.Toast

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ActivityDetailFragment : Fragment(), AdapterView.OnItemClickListener, ActivityDateDialogFragment.Callbacks {

    class object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        public val ARG_ITEM_ID: String = "item_id"
    }

    /**
     * The dummy content this fragment is presenting.
     */
    private var mItem: DummyContent.DummyItem? = null

    private val mElapsedDateText: TextView by bindView(R.id.elapsed_date)
    private val mDateText: TextView by bindView(R.id.date)
    private val mActionButton: FloatingActionButton by bindView(R.id.fab)
    private val mListView: ListView by bindView(android.R.id.list)
    private val mEmptyView: View by bindView(android.R.id.empty)

    /** {@inheritDoc} */
    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID))
        }
        setHasOptionsMenu(true)
        setRetainInstance(true)
    }

    /** {@inheritDoc} */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_item_detail, container, false)

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //(rootView.findViewById(R.id.item_detail) as TextView).setText(mItem!!.content)
        }
        return rootView
    }

    /** {@inheritDoc} */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super<Fragment>.onViewCreated(view, savedInstanceState)

        val adapter = ActivityDateAdapter(getActivity())
        adapter.addAll(DummyContent.ITEMS.map { it.content })
        mListView.setAdapter(adapter)
        mListView.setOnItemClickListener(this)

        mListView.setEmptyView(mEmptyView)

        mActionButton.setOnClickListener {
            showActivityDateDialog()
        }
    }

    /** {@inheritDoc} */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail, menu)
    }

    /** {@inheritDoc} */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super<Fragment>.onOptionsItemSelected(item)
    }

    /** {@inheritDoc} */
    override fun onItemClick(parent: AdapterView<out Adapter>, view: View, position: Int, id: Long) {
        throw UnsupportedOperationException()
    }

    /** {@inheritDoc} */
    override fun onActivityDateEnterd(activityDate: ActivityDate) {
        val adapter = mListView.getAdapter() as ActivityDateAdapter
        if (activityDate.isNew) {
            adapter.add("Item 4")
        }

        Toast.makeText(
                getActivity(),
                if (activityDate.isNew) R.string.toast_create_activity_date else R.string.toast_modify_activity_memo,
                Toast.LENGTH_SHORT).show()
    }

    private fun showActivityDateDialog() {
        ActivityDateDialogFragment.newInstance(null).show(this)
    }
}