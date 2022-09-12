package com.danc.weatherdvt.presentation.fragments

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.danc.weatherdvt.R
import kotlinx.android.synthetic.main.network_layout.*

class CustomDialog(val title: String, val buttonTitle: String, val message: String, val callback: OnClick): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.round_corners)
        return inflater.inflate(R.layout.network_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_header.text = title
        tv_message.text = message
        btn_continue.text = buttonTitle
        btn_continue.setOnClickListener {
            callback.dialogContinue()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    interface OnClick {
        fun dialogContinue()
    }

}