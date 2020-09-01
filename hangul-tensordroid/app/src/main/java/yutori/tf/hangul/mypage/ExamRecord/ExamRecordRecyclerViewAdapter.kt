package yutori.tf.hangul.mypage.ExamRecord

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import yutori.tf.hangul.R

class ExamRecordRecyclerViewAdapter(val ctx : Context, val dataList : ArrayList<ExamRecordData>)
    : RecyclerView.Adapter<ExamRecordRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view : View = LayoutInflater.from(ctx).inflate(R.layout.rv_exam_record, parent, false)
        return Holder(view)

    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.date.text = dataList[position].date
        holder.time.text = dataList[position].time
        holder.sentenceType.text = dataList[position].sentenceType
        holder.levelType.text = dataList[position].levelType
        holder.numType.text = dataList[position].numType
        holder.score.text = dataList[position].score.toString()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.item_exam_date) as TextView
        var time: TextView = itemView.findViewById(R.id.item_exam_time) as TextView
        var sentenceType: TextView = itemView.findViewById(R.id.item_exam_sentenceType) as TextView
        var levelType: TextView = itemView.findViewById(R.id.item_exam_levelType) as TextView
        var numType: TextView = itemView.findViewById(R.id.item_exam_numType) as TextView
        var score: TextView = itemView.findViewById(R.id.item_exam_score) as TextView
    }
}