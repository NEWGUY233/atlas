package com.atlas.crmapp.workplace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.SelectMemberAdapter;
import com.atlas.crmapp.model.bean.ItemSelectMember;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectMemberActivity extends AppCompatActivity {
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    private  SelectMemberAdapter selectMemberAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.s92);
        List<ItemSelectMember> itemSelectMemberList = new ArrayList<>();
        for(int i=0;i<20;i++){
            ItemSelectMember itemSelectMember = new ItemSelectMember();
            itemSelectMember.setIcon("");
            itemSelectMember.setName("Jsondan");
            itemSelectMemberList.add(itemSelectMember);
        }
        selectMemberAdapter = new SelectMemberAdapter(this,itemSelectMemberList);
        listView.setAdapter(selectMemberAdapter);
        addListviewHead();
    }

    private void addListviewHead(){
        View view=  LayoutInflater.from(this).inflate(R.layout.view_search, null);
        listView.addHeaderView(view);
    }

    @OnClick(R.id.ibBack)
    void onBack() {
        finish();
    }
}
