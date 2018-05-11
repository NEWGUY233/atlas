package com.atlas.crmapp.usercenter.printfragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.MyPrintAdapter;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.PrintDataJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.usercenter.MyPrintActivity;
import com.atlas.crmapp.util.GetCommonObjectUtils;
import com.atlas.crmapp.view.RefreshFootView;
import com.liaoinstan.springview.widget.SpringView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/2/2.
 */

public class PrintDYFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springview)
    SpringView springView;
    private RefreshFootView refreshFootView;
    int page = 0;

    MyPrintAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.include_recycler;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);

        adapter = new MyPrintAdapter(getContext(),MyPrintAdapter.DY);
        recyclerView.setAdapter(adapter);
        getData();

        refreshFootView = new RefreshFootView(getActivity());
        springView.setFooter(refreshFootView);
        springView.setType(SpringView.Type.FOLLOW);

        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
                getData();
                GetCommonObjectUtils.onFinishFreshAndLoad(springView);
            }
        });
    }

    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    private void getData(){
        BizDataRequest.getPrint(getContext(), page, MyPrintActivity.PRINT, statusLayout, new BizDataRequest.MyPrintRequestRescult() {
            @Override
            public void onSuccess(PrintDataJson printJason) {

                if (page == 0) {
                    adapter.setList(printJason.getRows());
                    if (printJason.getRows() == null || printJason.getRows().size() == 0)
                        statusLayout.showEmpty();
                }else
                    adapter.updateList(printJason.getRows());
                page++;
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


}
