package onefootball.com.testonefootball.ui

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import onefootball.com.testonefootball.R
import onefootball.com.testonefootball.data.repositories.PushRepositoryImpl
import onefootball.com.testonefootball.data.model.Team
import onefootball.com.testonefootball.ui.fragments.PushDialogFragment
import onefootball.com.testonefootball.ui.interfaces.PushDialog

open class MainActivity : AppCompatActivity() {
    private lateinit var team: Team
    private lateinit var pushRepository: PushRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        team = Team(1, getString(R.string.arsenal))
        pushRepository = PushRepositoryImpl()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val ret = super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_team, menu)

        return ret
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        prepareMenuPushNotification(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_push) {
            showTeamPushDialog(supportFragmentManager, team.id, team.name)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun prepareMenuPushNotification(menu: Menu) {
        // if menu is nul, it won't execute and if it is not visible it also it won't show
        menu.findItem(R.id.menu_push)?.setIcon(R.drawable.ic_action_push_on)
    }

    private fun showTeamPushDialog(fm: FragmentManager, teamId: Long, teamName: String) {
        val dialogFragment = PushDialogFragment.newInstanceTeam(teamId, teamName, pushRepository)
        dialogFragment.show(fm, PushDialog.DIALOG_FRAGMENT_TAG)
    }
}
