package fr.pomp.adfuell;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.fragment.AuthentCreateFragment;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.utils.edena.EDButterKniff;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by edena on 27/03/2017.
 */

public class BaseActivity extends FragmentActivity {
    public EDButterKniff _edButterKniff;
    public App _app;

    @BindView(R.id.header_title)
    public TextView _headerTitle;
    @BindView(R.id.header_left_back_btn)
    public ImageButton _headerLeftBackBtn;
    @BindView(R.id.header_left_menu_btn)
    public ImageButton _headerLeftMenuBtn;
    @BindView(R.id.header_right_btn)
    public Button _headerRightBtn;

    @BindView(R.id.header_img)
    public ImageView _headerImg;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        _edButterKniff = EDButterKniff.getInstance();

        _app = (App) getApplication();
        /*
        _slideMenu = new SlidingMenu(this);
        _slideMenu.setMode(SlidingMenu.LEFT);
        _slideMenu.setShadowWidthRes(R.dimen.slide_width);
        _slideMenu.setBehindOffsetRes(R.dimen.slide_offset);
        // _slideMenu.setFadeDegree(0.35f);
        _slideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        _slideMenu.attachToActivity(this, SlidingMenu.);
        _slideMenu.setMenu(R.layout.view_menu_slide);

        */
    }

    public void getToken(final IWSDelegate delegate){
        WSService.getInstance(_app._retrofit).getTokent(new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                delegate.onFaillure(obj);
            }

            @Override
            public void onSuccess(Object obj) {
                if(obj != null){
                    String data = (String)obj;
                    CommonUtils.log(data);
                    _app.setTokenJson(data);
                    delegate.onSuccess(_app.getToken());
                }else{
                    delegate.onFaillure(null);
                }


            }
        });
    }

    /**
     * Remplace le fragment dans le conteneur main_frame
     * @param fragment
     */
   public void replaceMainFragment(Fragment fragment, boolean addBackStack){
       changeHeaderBackground(R.color.transparent);
       String tag = fragment.getClass().getSimpleName();
       FragmentManager fm = getSupportFragmentManager();
       FragmentTransaction fmt =  fm.beginTransaction();
       Fragment frag = fm.findFragmentByTag(tag);
       if(frag != null){

           fmt.show(frag);
           _app.setFragmentCurrent(frag);
       }else{
           fmt.replace(R.id.main_frame, fragment);
           fmt.addToBackStack(tag);
           _app.setFragmentCurrent(fragment);
       }
       fmt.commit();

    }

    public void backToPreviousFragment(){
        changeHeaderBackground(R.color.transparent);
        FragmentManager fm = getSupportFragmentManager();

            //getSupportFragmentManager().popBackStackImmediate();
            if(fm.getBackStackEntryCount() > 0){
                try{
                    fm.popBackStack();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


    }

    /**
     * verifier si c'est un tablet ou un smart
     * true si tab et false si smart
     * @return
     */
    public boolean isTablet(){
        return getResources().getBoolean(R.bool.is_tablet);
    }



    /**
     * Hide or Show the title bar
     * @param show
     */
    public void headerShow(boolean show){
        View header = findViewById(R.id.header);
            if(header != null){
                if(show){
                    if(!header.isShown())
                        header.setVisibility(View.VISIBLE);
                }else{
                    header.setVisibility(View.GONE);
                }
            }

    }

    /**
     * Change the text title bar
     * @param title
     */
    public void changeHeaderTitle(String title){
        if(_headerTitle != null){
            _headerTitle.setText(title);
            _headerTitle.setVisibility(View.VISIBLE);
            _headerImg.setVisibility(View.GONE);
        }
    }

    public void headerShowLogo(){
        if(_headerTitle != null){
            _headerTitle.setText("");
            _headerTitle.setVisibility(View.GONE);
            _headerImg.setVisibility(View.VISIBLE);
        }
    }

    public void headerHideLogo(){
        if(_headerTitle != null){
            _headerTitle.setText("");
            _headerTitle.setVisibility(View.GONE);
            _headerImg.setVisibility(View.GONE);
        }
    }

    /**
     * Change the text of the hdeader right btn
     * @param text
     */
    public void changeHeaderRightText(String text){
        if(_headerRightBtn != null){
            headerRightBtnShow(true);
            _headerRightBtn.setText(text);
        }
    }

    /**
     * Make visible or not the right button
     * @param show
     */
    public void headerRightBtnShow(boolean show){
       if(_headerRightBtn != null)
            if(show){
                if(!_headerRightBtn.isShown())
                    _headerRightBtn.setVisibility(View.VISIBLE);
            }else{
                _headerRightBtn.setVisibility(View.GONE);
            }
    }

    public  void headerRightShowAdd(){
        changeHeaderRightText(getResources().getString(R.string.add));
        _headerRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceMainFragment(new AuthentCreateFragment(),true);
            }
        });
    }

    public  void headerRightShowNext(){
        changeHeaderRightText(getResources().getString(R.string.next));
        _headerRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void headerShowLeftBack(){
        if(_headerLeftBackBtn != null){
            _headerLeftBackBtn.setVisibility(View.VISIBLE);
            _headerLeftMenuBtn.setVisibility(View.GONE);
        }

    }

    public void headerShowLeftMenu(){
        if(_headerLeftBackBtn != null){
            _headerLeftMenuBtn.setVisibility(View.VISIBLE);
            _headerLeftBackBtn.setVisibility(View.GONE);
        }

    }

    /**
     *  change header background color
     * @param colorId
     */
    public void changeHeaderBackground(int colorId){
        View view = findViewById(R.id.header);
        if(view != null){
            if(view.isShown())
                view.setBackgroundColor(ContextCompat.getColor(this,colorId));
        }
    }
    @OnClick(R.id.main_loading)
    public void loadingClick(View v){

    }
}
