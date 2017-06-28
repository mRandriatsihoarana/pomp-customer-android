package fr.pomp.adfuell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.fragment.AuthentFragment;
import fr.pomp.adfuell.fragment.CGVFragment;
import fr.pomp.adfuell.fragment.CarListFragment;
import fr.pomp.adfuell.fragment.CardListFragment;
import fr.pomp.adfuell.fragment.ConfirmationFragment;
import fr.pomp.adfuell.fragment.ContactFragment;
import fr.pomp.adfuell.fragment.FaqFragment;
import fr.pomp.adfuell.fragment.MenuSliderFragment;
import fr.pomp.adfuell.fragment.OrderHomeFragment;
import fr.pomp.adfuell.fragment.ProfilFragment;
import fr.pomp.adfuell.fragment.StoryFragment;
import fr.pomp.adfuell.utils.comon.CommonUtils;

/**
 * Created by edena on 27/03/2017.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_drawer)
    public DrawerLayout _drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _edButterKniff.activity(this);
        Fragment fragment = null;
        if (_app.user() != null) {
            fragment = new OrderHomeFragment();
            initCompteName();
        } else {
            fragment = new AuthentFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragment).commit();
        MenuSliderFragment sliderFragment = (MenuSliderFragment) getSupportFragmentManager().findFragmentById(R.id.main_menu_fragment);
        _drawer.addDrawerListener(sliderFragment);
        //successfulLogin();

    }
/*
    @OnClick(R.id.main_btn)
    public void k(View v){
        Intercom.client().displayConversationsList();
    }

    private void successfulLogin(){

        Registration registration = Registration.create().withUserId("1234567");
        Intercom.client().registerIdentifiedUser(registration);
    }

    private void logout() {

        Intercom.client().reset();
    }
    */

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.header_left_back_btn)
    public void onClickHeaderLeftBack(ImageButton button) {
        backPressed();
    }

    @OnClick(R.id.header_left_menu_btn)
    public void onClickHeaderLeftMenu(ImageButton button) {
        drawerOpen();

    }


    @Override
    public void onBackPressed() {

        backPressed();
        //super.onBackPressed();


    }

    public void backPressed(){
        try {
            Fragment frag = _app.getFragmentCurrent();
            if (frag == null) {
                startHome();
                return;
            } else if ((frag instanceof AuthentFragment) || frag instanceof OrderHomeFragment) {
                startHome();
                CommonUtils.log("Back press isCurrentFrament ");
                return;
            } else {
                if(frag instanceof StoryFragment || frag instanceof CardListFragment || frag instanceof CarListFragment || frag instanceof CGVFragment || frag instanceof ProfilFragment || frag instanceof ContactFragment || frag instanceof ConfirmationFragment || frag instanceof FaqFragment)
                    replaceMainFragment(new OrderHomeFragment(),true);
                else
                    backToPreviousFragment();
                CommonUtils.log("Back press backToPreviousFragment " + frag.getClass().getSimpleName());
                return;
            }
        } catch (NullPointerException e) {
            startHome();
            CommonUtils.log("Back press NullPointerException ");
        }
    }

    public void startHome() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    /**
     * Open the drawer menu left
     */
    public void drawerOpen() {
        if (_drawer != null) {
            _drawer.openDrawer(Gravity.LEFT);
        }
    }

    /**
     * Close the drawer menu left
     */
    public void drawerClose() {
        if (_drawer != null) {
            _drawer.closeDrawer(Gravity.LEFT);
        }
    }

    /**
     * Hide or Show the loading
     *
     * @param show
     */
    public void loadingShow(boolean show) {
        View loading = findViewById(R.id.main_loading);
        if (show) {
            if (!loading.isShown())
                loading.setVisibility(View.VISIBLE);
            lockedDrawer(true);

        } else {
            loading.setVisibility(View.GONE);
            lockedDrawer(false);
        }
    }

    public void lockedDrawer(boolean locked) {
        if (_drawer != null) {
            if (locked) {
                drawerClose();
                _drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                _drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

        }
    }

    public void initCompteName() {
        if (_app.user() != null) {
            TextView text = (TextView) _drawer.findViewById(R.id.slider_menu_name);
            text.setText(_app.user().firstname + " " + _app.user().lastname.toUpperCase());
        }
    }

    public void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public String getStringRessource(int id) {
        return getResources().getString(id);
    }
}
