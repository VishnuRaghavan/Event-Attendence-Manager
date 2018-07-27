package com.example.vishnuraghavan.attendence.Event



import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vishnuraghavan.attendence.R
import kotlinx.android.synthetic.main.row.view.*
import java.util.*

class EventAdapter(val events: ArrayList<Event>): RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val layout = LayoutInflater.from(parent.context).inflate(R.layout.row, parent,false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events.get(position));
    }

    override fun getItemCount(): Int {
        return events.size
    }


    class ViewHolder (val v: View): RecyclerView.ViewHolder(v){

        fun bind(item: Event){
            v.name.text = item.name
            v.venue.text = item.venue
            v.date.text = item.Date
        }

    }
}