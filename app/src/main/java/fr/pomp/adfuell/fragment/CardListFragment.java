package fr.pomp.adfuell.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.CardListModel;
import fr.pomp.adfuell.model.CardModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by edena on 31/03/2017.
 */

public class CardListFragment extends BaseFragment {

    @BindView(R.id.card_list_contener)
    LinearLayout _contenerList;
    @BindView(R.id.card_list_empty_msg)
    View _emptyView;

    LayoutInflater _layoutInflater;

    private int _poolAuth = 0;

    List<CardListModel> _list = new ArrayList<>();

    public CardListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_card_list, container, false);

        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.changeHeaderTitle(getString(R.string.carte));
        _mainActivity.headerRightBtnShow(false);
        _layoutInflater = (LayoutInflater)_mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _emptyView.setVisibility(View.GONE);
        CardModel card = new CardModel();
        card.number = "number";
        card.date = "date";
        getList();


        return v;
    }

    void updateList(){

        //final ArrayList<CardModel> list =  CardService.getIntance(_app).list();
       _contenerList.removeAllViews();
        //CardListModel card = null;
        RelativeLayout view = null;
        int listSize = _list != null?_list.size():0;
        if(listSize>0){
            _emptyView.setVisibility(View.GONE);
            for (int index = 0; index<listSize; index++) {
                final int  indexe = index;
                final CardListModel card = _list.get(index);
                view = (RelativeLayout)_layoutInflater.inflate(R.layout.view_card_list,null);
                TextView cardNumberView = (TextView)view.findViewById(R.id.card_number);
                TextView cardExpiredView = (TextView)view.findViewById(R.id.card_expired);
                ImageView img_delete = (ImageView) view.findViewById(R.id.card_del);
                //String val = card.number;
                String val = card.name;
                try{
                    //val = card.number.substring(0,4).concat(" **** **** ****");
                }catch (StringIndexOutOfBoundsException e){
                    e.printStackTrace();
                }

                cardNumberView.setText(val);
                //cardExpiredView.setText(card.date);
                cardExpiredView.setText(getStringRes(R.string.text_create_on)+" "+card.createdOn.getFormatedDate());
                if(card.order == 0){
                    img_delete.setVisibility(View.VISIBLE);
                    img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            CommonUtils.alert(getStringRes(R.string.supress_carte), null,0, new CommonUtils.IDialog() {
                                @Override
                                public void no(Object obj) {

                                }

                                @Override
                                public void yes(Object obj) {
                                    //_list.remove(indexe);
                                    //CardService.getIntance(_app).setList(_list);
                                    //updateList();
                                    delete(card.id);
                                }
                            },_mainActivity);


                        }
                    });
                }else {
                    img_delete.setVisibility(View.GONE);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardAddFragment fragment = new CardAddFragment();
                        fragment._cardUpdate = card;
                        _mainActivity.replaceMainFragment(fragment,true);
                    }
                });
                _contenerList.addView(view);
                _contenerList.addView(separtor());

            }
        }else{
            _emptyView.setVisibility(View.VISIBLE);
        }


    }


    View separtor(){
        View view = new View(_mainActivity);
        view.setBackgroundResource(R.color.gris);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1 ));
        return view;
    }


    @OnClick({R.id.card_list_add,R.id.card_list_add_bas})
    public void create(Button btn){

       _mainActivity.replaceMainFragment(new CardAddFragment(),true);

    }

    void delete(String id){
        _poolAuth = 0;
        runDeleteCard(id);
    }

    private void runDeleteCard(String id){
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).deleteCard(id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if(obj != null){
                    int codeError = (int) obj;
                    CommonUtils.log("codeError "+codeError);
                    if(codeError == 401){
                        getToken();
                    }else if(codeError == 404){
                        CommonUtils.alertCustom(getString(R.string.text_suppress_error), null, R.drawable.ok, null, getContext());
                    }else{
                        CommonUtils.alertCustom(getString(R.string.succes_delete_carte), null, R.drawable.ok, null, getContext());
                        getList();

                    }
                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                CommonUtils.log("Suppression carte effectué");
                CommonUtils.alertCustom(getString(R.string.succes_delete_carte), null, R.drawable.ok, null, getContext());
                getList();
            }
        });
    }

    void getList(){
        _poolAuth = 0;
        runCardList();
    }

    private void runCardList(){
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).getCardList(_app.userObject().id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if(obj != null){
                    int codeError = (int) obj;
                    CommonUtils.log("codeError "+codeError);
                    if(codeError == 401 && _poolAuth == 0) getToken();
                    else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                try{
                    if(obj != null){
                        String jsonStr = (String)obj;
                        CommonUtils.log(jsonStr);
                        _list = _app._json.deSerializeArray(jsonStr,CardListModel.class);
                        updateList();
                   /*
                   _app.carListSaveJson(jsonStr);
                    _list = _app.carList();
                    updateList();
                    UserModel user = _app.userObject();
                    user.vehicles = _app.carList().size();
                    _app.userSaveObject(user);
                    */
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * Success token
     * @param obj
     */
    @Override
    public void onSuccess(Object obj) {
        _mainActivity.loadingShow(false);
        if(obj != null){
            TokenModel token = (TokenModel) obj;
            CommonUtils.log(token.accessToken);
            if(_poolAuth == 0)
                getList();
            _poolAuth = 1;
        }

    }


}
