package com.example.vishnuraghavan.attendence.dashboard.fragments.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vishnuraghavan.attendence.R
import kotlinx.android.synthetic.main.list_row.view.*

class MemberAdapter(val member: ArrayList<Member>) : RecyclerView.Adapter<MemberAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return viewHolder(layout)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.Bind(member.get(position))
    }

    override fun getItemCount(): Int {
        return member.size
    }


    class viewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        fun Bind(mem: Member) {
            v.registrationTextView.text = mem.regId
        }

    }
}

class Member(var name: String, var phoneNumber: String, var email: String, var organization: String, var regId: String) {

}