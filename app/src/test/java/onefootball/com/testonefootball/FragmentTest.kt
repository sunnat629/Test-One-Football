package onefootball.com.testonefootball

import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatActivity
import onefootball.com.testonefootball.data.PushRepositoryImpl
import onefootball.com.testonefootball.data.model.Team
import onefootball.com.testonefootball.framework.PushDialogFragment
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment

@RunWith(RobolectricTestRunner::class)
class FragmentTest {
    private lateinit var fragment: BottomSheetDialogFragment

    @Before
    fun setFragment(){
        val team= Team(1, "Arsenal")
        val pushRepository = PushRepositoryImpl()
        fragment = PushDialogFragment.newInstanceTeam(team.id, team.name, pushRepository)
        startFragment(fragment, AppCompatActivity::class.java)
        startVisibleFragment(fragment)
    }

    // to test the fragment is null or not
    @Test
    @Throws(Exception::class)
    fun shouldNotBeNull() {
        assertNotNull(fragment)
    }
}