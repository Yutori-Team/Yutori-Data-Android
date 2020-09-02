package yutori.tf.hangul.data

data class GetExamRecordResponse(
        var id: Long,
        var userId: Long,
        var sentenceTypes: String,
        var levelTypes: String,
        var numTypes: String,
        var score: Int,
        var time: String
)