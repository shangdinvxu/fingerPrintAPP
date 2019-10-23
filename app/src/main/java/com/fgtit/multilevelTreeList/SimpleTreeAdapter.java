package com.fgtit.multilevelTreeList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SimpleTreeAdapter extends TreeListViewAdapter {
    private Context context;
    public SimpleTreeAdapter(ListView mTree, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
        this.context = context;
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = mInflater.inflate(com.fgtit.fingerprintapp.R.layout.list_tree, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.up_down = (ImageView) convertView
                    .findViewById(com.fgtit.fingerprintapp.R.id.up_down);
            viewHolder.is_show_person = (ImageView) convertView
                    .findViewById(com.fgtit.fingerprintapp.R.id.is_show_person);
            viewHolder.tv_employee_name = (TextView) convertView.findViewById(com.fgtit.fingerprintapp.R.id.tv_employee_name);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_employee_name.setText(node.getName());

//        viewHolder.description.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new TipDialog(context, R.style.DialogTheme2, node.getDescription())
//                        .show();
//            }
//        });
        if (node.getIcon() == -1) {
            viewHolder.up_down.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.up_down.setVisibility(View.VISIBLE);
            viewHolder.up_down.setImageResource(node.getIcon());
        }

        return convertView;
    }

    private final class ViewHolder
    {
        ImageView up_down;
        ImageView is_show_person;
        TextView tv_employee_name;
    }

}
