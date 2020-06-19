package com.eugenethedev.introubleapp.presentation.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eugenethedev.introubleapp.R
import kotlinx.android.synthetic.main.item_receiver.view.*

class ReceiversAdapter : RecyclerView.Adapter<ReceiversAdapter.ReceiverViewHolder>() {

    private val receivers = mutableListOf("Bob", "Jack", "Another", "Name")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiverViewHolder {
        val vView = LayoutInflater.from(parent.context).inflate(R.layout.item_receiver, parent, false)
        return ReceiverViewHolder(vView)

    }

    override fun getItemCount() = receivers.size


    fun addNewAddItem() {

    }

    override fun onBindViewHolder(holder: ReceiverViewHolder, position: Int) {
        holder.bind(receivers[position]) {
            receivers.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }

    }

    class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(receiver: String, removeAction: () -> Unit) = itemView.apply {
            receiverName.text = receiver
            receiverNumber.text = "+712345678910"
            removeReceiver.setOnClickListener { removeAction() }
        }
    }
}
