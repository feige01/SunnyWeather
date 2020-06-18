package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment :Fragment() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter:PlaceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager=LinearLayoutManager(activity)
        recycleView.layoutManager=layoutManager
        adapter= PlaceAdapter(this,viewModel.placeList)
        recycleView.adapter=adapter
        searchPlaceEdit.addTextChangedListener {
            val content=it.toString()
            if(content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                recycleView.visibility=View.GONE
                bgImageView.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(this, Observer { result->
            val places=result.getOrNull()
            if(places!=null){
                Log.e("feifei","777")
                recycleView.visibility=View.VISIBLE
                bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places as Collection<Place> )
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未查询到任何地点",Toast.LENGTH_SHORT).show()
                Log.e("feifei","666")
                result.exceptionOrNull()?.printStackTrace()
            }
        })

    }

}