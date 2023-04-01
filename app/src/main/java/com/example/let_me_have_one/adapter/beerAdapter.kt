package com.example.let_me_have_one.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.let_me_have_one.databinding.CardViewBinding
import com.example.let_me_have_one.db.model

class beerAdapter : RecyclerView.Adapter<beerAdapter.BeerViewHolder>() {

    lateinit var binding : CardViewBinding


    inner class BeerViewHolder(itemView: CardViewBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        binding = CardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BeerViewHolder(binding)
    }

    val differCallBack = object :DiffUtil.ItemCallback<model>(){
        override fun areItemsTheSame(oldItem: model, newItem: model): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: model, newItem: model): Boolean {
            return oldItem.hashCode()==newItem.hashCode()
        }

    }

    val differ= AsyncListDiffer(this,differCallBack)


    fun submitList(list : List<model>) = differ.submitList(list)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {

        val model = differ.currentList[position]
        holder.itemView.apply {

            Glide.with(this)
                .load(model.image)
                .dontTransform()
                .into(binding.beerImage)

           // binding.beerImage.setImageBitmap(model.image)
                binding.tagLine.setText(model.tagLine)
                binding.title.setText(model.name)

            setOnClickListener{
                onItemClickListener?.let{
                    it(model)
                }
            }
        }

    }

    private var onItemClickListener : ((model) -> Unit)? = null

    fun setOnItemClickListener(listener : (model)->Unit){
        onItemClickListener = listener
    }
}