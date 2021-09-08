package com.sibtain.navigation


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import com.sibtain.navigation.model.Contacts
import kotlinx.android.synthetic.main.activity_contact_details.*
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.content.ContentProviderOperation


class ContactDetails : AppCompatActivity() {
    private var contactId: Int? = null
    private lateinit var contactDetail: Contacts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)
        setData()
        button.setOnClickListener {

            val idContact = contactDetail.contactId
            val i = Intent(Intent.ACTION_EDIT)
            val contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                idContact.toLong()
            )
            i.data = contactUri
            i.putExtra("finishActivityOnSaveCompleted", true)
            startActivity(i)

        }
        btnDelete.setOnClickListener {

            deleteContact(contactDetail.contactId)
        }

    }

    private fun setData() {
        contactId = intent.getIntExtra("contactId", 0)
        contactDetail = getContactList(contactId)
        editTextTextPersonName.setText(contactDetail.name)
        editTextTextPersonName2.setText(contactDetail.number)
        if (contactDetail.contactThumbnail != null) {
            imageViewThum.setImageURI(Uri.parse(contactDetail.contactThumbnail))
        } else {
            imageViewThum.setImageResource(R.drawable.ic_baseline_person_24)
        }
    }

    private fun deleteContact(contactId: String?) {
//        val contactHelper = contentResolver
//        val contactUri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI)
//        var check = contactHelper.delete(contactUri, "", null)
        val ops: ArrayList<ContentProviderOperation> = ArrayList()


        val arg = arrayOf(contactId)
        ops.add(
            ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(
                    ContactsContract.RawContacts.CONTACT_ID + "=?",
                    arg
                ).build()
        )
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()

        }
    }

    private fun getContactList(contactId: Int?): Contacts {
        val contact = contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC "
        )
        var contactList: Contacts? = null
        if (contact != null) {

            while (contact.moveToNext()) {
                val id =
                    contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                if (contactId == id.toInt()) {
                    val name =
                        contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phone =
                        contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val obj: Contacts
                    val contactThumb =
                        contact.getString(contact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                    obj = if (contactThumb != null) {
                        Contacts(name, phone, contactId.toString(), contactThumb)

                    } else {
                        Contacts(name, phone, contactId.toString())

                    }
                    contactList = obj
                    return contactList
                }
            }
            contact.close()

        }
        return contactList!!

    }


}