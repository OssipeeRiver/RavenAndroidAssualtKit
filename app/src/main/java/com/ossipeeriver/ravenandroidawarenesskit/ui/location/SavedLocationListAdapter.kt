package com.ossipeeriver.ravenandroidawarenesskit.ui.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ossipeeriver.ravenandroidawarenesskit.R
import com.ossipeeriver.ravenandroidawarenesskit.database.SavedLocation
import com.ossipeeriver.ravenandroidawarenesskit.ui.location.SavedLocationListAdapter.SavedLocationViewHolder

class SavedLocationListAdapter : ListAdapter<SavedLocation, SavedLocationViewHolder>(SAVED_LOCATION_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedLocationViewHolder {
        return SavedLocationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SavedLocationViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.latitudeAndLongitude)
    }

    class SavedLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val savedLocationItemView: TextView = itemView.findViewById(R.id.saved_location_digits)

        fun bind(text: String?) {
            savedLocationItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): SavedLocationViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.saved_location_item, parent, false)
                return SavedLocationViewHolder(view)
            }
        }
    }

    companion object {
        private val SAVED_LOCATION_COMPARATOR = object : DiffUtil.ItemCallback<SavedLocation>() {
            override fun areItemsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
                return oldItem.latitudeAndLongitude == newItem.latitudeAndLongitude
            }
        }
    }
}