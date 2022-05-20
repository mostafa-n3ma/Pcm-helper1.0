package com.mostafan3ma.android.pcm_helper10.lines.operations.Points

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mostafan3ma.android.pcm_helper10.R
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.databinding.BendItemBinding
import com.mostafan3ma.android.pcm_helper10.databinding.PointItemBinding
import kotlinx.android.synthetic.main.bend_item.view.*
import kotlinx.android.synthetic.main.point_item.view.*
import kotlinx.android.synthetic.main.point_item.view.delete_point_btn
import java.lang.ClassCastException

private const val POINT_ITEM_VIEW_TYPE=1
private const val BEND_ITEM_VIEW_TYPE=0
class PointsAdapter(private val deleteListener:DeleteListener,private val editListener: EditListener)
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




        when(holder){
            is PointViewHolder-> {
                holder.bindPoint(pointItem)
                holder.itemView.delete_point_btn.setOnClickListener {
                    deleteListener.onDelete(pointItem)

                }
                holder.itemView.point_gps_txt.setText("${pointItem.gps_x};${pointItem.gps_y}")
                holder.itemView.edit_point_btn.setOnClickListener {
                    editListener.onEdit(pointItem)
                }
            }

            is BendViewHolder-> {
                holder.bindBend(pointItem)
                holder.itemView.delete_bend_btn.setOnClickListener {
                    deleteListener.onDelete(pointItem)
                }
                holder.itemView.bend_gps_txt.setText("${pointItem.gps_x};${pointItem.gps_y}")
            }
        }

    }


}




class PointViewHolder(private val binding:PointItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bindPoint(pointItem:DamagePoint){
        binding.point=pointItem
        binding.executePendingBindings()
        binding.pointArrowBtn.setOnClickListener {
            if (binding.pointExpandableLayout.visibility==View.GONE){
                TransitionManager.beginDelayedTransition(binding.pointCardView, AutoTransition())
                binding.pointExpandableLayout.visibility = View.VISIBLE
                binding.pointArrowBtn.setBackgroundResource(R.drawable.up_arrow)
            }else{
                TransitionManager.beginDelayedTransition(binding.pointCardView, AutoTransition())
                binding.pointExpandableLayout.visibility = View.GONE
                binding.pointArrowBtn.setBackgroundResource(R.drawable.down_arrow)
            }
        }
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
        binding.bendArrowBtn.setOnClickListener {
            if (binding.bendExpandableLayout.visibility==View.GONE){
                TransitionManager.beginDelayedTransition(binding.bendCardView, AutoTransition())
                binding.bendExpandableLayout.visibility = View.VISIBLE
                binding.bendArrowBtn.setBackgroundResource(R.drawable.up_arrow)
            }else{
                TransitionManager.beginDelayedTransition(binding.bendCardView, AutoTransition())
                binding.bendExpandableLayout.visibility = View.GONE
                binding.bendArrowBtn.setBackgroundResource(R.drawable.down_arrow)
            }
        }

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


class DeleteListener(val deleteListener: (point: DamagePoint) -> Unit) {
    fun onDelete(point: DamagePoint) = deleteListener(point)
}

class EditListener(val editListener:(point:DamagePoint)->Unit){
    fun onEdit(point: DamagePoint)=editListener(point)
}
