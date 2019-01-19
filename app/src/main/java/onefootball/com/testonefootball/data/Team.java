package onefootball.com.testonefootball.data;

public class Team
{
    private final String name;
    private final int id;

    public Team(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
