package trackerapp.trackerapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trackerapp.trackerapp.R;
import trackerapp.trackerapp.model.ActivityModelDTO;
import trackerapp.trackerapp.model.TypeModel;

/**
 * Created by Krystian on 04.05.2017.
 */

public class ListActivityTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<TypeModel> activityTypes;
    private ActivityModelDTO model;

    //Constructor
    public ListActivityTypeAdapter(Context mContext, ActivityModelDTO model) {
        this.mContext = mContext;
        this.activityTypes = new ArrayList<>();
        this.model = model;
    }

    public void clearList() {
        activityTypes.clear();
        this.notifyDataSetChanged();
    }

    public void setActivityTypes(List<TypeModel> list) {
        activityTypes.clear();
        if (list != null) {
            activityTypes.addAll(list);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        try {
            return activityTypes.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public TypeModel getItem(int position) {
        try {
            return activityTypes.get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(mContext, R.layout.adapter_activity_type_item, null);
        TypeModel at = activityTypes.get(position);

        if (model.getActivityType() != null && (at.getName().equals(model.getActivityType().getName())))
        {
            RelativeLayout rlItemBlock = (RelativeLayout) view.findViewById(R.id.rr_item_block);
            rlItemBlock.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite2));

        }

        TextView tvFullname = (TextView) view.findViewById(R.id.tv_fullname);
        tvFullname.setText(at.getFullname());

        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);

        String resoursceName = at.getName();
        resoursceName = "ai_" + resoursceName.replaceAll("-", "_").toLowerCase() + "_color";

        int iconId = mContext.getResources().getIdentifier(resoursceName, "drawable", mContext.getPackageName());
        if (iconId != 0) {
            ivIcon.setImageResource(iconId);
        }

        return view;
    }

}
