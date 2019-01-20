package onefootball.com.testonefootball.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.ListView
import onefootball.com.testonefootball.R
import onefootball.com.testonefootball.data.PushEventType
import onefootball.com.testonefootball.ui.adapter.PushOptionAdapter.PushOptionItem

class PushOptionAdapter(context: Context, selectedPushOptions: List<PushOptionItem>) :
        ArrayAdapter<PushOptionItem>(context, R.layout.list_item_push, selectedPushOptions) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        var holder = ViewHolder()
        val item = getItem(position)

        if (view == null) {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_item_push, parent, false)
            view.tag = holder

            holder.pushOptionCheckedTextView = view!!.findViewById<View>(R.id.checkIcon) as CheckedTextView
            holder.divider = view.findViewById<View>(R.id.divider) as View
        } else {
            holder = view.tag as ViewHolder
        }

        holder.pushOptionCheckedTextView!!.text = item!!.name
        holder.pushOptionCheckedTextView!!.setCompoundDrawablesWithIntrinsicBounds(item.drawable!!, 0, 0, 0)

        val listView = parent as ListView
        holder.pushOptionCheckedTextView!!.isChecked = listView.isItemChecked(position)

        if (isLastPosition(position)) {
            holder.divider!!.visibility = View.VISIBLE
        } else {
            holder.divider!!.visibility = View.GONE
        }
        return view
    }

    private fun isLastPosition(position: Int): Boolean {
        return position == count - 1
    }

    // viewholder of this adapter. it has two variables of a CheckedTextView and a View
    private class ViewHolder {
        internal var pushOptionCheckedTextView: CheckedTextView? = null
        internal var divider: View? = null
    }


    class PushOptionItem(stringResource: String, pushDrawable: Int, option: PushEventType) {
        val name: String? = stringResource
        val drawable: Int? = pushDrawable
        val pushOption: PushEventType? = option
    }
}