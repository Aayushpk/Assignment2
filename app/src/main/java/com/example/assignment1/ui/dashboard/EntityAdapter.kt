package com.example.assignment1.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment1.R
import com.example.assignment1.data.model.Entity


class EntityAdapter(
    private val onClick: (Entity) -> Unit
) : ListAdapter<Entity, EntityAdapter.EntityViewHolder>(DIFF) {

    inner class EntityViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.itemProperty1)
        private val subtitle: TextView = itemView.findViewById(R.id.itemProperty2)

        @SuppressLint("SetTextI18n")
        fun bind(entity: Entity) {
            title.text = entity.ticker
            subtitle.text = "${entity.assetType}  •  $${entity.currentPrice}"
            itemView.setOnClickListener { onClick(entity) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entity, parent, false)
        return EntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        // DiffUtil lets ListAdapter animate/update efficiently.
        private val DIFF = object : DiffUtil.ItemCallback<Entity>() {
            override fun areItemsTheSame(a: Entity, b: Entity) = a.ticker == b.ticker
            override fun areContentsTheSame(a: Entity, b: Entity) = a == b
        }
    }
}
