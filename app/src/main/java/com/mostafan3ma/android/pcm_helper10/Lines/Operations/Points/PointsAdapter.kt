package com.mostafan3ma.android.pcm_helper10.Lines.Operations.Points

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.databinding.PointItemBinding

class PointsAdapter(private val clickListener:PointListener)
    :ListAdapter<DamagePoint,PointViewHolder>(PointDiffCallBack())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
       return PointViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        val pointItem=getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(pointItem)
        }
        holder.bind(pointItem)
    }


}




class PointViewHolder(private val binding:PointItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(pointItem:DamagePoint){
        binding.point=pointItem
        binding.executePendingBindings()
    }
    companion object{
        fun from(parent:ViewGroup):PointViewHolder{
            val layoutInflater=LayoutInflater.from(parent.context)
            val binding=PointItemBinding.inflate(layoutInflater,parent,false)
            return PointViewHolder(binding)
        }
    }
}

class PointDiffCallBack : DiffUtil.ItemCallback<DamagePoint>() {
    override fun areItemsTheSame(oldItem: DamagePoint, newItem: DamagePoint): Boolean {
        return oldItem.db == newItem.db
    }

    override fun areContentsTheSame(oldItem: DamagePoint, newItem: DamagePoint): Boolean {
    return oldItem==newItem
    }

}


class PointListener(val clickListener: (point: DamagePoint) -> Unit) {
    fun onClick(point: DamagePoint) = clickListener(point)
}