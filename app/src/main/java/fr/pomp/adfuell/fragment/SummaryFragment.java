package fr.pomp.adfuell.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.CarModel;
import fr.pomp.adfuell.model.CardListModel;
import fr.pomp.adfuell.model.CommandeModel;
import fr.pomp.adfuell.model.PricePerLiterModel;
import fr.pomp.adfuell.model.ScheduleModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.utils.munix.Strings;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by Mandimbisoa on 21/04/2017.
 */

public class SummaryFragment extends BaseFragment implements OnMapReadyCallback {

    @BindView(R.id.id_entreprise)
    TextView _entrepriseTextView;
    @BindView(R.id.id_adresse)
    TextView _adresseTextView;
    @BindView(R.id.id_carburant)
    TextView _carburantTextView;
    @BindView(R.id.id_prix_litre)
    TextView _prixLitreTextView;
    @BindView(R.id.id_code_carte)
    TextView _codeCarteTextView;
    @BindView(R.id.id_total)
    TextView _totalTextView;
    @BindView(R.id.spn_card)
    Spinner _cardSpinner;

    static View _view;

    private GoogleMap _map;
    int _poolAuth = 0;

    public String _action = "";
    private CommandeModel _commande;
    private Integer _positionCard = null;

    ArrayList<CardListModel> _listCard = new ArrayList<CardListModel>();
    CardAdapter _cardAdapter;

    boolean _noCarte = false;

    public SummaryFragment() {
    }


    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (_view != null) {
            ViewGroup parent = (ViewGroup) _view.getParent();
            if (parent != null)
                parent.removeView(_view);
        }
        try {
            _view = inflater.inflate(R.layout.fragment_summary, container, false);
        } catch (InflateException e) {

        }

