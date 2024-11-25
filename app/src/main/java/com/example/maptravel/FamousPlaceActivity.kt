package com.example.maptravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FamousPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_famous_place)

        val data = Constants.getPlaceList()
        setAdapterRecyclerView(data)
    }


    private fun setAdapterRecyclerView(data:ArrayList<PlaceModel>)
    {
        val recyclerView = findViewById<RecyclerView>(R.id.rvPlaceList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PlaceAdapter(data)
        recyclerView.adapter = adapter

        adapter.setOnClickListener(object  : PlaceAdapter.OnClickListener{
            override fun onClick(position: Int) {
                val intent = Intent(this@FamousPlaceActivity,StreetViewsActivity::class.java)
                intent.putExtra("position", position)
                startActivity(intent)
            }
        })
    }
}