package com.example.xuedanzoustressmeter.ui.stressmeter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.xuedanzoustressmeter.R

// internal shows that this class is only used for the current module
class GridAdapter(
    private val context: Context,
    private val image_array: IntArray
): BaseAdapter(){
    private var layout_inflater: LayoutInflater? = null
    private lateinit var image_view: ImageView

    override fun getCount(): Int{
        return image_array.size
    }

    override fun getItem(position: Int): Any?{
        return image_array[position]
    }

    override fun getItemId(position: Int): Long{
        return position.toLong()
    }
    override fun getView(position: Int,
        inconvert_view: View?, parent: ViewGroup?
    ): View?{
        var convert_view = inconvert_view
        if(layout_inflater == null){
            layout_inflater=
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if(convert_view == null){
            convert_view = layout_inflater!!.inflate(R.layout.item_gridview, null)
        }

        image_view = convert_view!!.findViewById<ImageView>(R.id.img_gridview)
        image_view.setImageResource(image_array[position])
        return convert_view
    }

}