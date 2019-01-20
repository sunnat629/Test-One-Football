package onefootball.com.testonefootball.data.repositories

import onefootball.com.testonefootball.data.model.PushItem

interface PushRepository {
    fun setTeamPush(teamId: Long, name: String, pushOptions: Int)

    // add ? to ensure that this function can be return null
    fun getTeamPush(teamId: Long): PushItem?
}
