package fr.pomp.adfuell.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.utils.comon.CommonUtils;

/**
 * Created by edena on 31/03/2017.
 */

public class MenuSliderFragment extends BaseFragment implements DrawerLayout.DrawerListener{


    @BindView(R.id.slider_menu_name)
    public TextView _menuNameTextView;

    @BindView(R.id.slider_menu_right_order)
    ImageView _slider_menu_right_order;

    @BindView(R.id.slider_menu_right_paye)
    ImageView _slider_menu_right_paye;

    @BindView(R.id.slider_menu_right_story)
    ImageView _slider_menu_right_story;

    @BindView(R.id.slider_menu_right_parain)
    ImageView _slider_menu_right_parrain;

    @BindView(R.id.slider_menu_right_propos)
    ImageView _slider_menu_right_propos;

    @BindView(R.id.slider_menu_right_localisation)
    ImageView _slider_menu_right_localisation;

    @BindView(R.id.slider_menu_right_vehicule)
    ImageView _slider_menu_right_vehicule;

    @BindView(R.id.slider_menu_right_faq)
    ImageView _slider_menu_right_faq;

    MenuEnum _menuClicked = MenuEnum.PROFIL;

     public MenuSliderFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_slider_menu, container, false);
        _edButterKniff.view(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if(_app.user() != null)
            _menuNameTextView.setText(_app.user().firstname+" "+_app.user().lastname.toUpperCase());*/
    }

    @OnClick(R.id.button_propos)
    public void clickPropos(View view){
        _menuClicked = MenuEnum.PROPOS;
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof CGVFragment) {

        }
        else {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new CGVFragment(),true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.button_name)
    public void goToProfile(View view){
        _menuClicked = MenuEnum.PROFIL;
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof ProfilFragment) {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new ProfilFragment(), true);
        }
        else {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new ProfilFragment(), true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.button_commander)
    public void goToOrder(View view){
        _menuClicked = MenuEnum.ORDER;
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof OrderHomeFragment) {

        }
        else {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new OrderHomeFragment(), true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.button_historique)
    public void goToStory(View view){
        _menuClicked = MenuEnum.STORY;
        updateMenuClicked();
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof StoryFragment) {

        }
        else {

            _mainActivity.replaceMainFragment(new StoryFragment(),true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.button_faq)
    public void goToFaq(View view){
        _menuClicked = MenuEnum.FAQ;
        updateMenuClicked();
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof FaqFragment) {

        }
        else {

            _mainActivity.replaceMainFragment(new FaqFragment(),true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.slider_menu_nous_contacter)
    public void goToContact(View view){
        _menuClicked = MenuEnum.CONTACT;
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof ContactFragment) {

        }
        else {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new ContactFragment(),true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.button_parainage)
    public void goToParain(View view){
         _menuClicked = MenuEnum.PARRAIN;
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof ParrainFragment) {

        }
        else {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new ParrainFragment(),true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.button_paiement)
    public void goToPay(View view){
        _menuClicked = MenuEnum.PAYE;
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof CardListFragment) {

        }
        else {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new CardListFragment(),true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.button_localisation)
    public void goToLocalisation(View view){
        _menuClicked = MenuEnum.LOCALISATION;
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof LocalisationFragment) {

        }
        else {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new LocalisationFragment(),true);
        }
        _mainActivity.drawerClose();
    }

    @OnClick(R.id.button_vehicule)
    public void goToVehicule(View view){
        _menuClicked = MenuEnum.VEHICULE;
        Fragment frag = _app.getFragmentCurrent();
        if(frag != null && frag instanceof CarListFragment) {

        }
        else {
            updateMenuClicked();
            _mainActivity.replaceMainFragment(new CarListFragment(),true);
        }
        _mainActivity.drawerClose();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        CommonUtils.log("onDrawerOpened "+_menuClicked);
        if(_app.getFragmentCurrent() != null && _app.getFragmentCurrent() instanceof OrderHomeFragment)
            _menuClicked = MenuEnum.CONTACT;
        updateMenuClicked();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        CommonUtils.log("onDrawerClosed ");
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        //CommonUtils.log("onDrawerStateChanged "+newState);
    }

    public enum MenuEnum{
         PAYE,PROPOS,ORDER,STORY,PARRAIN,PROFIL,CONTACT,LOCALISATION,VEHICULE,FAQ
     }

     void updateMenuClicked(){
         switch (_menuClicked){
             case VEHICULE:
                 setImageMenuPay(false);
                 setImageMenuParrain(false);
                 setImageMenuOrder(false);
                 setImageMenuPropos(false);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(true);
                 setImageMenuFaq(false);
                 break;
             case LOCALISATION:
                 setImageMenuPay(false);
                 setImageMenuParrain(false);
                 setImageMenuOrder(false);
                 setImageMenuPropos(false);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(true);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(false);
                 break;
             case PAYE:
                 setImageMenuPay(true);
                 setImageMenuParrain(false);
                 setImageMenuOrder(false);
                 setImageMenuPropos(false);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(false);
                 break;
             case PROPOS:
                 setImageMenuPay(false);
                 setImageMenuParrain(false);
                 setImageMenuOrder(false);
                 setImageMenuPropos(true);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(false);
                 break;
             case ORDER:
                 setImageMenuPay(false);
                 setImageMenuParrain(false);
                 setImageMenuOrder(true);
                 setImageMenuPropos(false);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(false);
                 break;
             case STORY:
                 setImageMenuPay(false);
                 setImageMenuParrain(false);
                 setImageMenuOrder(false);
                 setImageMenuPropos(false);
                 setImageMenuStory(true);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(false);
                 break;
             case PARRAIN:
                 setImageMenuPay(false);
                 setImageMenuParrain(true);
                 setImageMenuOrder(false);
                 setImageMenuPropos(false);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(false);
                 break;
             case PROFIL:
                 setImageMenuPay(false);
                 setImageMenuParrain(false);
                 setImageMenuOrder(false);
                 setImageMenuPropos(false);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(false);
                 break;
             case CONTACT:
                 setImageMenuPay(false);
                 setImageMenuParrain(false);
                 setImageMenuOrder(false);
                 setImageMenuPropos(false);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(false);
                 return;
             case FAQ:
                 setImageMenuPay(false);
                 setImageMenuParrain(false);
                 setImageMenuOrder(false);
                 setImageMenuPropos(false);
                 setImageMenuStory(false);
                 setImageMenuLocalisation(false);
                 setImageMenuVehicule(false);
                 setImageMenuFaq(true);
                 return;
             default:
                     break;


         }
     }

    void setImageMenuLocalisation(boolean isClicked){
        if(isClicked){
            _slider_menu_right_localisation.setImageResource(R.drawable.menu_right_blue);
        }
        else {
            _slider_menu_right_localisation.setImageResource(R.drawable.menu_right_grey);

        }
    }

    void setImageMenuVehicule(boolean isClicked){
        if(isClicked){
            _slider_menu_right_vehicule.setImageResource(R.drawable.menu_right_blue);
        }
        else {
            _slider_menu_right_vehicule.setImageResource(R.drawable.menu_right_grey);

        }
    }

     void setImageMenuPay(boolean isClicked){
        if(isClicked){
            _slider_menu_right_paye.setImageResource(R.drawable.menu_right_blue);
        }
        else {
            _slider_menu_right_paye.setImageResource(R.drawable.menu_right_grey);

        }
    }

    void setImageMenuParrain(boolean isClicked){
        if(isClicked) _slider_menu_right_parrain.setImageResource(R.drawable.menu_right_blue);
        else _slider_menu_right_parrain.setImageResource(R.drawable.menu_right_grey);
    }

    void setImageMenuOrder(boolean isClicked){
        if(isClicked) _slider_menu_right_order.setImageResource(R.drawable.menu_right_blue);
        else _slider_menu_right_order.setImageResource(R.drawable.menu_right_grey);
    }

    void setImageMenuPropos(boolean isClicked){
        if(isClicked) _slider_menu_right_propos.setImageResource(R.drawable.menu_right_blue);
        else _slider_menu_right_propos.setImageResource(R.drawable.menu_right_grey);
    }

    void setImageMenuStory(boolean isClicked){
        if(isClicked) _slider_menu_right_story.setImageResource(R.drawable.menu_right_blue);
        else _slider_menu_right_story.setImageResource(R.drawable.menu_right_grey);
    }

    void setImageMenuFaq(boolean isClicked){
        if(isClicked) _slider_menu_right_faq.setImageResource(R.drawable.menu_right_blue);
        else _slider_menu_right_faq.setImageResource(R.drawable.menu_right_grey);
    }

}
