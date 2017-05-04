package trackerapp.trackerapp.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import trackerapp.trackerapp.model.TypeModel;

/**
 * Created by Krystian on 04.05.2017.
 */

public class ActivityService {

    // activity types
    public enum ACTIVITY_TYPES {
        running(new TypeModel(2, "running", "running", "F7902A", 2, new int[]{1, 2, 3, 4, 5, 8})),
        cyclingRoad(new TypeModel(1, "cycling-road", "cycling-road", "00CECE", 6, new int[]{1, 2, 3, 4, 5})),
        runningTreadmill(new TypeModel(5, "running-treadmill", "running-treadmill", "00309B", 3, new int[]{1, 2, 3, 4, 5, 8})),
        walking(new TypeModel(7, "walking", "walking", "A3D830", 1, new int[]{1, 2, 3, 4, 5, 8}));


        private final TypeModel type;
        private ACTIVITY_TYPES(TypeModel t) {
            type = t;
        }

        public TypeModel getType() {
            return type;
        }
    }

    public static final List<TypeModel> getAllActivityTypes(Context context) {

        List<TypeModel> at = new ArrayList<>();

        for(ActivityService.ACTIVITY_TYPES f : ActivityService.ACTIVITY_TYPES.values()) {
            TypeModel tm = f.getType();

            String packageName = context.getPackageName();
            int resId = context.getResources().getIdentifier("activity_fullname_" + tm.getName().replace('-', '_'), "string", packageName);
            tm.setFullname(context.getResources().getString(resId));

            at.add(tm);
        }

        Collections.sort(at, new Comparator<TypeModel>(){
            public int compare(TypeModel o1, TypeModel o2){
                if(o1.getSort() == o2.getSort())
                    return 0;
                return o1.getSort() < o2.getSort() ? -1 : 1;
            }
        });

        return at;
    }

}
