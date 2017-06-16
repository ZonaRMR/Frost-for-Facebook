package com.pitchedapps.frost

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ca.allanwang.kau.utils.*
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.pitchedapps.frost.facebook.FbCookie
import com.pitchedapps.frost.utils.Prefs
import com.pitchedapps.frost.utils.cookies
import com.pitchedapps.frost.utils.launchNewTask
import com.pitchedapps.frost.views.AccountItem

/**
 * Created by Allan Wang on 2017-06-04.
 */
class SelectorActivity : BaseActivity() {

    val recycler: RecyclerView by bindView(R.id.selector_recycler)
    val adapter = FastItemAdapter<AccountItem>()
    val text: AppCompatTextView by bindView(R.id.text_select_account)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selector)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter
        adapter.add(cookies().map { AccountItem(it) })
        adapter.add(AccountItem(null)) // add account
        adapter.withEventHook(object : ClickEventHook<AccountItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? = (viewHolder as? AccountItem.ViewHolder)?.v

            override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<AccountItem>, item: AccountItem) {
                if (item.cookie == null) this@SelectorActivity.launchNewTask(LoginActivity::class.java)
                else FbCookie.switchUser(item.cookie, { launchNewTask(MainActivity::class.java, cookies()) })
            }
        })
        theme()
    }

    fun theme() {
        val darkAccent = Prefs.headerColor.darken()
        statusBarColor = darkAccent.darken().withAlpha(255)
        navigationBarColor = darkAccent
        text.setTextColor(Prefs.textColor)
//        toolbar.setBackgroundColor(darkAccent)
//        toolbar.setTitleTextColor(Prefs.iconColor)
        window.setBackgroundDrawable(ColorDrawable(Prefs.bgColor))
//        toolbar.overflowIcon?.setTint(Prefs.iconColor)
    }
}