        _edButterKniff.view(this, _view);
        _mainActivity.headerShow(true);
        _mainActivity.lockedDrawer(false);
        _mainActivity.headerShowLeftBack();
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getString(R.string.text_recapitulatif));

        SupportMapFragment mapSupport = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapSupport.getMapAsync(this);

        _commande = _app.commandeObject();
        getListCard();
        setData();

        _cardAdapter = new CardAdapter();
        _cardSpinner.setAdapter(_cardAdapter);

        return _view;
    }

    @Override
    public void onResume() {
        _app._isOrderConfirmedView = true;
        super.onResume();
    }

    private void setData() {
        if (_app.scheduleObject() != null) {
            ScheduleModel scheduleModel = _app.scheduleObject();
            _entrepriseTextView.setText(getStringRes(R.string.text_entreprise_name) + " " + scheduleModel.company.name);
            _adresseTextView.setText(scheduleModel.location.address);

            if (_app.carObject() != null) {
                CarModel car = _app.carObject();
                _carburantTextView.setText(Strings.ucfirst(_app.carObject().fuelType));

                PricePerLiterModel pricePerLiterModel = scheduleModel.company.pricePerLiter;
                if (car.fuelType.equalsIgnoreCase("diesel"))
                    _prixLitreTextView.setText(getStringRes(R.string.text_price_liter) + " " + pricePerLiterModel.pricePerLiterDiesel + " €");
                else if (car.fuelType.equalsIgnoreCase("unleaded95"))
                    _prixLitreTextView.setText(getStringRes(R.string.text_price_liter) + " " + pricePerLiterModel.pricePerLiterUnleaded95 + " €");
                else if (car.fuelType.equalsIgnoreCase("unleaded98"))
                    _prixLitreTextView.setText(getStringRes(R.string.text_price_liter) + " " + pricePerLiterModel.pricePerLiterUnleaded98 + " €");
            }
        }
        if (_commande != null) {
            if (_commande.orderType.equals("fillUp")) {
                _totalTextView.setText(getStringRes(R.string.text_full_resume));
            } else {
                _totalTextView.setText(_commande.amount+"€ TTC");
            }
        }
    }

    void getListCard() {
        _poolAuth = 0;
        runCardList();
    }

    private void runCardList() {
        _action = "get_card";
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).getCardList(_app.userObject().id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                _noCarte = true;
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) getToken();
                    else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                try {
                    if (obj != null) {
                        String jsonStr = (String) obj;
                        CommonUtils.log(jsonStr);
                        CommonUtils.log("size card list= " + _app.carList().size());
                        CommonUtils.log("jsonStr " + jsonStr);
                        _listCard = _app._json.deSerializeArray(jsonStr, CardListModel.class);
//                        _listCard = null;
                        if (_listCard != null && _listCard.size() > 0) {
                            _cardAdapter.notifyDataSetChanged();
                            _noCarte = false;

                            _codeCarteTextView.setVisibility(View.GONE);
                            _cardSpinner.setVisibility(View.VISIBLE);
                        } else {
                            _noCarte = true;
                            _codeCarteTextView.setVisibility(View.VISIBLE);
                            _cardSpinner.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnClick(R.id.id_code_carte)
    public void goToAddCard(){
        CardAddFragment frag = new CardAddFragment();
        frag._beforeIsSummary = true;
        _mainActivity.replaceMainFragment(frag,true);
    }

    @OnItemSelected(R.id.spn_card)
    public void onSelectedCard(View view, int position) {
        if (_commande != null) {
            _positionCard = position;
            _commande.idPaymentMethod = _listCard.get(position).id;
            CommonUtils.log("onSelectedCard _commande.idPaymentMethod " + _commande.idPaymentMethod);
        }
    }

    @OnClick(R.id.btn_confirmation)
    public void sendCommande() {
        _poolAuth = 0;
        if (_noCarte) {
            CommonUtils.alertCustom(getStringRes(R.string.msg_no_carte_save), null, 0, null, getActivity());
        } else {
            runPostCommande();

        }
    }

    private void runPostCommande() {
        _action = "post_commande";
        JsonObject json = new JsonObject();
        if (_commande.orderType.equals("fillUp")) {
            json.addProperty("idUser", _commande.idUser);
            json.addProperty("idSchedule", _commande.idSchedule);
            json.addProperty("idVehicle", _commande.idVehicle);
            json.addProperty("idPaymentMethod", _commande.idPaymentMethod);
            json.addProperty("orderType", _commande.orderType);
            json.addProperty("currency", _commande.currency);
            json.addProperty("placeNumber", _commande.placeNumber);
//            CommonUtils.log("json fillUp "+json.toString());
        } else {
            json.addProperty("idUser", _commande.idUser);
            json.addProperty("idSchedule", _commande.idSchedule);
            json.addProperty("idVehicle", _commande.idVehicle);
            json.addProperty("idPaymentMethod", _commande.idPaymentMethod);
            json.addProperty("orderType", _commande.orderType);
            json.addProperty("currency", _commande.currency);
            json.addProperty("amount", _commande.amount);
            json.addProperty("placeNumber", _commande.placeNumber);
//            CommonUtils.log("json priceamount "+json.toString());
        }

        postCommande(json);
    }

    private void postCommande(JsonObject json) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).postCommande(json, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) getToken();
                    else if (codeError == WSService.ERROR_CODE_ORDER_ALREADY) {
                        CommonUtils.alertCustom(getStringRes(R.string.commande_deja_passe), "", R.drawable.warning, null, _mainActivity);
                    } else {
                        CommonUtils.alertCustom(getStringRes(R.string.commande_non_envoye), "", R.drawable.warning, null, _mainActivity);

                    }
                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                try {
                    if (obj != null) {
                        String jsonStr = (String) obj;
                        CommonUtils.log(jsonStr);
                        _app.historySaveJson(jsonStr);
                        CommonUtils.alertCustom(getStringRes(R.string.commande_envoye), "", R.drawable.ok, null, _mainActivity);
                        goToDetails();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void goToDetails() {
        StoryDetailFragment fragment = new StoryDetailFragment();
        fragment._history = _app.history();
        _mainActivity.replaceMainFragment(fragment, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        _map = googleMap;

        LatLng paris = new LatLng(48.901236, 2.296785);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(paris, 17);
        _map.animateCamera(cameraUpdate);
        _map.setMyLocationEnabled(true);

        _map.addCircle(new CircleOptions().center(paris).radius(12)
                .strokeColor(ContextCompat.getColor(_mainActivity, R.color.fondBleu)));
        _map.addMarker(new MarkerOptions().position(paris));

    }

    /**
     * Success token
     *
     * @param obj
     */
    @Override
    public void onSuccess(Object obj) {
        _mainActivity.loadingShow(false);
        if (obj != null) {
            TokenModel token = (TokenModel) obj;
            CommonUtils.log(token.accessToken);
            if (_poolAuth == 0) {
                if (_action == "get_location")
                    getLocation();
                else if (_action == "post_commande") {
                    runPostCommande();
                } else
                    runCardList();
            }
            _poolAuth = 1;
        }

    }

    private void getLocation() {
        _action = "get_location";
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).getLocations(_app.user().id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401) getToken();

                }

            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    String jsonStr = (String) obj;
                    CommonUtils.log(jsonStr);
                }

            }
        });
    }

    public class CardAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return _listCard.size();
        }

        @Override
        public CardListModel getItem(int i) {
            return _listCard.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CardHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_card_item, null);
                holder = new CardHolder();
                holder.name = (TextView) convertView.findViewById(R.id.item_name);
                convertView.setTag(holder);
            } else {
                holder = (CardHolder) convertView.getTag();
            }
            holder.name.setText(_listCard.get(position).name);
            return convertView;
        }
    }

    public class CardHolder {
        public TextView name;
    }
}
