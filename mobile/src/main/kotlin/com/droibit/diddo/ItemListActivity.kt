package com.droibit.diddo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.droibit.diddo.fragments.ActivityDetailFragment
import com.droibit.diddo.fragments.ActivityListFragment
import com.droibit.diddo.models.RefreshEvent
import com.droibit.diddo.models.UserActivity
import com.droibit.easycreator.compat.makeSceneTransitionAnimation
import com.droibit.easycreator.compat.startActivityForResultCompat
import com.droibit.easycreator.intent


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * [ItemListFragment] and the item details
 * (if present) is a [ItemDetailFragment].
 * <p/>
 * This activity also implements the required
 * [ItemListFragment.Callbacks] interface
 * to listen for item selections.
 */
public class ItemListActivity : AppCompatActivity(), ActivityListFragment.Callbacks, Handler.Callback {

    companion object {
        private val TAG = javaClass<ItemListActivity>().getSimpleName()

        val REQUEST_ACTIVITY = 1
        val MESSAGE_REFRESH = 1
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane = false

    /** {@inheritDoc} */
    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.getBackground().setAlpha(255)
        setSupportActionBar(toolbar)

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            (getSupportFragmentManager().findFragmentById(R.id.item_list) as ActivityListFragment)
                    .setActivateOnItemClick(true)
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /** {@inheritDoc} */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super<AppCompatActivity>.onActivityResult(requestCode, resultCode, data)

        getSupportFragmentManager().findFragmentById(R.id.item_list)
            ?.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Callback method from [ItemListFragment.Callbacks]
     * indicating that the item with the given ID was selected.
     */
    override fun onItemSelected(activity: UserActivity?, sharedView: View?) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            val fragment = if (activity != null)
                                ActivityDetailFragment.newInstance(activity.getId())
                            else
                                ActivityDetailFragment()
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            return
        }

        if (activity != null && sharedView != null) {
            startActivityForResultCompat(
                    intent = intent<ItemDetailActivity>(this) {
                        putExtra(ActivityDetailFragment.ARG_ITEM_ID, activity.getId())
                        putExtra(ActivityDetailFragment.ARG_ITEM_TITLE, activity.name)
                    },
                    options = if (Build.VERSION.SDK_INT >= 21)//Build.VERSION_CODES.LOLLIPOP)
                                makeSceneTransitionAnimation(sharedView, getString(R.string.transition_date))
                            else
                                null,
                    requestCode = REQUEST_ACTIVITY
            )
        }
    }

    /** {@inheritDoc} */
    override fun handleMessage(msg: Message): Boolean {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Message : ${msg.toString()}")
        }

        if (msg.what != MESSAGE_REFRESH) {
            return false
        }

        // 2画面の場合のみ
        if (mTwoPane) {
            val f = getSupportFragmentManager().findFragmentById(R.id.item_list) as? ActivityListFragment
            f?.onResreshEvent(msg.obj as RefreshEvent)
        }
        return true
    }
}
