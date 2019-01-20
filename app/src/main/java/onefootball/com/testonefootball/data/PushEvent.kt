package onefootball.com.testonefootball.data

import onefootball.com.testonefootball.ui.enums.PushRegistrationCategory
import java.util.*

object PushEvent {

    /**
     * Set of the push options, that the middleware actually supports.
     */
    val SUPPORTED_TEAM_PUSH_OPTIONS: Set<PushEventType> = EnumSet.of(PushEventType.ALL, PushEventType.GOAL, PushEventType.STARTSTOP, PushEventType.FACTS, PushEventType.LINEUP, PushEventType.TRANSFER, PushEventType.REDCARD,
            PushEventType.PENALTY, PushEventType.GOALRETRACTION, PushEventType.REDCARDRETRACTION)

    /**
     * Encode the given Set of PushOptionTypes to the appropriate integer representation.
     *
     * @param pushOptions - Set of PushOptionTypes
     * @return Integer representation of the push options
     */
    fun encode(pushOptions: Set<PushEventType>): Int {

        // if all options are selected
        if (pushOptions.contains(PushEventType.ALL)) {
            return PushEventType.ALL.bitPosition
        }

        var options = 0

        // for specific options are selected
        for (option in pushOptions) {
            // shl means shift left (same as << of java)
            options = options or (1 shl option.bitPosition)
        }
        return options
    }

    /**
     * Decode the given integer representation of the push options to the appropriate Set of PushOptions.
     *
     * @param pushOptionsAsInt - integer representation of the push options
     * @param pushType         - type of the push
     * @return Set of PushOptionTypes
     */
    fun decode(pushOptionsAsInt: Int, pushType: PushRegistrationCategory): Set<PushEventType> {
        if (pushOptionsAsInt == PushEventType.ALL.bitPosition) {
            when (pushType) {
                PushRegistrationCategory.TEAM -> return EnumSet.copyOf(SUPPORTED_TEAM_PUSH_OPTIONS)
            }
        }

        val options = EnumSet.noneOf(PushEventType::class.java)

        for (option in PushEventType.values()) {
            if (pushOptionsAsInt and (1 shl option.bitPosition) == 1 shl option.bitPosition) {
                options.add(option)
            }
        }
        return options
    }
}