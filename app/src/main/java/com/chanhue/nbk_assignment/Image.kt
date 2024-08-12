package com.chanhue.nbk_assignment



data class Image(
    val thumbnailUrl: String,
    val displaySitename: String,
    val datetime: String,
    var isLiked: Boolean = false // 좋아요 관리
)


data class KakaoResponse(
    val documents: List<Document>
)

data class Document(
    val thumbnail_url: String,
    val display_sitename: String,
    val datetime: String
)

