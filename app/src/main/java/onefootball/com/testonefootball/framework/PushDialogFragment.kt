package onefootball.com.testonefootball.framework

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView

import java.util.ArrayList
import java.util.EnumSet

import onefootball.com.testonefootball.R
import onefootball.com.testonefootball.data.PushEventType
import onefootball.com.testonefootball.data.PushRepository
import onefootball.com.testonefootball.data.model.PushItem
import onefootball.com.testonefootball.ui.adapter.PushOptionAdapter

class PushDialogFragment : BottomSheetDialogFragment() {

    private var pushType: PushRegistrationCategory? = null
    private var pushId: Long = 0
    private var pushName: String? = null
    private var teamId: Long = 0
    private var selectedPushOption: Int = 0
    private var maxSelectableItems: Int = 0

    private var pushDialog: View? = null
    private var listView: ListView? = null

    private lateinit var pushRepository: PushRepository

    private var dialogListener: PushDialogActionListener? = null
    private val onDismissListener: DialogDismissListener? = null
    private val onCancelListener: DialogCancelListener? = null

    private var preselectAllOptions: Boolean = false

    private val pushEntry: PushItem?
        get() = pushRepository.getTeamPush(pushId)

    private val selectedPushOptionsAsInt: Int
        get() {
            val selectedPushOptions = EnumSet.noneOf(PushEventType::class.java)
            val adapter = listView!!.adapter as PushOptionAdapter
            val selectedItems = listView!!.checkedItemPositions

            val count = selectedItems.size()
            for (i in 0 until count) {
                if (selectedItems.valueAt(i)) {
                    selectedPushOptions.add(adapter.getItem(selectedItems.keyAt(i))!!.pushOption)
                }
            }

            return PushEventType.encode(selectedPushOptions)
        }

    private fun setPushRepository(repository: PushRepository) {
        this.pushRepository = repository
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dialogListener = if (context is PushDialogActionListener) context else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = savedInstanceState ?: arguments

        pushType = bundle!!.getSerializable(ARGS_PUSH_TYPE) as PushRegistrationCategory
        pushId = bundle.getLong(ARGS_PUSH_ID)
        teamId = bundle.getLong(ARGS_TEAM_ID)
        pushName = bundle.getString(ARGS_PUSH_NAME)
        selectedPushOption = bundle.getInt(ARGS_SELECTED_PUSH_OPTION)

        preselectAllOptions = bundle.getBoolean(ARGS_PRESELECT_ALL_ITEMS)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable(ARGS_PUSH_TYPE, pushType)
        outState.putLong(ARGS_PUSH_ID, pushId)
        outState.putLong(ARGS_TEAM_ID, teamId)
        outState.putString(ARGS_PUSH_NAME, pushName)
        outState.putInt(ARGS_SELECTED_PUSH_OPTION, selectedPushOptionsAsInt)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        pushDialog = (activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.dialog_push, null, false)

        initListView()

        val adapter = PushOptionAdapter(activity!!, buildPushOptions())
        listView!!.adapter = adapter

        // minus 1 because "Select all" should not be counted
        maxSelectableItems = adapter.count - 1
        // set selected items
        setUserSelectedPushesToDialog(adapter)

        val dialog = BottomSheetDialog(context!!)
        dialog.setContentView(pushDialog)

        setupPushEntityName(dialog)
        setupPrimaryButton(dialog)

        dialog.setOnShowListener { dialogParam ->
            val bottomSheetDialog = dialogParam as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    private fun initListView() {
        listView = pushDialog!!.findViewById(R.id.list_view)
        listView!!.dividerHeight = 0
        listView!!.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        listView!!.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as PushOptionAdapter.PushOptionItem
            val isPushOptionAll = item.pushOption!!.isPushOption(PushEventType.ALL)
            val isSelected = listView!!.isItemChecked(position)

            if (isPushOptionAll) {
                setAllCheckBoxes(isSelected)
            }

            if (isSelected && listView!!.checkedItemCount == maxSelectableItems) {
                setAllCheckBoxes(true)
            }

            if (!isSelected && listView!!.checkedItemCount == maxSelectableItems) {
                listView!!.setItemChecked(0, false)
            }
            listView!!.invalidateViews()
        }
    }

    private fun setupPrimaryButton(dialog: Dialog) {
        val primaryButton = dialog.findViewById<Button>(R.id.button_primary)

        primaryButton.setOnClickListener {
            setPushOptions()
            initPushDialogPositiveClick()
            dismiss()
        }
    }

    private fun setupPushEntityName(dialog: Dialog) {
        val entityName = dialog.findViewById<TextView>(R.id.entity_name)
        entityName.text = pushName
    }

    private fun setUserSelectedPushesToDialog(adapter: PushOptionAdapter) {
        if (selectedPushOption != PUSH_OPTION_INVALID) {
            setPushOptions(adapter, selectedPushOption)
        } else if (preselectAllOptions) {
            setPushOptions(adapter, PUSH_OPTIONS_ALL)
        } else {
            retrieveAndSetPreviousPushOptions(adapter)
        }
    }

    private fun retrieveAndSetPreviousPushOptions(adapter: PushOptionAdapter) {
        var pushEntry = pushEntry

        // create new one if it is null
        if (pushEntry == null) {
            when (pushType) {
                PushRegistrationCategory.TEAM -> pushEntry = PushItem(0L, pushId, PushItem.TYPE_TEAM_PUSH, pushName!!, (-1).toString())
            }

        }

        selectedPushOption = Integer.parseInt(pushEntry!!.pushOption!!)

        setPushOptions(adapter, selectedPushOption)
    }

    private fun setPushOptions(adapter: PushOptionAdapter, pushOption: Int) {
        val selectedPushOptions = PushEventType.decode(pushOption, pushType!!)
        setSelectedCheckBoxes(selectedPushOptions, adapter)
    }

    private fun setSelectedCheckBoxes(pushOptions: Set<PushEventType>?, adapter: PushOptionAdapter) {
        if (pushOptions == null || pushOptions.isEmpty()) {
            return
        }

        for (position in 0 until adapter.count) {
            if (pushOptions.contains(adapter.getItem(position)!!.pushOption)) {
                listView!!.setItemChecked(position, true)
            }
        }
    }

    private fun setAllCheckBoxes(enableCheckBoxes: Boolean) {
        if (enableCheckBoxes) {
            val count = listView!!.count
            for (position in 0 until count) {
                listView!!.setItemChecked(position, true)
            }
        } else {
            listView!!.clearChoices()
        }
    }

    private fun setPushOptions() {
        val pushOptionsAsInt = selectedPushOptionsAsInt
        when (pushType) {
            PushRegistrationCategory.TEAM -> {
                pushRepository.setTeamPush(pushId, pushName!!, pushOptionsAsInt)
            }
        }
    }

    private fun buildPushOptions(): List<PushOptionAdapter.PushOptionItem> {
        var availablePushOptions: Set<PushEventType>? = null
        when (pushType) {
            PushRegistrationCategory.TEAM -> availablePushOptions = PushEventType.SUPPORTED_TEAM_PUSH_OPTIONS
        }

        //        Log.wtf("******", availablePushOptions.toString());
        val items = ArrayList<PushOptionAdapter.PushOptionItem>()
        for (option in availablePushOptions!!) {
            //            Log.i("******", option.toString() +" --> isAlias(option) "+isAlias(option)+" : "+option.getRegistrationNames().toString() );
            if (!isAlias(option) && option.registrationNames.size != 0) {
                items.add(PushOptionAdapter.PushOptionItem(getStringResource(option), getPushDrawable(option), option))
            }
        }
        return items
    }

    private fun isAlias(option: PushEventType): Boolean {
        return option.isAlias
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)

        onDismissListener?.onDismiss()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)

