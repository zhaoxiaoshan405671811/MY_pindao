package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.my_yuekaob.R;

import java.util.List;

import bean.MyBean;

/**
 * Created by 1 on 2017/5/28.
 */

public class MyAdapter extends BaseAdapter {
    private   List<MyBean.AppBean> mAppBeanList;
    private Context context;

    public MyAdapter(List<MyBean.AppBean> appBeanList, Context context) {
        mAppBeanList = appBeanList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mAppBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView =View.inflate(context,R.layout.xlist_item,null);
            holder = new ViewHolder();
            holder.MyappName = (TextView) convertView.findViewById(R.id.appname);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.MyappName.setText(mAppBeanList.get(position).getName());
        return convertView;
    }
    class ViewHolder{
        TextView MyappName;
    }
}
