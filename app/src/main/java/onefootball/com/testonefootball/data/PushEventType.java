package onefootball.com.testonefootball.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import onefootball.com.testonefootball.framework.PushRegistrationCategory;

/**
 * Representation of the possible push options.
 * <ul>
 * <li>Every {@link PushEventType} contains the appropriate category to register it with push.io</li>
 * <li>A {@link Set} of {@link PushEventType} could be encoded to an integer representation and back.</li>
 * </ul>
 * Please use SUPPORTED_TEAM_PUSH_OPTIONS to obtain the push options who are supported by the middleware.
 */
public enum PushEventType
{
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

    /**
     * Set of the push options, that the middleware actually supports.
     */
    public static final Set<PushEventType> SUPPORTED_TEAM_PUSH_OPTIONS = EnumSet.of(ALL, GOAL, STARTSTOP, FACTS, LINEUP, TRANSFER, REDCARD,
            PENALTY, GOALRETRACTION, REDCARDRETRACTION);

    // use a fixed bit position to decouple the options from their order in this enum and allow save encoding and
    // decoding to an integer
    private final int bitPosition;
    // stores the string representation of this push option
    private final Set<String> registrationNames;
    private final boolean alias;

    PushEventType(int bitPosition, String... registrationNames)
    {
        this(bitPosition, Arrays.asList(registrationNames), false);
    }

    PushEventType(PushEventType aliasedEventType, String... registrationNames)
    {
        this(aliasedEventType.bitPosition, Arrays.asList(registrationNames), true);
    }

    PushEventType(int bitPosition, Collection<String> registrationNames, boolean alias)
    {
        this.bitPosition = bitPosition;
        this.registrationNames = new HashSet<>(registrationNames);
        this.alias = alias;
    }

    /**
     * Gets all registration names for this push option
     *
     * @return List of this push option registration names
     */
    public Set<String> getRegistrationNames()
    {
        return registrationNames;
    }

    public boolean isAlias()
    {
        return alias;
    }

    /**
     * Gets the String representation of this push option
     *
     * @return String representation of this push option
     */
    @Override
    public String toString()
    {
        return this.name().toLowerCase();
    }

    /**
     * Checks if the given {@link PushEventType} is the same as this option.
     *
     * @param option {@link PushEventType}
     *
     * @return true if they are the same
     */
    public boolean isPushOption(PushEventType option)
    {
        return this.equals(option);
    }

    /**
     * Encode the given Set of PushOptionTypes to the appropriate integer representation.
     *
     * @param pushOptions - Set of PushOptionTypes
     *
     * @return Integer representation of the push options
     */
    public static int encode(final Set<PushEventType> pushOptions)
    {
        if (pushOptions.contains(ALL))
        {
            return ALL.bitPosition;
        }

        int options = 0;

        for (final PushEventType option : pushOptions)
        {
            options |= (1 << option.bitPosition);
        }

        return options;
    }

    /**
     * Decode the given integer representation of the push options to the appropriate Set of PushOptions.
     *
     * @param pushOptionsAsInt - integer representation of the push options
     * @param pushType - type of the push
     *
     * @return Set of PushOptionTypes
     */
    public static Set<PushEventType> decode(final int pushOptionsAsInt, PushRegistrationCategory pushType)
    {
        if (pushOptionsAsInt == ALL.bitPosition)
        {
            switch (pushType)
            {
                case TEAM:
                    return EnumSet.copyOf(SUPPORTED_TEAM_PUSH_OPTIONS);
            }
        }

        final Set<PushEventType> options = EnumSet.noneOf(PushEventType.class);

        for (final PushEventType option : PushEventType.values())
        {
            if ((pushOptionsAsInt & (1 << option.bitPosition)) == (1 << option.bitPosition))
            {
                options.add(option);
            }
        }

        return options;
    }

}
