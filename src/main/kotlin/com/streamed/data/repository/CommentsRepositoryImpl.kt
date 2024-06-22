package com.streamed.data.repository

import com.streamed.data.models.CommentsModel
import com.streamed.data.models.tables.CommentsTable
import com.streamed.domain.repository.CommentsRepository
import com.streamed.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CommentsRepositoryImpl : CommentsRepository {
    override suspend fun addComment(comment: CommentsModel) {
        dbQuery {
            CommentsTable.insert {
                table ->
                table[ownerName] = comment.ownerName
                table[ownerSurname] = comment.ownerSurname
                table[webinarId] = comment.webinarId
                table[text] = comment.text
                table[isAnon] = comment.isAnon
            }
        }
    }

    override suspend fun deleteComment(commentId: Int) {
        dbQuery {
            CommentsTable.deleteWhere { CommentsTable.id.eq(commentId) }
        }
    }

    override suspend fun getAllComments(webinarId: Int): List<CommentsModel> {
        return dbQuery {
            CommentsTable.select {
                CommentsTable.webinarId eq webinarId
            }.mapNotNull { rowToComments(it) }
        }
    }

    private fun rowToComments(row: ResultRow): CommentsModel {
        return CommentsModel(
            id = row[CommentsTable.id],
            ownerName = row[CommentsTable.ownerName],
            ownerSurname = row[CommentsTable.ownerSurname],
            webinarId = row[CommentsTable.webinarId],
            text = row[CommentsTable.text],
            isAnon = row[CommentsTable.isAnon]
        )
    }
}
