package com.fgtit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fgtit.fingerprintapp.R;
import com.fgtit.multilevelTreeList.Node;

import java.util.ArrayList;
import java.util.List;

public class SearchEditAdapter extends BaseAdapter implements Filterable {

       private ArrayFilter mFilter;
      private List<Node> mList;
      private Context context;
      private List<Node> mUnfilteredData;

    public SearchEditAdapter(List<Node> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    public List<Node> getmList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                ViewHolder holder;
                if(convertView==null){
                         view = View.inflate(context, R.layout.list_search_result, null);

                         holder = new ViewHolder();
                         holder.tv_employee_name = (TextView) view.findViewById(R.id.tv_employee_name);


                        view.setTag(holder);
                     }else{
                         view = convertView;
                         holder = (ViewHolder) view.getTag();
                     }


                 holder.tv_employee_name.setText(mList.get(position).getName());

                 return view;
    }

    static class ViewHolder{
          public TextView tv_employee_name;
          }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
                         mFilter = new ArrayFilter();
                     }
                 return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

                         if (mUnfilteredData == null) {
                                 mUnfilteredData = mList;
                             }

                         if (constraint == null || constraint.length() == 0) {
                                 List<Node> list = mUnfilteredData;
                                 results.values = list;
                                 results.count = list.size();
                             } else {
                                 String prefixString = constraint.toString().toLowerCase();

                                 List<Node> unfilteredValues = mUnfilteredData;
                                 int count = unfilteredValues.size();

                                 ArrayList<Node> newValues = new ArrayList<Node>(count);

                                 for (int i = 0; i < count; i++) {
                                         String pc = unfilteredValues.get(i).getName();
                                         if (!StringUtils.isEmpty(pc)) {

                                                 if(pc.toLowerCase().contains(prefixString.toLowerCase())){
                                                         newValues.add(unfilteredValues.get(i));
                                                 }
                                            }
                                     }

                                 results.values = newValues;
                                 results.count = newValues.size();
                             }

                         return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList = (List<Node>) results.values;
                         if (results.count > 0) {
                                 notifyDataSetChanged();
                             } else {
                                 notifyDataSetInvalidated();
                             }
        }
    }
}
