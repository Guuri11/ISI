package domain.entity

/**
 * @param value name to show in the app
 * @param show: boolean to indicate if we show this type of tasks to the user in the UI
 */
enum class TaskType(val value: String, val show: Boolean) {
    OTHER_TOPICS("Other", true),
    BOOKMARK_RECOMMENDATIONS("Bookmarks", true),
    REFACTOR("Refactor", true),
    OPEN_APP("Open App", false),
    LINKEDIN_OFFER_REJECTION("Linkedin Offer Rejections", false),
    WEATHER("Weather", false),
}