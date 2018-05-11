/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atlas.crmapp.huanxin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.huanxin.db.InviteMessgeDao;
import com.atlas.crmapp.huanxin.domain.InviteMessage;
import com.atlas.crmapp.model.NewFriendsJson;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.SpUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.Serializable;
import java.util.List;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

	private Context context;
	private InviteMessgeDao messgeDao;
	private List<InviteMessage> msgs;

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.msgs = objects;
		this.context = context;

		messgeDao = new InviteMessgeDao(context);

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.em_row_invite_msg, null);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.agree = (Button) convertView.findViewById(R.id.agree);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			holder.groupContainer = (LinearLayout) convertView.findViewById(R.id.ll_group);
			holder.groupname = (TextView) convertView.findViewById(R.id.tv_groupName);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String str1 = context.getResources().getString(R.string.Has_agreed_to_your_friend_request);
		String str2 = context.getResources().getString(R.string.agree);
		
		String str3 = context.getResources().getString(R.string.Request_to_add_you_as_a_friend);
		String str4 = context.getResources().getString(R.string.Apply_to_the_group_of);
		String str5 = context.getResources().getString(R.string.Has_agreed_to);
		String str6 = context.getResources().getString(R.string.Has_refused_to);
		
		String str7 = context.getResources().getString(R.string.refuse);
		String str8 = context.getResources().getString(R.string.invite_join_group);
        String str9 = context.getResources().getString(R.string.accept_join_group);
		String str10 = context.getResources().getString(R.string.refuse_join_group);
		
		final InviteMessage msg = getItem(position);
		if (msg != null) {
		    
		    holder.agree.setVisibility(View.INVISIBLE);
		    
			if(msg.getGroupId() != null){ // show group name
				holder.groupContainer.setVisibility(View.VISIBLE);
				holder.groupname.setText(msg.getGroupName());
			} else{
				holder.groupContainer.setVisibility(View.GONE);
			}

			if(!TextUtils.isEmpty(msg.getReason())){
				Gson gson = new Gson();
				NewFriendsJson friends = gson.fromJson(msg.getReason(),NewFriendsJson.class);
				holder.reason.setText(friends.getMessage());
			}else {
				holder.reason.setText("");
			}


			if(TextUtils.isEmpty(msg.getNickName())){
				holder.name.setText(msg.getFrom());
			}else{
				holder.name.setText(msg.getNickName());

			}
			Glide.with(context).load(msg.getAvator()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(com.hyphenate.easeui.R.drawable.ease_default_avatar)).into(holder.avator);

			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAGREED) {
				holder.status.setVisibility(View.INVISIBLE);
				holder.reason.setText(str1);
				convertView.setVisibility(View.VISIBLE);
			} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED ||
			        msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION) {
			    holder.agree.setVisibility(View.VISIBLE);
                holder.agree.setEnabled(true);
                //holder.agree.setBackgroundResource(android.R.drawable.btn_default);
                holder.agree.setText(str2);
			    
				holder.status.setVisibility(View.VISIBLE);
				holder.status.setEnabled(true);
				//holder.status.setBackgroundResource(android.R.drawable.btn_default);
				holder.status.setText(str7);
				if(msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED){
					if (msg.getReason() == null) {
						// use default text
						holder.reason.setText(str3);
					}
				}else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) { //application to join group
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.reason.setText(str4 + msg.getGroupName());
					}
				} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION) {
				    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.reason.setText(str8 + msg.getGroupName());
                    }
				}
				
				// set click listener
                holder.agree.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // accept invitation
                        acceptInvitation(holder.agree, holder.status, msg);
						EMMessage message = EMMessage.createTxtSendMessage(context.getString(R.string.t25), msg.getFrom());
						String userPic = SpUtil.getString(context,"logoUrl", "");
						if (!TextUtils.isEmpty(userPic)) {
							message.setAttribute("userPic", userPic);
						}
						String userName = SpUtil.getString(context,"name", "");
						if (!TextUtils.isEmpty(userName)) {
							message.setAttribute("userName", userName);
						}
						EMClient.getInstance().chatManager().sendMessage(message);
                    }
                });
				holder.status.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// decline invitation
					    refuseInvitation(holder.agree, holder.status, msg);
					}
				});
			} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.AGREED) {
				holder.status.setText(str5);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			} else if(msg.getStatus() == InviteMessage.InviteMesageStatus.REFUSED){
				holder.status.setText(str6);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			} else if(msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION_ACCEPTED){
			    String str = msg.getGroupInviter() + str9 + msg.getGroupName();
                holder.status.setText(str);
                holder.status.setBackgroundDrawable(null);
                holder.status.setEnabled(false);
            } else if(msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION_DECLINED){
                String str = msg.getGroupInviter() + str10 + msg.getGroupName();
                holder.status.setText(str);
                holder.status.setBackgroundDrawable(null);
                holder.status.setEnabled(false);
            }
		/*	int statusVisiable = holder.status.getVisibility();
            if(statusVisiable == View.INVISIBLE ||statusVisiable == View.GONE ){
				convertView.setVisibility(View.GONE);
			}*/
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserCardActivity.class);
				intent.putExtra(UserCardActivity.KEY_MSG, (Serializable) msgs.get(position));
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	/**
	 * accept invitation
	 * 
	 */
	private void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
		String str1 = context.getResources().getString(R.string.Are_agree_with);
		final String str2 = context.getResources().getString(R.string.Has_agreed_to);
		final String str3 = context.getResources().getString(R.string.Agree_with_failure);
		final KProgressHUD pd = KProgressHUD.create(context)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel(str1)
				//.setDetailsLabel("Downloading data")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

		new Thread(new Runnable() {
			public void run() {
				// call api
				try {
					messgeDao.updateMessageAgreed(msg);
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							buttonAgree.setText(str2);
							buttonAgree.setBackgroundDrawable(null);
							buttonAgree.setEnabled(false);
							for(int i=0;i<msgs.size();i++){
								if(msgs.get(i).getStatus() != InviteMessage.InviteMesageStatus.BEINVITEED){
									msgs.remove(i);
								}
							}
							notifyDataSetChanged();
							buttonRefuse.setVisibility(View.INVISIBLE);
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});

				}
			}
		}).start();
	}
	
	/**
     * decline invitation
     * 
     */
    private void refuseInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
		String str1 = context.getResources().getString(R.string.Are_refuse_with);
		final String str2 = context.getResources().getString(R.string.Has_refused_to);
		final String str3 = context.getResources().getString(R.string.Refuse_with_failure);
		final KProgressHUD pd = KProgressHUD.create(context)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel(str1)
				//.setDetailsLabel("Downloading data")
				.setCancellable(false)
				.setAnimationSpeed(2)
				.setDimAmount(0.5f)
				.show();

        new Thread(new Runnable() {
            public void run() {
                // call api
                try {
                    messgeDao.updateMessageRefused(msg);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonRefuse.setText(str2);
                            buttonRefuse.setBackgroundDrawable(null);
                            buttonRefuse.setEnabled(false);
							if(msgs!= null){
								for(int i= 0 ; i< msgs.size() ; i++){
									 if(msgs.get(i).getStatus() != InviteMessage.InviteMesageStatus.BEINVITEED){
										 msgs.remove(i);
									 }
								}
								notifyDataSetChanged();
							}
                            buttonAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();
    }

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView reason;
        Button agree;
		Button status;
		LinearLayout groupContainer;
		TextView groupname;
		// TextView time;
	}

}
