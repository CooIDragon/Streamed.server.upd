package com.streamed.domain.usecase

import com.streamed.data.models.CommentsModel
import com.streamed.domain.repository.CommentsRepository

class CommentsUseCase (
    private val commentsRepository: CommentsRepository
) {
    suspend fun addComment(comment: CommentsModel) {
        commentsRepository.addComment(comment = comment)
    }

    suspend fun deleteComment(commentId: Int) {
        commentsRepository.deleteComment(commentId = commentId)
    }

    suspend fun getAllComments(webinarId: Int): List<CommentsModel> {
        return commentsRepository.getAllComments(webinarId)
    }
}