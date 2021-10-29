package com.example.anitultimateteambuilder.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.anitultimateteambuilder.R

class MySettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}