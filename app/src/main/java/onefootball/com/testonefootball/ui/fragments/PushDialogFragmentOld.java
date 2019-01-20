package onefootball.com.testonefootball.ui.fragments;//package onefootball.com.testonefootball.framework;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomSheetBehavior;
//import android.support.design.widget.BottomSheetDialog;
//import android.support.design.widget.BottomSheetDialogFragment;
//import android.util.SparseBooleanArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.EnumSet;
//import java.util.List;
//import java.util.Set;
//
//import onefootball.com.testonefootball.R;
//import onefootball.com.testonefootball.data.PushEventType;
//import onefootball.com.testonefootball.data.repositories.PushRepository;
//import onefootball.com.testonefootball.data.model.PushItem;
//import onefootball.com.testonefootball.ui.adapter.PushOptionAdapter;
//
//public class PushDialogFragmentOld extends BottomSheetDialogFragment implements PushDialog {
//    public static final String ARGS_PUSH_TYPE = "pushType";
//    public static final String ARGS_PUSH_ID = "pushId";
//    public static final String ARGS_TEAM_ID = "teamId";
//    public static final String ARGS_PUSH_NAME = "pushName";
//    public static final String ARGS_SELECTED_PUSH_OPTION = "selectedPushOption";
//    public static final String ARGS_PRESELECT_ALL_ITEMS = "preselectAllOptions";
//
//    public static final int PUSH_OPTION_INVALID = -2;
//    public static final int PUSH_OPTIONS_ALL = -1;
//
//    private PushRegistrationCategory pushType;
//    private long pushId;
//    private String pushName;
//    private long teamId;
//    private int selectedPushOption;
//    private int maxSelectableItems;
//
//    private View pushDialog;
//    private ListView listView;
//
//    PushRepository pushRepository;
//
//    private PushDialogActionListener dialogListener;
//    private DialogDismissListener onDismissListener;
//    private DialogCancelListener onCancelListener;
//
//    private boolean preselectAllOptions;
//
//    public static PushDialogFragmentOld newInstanceTeam(long teamId, String teamName, PushRepository repository) {
//        final PushDialogFragmentOld dialog = new PushDialogFragmentOld();
//        dialog.setPushRepository(repository);
//        dialog.setArguments(getTeamBundle(teamId, teamName));
//        return dialog;
//    }
//
//    private void setPushRepository(PushRepository repository) {
//        this.pushRepository = repository;
//    }
//
//    private static Bundle getTeamBundle(long teamId, String teamName) {
//        final Bundle args = new Bundle();
//        args.putSerializable(ARGS_PUSH_TYPE, PushRegistrationCategory.TEAM);
//        args.putLong(ARGS_TEAM_ID, teamId);
//        args.putLong(ARGS_PUSH_ID, teamId);
//        args.putString(ARGS_PUSH_NAME, teamName);
//        args.putInt(ARGS_SELECTED_PUSH_OPTION, PUSH_OPTION_INVALID);
//
//        return args;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        dialogListener = context instanceof PushDialogActionListener ? (PushDialogActionListener) context : null;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        final Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
//
//        pushType = (PushRegistrationCategory) bundle.getSerializable(ARGS_PUSH_TYPE);
//        pushId = bundle.getLong(ARGS_PUSH_ID);
//        teamId = bundle.getLong(ARGS_TEAM_ID);
//        pushName = bundle.getString(ARGS_PUSH_NAME);
//        selectedPushOption = bundle.getInt(ARGS_SELECTED_PUSH_OPTION);
//
//        preselectAllOptions = bundle.getBoolean(ARGS_PRESELECT_ALL_ITEMS);
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putSerializable(ARGS_PUSH_TYPE, pushType);
//        outState.putLong(ARGS_PUSH_ID, pushId);
//        outState.putLong(ARGS_TEAM_ID, teamId);
//        outState.putString(ARGS_PUSH_NAME, pushName);
//        outState.putInt(ARGS_SELECTED_PUSH_OPTION, getSelectedPushOptionsAsInt());
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        pushDialog = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_push, null, false);
//        initListView();
//
//        final PushOptionAdapter adapter = new PushOptionAdapter(getActivity(), buildPushOptions());
//        listView.setAdapter(adapter);
//
//        // minus 1 because "Select all" should not be counted
//        maxSelectableItems = adapter.getCount() - 1;
//        // set selected items
//        setUserSelectedPushesToDialog(adapter);
//
//        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
//        dialog.setContentView(pushDialog);
//
//        setupPushEntityName(dialog);
//        setupPrimaryButton(dialog);
//
//        dialog.setOnShowListener(dialogParam -> {
//            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogParam;
//            FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
//            if (bottomSheet != null) {
//                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
//
//        return dialog;
//    }
//
//    private void initListView() {
//        listView = pushDialog.findViewById(R.id.list_view);
//        listView.setDividerHeight(0);
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//
//        listView.setOnItemClickListener((parent, view, position, id) -> {
//            final PushOptionAdapter.PushOptionItem item = (PushOptionAdapter.PushOptionItem) parent.getItemAtPosition(position);
//            final boolean isPushOptionAll = item.getPushOption().isPushOption(PushEventType.ALL);
//            final boolean isSelected = listView.isItemChecked(position);
//
//            if (isPushOptionAll) {
//                setAllCheckBoxes(isSelected);
//            }
//
//            if (isSelected && listView.getCheckedItemCount() == maxSelectableItems) {
//                setAllCheckBoxes(true);
//            }
//
//            if (!isSelected && listView.getCheckedItemCount() == maxSelectableItems) {
//                listView.setItemChecked(0, false);
//            }
//            listView.invalidateViews();
//        });
//    }
//
//    private void setupPrimaryButton(Dialog dialog) {
//        Button primaryButton = dialog.findViewById(R.id.button_primary);
//
//        primaryButton.setOnClickListener(v -> {
//            setPushOptions();
//            if (dialogListener != null) {
//                dialogListener.onPushDialogPositiveClick(PushDialogFragmentOld.this);
//            }
//            dismiss();
//        });
//    }
//
//    private void setupPushEntityName(Dialog dialog) {
//        TextView entityName = dialog.findViewById(R.id.entity_name);
//        entityName.setText(pushName);
//    }
//
//    private void setUserSelectedPushesToDialog(PushOptionAdapter adapter) {
//        if (selectedPushOption != PUSH_OPTION_INVALID) {
//            setPushOptions(adapter, selectedPushOption);
//        } else if (preselectAllOptions) {
//            setPushOptions(adapter, PUSH_OPTIONS_ALL);
//        } else {
//            retrieveAndSetPreviousPushOptions(adapter);
//        }
//    }
//
//    private void retrieveAndSetPreviousPushOptions(PushOptionAdapter adapter) {
//        PushItem pushEntry = getPushEntry();
//
//        // create new one if it is null
//        if (pushEntry == null) {
//            switch (pushType) {
//                case TEAM:
//                    pushEntry = new PushItem(0L, pushId, PushItem.TYPE_TEAM_PUSH, pushName, String.valueOf(-1));
//                    break;
//            }
//
//        }
//
//        selectedPushOption = Integer.parseInt(pushEntry.getPushOption());
//
//        setPushOptions(adapter, selectedPushOption);
//    }
//
//    private void setPushOptions(PushOptionAdapter adapter, int pushOption) {
//        Set<PushEventType> selectedPushOptions = PushEventType.decode(pushOption, pushType);
//        setSelectedCheckBoxes(selectedPushOptions, adapter);
//    }
//
//    private PushItem getPushEntry() {
//        return pushRepository.getTeamPush(pushId);
//    }
//
//    private void setSelectedCheckBoxes(Set<PushEventType> pushOptions, PushOptionAdapter adapter) {
//        if (pushOptions == null || pushOptions.isEmpty()) {
//            return;
//        }
//
//        for (int position = 0; position < adapter.getCount(); position++) {
//            if (pushOptions.contains(adapter.getItem(position).getPushOption())) {
//                listView.setItemChecked(position, true);
//            }
//        }
//    }
//
//    private void setAllCheckBoxes(boolean enableCheckBoxes) {
//        if (enableCheckBoxes) {
//            final int count = listView.getCount();
//            for (int position = 0; position < count; position++) {
//                listView.setItemChecked(position, true);
//            }
//        } else {
//            listView.clearChoices();
//        }
//    }
//
//    private int getSelectedPushOptionsAsInt() {
//        final Set<PushEventType> selectedPushOptions = EnumSet.noneOf(PushEventType.class);
//        final PushOptionAdapter adapter = (PushOptionAdapter) listView.getAdapter();
//        final SparseBooleanArray selectedItems = listView.getCheckedItemPositions();
//
//        final int count = selectedItems.size();
//        for (int i = 0; i < count; i++) {
//            if (selectedItems.valueAt(i)) {
//                selectedPushOptions.add(adapter.getItem(selectedItems.keyAt(i)).getPushOption());
//            }
//        }
//
//        return PushEventType.encode(selectedPushOptions);
//    }
//
//    private void setPushOptions() {
//        final int pushOptionsAsInt = getSelectedPushOptionsAsInt();
//
//        switch (pushType) {
//            case TEAM: {
//                pushRepository.setTeamPush(pushId, pushName, pushOptionsAsInt);
//                break;
//            }
//        }
//    }
//
//    private List<PushOptionAdapter.PushOptionItem> buildPushOptions() {
//        Set<PushEventType> availablePushOptions = null;
//        switch (pushType) {
//            case TEAM:
//                availablePushOptions = PushEventType.SUPPORTED_TEAM_PUSH_OPTIONS;
//                break;
//        }
//
//        final List<PushOptionAdapter.PushOptionItem> items = new ArrayList<>();
//        for (final PushEventType option : availablePushOptions) {
//            if (!isAlias(option) && option.getRegistrationNames().size() != 0) {
//                items.add(new PushOptionAdapter.PushOptionItem(getStringResource(option), getPushDrawable(option), option));
//            }
//        }
//        return items;
//    }
//
//    private boolean isAlias(PushEventType option) {
//        return option.isAlias();
//    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//
//        if (onDismissListener != null) {
//            onDismissListener.onDismiss();
//        }
//    }
//
//    @Override
//    public void onCancel(DialogInterface dialog) {
//        super.onCancel(dialog);
//
//        if (onCancelListener != null) {
//            onCancelListener.onCancel();
//        }
//    }
//
//    public void setOnDismissListener(DialogDismissListener onDismissListener) {
//        this.onDismissListener = onDismissListener;
//    }
//
//    public void setOnCancelListener(DialogCancelListener onCancelListener) {
//        this.onCancelListener = onCancelListener;
//    }
//
//    public String getStringResource(PushEventType option) {
//        switch (option) {
//            case ALL:
//                return getString(R.string.dialog_push_option_all);
//            case GOAL:
//                return getString(R.string.dialog_push_option_goals);
//            case STARTSTOP:
//                return getString(R.string.dialog_push_option_start_stop);
//            case FACTS:
//                return getString(R.string.dialog_push_option_facts);
//            case TRANSFER:
//                return getString(R.string.dialog_push_option_transfers);
//            case REDCARD:
//                return getString(R.string.dialog_push_option_red_cards);
//            case NEWS:
//                return getString(R.string.dialog_push_option_news);
//            default:
//                return "";
//        }
//    }
//
//    public int getPushDrawable(PushEventType option) {
//        switch (option) {
//            case FACTS:
//                return R.drawable.ic_push_list_black_24_px;
//            case LINEUP:
//                return R.drawable.ic_push_shirt;
//            case TRANSFER:
//                return R.drawable.ic_push_match_event_substitution;
//            case REDCARD:
//                return R.drawable.ic_push_match_event_red_card;
//            case GOAL:
//                return R.drawable.ic_push_match_event_goal;
//            case NEWS:
//                return R.drawable.ic_push_list_black_24_px;
//            case STARTSTOP:
//                return R.drawable.ic_push_default_coming_soon;
//            default:
//                return 0;
//        }
//
//    }
//
//}
//
