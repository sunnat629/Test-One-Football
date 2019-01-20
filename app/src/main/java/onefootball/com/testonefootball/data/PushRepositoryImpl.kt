package onefootball.com.testonefootball.data

import java.util.HashMap

import onefootball.com.testonefootball.data.model.PushItem

class PushRepositoryImpl : PushRepository {
    private val pushMap: HashMap<Long, PushItem> = hashMapOf()

    override fun setTeamPush(teamId: Long, name: String, pushOptions: Int) {
        val item = PushItem(teamId, PushItem.TYPE_TEAM_PUSH, name, pushOptions.toString())
        pushMap[teamId] = item
    }

    override fun getTeamPush(teamId: Long): PushItem? {
        return pushMap[teamId]
    }
}
