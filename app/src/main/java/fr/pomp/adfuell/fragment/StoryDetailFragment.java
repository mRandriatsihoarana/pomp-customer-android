package fr.pomp.adfuell.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.HistoryModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoryDetailFragment extends BaseFragment {

    @BindView(R.id.story_adress)
    TextView _storyAdress;

    @BindView(R.id.story_date)
    TextView _storyDate;

    @BindView(R.id.story_fuel)
    TextView _storyFuell;

    @BindView(R.id.story_prix)
    TextView _storyPrix;

    @BindView(R.id.story_vehicule)
    TextView _storyVehicule;
    @BindView(R.id.story_parking)
    TextView _storyParking;
    @BindView(R.id.story_status)
    TextView _storyStatus;
    @BindView(R.id.btn_cancel)
    Button _cancelButton;
    @BindView(R.id.btn_update)
    Button _updateButton;
    @BindView(R.id.edt_place)
    EditText _placeEditText;


    public HistoryModel _history;

    public static final String FORMAT_DATE_SCHEDULE = "yyyy-MM-dd'T'HH:mm:ss";
    SimpleDateFormat _dateFormatString = null;
    SimpleDateFormat _dateFormatScedule = new SimpleDateFormat(FORMAT_DATE_SCHEDULE);

    private int _poolAuth = 0;
    public String _action = "";

    public StoryDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_story_detail, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.headerShowLeftBack();
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_historique));
        _dateFormatString = new SimpleDateFormat(getStringRes(R.string.date_format_history));//dd/MM/yyyy hh:mm
        if(_history != null)
            setData();
        return v;
    }

    void setData() {

        String adress = _history.schedule.location.name + ", " + _history.schedule.location.address;
        _storyAdress.setText(adress);
        String date = "";
        try {

            Date dateScedule = CommonUtils.getDateWithTimeZone(_history.schedule.date);
            date = _dateFormatString.format(dateScedule);

        } catch (Exception e) {
            e.printStackTrace();
        }
        _storyDate.setText(date);
        _storyFuell.setText(_history.vehicle.fuelType);

        String price = "";
        if (_history.orderType != null && _history.orderType.equals("fillUp")) {
            price = getStringRes(R.string.text_fillup_commande);
        } else {
            try {
                price = price + (_history.amount != null ? _history.amount + (_history.currency != null ? _history.currency : "€") : "");
                //price = price + (_history.currency != null ? _history.currency : "€");
                price = price + ("\t\t " + (_history.liters != null ? _history.liters + "L" : ""));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        price = price + (_history.status != null ? " ("+valueStatusString(_history.status)+")" : "");
        _storyPrix.setText(price);

        String vehicule = "";
        vehicule = vehicule + _history.vehicle.brand + " " + _history.vehicle.model + " - " + _history.vehicle.imat;
        _storyVehicule.setText(vehicule);
        _placeEditText.setText(_history.placeNumber != null ? _history.placeNumber : "");

        //_storyParking.setText(getStringRes(R.string.text_parking_number)+" : "+ (_history.placeNumber!=null?_history.placeNumber:""));
        //_storyStatus.setText(getStringRes(R.string.text_status)+" : " +_history.status);
        if (_history.status.equalsIgnoreCase("cancelled") || _history.status.equalsIgnoreCase("failed")) {
            _cancelButton.setVisibility(View.GONE);
            _updateButton.setVisibility(View.GONE);
            _placeEditText.setFocusableInTouchMode(false);
        } else if (_history.status.equalsIgnoreCase("toValidate") || _history.status.equalsIgnoreCase("incoming")) {

            try {
                Date dateScedule = _dateFormatScedule.parse(_history.schedule.date);
                Date now = new Date();
                if (dateScedule.equals(now)) {
                    CommonUtils.log("equals");
                    _cancelButton.setVisibility(View.VISIBLE);
                    _updateButton.setVisibility(View.VISIBLE);
                    _placeEditText.setFocusableInTouchMode(true);
                } else if (dateScedule.before(now)) {
                    CommonUtils.log("before");
                    _cancelButton.setVisibility(View.GONE);
                    _updateButton.setVisibility(View.GONE);
                    _placeEditText.setFocusableInTouchMode(false);
                } else if (dateScedule.after(now)) {
                    CommonUtils.log("after");
                    _cancelButton.setVisibility(View.VISIBLE);
                    _updateButton.setVisibility(View.VISIBLE);
                    _placeEditText.setFocusableInTouchMode(true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }   else {
            _cancelButton.setVisibility(View.GONE);
            _updateButton.setVisibility(View.GONE);
            _placeEditText.setFocusableInTouchMode(false);
        }

    }

    @OnClick(R.id.btn_cancel)
    public void cancelBtnClicked(View view) {
        _poolAuth = 0;
        Date dateScedule = CommonUtils.getDateWithTimeZone(_history.schedule.date);
//            Date dateScedule = _dateFormatScedule.parse("2017-05-31T17:50:00+02:00");

        Date now = new Date();
        long different = dateScedule.getTime() - now.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        if (elapsedDays > 0) {
            CommonUtils.alert(getStringRes(R.string.confirme_commande_cancel), "", 0, new CommonUtils.IDialog() {
                @Override
                public void no(Object obj) {

                }

                @Override
                public void yes(Object obj) {
                    runCancelledCommande();
                }
            }, _mainActivity);
        } else { // 5€ de charge
            CommonUtils.alert(getStringRes(R.string.text_charge_annulation_commade) + "\n" + getStringRes(R.string.confirme_commande_cancel), "", 0, new CommonUtils.IDialog() {
                @Override
                public void no(Object obj) {

                }

                @Override
                public void yes(Object obj) {
                    runCancelledCommande();
                }
            }, _mainActivity);

        }
    }

    private void runCancelledCommande() {
        _action = "cancel_commade";
        cancelCommande(_history.id);
    }

    private void cancelCommande(String id) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).cancelCommande(id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) {
                        getToken();
                    } else {
                        CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                    }

                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                CommonUtils.alertCustom(getStringRes(R.string.succes_commande_cancel), "", R.drawable.ok, null, _mainActivity);
                goToStory();
            }
        });
    }

    private void goToStory() {
        StoryFragment fragment = new StoryFragment();
        _mainActivity.replaceMainFragment(fragment, false);
    }

    @OnClick(R.id.btn_update)
    public void updateBtnClicked(View view) {
        _poolAuth = 0;
        if (!_placeEditText.getText().toString().isEmpty())
            runUpdateCommande();
        else
            CommonUtils.alert(getStringRes(R.string.empty_placenumber), "", 0, null, _mainActivity);
    }

    private void runUpdateCommande() {
        _action = "update_commade";
        updateCommande(_history.id, _placeEditText.getText().toString());
    }

    private void updateCommande(String id, String placenumber) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).updateCommande(id, placenumber, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) {
                        getToken();
                    } else {
                        CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                    }

                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                CommonUtils.alertCustom(getStringRes(R.string.succes_commande_update), "", R.drawable.ok, null, _mainActivity);
                goToStory();
            }
        });
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
                if (_action == "cancel_commade")
                    runCancelledCommande();
                else if (_action == "update_commade")
                    runUpdateCommande();

            }

            _poolAuth = 1;
        }

    }

}
