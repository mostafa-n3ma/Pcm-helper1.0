package com.mostafan3ma.android.pcm_helper10.Lines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLine
import com.mostafan3ma.android.pcm_helper10.databinding.LineItemBinding

class LinesAdapter(private val clickListener:LineListener)
    :ListAdapter<PipeLine,LineViewHolder>(LineDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        return LineViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val lineItem=getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(lineItem)
        }
        holder.bind(lineItem)
    }

}



class LineViewHolder(private val binding:LineItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(item:PipeLine){
        binding.pipeLine=item
        binding.executePendingBindings()
    }
    companion object{
        fun from(parent: ViewGroup):LineViewHolder{
            val layoutInflater=LayoutInflater.from(parent.context)
            val binding=LineItemBinding.inflate(layoutInflater,parent,false)
            return LineViewHolder(binding)
        }
    }
}




class LineDiffCallBack : DiffUtil.ItemCallback<PipeLine>() {
    override fun areItemsTheSame(oldItem: PipeLine, newItem: PipeLine): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PipeLine, newItem: PipeLine): Boolean {
        return oldItem == newItem
    }
}

class LineListener(val clickListener: (line: PipeLine) -> Unit) {
    fun onClick(line: PipeLine) = clickListener(line)
}