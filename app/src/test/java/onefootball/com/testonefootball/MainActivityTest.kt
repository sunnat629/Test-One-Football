package onefootball.com.testonefootball

import android.view.Menu
import android.widget.TextView
import onefootball.com.testonefootball.framework.MainActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowActivity


@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private lateinit var activity: MainActivity
    private lateinit var shadowActivity: ShadowActivity
    /**
     * @Before will call before the @Test execute
     * */
    @Before
    fun initMainActivity(){
        // this is the convenience method to run MainActivity and it will maintain the Activity lifecycle
        activity = Robolectric.setupActivity(MainActivity::class.java)
        shadowActivity = Shadows.shadowOf(activity)
    }

    // Is TestView null or not?
    @Test
    @Throws(Exception::class)
    fun textViewNullChecking(){
        val textView = activity.findViewById(R.id.displayText) as TextView
        assertNotNull("TextView is null", textView)
    }
    // TestView string check
    @Test
    @Throws(Exception::class)
    fun textViewStringChecking(){
        val textView = activity.findViewById(R.id.displayText) as TextView
        assertTrue("TextView contains incorrect text",
                activity.getString(R.string.task_description) == textView.text)
    }

    // MenuItem click check
    @Test
    @Throws(Exception::class)
    fun menuItemNullChecking(){
        val menu: Menu = shadowActivity.optionsMenu
        // to check null
        assertNotNull("Menu Item is null", menu)

        // clickable menu item or not
        shadowActivity.clickMenuItem(R.id.menu_push)

    }

    // MenuItem click check
    @Test
    @Throws(Exception::class)
    fun menuItemClickChecking(){
        val menu: Menu = shadowActivity.optionsMenu
        assertTrue("Menu Item contains incorrect title",
                menu.findItem(R.id.menu_push).title == activity.getString(R.string.team_push))
    }
}