package com.example.maptravel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(private val dataSet:ArrayList<PlaceModel>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>()
{
    private var onClickListener: OnClickListener? = null

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val name:TextView = view.findViewById(R.id.tv_place_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.place_item_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = dataSet[position].name
        holder.itemView.setOnClickListener {
            if (onClickListener!=null)
            {
                onClickListener!!.onClick(position)
            }
        }
    }

    fun setOnClickListener(onClickListener:OnClickListener)
    {
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onClick(position: Int)
    }
}