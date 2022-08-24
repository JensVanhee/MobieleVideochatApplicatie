package com.example.mobielevideochatapplicatie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(contactList: MutableList<String>, clickListener: ClickListener): RecyclerView.Adapter<ContactAdapter.Viewholder>() {

    private var contacten: MutableList<String> = contactList
    private var clickListener: ClickListener = clickListener

    fun setData(contact: MutableList<String>) {
        this.contacten = contact
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_item, parent, false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val contact = contacten[position]
        holder.name.text = contacten[position]

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(contact)
        }
    }

    override fun getItemCount(): Int {
        return contacten.size
    }

    interface ClickListener{
        fun clickedItem(contact: String)
    }

    inner class Viewholder(itemView:View) : RecyclerView.ViewHolder(itemView){
        lateinit var name: TextView

        init {
            name = itemView.findViewById(R.id.name)
        }
    }
}