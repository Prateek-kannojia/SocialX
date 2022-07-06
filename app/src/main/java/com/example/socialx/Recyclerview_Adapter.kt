package com.example.socialx

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Recyclerview_Adapter(private val context: Context):RecyclerView.Adapter<Recyclerview_Adapter.viewholder>() {

    private val items:ArrayList<Model> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return viewholder(view)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val currentitem= items[position]
        holder.title.text = currentitem.title
        holder.publishedat.text=currentitem.publishedAt
        holder.description.text=currentitem.description
        Picasso.get().load(currentitem.urlToImage).into(holder.image)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateNews(updatednews:ArrayList<Model>){
        items.clear()
        items.addAll(updatednews)
        notifyDataSetChanged()
    }
    class viewholder(itemview:View):RecyclerView.ViewHolder(itemview) {
        var publishedat:TextView=itemview.findViewById(R.id.news_published)
        var title:TextView=itemview.findViewById(R.id.news_title)
        var image:ImageView=itemview.findViewById(R.id.news_image)
        val description:TextView=itemview.findViewById(R.id.news_description)
        val source:TextView=itemview.findViewById(R.id.news_source)
    }
}
