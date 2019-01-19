package onefootball.com.testonefootball.data

class PushItem {
    var id: Long? = null
    var pushItemId: Long? = null
    var pushItemType: Int? = null
    var pushItemName: String? = null
    var pushOption: String? = null

    constructor() {}

    constructor(id: Long?) {
        this.id = id
    }

    constructor(id: Long?, pushItemId: Long?, pushItemType: Int?, pushItemName: String, pushOption: String) {
        this.id = id
        this.pushItemId = pushItemId
        this.pushItemType = pushItemType
        this.pushItemName = pushItemName
        this.pushOption = pushOption
    }

    companion object {

        val TYPE_TEAM_PUSH = 1
    }
}
