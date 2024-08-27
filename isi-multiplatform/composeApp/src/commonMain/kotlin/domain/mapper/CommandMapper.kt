package domain.mapper

import domain.entity.Chat
import domain.entity.Command
import domain.entity.MessageType
import domain.entity.TaskType
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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