package com.ossipeeriver.ravenandroidawarenesskit.ui.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ossipeeriver.ravenandroidawarenesskit.R
import com.ossipeeriver.ravenandroidawarenesskit.database.SavedLocation
import com.ossipeeriver.ravenandroidawarenesskit.ui.location.SavedLocationListAdapter.SavedLocationViewHolder

class SavedLocationListAdapter : ListAdapter<SavedLocation, SavedLocationViewHolder>(SAVED_LOCATION_COMPARATOR), Filterable {

    private var savedLocationFilteredList = ArrayList<SavedLocation>()
    private var originalSavedLocationList = ArrayList<SavedLocation>()
    init {
        savedLocationFilteredList = ArrayList(currentList)
        originalSavedLocationList = ArrayList(currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedLocationViewHolder {
        return SavedLocationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SavedLocationViewHolder, position: Int) {
        val current = savedLocationFilteredList[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int {
        return savedLocationFilteredList.size
    }

    override fun submitList(list: List<SavedLocation>?) {
        super.submitList(list)
        // Update with the descriptions from the new list
        originalSavedLocationList = list as ArrayList<SavedLocation>? ?: ArrayList()
        savedLocationFilteredList = ArrayList(originalSavedLocationList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    filterResults.values = originalSavedLocationList
                } else {
                    val filteredList = ArrayList<SavedLocation>()
                    val filterInput = constraint.toString().lowercase().trim()
                    for (location in originalSavedLocationList) {
                        if (location.description.lowercase().contains(filterInput) || location.latitudeAndLongitude.contains(filterInput)) {
                            filteredList.add(location)
                        }
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                savedLocationFilteredList.clear()
                savedLocationFilteredList.addAll(results?.values as ArrayList<SavedLocation>)
                notifyDataSetChanged()
            }
        }
    }

    class SavedLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val savedLocationDigits: TextView = itemView.findViewById(R.id.saved_location_digits)
        private val savedLocationDescription: TextView = itemView.findViewById(R.id.saved_location_description)

        fun bind(savedLocation: SavedLocation) {
            savedLocationDigits.text = savedLocation.latitudeAndLongitude
            savedLocationDescription.text = savedLocation.description
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
                return oldItem.description == newItem.description
            }
        }
    }
}