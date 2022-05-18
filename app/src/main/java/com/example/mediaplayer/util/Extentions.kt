package com.example.mediaplayer.util

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showShortToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}