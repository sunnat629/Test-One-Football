package onefootball.com.testonefootball.data;

public interface PushRepository
{
    void setTeamPush(long teamId, String name, int pushOptions);

    PushItem getTeamPush(long teamId);
}
