package onefootball.com.testonefootball.framework;

/**
 * Push registration categories to register against to receive pushes for that category.
 */
public enum PushRegistrationCategory
{
    TEAM("it%d");

    private final String value;

    PushRegistrationCategory(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return this.value;
    }

}
