package com.example.vishnuraghavan.attendence.Event


import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vishnuraghavan.attendence.R
import com.example.vishnuraghavan.attendence.dashboard.dashboardActivity
import kotlinx.android.synthetic.main.row.view.*
import org.jetbrains.anko.intentFor
import java.util.*

class EventAdapter(val events: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    // this function adds the custom layout to recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(layout)
    }

    // this fucntion binds each piece of record to the recycler view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events.get(position))
    }

    // this function return the count of the events arraylist
    override fun getItemCount(): Int {
        return events.size
    }


    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        // custom function for binding data to the recycler view
        fun bind(item: Event) {
            v.name.text = item.name
            v.venue.text = item.venue
            v.date.text = item.Date
            // setting up onclick event to make intents
            v.row.setOnClickListener {
                val context = v.context
                context.startActivity(context.intentFor<dashboardActivity>(
                        "event_id" to item.id,
                        "name" to item.name,
                        "days" to item.days,
                        "date" to item.Date,
                        "venue" to item.venue))
            }
        }
    }
}

// custom data class for storing structre of data
data class Event(val id: String,val name:  String, val Date: String, val venue: String, val days: Int, val numPart: Int){

}