package com.streamed.domain.repository

import com.streamed.data.models.CommentsModel

interface CommentsRepository {
    suspend fun addComment(comment: CommentsModel)
    suspend fun deleteComment(commentId: Int)
    suspend fun getAllComments(webinarId: Int): List<CommentsModel>
}