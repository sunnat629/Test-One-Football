package onefootball.com.testonefootball.framework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.List;

import onefootball.com.testonefootball.R;
import onefootball.com.testonefootball.data.PushEventType;
import onefootball.com.testonefootball.framework.PushOptionAdapter.PushOptionItem;

public class PushOptionAdapter extends ArrayAdapter<PushOptionItem>
{
    private final LayoutInflater inflater;

    public PushOptionAdapter(Context context, List<PushOptionItem> selectedPushOptions)
    {
        super(context, R.layout.list_item_push, selectedPushOptions);

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = new ViewHolder();
        View view = convertView;
        final PushOptionItem item = getItem(position);

        if (view == null)
        {
            view = inflater.inflate(R.layout.list_item_push, parent, false);
            holder.pushOptionCheckedTextView = (CheckedTextView) view.findViewById(R.id.checkIcon);
            holder.divider = view.findViewById(R.id.divider);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        holder.pushOptionCheckedTextView.setText(item.getName());
        holder.pushOptionCheckedTextView.setCompoundDrawablesWithIntrinsicBounds(item.getDrawable(), 0, 0, 0);
        ListView listView = (ListView) parent;
        holder.pushOptionCheckedTextView.setChecked(listView.isItemChecked(position));

        if (isLastPosition(position))
        {
            holder.divider.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.divider.setVisibility(View.GONE);
        }
        return view;
    }

    public boolean isLastPosition(int position)
    {
        return position == getCount() - 1;
    }

    private static class ViewHolder
    {
        private CheckedTextView pushOptionCheckedTextView;
        private View divider;
    }

    public static class PushOptionItem
    {
        private final String name;
        private final int drawable;
        private final PushEventType pushOption;

        public PushOptionItem(String name, int drawable, PushEventType pushOption)
        {
            this.name = name;
            this.drawable = drawable;
            this.pushOption = pushOption;
        }

        String getName()
        {
            return name;
        }

        PushEventType getPushOption()
        {
            return pushOption;
        }

        int getDrawable()
        {
            return drawable;
        }
    }
}