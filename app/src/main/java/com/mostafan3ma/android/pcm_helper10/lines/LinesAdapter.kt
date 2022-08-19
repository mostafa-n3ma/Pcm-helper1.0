package com.mostafan3ma.android.pcm_helper10.lines

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mostafan3ma.android.pcm_helper10.R
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
        when(item.end_work_date){
            ""->{
                binding.lineEndDate.text="Not Finished yet"
                binding.lineEndDate.apply {
                    setTextColor(resources.getColor(R.color.oil_txt_color))
                }
            }
            else->{
                binding.lineEndDate.text=item.end_work_date

            }
        }
        when(item.type){
            "Oil"->binding.lineType.apply {
                setTextColor(resources.getColor(R.color.oil_txt_color))
            }
            else->binding.lineType.apply {
                setTextColor(resources.getColor(R.color.water_txt_color))
            }
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



