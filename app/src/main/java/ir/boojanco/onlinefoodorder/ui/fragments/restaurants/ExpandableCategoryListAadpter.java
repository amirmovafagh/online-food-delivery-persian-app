package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import java.util.HashMap;
import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.ExpandableListViewChildBinding;
import ir.boojanco.onlinefoodorder.databinding.ExpandableListViewGroupBinding;

public class ExpandableCategoryListAadpter extends BaseExpandableListAdapter {
    Context context;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    ExpandableListViewGroupBinding parentBinding;
    ExpandableListViewChildBinding childBinding;


    public ExpandableCategoryListAadpter(Context context, List<String> listGroup, HashMap<String, List<String>> listItem) {
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItem.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String group = (String) getGroup(groupPosition);

        if (convertView == null) {
            parentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.expandable_list_view_group, parent, false);
            convertView = parentBinding.getRoot();
        }
        parentBinding.setParent(group);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            childBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.expandable_list_view_child, parent, false);
            convertView = childBinding.getRoot();
        }
        childBinding.setChild(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
