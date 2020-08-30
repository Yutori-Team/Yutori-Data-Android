package yutori.tf.hangul.data;

data class PostCheckResponse (
        var score: Long,
        var resCheckDtoList: List<ResCheckDtoList>
)
