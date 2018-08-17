package com.example.vishnuraghavan.attendence.dashboard.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vishnuraghavan.attendence.DetailsActivity
import com.example.vishnuraghavan.attendence.R
import kotlinx.android.synthetic.main.list_row.view.*
import org.jetbrains.anko.intentFor

class MemberAdapter(val member: ArrayList<Member>) : RecyclerView.Adapter<MemberAdapter.viewHolder>() {

    // this function adds the custom layout to recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return viewHolder(layout)
    }

    // this fucntion binds each piece of record to the recycler view
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.Bind(member.get(position))
    }

    // this function return the count of the member arraylist
    override fun getItemCount(): Int {
        return member.size
    }


    class viewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        // custom function for binding data to the recycler view
        fun Bind(mem: Member) {
            v.name.text = mem.name
            v.registrationTextView.text = mem.regId

            v.setOnClickListener(){
                v.context.startActivity(v.context.intentFor<DetailsActivity>(
                        "name" to mem.name,
                        "email" to mem.email,
                        "phone" to mem.phoneNumber,
                        "org" to mem.organization,
                        "regId" to mem.regId
                ))
            }

        }

    }
}

// custom data class for storing structre of data
data class Member(var name: String, var phoneNumber: String, var email: String, var organization: String, var regId: String) {

}