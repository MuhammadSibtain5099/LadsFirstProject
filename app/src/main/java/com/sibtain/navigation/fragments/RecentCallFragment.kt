package com.sibtain.navigation.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.core.app.ActivityCompat
import com.sibtain.navigation.R
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import com.sibtain.navigation.RecentDetails


class RecentCallFragment : Fragment() {

    private var cols = listOf(
        CallLog.Calls._ID,
        CallLog.Calls.NUMBER,
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.DURATION
    ).toTypedArray()
    private lateinit var list: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_recent_call, container, false)
        list = v.findViewById(R.id.listView)
        if (ActivityCompat.checkSelfPermission(
                v.context,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                Array(1) { Manifest.permission.READ_CALL_LOG },
                101
            )
        } else {
            displayLogs()
        }
        return v
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            displayLogs()
        }
    }

    private fun displayLogs() {
        val from = listOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.DURATION,
            CallLog.Calls.CACHED_NAME
        ).toTypedArray()
        val to = intArrayOf(R.id.txtNumber, R.id.txtDuration, R.id.txtTyped)
        val rs = activity?.contentResolver?.query(
            CallLog.Calls.CONTENT_URI, cols, null, null,
            null
        )
        val adapter = SimpleCursorAdapter(context, R.layout.call_logs_list_item, rs, from, to, 0)
        list.adapter = adapter
        list.onItemClickListener =
            OnItemClickListener { _, v, _, _ ->
                val numberTxt: TextView = v.findViewById(R.id.txtNumber)
                val number = numberTxt.text.toString()
                val sib = getContactIdFromNumber(number)

                val intent = Intent(context, RecentDetails::class.java)
                intent.putExtra("contactID", sib)
                context?.startActivity(intent)


            }
    }

    private fun getContactIdFromNumber(number: String): String? {
        val projection = arrayOf(ContactsContract.Contacts._ID)
        val contactUri: Uri = Uri.withAppendedPath(
            ContactsContract.Contacts.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val c: Cursor? = activity?.contentResolver?.query(
            contactUri, projection,
            null, null, null
        )
        if (c != null) {
            return if (c.moveToFirst()) {
                c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))

            } else null
        }
        c?.close()
        return null
    }

}