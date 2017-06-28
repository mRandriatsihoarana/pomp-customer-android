package fr.pomp.adfuell.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;
import com.eralp.circleprogressview.ProgressAnimationListener;

import butterknife.BindView;
import fr.pomp.adfuell.R;

/**
 * Created by edena on 31/03/2017.
 */

public class ConfirmOkFragment extends BaseFragment {

    @BindView(R.id.circle_progress_view)
    CircleProgressView _circleProgressView;
    @BindView(R.id.img_confirmation)
    ImageView _confirmationImage;
    @BindView(R.id.text_info)
    TextView _infoTextView;
    @BindView(R.id.img_step1)
    ImageView _step1IamgeView;
    @BindView(R.id.img_step2)
    ImageView _step2IamgeView;
    @BindView(R.id.img_step3)
    ImageView _step3IamgeView;
    @BindView(R.id.img_step4)
    ImageView _step4IamgeView;
    @BindView(R.id.img_step5)
    ImageView _step5IamgeView;
    @BindView(R.id.img_jerricane)
    ImageView _jerricaneIamgeView;
    @BindView(R.id.img_complete)
    ImageView _completeIamgeView;

    public ConfirmOkFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_confirm_ok, container, false);
        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.headerShowLeftMenu();
        _mainActivity.headerShowLogo();

        _circleProgressView.setTextEnabled(true);
        _circleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
        _circleProgressView.setStartAngle(270);
        _circleProgressView.setProgressWithAnimation(100, 5000);

        final Handler handler = new Handler();

        _circleProgressView.addAnimationListener(new ProgressAnimationListener() {
            @Override
            public void onValueChanged(float v) {
                /*_circleProgressView.setTextPrefix(""+((int)v));
                _circleProgressView.setTextSuffix("%");*/
            }

            @Override
            public void onAnimationEnd() {
                _confirmationImage.setVisibility(View.VISIBLE);
                _infoTextView.setText(getStringRes(R.string.text_commande_accept));
                _step2IamgeView.setImageResource(R.drawable.step_color);

                /*handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 5000ms
                        _confirmationImage.setVisibility(View.GONE);
                        _circleProgressView.setVisibility(View.GONE);
                        _jerricaneIamgeView.setVisibility(View.VISIBLE);
                        _infoTextView.setText(getStringRes(R.string.text_commande_livraison));
                        _step3IamgeView.setImageResource(R.drawable.step_color);
                        _step4IamgeView.setImageResource(R.drawable.step_color);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 5000ms
                                _confirmationImage.setVisibility(View.GONE);
                                _circleProgressView.setVisibility(View.GONE);
                                _jerricaneIamgeView.setVisibility(View.GONE);
                                _completeIamgeView.setVisibility(View.VISIBLE);
                                _infoTextView.setText(getStringRes(R.string.text_commande_complete));
                                _step5IamgeView.setImageResource(R.drawable.step_color);
                            }
                        },5000);
                    }
                }, 5000);*/
            }
        });

        return v;
    }
}
