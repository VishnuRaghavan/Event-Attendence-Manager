package com.example.vishnuraghavan.attendence.dashboard.fragments.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vishnuraghavan.attendence.R
import kotlinx.android.synthetic.main.list_row.view.*

class AttendenceAdapter(val Dates: ArrayList<AttendDates>): RecyclerView.Adapter<AttendenceAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_fragment,parent,false)
        return viewHolder(layout)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.Bind(Dates.get(position))
    }

    override fun getItemCount(): Int {
        return Dates.size
    }


    class viewHolder(val v: View): RecyclerView.ViewHolder(v){

        fun Bind(date: AttendDates) {
            v.date.text = date.date
        }


    }
}

class AttendDates(var date: String){

}