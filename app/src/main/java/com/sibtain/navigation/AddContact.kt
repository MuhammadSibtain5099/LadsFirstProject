package com.sibtain.navigation

import android.content.ContentProviderOperation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_contact.*
import java.io.ByteArrayOutputStream




class AddContact : AppCompatActivity() {
    private val IMAGE_PICK_GALLERY_CODE = 200
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        imgThumb.setOnClickListener {
            openGalleryIntent()
        }
        button2.setOnClickListener{
            //saveContact()
            addContact("Sibtain","sunnyabbas5099@gmail.com","03155022905")
        }
    }
    private fun saveContact(){
        val name = edName.text.toString().trim()
        val phone = edPhone.text.toString().trim()

        val cpo = ArrayList<ContentProviderOperation>()

        val rawContactId = cpo.size
        cpo.add(ContentProviderOperation.newInsert(
            ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null)
            .build())

        cpo.add(ContentProviderOperation.newInsert(
            ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContactId)
            .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name)
            .build())
        cpo.add(ContentProviderOperation.newInsert(
            ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContactId)
            .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,phone)
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            .build())
        val imageByte = imageUriToBytes()
        if (imageByte!=null){
            cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContactId)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,imageByte).build())


        }else{

        }
    }
    private fun imageUriToBytes():ByteArray?{
        val bitMap:Bitmap
        val baos : ByteArrayOutputStream?
        return try {
            bitMap = if (Build.VERSION.SDK_INT<28){
                MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
            }else{
                val source = ImageDecoder.createSource(contentResolver,imageUri!!)
                ImageDecoder.decodeBitmap(source)
            }
            baos= ByteArrayOutputStream()
            bitMap.compress(Bitmap.CompressFormat.JPEG,50,baos)
            baos.toByteArray()

        }catch (e: Exception){
            null
        }
    }
    private fun openGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri = data!!.data
                imgThumb.setImageURI(imageUri)
            } else if (requestCode == 1) {
                // we are checking if the request code is 1
                if (resultCode == RESULT_OK) {
                    // if the result is ok we are displaying a toast message.
                    Toast.makeText(this, "Contact has been added.", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
                // else we are displaying a message as contact addition has cancelled.
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(
                        this, "Cancelled Added Contact",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_LONG).show()
        }
    }
    private fun addContact(name: String, email: String, phone: String) {
        // in this method we are calling an intent and passing data to that
        // intent for adding a new contact.
        val contactIntent = Intent(ContactsContract.Intents.Insert.ACTION)
        contactIntent.type = ContactsContract.RawContacts.CONTENT_TYPE
        contactIntent
            .putExtra(ContactsContract.Intents.Insert.NAME, name)
            .putExtra(ContactsContract.Intents.Insert.PHONE, phone)
            .putExtra(ContactsContract.Intents.Insert.EMAIL, email)

        startActivityForResult(contactIntent, 1)
    }


}