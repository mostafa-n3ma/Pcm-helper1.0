package com.mostafan3ma.android.pcm_helper10.lines

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import com.mostafan3ma.android.pcm_helper10.databinding.LineItemBinding

class LinesAdapter(private val clickListener:LineListener,private val longListener:LongClickListener)
    :ListAdapter<PipeLine,LineViewHolder>(LineDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        return LineViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val lineItem=getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(lineItem)
        }
        holder.itemView.setOnLongClickListener {
            longListener.onLongClick(lineItem,it)
            return@setOnLongClickListener true
        }
        holder.bind(lineItem)
    }

}



class LineViewHolder(private val binding:LineItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(item: PipeLine){
        binding.pipeLine=item
        if(item.end_work_date!!.isEmpty()){
            binding.lineEndDate.text="Not Finished yet"
            binding.lineEndDate.setTextColor(Color.RED)
        }else{
            binding.lineEndDate.text=item.end_work_date
        }
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

class LongClickListener(val longListener:(line:PipeLine,view: View)->Unit){
    fun onLongClick(line: PipeLine,view: View)=longListener(line,view)
}
