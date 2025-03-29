package com.kstraupenieks.asca


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.telephony.TelephonyManager
import android.util.Log


class CallReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    Log.d("CallReceiver", "📞 Incoming call detected")
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    Log.d("CallReceiver", "✅ Call connected")

                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    Log.d("CallReceiver", "❌ Call ended")

                }
            }
        }
    }







}
