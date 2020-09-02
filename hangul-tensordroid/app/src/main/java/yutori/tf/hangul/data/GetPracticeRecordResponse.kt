package yutori.tf.hangul.data

data class GetPracticeRecordResponse(
        var id: Long,
        var userId: Long,
        var sentenceTypes: String,
        var levelTypes: String,
        var numTypes: String,
        var time: String
)