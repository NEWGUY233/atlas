
package com.atlas.crmapp.huanxin;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.huanxin.db.InviteMessgeDao;
import com.atlas.crmapp.huanxin.domain.InviteMessage;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Application and notification
 * * 新的朋友
 *
 */
public class NewFriendsMsgActivity extends BaseActivity {

	ListView listView;
	List<InviteMessage> msgs;
	InviteMessgeDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_friends_msg);
		listView = (ListView) findViewById(R.id.list);
		getMsgsList();
	}


	public void getMsgsList(){
		dao = new InviteMessgeDao(this);
		msgs = dao.getMessagesList();


		if(msgs!=null&&msgs.size()>0) {
			ArrayList<String> list = new ArrayList<String>();

			for (int i = 0; i < msgs.size(); i++) {
				list.add(msgs.get(i).getFrom());
			}

			getUserInfoList(list);
		}
	}

	public void back(View view) {
		finish();
	}

	private void getUserInfoList(ArrayList<String> list){
		BizDataRequest.requestUidUserInfo(NewFriendsMsgActivity.this, list, new BizDataRequest.OnUidUserInfo() {
			@Override
			public void onSuccess(List<PersonInfoJson> personList) {
				for(int i=0;i<personList.size();i++){
					for(int j=0;j<msgs.size();j++){
						InviteMessage msg = msgs.get(j);
						if(msg.getFrom().equals(personList.get(i).uid.toLowerCase())){
							msg.setNickName(personList.get(i).nick);
							msg.setAvator(personList.get(i).avatar);
							Logger.d("mags ---" + msgs.get(j).getStatus()+ "  personList.get(i).uid   " + personList.get(i).uid);
							if (msg.getStatus() !=InviteMessage.InviteMesageStatus.BEINVITEED ){
								msgs.remove(j);
							}
						}
					}
				}

				NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(NewFriendsMsgActivity.this, 1, msgs);
				listView.setAdapter(adapter);
				dao.saveUnreadMessageCount(0);
			}

			@Override
			public void onError(DcnException error) {

			}
		});
	}
}
