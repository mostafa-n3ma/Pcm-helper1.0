package com.mostafan3ma.android.pcm_helper10.lines.operations.Points

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.databinding.BendItemBinding
import com.mostafan3ma.android.pcm_helper10.databinding.PointItemBinding
import java.lang.ClassCastException

private const val POINT_ITEM_VIEW_TYPE=1
private const val BEND_ITEM_VIEW_TYPE=0
class PointsAdapter(private val clickListener:PointListener)
    :ListAdapter<DamagePoint,RecyclerView.ViewHolder>(PointDiffCallBack())
{



    override fun getItemViewType(position: Int): Int {
        val item: DamagePoint =getItem(position)
        return when(item.is_point){
            true-> POINT_ITEM_VIEW_TYPE
            else-> BEND_ITEM_VIEW_TYPE
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType){
           POINT_ITEM_VIEW_TYPE->PointViewHolder.from(parent)
           BEND_ITEM_VIEW_TYPE->BendViewHolder.from(parent)
           else-> throw ClassCastException("Unknown view type $viewType")
       }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pointItem=getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(pointItem)
        }

        when(holder){
            is PointViewHolder-> holder.bindPoint(pointItem)
            is BendViewHolder-> holder.bindBend(pointItem)
        }

    }


}




class PointViewHolder(private val binding:PointItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bindPoint(pointItem:DamagePoint){
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


class BendViewHolder(private val binding:BendItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bindBend(bendItem:DamagePoint){
        binding.bend=bendItem
        binding.executePendingBindings()
    }
    companion object{
        fun from(parent: ViewGroup):BendViewHolder{
            val layoutInflater=LayoutInflater.from(parent.context)
            val binding=BendItemBinding.inflate(layoutInflater,parent,false)
            return BendViewHolder(binding)
        }
    }

}


class PointDiffCallBack : DiffUtil.ItemCallback<DamagePoint>() {
    override fun areItemsTheSame(oldItem: DamagePoint, newItem: DamagePoint): Boolean {
        return oldItem.no == newItem.no
    }

    override fun areContentsTheSame(oldItem: DamagePoint, newItem: DamagePoint): Boolean {
    return oldItem==newItem
    }

}


class PointListener(val clickListener: (point: DamagePoint) -> Unit) {
    fun onClick(point: DamagePoint) = clickListener(point)
}