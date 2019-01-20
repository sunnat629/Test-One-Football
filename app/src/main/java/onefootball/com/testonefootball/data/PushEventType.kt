package onefootball.com.testonefootball.data

import java.util.*

/**
 * Representation of the possible push options.
 *
 *  * Every [PushEventType] contains the appropriate category to register it with push.io
 *  * A [Set] of [PushEventType] could be encoded to an integer representation and back.
 *
 * Please use SUPPORTED_TEAM_PUSH_OPTIONS to obtain the push options who are supported by the middleware.
 */

enum class PushEventType(
        val bitPosition: Int, registrationNames: Collection<String>,
        val isAlias: Boolean) {
    //enums should be named exactly like the pushType in the push JSON
    ALL(-1, "a"),
    FACTS(1, "rpt", "lup"),
    LINEUP(FACTS, "lup"),
    TRANSFER(2, "ntdd", "ntrh", "ntrm"),
    REDCARD(3, "crd"),
    GOAL(4, "gl"),
    NEWS(5, "news"),
    STARTSTOP(8, "st"),
    PENALTY(13),
    GOALRETRACTION(14),
    REDCARDRETRACTION(15),

    UNKNOWN(31, "");


    // stores the string representation of this push option
    /**
     * Gets all registration names for this push option
     *
     * @return List of this push option registration names
     */
    val registrationNames: Set<String> = HashSet(registrationNames)

    // constructor for that option which doesn't have any alias
    constructor(bitPosition: Int, vararg registrationNames: String) :
            this(bitPosition, Arrays.asList<String>(*registrationNames), false)

    // constructor for that option which has an alias
    constructor(aliasedEventType: PushEventType, vararg registrationNames: String) :
            this(aliasedEventType.bitPosition, Arrays.asList<String>(*registrationNames), true)


    /**
     * Gets the String representation of this push option
     *
     * @return String representation of this push option
     */
    override fun toString(): String {
        return this.name.toLowerCase()
    }

    /**
     * Checks if the given [PushEventType] is the same as this option.
     *
     * @param option [PushEventType]
     * @return true if they are the same
     */
    fun isPushOption(option: PushEventType): Boolean {
        return this == option
    }
}
