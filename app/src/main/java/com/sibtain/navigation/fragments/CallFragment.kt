package com.sibtain.navigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sibtain.navigation.R
import kotlinx.android.synthetic.main.activity_main.*

class CallFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_call, container, false)
        Toast.makeText(activity, arguments?.getString("message"), Toast.LENGTH_LONG).show()
        activity?.nav_menu?.menu?.getItem(2)?.isChecked = true
        return v
    }

}