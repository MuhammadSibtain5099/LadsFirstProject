package com.sibtain.navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sibtain.navigation.R
import com.sibtain.navigation.interfaces.Communicator
import kotlinx.android.synthetic.main.fragment_chat.view.*


class ChatFragment : Fragment() {
    private lateinit var communicator: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_chat, container, false)
        communicator = activity as Communicator
        v.txtChat.setOnClickListener {
            communicator.passDataCom((" is from other fragment"))
        }
        return v
    }


}