package org.isi.domain.mapper

import org.isi.domain.models.Chat
import org.isi.domain.models.Command
import org.isi.domain.models.MessageType
import org.isi.domain.models.TaskType
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.isi.data.clients.ChatMessage
import java.util.*

fun createCommandFromString(content: String, favName: String? = null, messageType: MessageType = MessageType.USER, task: TaskType? = TaskType.OTHER_TOPICS): Command {
    val chat = Chat(
        id = UUID.randomUUID().toString(),
    )

    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

    return Command(
        id = UUID.randomUUID().toString(),
        chat = chat,
        content = content,
        favName = favName,
        messageType = messageType,
        task = task,
        createdAt = now,
        updatedAt = now
    )
}

fun createChatMessageFromCommand(command: Command): ChatMessage {
    return ChatMessage(role = command.messageType.toString().lowercase(), content = command.content)
}