package com.rama.txori.activities

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rama.txori.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.view_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val version = activity.packageManager
            .getPackageInfo(activity.packageName, 0).versionCode
        view.findViewById<TextView>(R.id.name_version).text =
            getString(R.string.app_name) + ' ' + version
    }
}
