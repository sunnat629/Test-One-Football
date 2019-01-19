package onefootball.com.testonefootball.data;

import java.util.HashMap;

public class PushRepositoryImpl implements PushRepository
{
    final HashMap<Long, PushItem> pushMap = new HashMap<>();

    @Override
    public void setTeamPush(long teamId, String name, int pushOptions)
    {
        PushItem item = new PushItem();
        item.setPushItemType(PushItem.TYPE_TEAM_PUSH);
        item.setPushItemId(teamId);
        item.setPushItemName(name);
        item.setPushOption(String.valueOf(pushOptions));

        pushMap.put(teamId, item);
    }

    @Override
    public PushItem getTeamPush(long teamId)
    {
        return pushMap.get(teamId);
    }
}
