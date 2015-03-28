package com.droibit.diddo

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.droibit.diddo.extension.ViewById
import com.droibit.diddo.extension.ViewByKey
import android.support.v7.widget.Toolbar
import com.droibit.diddo.fragments.dialogs.LicenseDialogFragment

/**
 * アプリの設定画面を表示するアクティビティ

 * @auther kumagai
 * @since 15/03/28
 */
public class SettingsActivity : PreferenceActivity() {

    private val mDeveloperPref: Preference by bindView(R.string.key_developer)
    private val mAppPref: Preference by bindView(R.string.key_app)
    private val mOpenSourcePref: Preference by bindView(R.string.key_open_source)
    private val mSourceCodePref: Preference by bindView(R.string.key_source_code)

    /** {@inheritDoc}  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)

        mAppPref.setSummary(getAppVersion())

        mDeveloperPref.setOnPreferenceClickListener { launchUrl(R.string.url_twitter) }
        mSourceCodePref.setOnPreferenceClickListener { launchUrl(R.string.url_github) }
        mOpenSourcePref.setOnPreferenceClickListener { showLicenseDialog() }
    }

    /** {@inheritDoc}  */
    override protected fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState);

        val root = findViewById(android.R.id.list).getParent().getParent().getParent() as LinearLayout
        val toolbar = LayoutInflater.from(this).inflate(R.layout.toolbar_settings, root, false) as Toolbar
        root.addView(toolbar, 0); // insert at top
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun launchUrl(resId: Int): Boolean {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(resId))))
        return true
    }

    private fun showLicenseDialog(): Boolean {
        LicenseDialogFragment.newInstance(R.raw.licenses)
                .show(getFragmentManager(), "")
        return true
    }

    private fun getAppVersion(): String {
        val pm = getPackageManager();

        try {
            val packageInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            return getString(R.string.version_format, packageInfo.versionName);
        } catch (e: PackageManager.NameNotFoundException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return getString(R.string.empty_text)
    }
}

/**
 * [Preference] をViewInjectionするための拡張メソッド
 */
public fun <T: Preference>PreferenceActivity.bindView(resId: Int): ViewByKey<T> = object: ViewByKey<T> {

    private var pref: T? = null

    [suppress("UNCHECKED_CAST")]
    override fun get(thisRef: Any, prop: PropertyMetadata): T {
        if (pref == null) {
            pref = findPreference(getString(resId)) as T
        }
        return pref!!
    }
}