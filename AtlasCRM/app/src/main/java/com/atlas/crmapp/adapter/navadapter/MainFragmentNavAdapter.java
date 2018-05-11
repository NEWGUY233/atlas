package com.atlas.crmapp.adapter.navadapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.atlas.crmapp.MainFragment;
import com.atlas.crmapp.coffee.fragment.CoffeeFragment;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.ecosphere.EcosphereFragment;
import com.atlas.crmapp.ecosphere.fragments.ActivitiesFragment;
import com.atlas.crmapp.fitness.FitnessFragment;
import com.atlas.crmapp.usercenter.MyFragment;
import com.atlas.crmapp.util.MTAUtils;
import com.atlas.crmapp.workplace.WorkPlaceFragment;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/14
 *         Description :
 */

public class MainFragmentNavAdapter implements FragmentNavigatorAdapter {

    public static final int FM_WORK_PLACE = 0;
    public static final int FM_COFFEE = 1;
    public static final int FM_KITCHEN = 2;
    public static final int FM_FITNESS = 3;
    public static final int FM_GOGREEN = 4;
    public static final int FM_STUDIO = 5;
    public static final int FM_ECOSPHERE = 6;
    public static final int FM_USER = 7;
    public static final int FM_MAIN = 8;

    public static final String WORKPLACE_TAG = "WORKPLACE_FRAGMENT";
    public static final String COFFEE_TAG = "COFFEE_FRAGMENT";
    public static final String KITCHEN_TAG = "KITCHEN_FRAGMENT";
    public static final String FITNESS_TAG = "FITNESS_FRAGMENT";
    public static final String GOGREEN_TAG = "GOGREEN_FRAGMENT";
    public static final String STUDIO_TAG = "STUDIO_FRAGMENT";
    public static final String ECOSPHERE_TAG = "ECOSPHERE_FRAGMENT";
    public static final String USER_TAG = "USER_FRAGMENT";
    public static final String MAIN_TAG = "MAIN_FRAGMENT";


    public boolean mainFragmentShowCode ;

    public void setMainFragmentShowCode(boolean mainFragmentShowCode) {
        this.mainFragmentShowCode = mainFragmentShowCode;
    }

    private Activity activity;

    public MainFragmentNavAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public Fragment onCreateFragment(int position) {
        Fragment fragment = null;
        String mtaEventId = "";
        switch (position) {
            case FM_COFFEE:
                fragment = CoffeeFragment.newInstance();
                mtaEventId = "business_coffee";
                break;
            case FM_ECOSPHERE:
                fragment = EcosphereFragment.newInstance();
                mtaEventId = "main_ecosphere";
                break;
            case FM_USER:
                fragment = MyFragment.newInstance("", "");
                mtaEventId = "main_user";
                break;
            case FM_WORK_PLACE:
                fragment = WorkPlaceFragment.newInstance();
                mtaEventId = "business_work_place";
                break;
            case FM_KITCHEN:
                fragment = CoffeeFragment.newInstance();
                mtaEventId = "business_kitchen";
                break;
            case FM_FITNESS:
                fragment = FitnessFragment.newInstance();
                mtaEventId = "business_fitness";
                break;
            case FM_GOGREEN:
                fragment = CoffeeFragment.newInstance();
                mtaEventId = "business_gogreen";
                break;
            case FM_STUDIO:
                fragment = ActivitiesFragment.newInstance( GlobalParams.getInstance().getStudioCode(),true);
                mtaEventId = "business_studio";
                break;
            case FM_MAIN:
                fragment = MainFragment.newInstance();
                mtaEventId = "business";
                break;
            default:
                fragment = MainFragment.newInstance();
                mtaEventId = "business";

        }

        MTAUtils.trackCustomEvent(activity, mtaEventId);
        return fragment;

    }

    @Override
    public String getTag(int position) {
        if (position == FM_USER) {
            return MyFragment.TAG;
        }else if(position == FM_ECOSPHERE){
            return  EcosphereFragment.TAG;
        }else if(position == FM_COFFEE){
            return  COFFEE_TAG;
        }else if(position == FM_WORK_PLACE){
            return  WorkPlaceFragment.TAG;
        }else if(position == FM_FITNESS){
            return  FitnessFragment.TAG;
        }else if(position == FM_KITCHEN){
            return  KITCHEN_TAG;
        }else if(position == FM_GOGREEN){
            return GOGREEN_TAG;
        }else if(position == FM_STUDIO){
            return  STUDIO_TAG;
        }else if(position == FM_MAIN){
            return  MAIN_TAG;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 9;
    }
}
