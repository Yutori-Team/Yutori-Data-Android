package yutori.tf.hangul.mypage.ExamRecord

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetExamRecordResponse
import java.time.LocalDateTime

class ExamRecordRecyclerViewAdapter(val ctx: Context, val dataList: ArrayList<GetExamRecordResponse>)
    : RecyclerView.Adapter<ExamRecordRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_exam_record, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val datetime: LocalDateTime = LocalDateTime.parse((dataList[position].time))
        val time = datetime.toLocalTime().hour.toString() + ":" + datetime.toLocalTime().minute.toString()

        when (dataList[position].sentenceTypes) {
            "SENTENCE" -> holder.sentenceType.text = "문장"
            "SING" -> holder.sentenceType.text = "노래"
        }

        when (dataList[position].levelTypes) {
            "TOP" -> holder.levelType.text = "상"
            "MIDDLE" -> holder.levelType.text = "중"
            "LOW" -> holder.levelType.text = "하"
        }

        when (dataList[position].numTypes) {
            "NUM1" -> holder.numType.text = "1"
            "NUM2" -> holder.numType.text = "2"
            "NUM3" -> holder.numType.text = "3"
            "NUM4" -> holder.numType.text = "4"
            "NUM5" -> holder.numType.text = "5"
        }

        holder.date.text = datetime.toLocalDate().toString()
        holder.time.text = time
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