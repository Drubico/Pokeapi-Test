package com.drubico.pokeapi.ui.dialog

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.drubico.pokeapi.R

@SuppressLint("InflateParams")
fun Fragment.toastMessageCustom(message: String, toastType: ToastType) {
    val inflater = LayoutInflater.from(context)
    val toastLayout = inflater.inflate(R.layout.custom_toast, null, false)
    val textView = toastLayout.findViewById<TextView>(R.id.toastMessage)
    textView.text = message

    when (toastType) {
        ToastType.ERROR -> {
            textView.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_exclamation),
                null, null, null
            )
            toastLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.notificationErrorColor
                )
            )
        }

        ToastType.SUCCESS -> {
            textView.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_accept),
                null, null, null
            )
            toastLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.notificationSuccessColor
                )
            )
        }

        ToastType.WARNING -> TODO()
        ToastType.INFO -> TODO()
    }

    val toast = Toast(context).apply {
        setGravity(Gravity.HORIZONTAL_GRAVITY_MASK, -1, 1000)
        setMargin(0f, 0f)
        duration = Toast.LENGTH_LONG
        view = toastLayout
    }
    toast.show()
}


enum class ToastType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}
