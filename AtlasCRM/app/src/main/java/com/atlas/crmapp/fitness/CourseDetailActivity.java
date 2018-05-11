package com.atlas.crmapp.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.FitnessCourseDetailAdapter;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ClassJson;
import com.atlas.crmapp.model.ClassesJson;
import com.atlas.crmapp.model.LessonJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.view.CourseDetailOrderItem;
import com.atlas.crmapp.view.ProductInfoView;
import com.atlas.crmapp.view.ProductIntroduceView;
import com.atlas.crmapp.view.popupwindow.CourseDetailPopup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.bottomdialog.BaseBottomDialog;

public class CourseDetailActivity extends BaseStatusActivity {

    @BindView(R.id.iv_shape)
    tangxiaolv.com.library.EffectiveShapeView mIvShape;
    @BindView(R.id.v_product_info)
    ProductInfoView vProductInfo;

    @BindView(R.id.v_produt_inroduce)
    ProductIntroduceView vIntroduce;

    @BindView(R.id.v_course_order_item)
    CourseDetailOrderItem vCourseItem;

    @BindView(R.id.tv_today_null_course)
    TextView tvTodayNullCourse;
    @BindView(R.id.ll_other_couser)
    View vOtherCouser;

    @BindView(R.id.sv_main)
    ScrollView svMain;
    @BindView(R.id.ll_list)
    LinearLayout llList;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_detail)
    TextView tvDescription;

    @BindView(R.id.ll_course_order_item)
    View llCourseOderItem;


    private FitnessCourseDetailAdapter adapter;
    private ArrayList<ClassJson> classes = new ArrayList<ClassJson>();;
    private int page = 0;
    private long id;

    private BaseBottomDialog mPayDialog;
    private TextView btnPay;
    private ClassJson currentClass;
    private boolean isFitContractOrAllowance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ButterKnife.bind(this);
        umengPageTitle = getString(R.string.t20);
        setTitle(umengPageTitle);
        id = getIntent().getLongExtra("id", 0);
        prepareActivityData();
    }


    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();

        if(GlobalParams.getInstance().isLogin()){
            //先判断是否有健身权益 是的话 显示为免费
            BizDataRequest.requestCheckFitContractOrAllowance(this, statusLayout, new BizDataRequest.OnResponseBoolean() {
                @Override
                public void onSuccess(boolean is) {
                    CourseDetailActivity.this.isFitContractOrAllowance = is;
                    getDetailAndLessonList(is);
                }
                @Override
                public void onError(DcnException error) {

                }
            });
        }else{
            getDetailAndLessonList(false);
        }
    }

    private void getDetailAndLessonList(final boolean isFitContractOrAllowance){
        //获取课程详情
        BizDataRequest.requestGetLesson(CourseDetailActivity.this, id, new BizDataRequest.OnLessonRequestResult() {
            @Override
            public void onSuccess(LessonJson lessonJson) {
                setViewData(lessonJson, isFitContractOrAllowance);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
        BizDataRequest.requestGetClasses(CourseDetailActivity.this, page, 100, id, statusLayout, new BizDataRequest.OnClassesRequestResult() {
            @Override
            public void onSuccess(ClassesJson classesJson) {
                List<ClassJson> rows = classesJson.rows;
                int rowsSize = rows.size();
                if(rowsSize >0) {
                    final ClassJson cls = rows.get(0);
                    vCourseItem.updateViews(cls, onClickBooking, isFitContractOrAllowance, false);
                    llCourseOderItem.setVisibility(View.VISIBLE);
                    for (int i = 1; i <rowsSize ; i++) {
                        classes.add(rows.get(i));
                    }
                    if(rowsSize == 1){
                        vOtherCouser.setVisibility(View.GONE);
                    }
                }else{
                    tvTodayNullCourse.setVisibility(View.VISIBLE);
                    vCourseItem.setVisibility(View.GONE);
                    vOtherCouser.setVisibility(View.GONE);
                }

                //使用listView 会导致滚动的问题。
               /* if (adapter != null) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter = new FitnessCourseDetailAdapter(CourseDetailActivity.this, classes, onClickBooking, isFitContractOrAllowance);
                    lvClass.setAdapter(adapter);
                }*/

               for (ClassJson classJson : classes){
                   CourseDetailOrderItem courseDetailOrderItem = new CourseDetailOrderItem(CourseDetailActivity.this);
                   courseDetailOrderItem.updateViews(classJson, onClickBooking, isFitContractOrAllowance, true);
                   llList.addView(courseDetailOrderItem);
               }





            }

            @Override
            public void onError(DcnException error) {
                statusLayout.showError(error);
            }
        });
    }



    private void setViewData(LessonJson lesson, boolean isFitContractOrAllowance) {
        GlideUtils.loadCustomImageView(this, R.drawable.product_thum, LoadImageUtils.loadMiddleImage(lesson.thumbnail) ,mIvShape);
        //vProductInfo.updateViews(lesson.name,lesson.price, lesson.description, isFitContractOrAllowance);

        tvName.setText(lesson.name);
        tvDescription.setText(lesson.description);
        vIntroduce.updateVies(lesson.medias,getResources().getString(R.string.lesson_introduce),lesson.detail);
    }

    private View.OnClickListener onClickBooking = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClassJson classJson = (ClassJson )v.getTag();
            if (CourseDetailActivity.this.getGlobalParams().isLogin()) {
                CourseDetailActivity.this.setCurrentOrder(classJson);
            } else {
                CourseDetailActivity.this.showAskLoginDialog();
            }
        }
    };



    //弹出预约框
    private void showPayBottomDialog() {

        final CourseDetailPopup courseDetailPopup = new CourseDetailPopup(this, currentClass, isFitContractOrAllowance);
        courseDetailPopup.setBottomOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BizDataRequest.requestCreateClassBooking(CourseDetailActivity.this, currentClass.id, "", new BizDataRequest.OnResponseOpenOrderJson() {
                    @Override
                    public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson) {
                        //Todo 订单处理
                        Intent intent = new Intent(CourseDetailActivity.this, OrderConfirmActivity.class);
                        intent.putExtra("type", Constants.ORDER_TYPE.FITNESS);
                        intent.putExtra("confirmOrder", (Serializable) responseOpenOrderJson);
                        startActivityForResult(intent, 999);
                        if(courseDetailPopup != null){
                            courseDetailPopup.dismiss();
                        }
                    }

                    @Override
                    public void onError(DcnException error) {
                        //Toast.makeText(CourseDetailActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        //dismissLoading();
                    }
                });
            }
        });
        courseDetailPopup.setPopupWindowFullScreen(true);
        courseDetailPopup.showPopupWindow();
    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }

    public void setCurrentOrder(ClassJson lesson){
        currentClass = lesson;
        showPayBottomDialog();
    }
}
