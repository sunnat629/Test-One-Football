package onefootball.com.testonefootball.data

class Team(private val id: Int, val name: String) {

    fun getId(): Long {
        return id.toLong()
    }
}
