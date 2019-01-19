package onefootball.com.testonefootball.framework;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import onefootball.com.testonefootball.R;
import onefootball.com.testonefootball.framework.PushDialog;
import onefootball.com.testonefootball.framework.PushDialogFragment;
import onefootball.com.testonefootball.data.PushRepository;
import onefootball.com.testonefootball.data.PushRepositoryImpl;
import onefootball.com.testonefootball.data.Team;

public class MainActivity extends AppCompatActivity
{
    private Team team = new Team(1, "Arsenal");
    private PushRepository pushRepository = new PushRepositoryImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        final boolean ret = super.onCreateOptionsMenu(menu);

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_team, menu);

        return ret;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        prepareMenuPushNotification(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    protected void prepareMenuPushNotification(Menu menu)
    {
        final MenuItem menuPush = menu.findItem(R.id.menu_push);
        if (menuPush != null)
        {
            menuPush.setVisible(team != null);

            if (team != null)
            {
                menuPush.setIcon(R.drawable.ic_action_push_on);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_push)
        {
            showTeamPushDialog(getSupportFragmentManager(), team.getId(), team.getName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public PushDialog showTeamPushDialog(FragmentManager fm, long teamId, String teamName)
    {
        final PushDialogFragment dialogFragment = PushDialogFragment.newInstanceTeam(teamId, teamName, pushRepository);
        dialogFragment.show(fm, PushDialog.DIALOG_FRAGMENT_TAG);
        return dialogFragment;
    }
}
