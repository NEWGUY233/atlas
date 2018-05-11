package com.atlas.crmapp.huanxin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.atlas.crmapp.R;

/**
 * Created by huangyang on 2017/5/1.
 * 通讯录界面
 */

public class ContentListActivity extends BaseActivity {


    private String fragmentTag = "contactListFragment";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_content_list);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = ContactListFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragmentList,fragment,fragmentTag).commit();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if(result != null) {
//            if(result.getContents() == null) {
//                Log.i("Test","result = "+"Cancelled");
//            } else {
//                Log.i("Test","result = "+result.getContents());
//            }
//
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//
//    }

}
