package com.drubico.pokeapi.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.drubico.pokeapi.R

class LoadingAlertDialog(context: Context, parent: ViewGroup): Dialog(context, R.style.LoadingDialogTheme) {
    private val mContext: Context = context
    private val mParent: ViewGroup = parent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflateView: View = inflater.inflate(R.layout.dialog_loading, mParent, false)
        setCancelable(false)
        setContentView(inflateView)
    }
}