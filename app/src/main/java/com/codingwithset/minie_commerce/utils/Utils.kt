package com.codingwithset.minie_commerce.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.codingwithset.minie_commerce.R


/*
this function [callSeller] call the merchant
 */
fun View.callSeller() {
    val phone: String = context.getString(R.string.seller_number_developer)
    val intent =
        Intent(Intent.ACTION_DIAL, Uri.fromParts(context.getString(R.string.tel), phone, null))
    context.startActivity(intent)
}

/*
set the view visibility to be visible
 */
fun View.visible() {
    this.visibility = View.VISIBLE

}

/*
set the view visibility to be gone
 */
fun View.gone() {
    this.visibility = View.GONE

}

/**
 * Check whether network is available
 *
 * @param context
 * @return Whether device is connected to Network.
 */
fun Context.checkInternetAccess(): Boolean {
    try {

        with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Device is running on Marshmallow or later Android OS.
                with(getNetworkCapabilities(activeNetwork)) {
                    return hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR
                    )
                }
            } else {
                @Suppress("DEPRECATION")
                activeNetworkInfo?.let {
                    // connected to the internet
                    @Suppress("DEPRECATION")
                    return listOf(
                        ConnectivityManager.TYPE_WIFI,
                        ConnectivityManager.TYPE_MOBILE
                    ).contains(it.type)
                }
            }
        }
        return false
    } catch (exc: NullPointerException) {
        return false
    }
}


/*
hide the keyboard
 */
fun Activity.hideKeyboard() {
    val inputMethodManager: InputMethodManager? =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
}

