package onefootball.com.testonefootball.framework

/**
 * Push registration categories to register against to receive pushes for that category.
 */
enum class PushRegistrationCategory(val value: String) {
    TEAM("it%d");

    override fun toString(): String {
        return this.value
    }
}
