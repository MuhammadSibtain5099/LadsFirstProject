package com.sibtain.navigation.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.SectionIndexer
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sibtain.navigation.ContactDetails
import com.sibtain.navigation.R
import com.sibtain.navigation.helper.Helpers
import com.sibtain.navigation.model.Contacts
import java.util.ArrayList
import java.util.HashMap

class RecyclerViewAdapter(
    private var contactList: List<Contacts>,
    private var context: Context
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(), SectionIndexer {

    private var sectionsTranslator = HashMap<Int, Int>()
    private var mSectionPositions: MutableList<Int> = ArrayList()
    private val mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.itemName.text = contactList[position].name
        holder.itemPhoneNumber.text = contactList[position].number
        holder.itemName.setOnClickListener {
            Toast.makeText(context, contactList[position].name, Toast.LENGTH_LONG).show()
        }
        holder.itemPhoneNumber.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + holder.itemPhoneNumber.text.toString())
                )
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }

    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.txtName)
        var itemPhoneNumber: TextView = itemView.findViewById(R.id.txtPhoneNumber)

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, ContactDetails::class.java)
                intent.putExtra("contactId", contactList[adapterPosition].contactId.toInt())
                context.startActivity(intent)
            }
        }

    }

    override fun getSectionForPosition(position: Int): Int {
        return 0
    }

    override fun getSections(): Array<out Any> {

        val sections: MutableList<String> = ArrayList(27)
        val alphabetFull = ArrayList<String>()
        mSectionPositions = ArrayList()
        run {
            var i = 0
            val size = contactList.size
            while (i < size) {
                val section = contactList[i].name[0].uppercaseChar().toString()
                if (!sections.contains(section)) {
                    sections.add(section)
                    mSectionPositions.add(i)
                }
                i++
            }
        }
        for (element in mSections) {
            alphabetFull.add(element.toString())
        }
        sectionsTranslator = Helpers.sectionsHelper(sections, alphabetFull)
        return alphabetFull.toTypedArray()
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mSectionPositions[sectionsTranslator[sectionIndex]!!]
    }


}