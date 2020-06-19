package com.eugenethedev.introubleapp.presentation.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eugenethedev.introubleapp.R
import com.eugenethedev.introubleapp.domain.entities.Receiver
import io.realm.RealmList
import kotlinx.android.synthetic.main.item_receiver.view.*

class ReceiversAdapter(
    private val receivers: RealmList<Receiver>,
    private val removeReceiver: (Receiver) -> Unit
) : RecyclerView.Adapter<ReceiversAdapter.ReceiverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiverViewHolder {
        val vView = LayoutInflater.from(parent.context).inflate(R.layout.item_receiver, parent, false)
        return ReceiverViewHolder(vView)

    }
    
    init {
        receivers.addChangeListener { _, changeSet ->
            changeSet.insertions.takeIf { it.isNotEmpty() }?.let {
                notifyItemRangeInserted(it.first(), it.size)
            }

            changeSet.deletions.takeIf { it.isNotEmpty() }?.let {
                notifyItemRemoved(it.first())
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = receivers.size

    override fun onBindViewHolder(holder: ReceiverViewHolder, position: Int) {
        val receiver = receivers[position]!!
        holder.bind(receiver, removeReceiver)
    }

    class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(receiver: Receiver, removeAction: (Receiver) -> Unit) = itemView.apply {
            receiverName.text = receiver.name
            receiverNumber.text = receiver.number
            removeReceiver.setOnClickListener { removeAction(receiver) }
        }
    }
}