        onCancelListener?.onCancel()
    }

    fun getStringResource(option: PushEventType): String {
        when (option) {
            PushEventType.ALL -> return getString(R.string.dialog_push_option_all)
            PushEventType.TRANSFER -> return getString(R.string.dialog_push_option_transfers)
            PushEventType.FACTS -> return getString(R.string.dialog_push_option_facts)
            PushEventType.REDCARD -> return getString(R.string.dialog_push_option_red_cards)
            PushEventType.GOAL -> return getString(R.string.dialog_push_option_goals)
            PushEventType.STARTSTOP -> return getString(R.string.dialog_push_option_start_stop)
//            PushEventType.NEWS -> return getString(R.string.dialog_push_option_news)
            else -> return ""
        }
    }

    fun getPushDrawable(option: PushEventType): Int {
        when (option) {
            PushEventType.TRANSFER -> return R.drawable.ic_push_match_event_substitution
            PushEventType.FACTS -> return R.drawable.ic_push_list_black_24_px
            PushEventType.REDCARD -> return R.drawable.ic_push_match_event_red_card
            PushEventType.GOAL -> return R.drawable.ic_push_match_event_goal
            PushEventType.STARTSTOP -> return R.drawable.ic_push_default_coming_soon
//            PushEventType.LINEUP -> return R.drawable.ic_push_shirt
//            PushEventType.NEWS -> return R.drawable.ic_push_list_black_24_px
            else -> return 0
        }
    }

    private fun initPushDialogPositiveClick() {
        if (dialogListener != null) {
            dialogListener!!.onPushDialogPositiveClick(this@PushDialogFragment)
        }
    }

    companion object {
        val ARGS_PUSH_TYPE = "pushType"
        val ARGS_PUSH_ID = "pushId"
        val ARGS_TEAM_ID = "teamId"
        val ARGS_PUSH_NAME = "pushName"
        val ARGS_SELECTED_PUSH_OPTION = "selectedPushOption"
        val ARGS_PRESELECT_ALL_ITEMS = "preselectAllOptions"

        val PUSH_OPTION_INVALID = -2
        val PUSH_OPTIONS_ALL = -1

        fun newInstanceTeam(teamId: Long, teamName: String, repository: PushRepository): PushDialogFragment {
            val dialog = PushDialogFragment()
            dialog.setPushRepository(repository)
            dialog.arguments = getTeamBundle(teamId, teamName)
            return dialog
        }

        private fun getTeamBundle(teamId: Long, teamName: String): Bundle {
            val args = Bundle()
            args.putSerializable(ARGS_PUSH_TYPE, PushRegistrationCategory.TEAM)
            args.putLong(ARGS_TEAM_ID, teamId)
            args.putLong(ARGS_PUSH_ID, teamId)
            args.putString(ARGS_PUSH_NAME, teamName)
            args.putInt(ARGS_SELECTED_PUSH_OPTION, PUSH_OPTION_INVALID)

            return args
        }
    }
}

