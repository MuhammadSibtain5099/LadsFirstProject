package com.sibtain.navigation.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sibtain.navigation.ContactDetails
import com.sibtain.navigation.R
import com.sibtain.navigation.helper.Helpers
import com.sibtain.navigation.model.Contacts
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
import java.util.*

class RecentCallLogsListStickeyHeaderAdapter(
    private var contactList: List<Contacts>,
    private var contexts: Context
) : RecyclerView.Adapter<RecentCallLogsListStickeyHeaderAdapter.HistoryviewHolder>() , StickyRecyclerHeadersAdapter<RecentCallLogsListStickeyHeaderAdapter.HeaderViewHolder>,
    SectionIndexer {


    private var sectionsTranslator = HashMap<Int, Int>()
    private var mSectionPositions: MutableList<Int> = ArrayList()
    private val mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentCallLogsListStickeyHeaderAdapter.HistoryviewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return HistoryviewHolder(v)
    }

    override fun onBindViewHolder(holder: HistoryviewHolder, position: Int) {
        holder.itemName.text = contactList[position].name
        holder.itemPhoneNumber.text = contactList[position].number
        holder.itemName.setOnClickListener {
            Toast.makeText(contexts, contactList[position].name, Toast.LENGTH_LONG).show()
        }
        holder.itemPhoneNumber.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + holder.itemPhoneNumber.text.toString())
                )
                contexts.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
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

    override fun getHeaderId(position: Int): Long {


        return contactList[position].name[0].toLong()
    }


    override fun onCreateHeaderViewHolder(parent: ViewGroup): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(headerViewHolder: HeaderViewHolder, i: Int) {
        var nameApha = contactList[i].name[0].uppercase()
        headerViewHolder.tvHeader.text = nameApha
    }



    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeader: TextView

        init {
            tvHeader = itemView.findViewById(R.id.textView)
        }
    }

    inner class HistoryviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.txtName)
        var itemPhoneNumber: TextView = itemView.findViewById(R.id.txtPhoneNumber)

        init {
            itemView.setOnClickListener {
                val intent = Intent(contexts, ContactDetails::class.java)
                intent.putExtra("contactId", contactList[adapterPosition].contactId.toInt())
                contexts.startActivity(intent)
            }
        }
    }

}