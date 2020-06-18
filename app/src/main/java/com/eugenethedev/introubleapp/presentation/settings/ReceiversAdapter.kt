package com.eugenethedev.introubleapp.presentation.settings

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eugenethedev.introubleapp.R
import kotlinx.android.synthetic.main.item_receiver.view.*
import kotlinx.android.synthetic.main.item_receiver_add.view.*

class ReceiversAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_EXIST = 0
        private const val TYPE_ADD = 1
    }

    data class AddData(
        var name: String = "",
        var number: String = ""
    )

    private val receivers = mutableListOf("Bob", "Jack", "Another", "Name")
    private var newReceivers = mutableListOf<AddData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_EXIST) {
            val vView = LayoutInflater.from(parent.context).inflate(R.layout.item_receiver, parent, false)
            ReceiverViewHolder(vView)
        } else {
            val vView = LayoutInflater.from(parent.context).inflate(R.layout.item_receiver_add, parent, false)
            AddViewHolder(vView)
        }
    }

    override fun getItemCount() = receivers.size + newReceivers.size

    override fun getItemViewType(position: Int): Int {
        return if (position < receivers.size) {
            TYPE_EXIST
        } else {
            TYPE_ADD
        }
    }

    fun addNewAddItem() {
        newReceivers.add(AddData())
        notifyItemInserted(itemCount - 1)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_EXIST) {
            (holder as ReceiverViewHolder).bind(receivers[position]) {
                receivers.removeAt(position)
                notifyItemRemoved(position)
                notifyDataSetChanged()
            }
        } else {
            val newReceiverPosition = position - receivers.size
            (holder as AddViewHolder).bind(newReceivers[newReceiverPosition]) {
                newReceivers.removeAt(newReceiverPosition)
                notifyItemRemoved(position)
                notifyDataSetChanged()
            }
        }
    }

    class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(receiver: String, removeAction: () -> Unit) = itemView.apply {
            receiverName.text = receiver
            receiverNumber.text = "+712345678910"
            removeReceiver.setOnClickListener { removeAction() }
        }
    }

    class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(addData: AddData, removeAction: () -> Unit) = itemView.apply {
            receiverNameEdit.text = SpannableStringBuilder(addData.name)
            receiverNumberEdit.text = SpannableStringBuilder(addData.number)
            removeNewReceiver.setOnClickListener { removeAction() }
            receiverNameEdit.setOnFocusChangeListener { _, _ ->
                addData.name = receiverNameEdit.text.toString()
            }
            receiverNumberEdit.setOnFocusChangeListener { _, _ ->
                addData.number = receiverNumberEdit.text.toString()
            }
        }
    }
}
