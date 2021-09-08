package com.sibtain.navigation.fragments

import `in`.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sibtain.navigation.R
import com.sibtain.navigation.adapter.RecyclerViewAdapter
import com.sibtain.navigation.model.Contacts
import com.sibtain.navigation.utility.AlphabetItem
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import java.util.*
import kotlin.collections.ArrayList

open class DashboardFragment : Fragment() {


    private lateinit var v: View
    private var mRecyclerView: IndexFastScrollRecyclerView? = null
    private val mDataArray: MutableList<String> = ArrayList()
    private var mAlphabetItems: MutableList<AlphabetItem>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_dashboard, container, false)
        mRecyclerView = v.findViewById(R.id.recyclerView)

        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_CONTACTS
                )
            } !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_CONTACTS,
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                    1
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_CONTACTS), 1
                )
            }
        } else {
            getContactList(v)
        }
        return v
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((
                            activity?.let {
                                ContextCompat.checkSelfPermission(
                                    it,
                                    Manifest.permission.WRITE_CONTACTS
                                )
                            }
                         ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
                        getContactList(v)
                    }
                } else {
                    Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun getContactList(vie: View) {
        val contact = activity?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC "
        )
        val contactList: MutableList<Contacts> = ArrayList()
        if (contact != null) {

            while (contact.moveToNext()) {
                val contactId =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))

                val name =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val obj = Contacts(name, phone, contactId)
                contactList.add(obj)

            }
            contact.close()
        }
        // filtering contactList
        val contactListFiltered: MutableList<Contacts> = ArrayList()

        for ((a, item) in contactList.withIndex()) {

            val b = a + 1
            if (contactList.size != b) {
                val ab = item.number.replace("[()\\s-]".toRegex(), "")
                val bc = contactList[b].number.replace("[\\s\\-]".toRegex(), "")
                if (ab != bc) {
                    mDataArray.add(item.name)
                    contactListFiltered.add(item)

                }
            } else {
                mDataArray.add(item.name)
                contactListFiltered.add(item)

            }
        }

        initialiseData()
        initialiseUI()


        vie.recyclerView.layoutManager = LinearLayoutManager(activity)
        vie.recyclerView.adapter = activity?.let { RecyclerViewAdapter(contactListFiltered, it) }
        Objects.requireNonNull(vie.recyclerView.layoutManager)?.scrollToPosition(0)
    }

    private fun initialiseData() {

        Log.d("checkList", (mDataArray as MutableList<String>?)?.size.toString())

        mAlphabetItems = ArrayList()
        val strAlphabets: MutableList<String> = java.util.ArrayList()
        for (i in mDataArray.indices) {
            val name = mDataArray[i]
            if (name.trim { it <= ' ' }.isEmpty()) continue
            val word = name.substring(0, 1)
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word)
                (mAlphabetItems as ArrayList<AlphabetItem>).add(AlphabetItem(i, word, false))
            }
        }
    }

    private fun initialiseUI() {

        mRecyclerView!!.setIndexTextSize(12)
        mRecyclerView!!.setIndexBarColor("#33334c")
        mRecyclerView!!.setIndexBarCornerRadius(0)
        mRecyclerView!!.setIndexBarTransparentValue(0.4.toFloat())
        mRecyclerView!!.setIndexbarMargin(0f)
        mRecyclerView!!.setIndexbarWidth(40f)
        mRecyclerView!!.setPreviewPadding(0)
        mRecyclerView!!.setIndexBarTextColor("#FFFFFF")
        mRecyclerView!!.setPreviewTextSize(60)
        mRecyclerView!!.setPreviewColor("#33334c")
        mRecyclerView!!.setPreviewTextColor("#FFFFFF")
        mRecyclerView!!.setPreviewTransparentValue(0.6f)
        mRecyclerView!!.setIndexBarVisibility(true)
        mRecyclerView!!.setIndexBarStrokeVisibility(true)
        mRecyclerView!!.setIndexBarStrokeWidth(1)
        mRecyclerView!!.setIndexBarStrokeColor("#000000")
        mRecyclerView!!.setIndexbarHighLightTextColor("#33334c")
        mRecyclerView!!.setIndexBarHighLightTextVisibility(true)
    }


}