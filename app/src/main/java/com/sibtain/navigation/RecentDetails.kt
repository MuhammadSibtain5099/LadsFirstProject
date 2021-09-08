package com.sibtain.navigation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import com.sibtain.navigation.model.Contacts
import kotlinx.android.synthetic.main.activity_recent_details.*

class RecentDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_details)
            val ss: String = intent.getStringExtra("contactID").toString()
        val contactDetail: Contacts? = getContactList(ss.toString())
        if (contactDetail?.contactThumbnail!=null) {
            imageView.setImageURI(Uri.parse(contactDetail?.contactThumbnail))
        }else{
            imageView.setImageResource(R.drawable.ic_baseline_person_24)
        }
    }

    private fun getContactList(contactId:String): Contacts? { val contact = contentResolver?.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )
        var obj : Contacts? = null
        if (contact != null) {
            while (contact.moveToNext()) {
                if (contactId== contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))){
                val name =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contactID =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val contactThumb =
                    contact.getString(contact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                if(contactThumb!=null){
                     obj = Contacts(name, phone, contactID,contactThumb)

                }
                else{
                     obj = Contacts(name, phone, contactID)

                }}


            }
            //  contact.close()
        }
        return obj
    }



}