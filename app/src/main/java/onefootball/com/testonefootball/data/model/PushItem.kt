package onefootball.com.testonefootball.data.model

class PushItem {
    var id: Long? = -1L
    var pushItemId: Long? = -1L
    var pushItemType: Int? = -1
    var pushItemName: String? = null
    var pushOption: String? = null

    constructor(id: Long?, pushItemId: Long?, pushItemType: Int?, pushItemName: String, pushOption: String) {
        this.id = id
        this.pushItemId = pushItemId
        this.pushItemType = pushItemType
        this.pushItemName = pushItemName
        this.pushOption = pushOption
    }

    constructor(pushItemId: Long?, pushItemType: Int?, pushItemName: String, pushOption: String){
        this.pushItemId = pushItemId
        this.pushItemType = pushItemType
        this.pushItemName = pushItemName
        this.pushOption = pushOption
    }

    // TYPE_TEAM_PUSH is static constant
    companion object {
        const val TYPE_TEAM_PUSH = 1
    }
}