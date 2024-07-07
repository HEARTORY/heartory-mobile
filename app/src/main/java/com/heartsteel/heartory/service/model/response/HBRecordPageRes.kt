package com.heartsteel.heartory.service.model.response

import com.heartsteel.heartory.service.model.domain.Activity
import com.heartsteel.heartory.service.model.domain.Emotion

data class HBRecordPageRes (
    val content: List<HBRecordRes>,
    val pageable: Pageable,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val first: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)

data class HBRecordRes(
    val id: Int,
    val hr: Int,
    val hrv: Any?,
    val deviceId: Any?,
    val numCycles: Any?,
    val emotion: Emotion,
    val activity: Activity
)

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

data class Pageable(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: Sort,
    val offset: Int,
    val unpaged: Boolean,
    val paged: Boolean
)