package fr.pomp.adfuell.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.CarModel;
import fr.pomp.adfuell.model.CommandeModel;
import fr.pomp.adfuell.model.PricePerLiterModel;
import fr.pomp.adfuell.model.ScheduleModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.model.UserModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.utils.munix.Strings;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by Mandimbisoa on 13/04/2017.
 */

public class ConfirmationFragment extends BaseFragment {

    @BindView(R.id.localisation_parent)
    View _localisationParent;
    @BindView(R.id.horaire_parent)
    View _horaireParent;
    @BindView(R.id.vehicule_parent)
    View _vehiculeParent;
    @BindView(R.id.commande_parent)
    View _commandeParent;
    @BindView(R.id.header_localisation)
    View _headerLocalisation;
    @BindView(R.id.header_commande)
    View _headerCommand;
    @BindView(R.id.header_horaire)
    View _headerHoraire;
    @BindView(R.id.header_vehicule)
    View _headerVehicule;

    @BindView(R.id.container_localisation)
    View _containerLocalisation;
    @BindView(R.id.container_commande)
    View _containerCommand;
    @BindView(R.id.container_horaire)
    View _containerHoraire;
    @BindView(R.id.container_vehicule)
    View _containerVehicule;


    @BindView(R.id.edt_entreprise)
    public EditText _entrepriseEditText;
    @BindView(R.id.spn_parking)
    public Spinner _parkingSpinner;
    @BindView(R.id.edt_place)
    public EditText _placeEditText;
    @BindView(R.id.edt_note)
    public EditText _noteEditText;
    @BindView(R.id.edt_parking)
    public EditText _parkingEditText;
    @BindView(R.id.localisation_empty)
    public View _emptyLocalisation;
    @BindView(R.id.localisation_field)
    public View _fieldLocalisation;
    @BindView(R.id.resume_localisation)
    public TextView _resumeLocalisation;
    @BindView(R.id.edt_message)
    public TextView _messageLocalisation;
    @BindView(R.id.edt_message_1)
    public TextView _messageSansCompany;
    @BindView(R.id.edt_message_2)
    public TextView _messageAvecCompany;
/*
    @BindView(R.id.txv_horaire_1)
    public TextView _horaire1TextView;
    @BindView(R.id.txv_horaire_2)
    public TextView _horaire2TextView;
    @BindView(R.id.txv_horaire_3)
    public TextView _horaire3TextView;*/

    @BindView(R.id.spn_imat)
    public Spinner _imatSpinner;
    @BindView(R.id.edt_marque)
    public TextView _marqueTextView;
    @BindView(R.id.edt_type)
    public TextView _typeTextView;
    @BindView(R.id.edt_couleur)
    public TextView _couleurTextView;
    @BindView(R.id.vehicule_empty)
    public View _emptyVehiculeView;
    @BindView(R.id.vehicule_field)
    public View _fieldVehiculeView;
    @BindView(R.id.resume_vehicule)
    public TextView _resumeVehiculeTextView;
    @BindView(R.id.resume_horaire)
    public TextView _resumeHoraireTextView;

    @BindView(R.id.horaire_list_contener)
    LinearLayout _contenerList;
    @BindView(R.id.ui_empty_horaire)
    LinearLayout _emptyView;

    @BindView(R.id.txt_type_carburant)
    TextView _typeCarburantTextview;
    @BindView(R.id.value_price)
    TextView _valuePriceTextview;
    @BindView(R.id.seekbar_price)
    SeekBar _priceSeekBar;
    /*@BindView(R.id.img_full)
    ImageView _fullImageView;*/
    @BindView(R.id.resume_commande)
    TextView _resumeCommandeTextView;
    @BindView(R.id.price_liter)
    TextView _priceLiterTextView;
    @BindView(R.id.view_full)
    LinearLayout _fullView;
    @BindView(R.id.view_slider)
    RelativeLayout _sliderView;

    @BindView(R.id.layout_commande)
    View _commandeView;
    @BindView(R.id.layout_schedule_empty)
    View _scheduleEmptyView;
    @BindView(R.id.layout_schedule_empty_company)
    View _scheduleEmptyCompanyView;

    @BindView(R.id.layout_full)
    RelativeLayout _fullRelativeLayout;
    @BindView(R.id.text_full)
    TextView _fullTextView;

    private LinearLayout _layoutHoraire = null;
    private String _message = "";

    CarAdapter _imatAdapter;
    ScheduleAdapter _parkingAdapter;

    private ArrayList<CarModel> _listCar = new ArrayList<CarModel>();
    private ArrayList<ScheduleModel> _listSchedule = new ArrayList<ScheduleModel>();
    private CarModel _selectedCar = new CarModel();
    private CommandeModel _commande = new CommandeModel();
    public String _orderType = "";
    public String _amount = "";
    LayoutInflater _layoutInflater;
    private ScheduleModel _selectedSchedule = null;

    private int _poolAuth = 0;
    public String _action = "";
    private boolean _enableSeekBar = false;
    private boolean _localisationValidate = false;
    private boolean _horaireValidate = false;
    private boolean _vehiculeValidate = false;
    private boolean _commandeValidate = false;

    private boolean _scheduleIsFinished = false;
    private boolean _vehiculeIsFinished = false;

    private Integer _positionHoraire = null;
    private Integer _positionVehicule = null;

    SimpleDateFormat _dateListFormatString = null;
    SimpleDateFormat _dateResumeFormatString = null;


    public ConfirmationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirmation, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(true);
        _mainActivity.lockedDrawer(false);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.headerShowLeftBack();
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_commander));

        _layoutInflater = (LayoutInflater) _mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _dateListFormatString = new SimpleDateFormat(getStringRes(R.string.date_format));
        _dateResumeFormatString = new SimpleDateFormat(getStringRes(R.string.date_format_sans_heure));

        _imatAdapter = new CarAdapter();
        _imatSpinner.setAdapter(_imatAdapter);

//        _parkingAdapter = new ScheduleAdapter();
//        _parkingSpinner.setAdapter(_parkingAdapter);
        _resumeCommandeTextView.setVisibility(View.GONE);

        _priceSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        if (!_app._isOrderConfirmedView) {
            getData();
            getListCar();
        }
        return v;
    }

    @Override
    public void onResume() {
        if (_app._isOrderConfirmedView) {
            setDataPrevious();
            _app._isOrderConfirmedView = false;
        }
        super.onResume();
    }

    private void setDataPrevious() {
        //set value schedule
        _listSchedule = _app.scheduleList();
        _positionHoraire = _app.positionHoraire();
        updateListSchedule();
        updateScheduleByParking(_positionHoraire);
        //set value vehicule
        _listCar = _app.carList();
        _positionVehicule = _app.positionVehicule();
        CommandeModel commande = _app.commandeObject();
        _orderType = commande.orderType;
        _placeEditText.setText(_commande.placeNumber);
        if (commande.orderType.equals("fillUp")) {

            _fullRelativeLayout.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_circle_full_2));
            _fullTextView.setTextColor(ContextCompat.getColor(_mainActivity, R.color.white));
            _valuePriceTextview.setText(getStringRes(R.string.text_full_resume));
            _commandeValidate = true;
            _enableSeekBar = true;
            String resume = _typeCarburantTextview.getText().toString() + "\n" + getStringRes(R.string.text_full_resume);
            _resumeCommandeTextView.setText(resume);
            _resumeCommandeTextView.setVisibility(View.VISIBLE);
        } else {

            _commandeValidate = true;
            _enableSeekBar = true;
            _amount = commande.amount;
            int i = (Integer.valueOf(_amount) - 5) / 5;
            _priceSeekBar.setProgress(i);
            String resume = _typeCarburantTextview.getText().toString() + "\n" + _valuePriceTextview.getText().toString();
            _resumeCommandeTextView.setText(resume);
            _resumeCommandeTextView.setVisibility(View.VISIBLE);
        }
        setDataVehicule();
        _imatSpinner.setSelection(_positionVehicule);
        updateDataVehicule();
        //set value commande

        updateDataCommande();


        setHoraire();
        setVehicule();
        setCommande();
    }

    /**
     * Recuperer les données
     */
    void getData() {
        _poolAuth = 0;
        runSchedules();
    }

    public void runSchedules() {
        _action = "get_schedule";
        _scheduleIsFinished = false;
        getSchedules(_app.user().id);
    }

    private void getSchedules(String id) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).getSchedules(id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                if (_vehiculeIsFinished)
                    _mainActivity.loadingShow(false);

                _scheduleIsFinished = true;
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) {
                        getToken();
                    } else {
//                        CommonUtils.alertCustom("ERROR schedules!!", null, R.drawable.warning, null, getContext());
                        _listSchedule = null;
                        updateListSchedule();
                        CommonUtils.alert(getStringRes(R.string.faillure_request), "", 0, null, _mainActivity);
                    }
                } else
                    CommonUtils.alert(getStringRes(R.string.faillure_request), "", 0, null, _mainActivity);

            }

            @Override
            public void onSuccess(Object obj) {
                if (_vehiculeIsFinished)
                    _mainActivity.loadingShow(false);

                if (obj != null) {

                    String jsonStr = (String) obj;
                    _app.schedulesListSaveJson(jsonStr);
                    if (_app.scheduleList() != null) {
                        CommonUtils.log(jsonStr);
                        _listSchedule = _app.scheduleList();
                        Collections.sort(_listSchedule, new Comparator<ScheduleModel>() {
                            @Override
                            public int compare(ScheduleModel scheduleModel, ScheduleModel t1) {
                                return scheduleModel.date.compareToIgnoreCase(t1.date);
                            }
                        });
                        /*if (_listSchedule != null) {
                            if (_listSchedule.size() > 0)
                                completeLocalisationField(_listSchedule.get(0));
                        }*/
                    }
                    updateListSchedule();
                }
                _scheduleIsFinished = true;
            }
        });
    }

    public void getListCar() {
        _poolAuth = 0;
        runCarList();
    }

    private void runCarList() {
        _action = "get_car";
        _vehiculeIsFinished = false;
        getCar(_app.userObject().id);
    }

    private void getCar(String id) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).getCarList(id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                if (_scheduleIsFinished)
                    _mainActivity.loadingShow(false);

                _vehiculeIsFinished = true;
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) getToken();
                    else {
//                        CommonUtils.alertCustom("ERROR vehicule!!", null, R.drawable.warning, null, getContext());
                        _listCar = null;
                        setDataVehicule();
                        CommonUtils.alert(getStringRes(R.string.faillure_request), "", 0, null, _mainActivity);
                    }
                } else
                    CommonUtils.alert(getStringRes(R.string.faillure_request), "", 0, null, _mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                if (_scheduleIsFinished)
                    _mainActivity.loadingShow(false);
                if (obj != null) {

                    String jsonStr = (String) obj;
                    _app.carListSaveJson(jsonStr);
                    CommonUtils.log(jsonStr);
                    CommonUtils.log("size car list= " + _app.carList().size());
                    UserModel user = _app.userObject();
                    user.vehicles = _app.carList().size();
                    _app.userSaveObject(user);
                    _listCar = _app.carList();


                    setDataVehicule();

                    int size = _listCar != null ? _listCar.size() : 0;
                    if (size > 0) {
                        _positionVehicule = 0;
                        _imatSpinner.setSelection(0);
                        updateDataVehicule();
                        setVehicule();
                        updateDataCommande();
                    }
                    full();
                    _vehiculeIsFinished = true;
                    openCommandeView();
                }
            }
        });
    }

    private void completeLocalisationField(ScheduleModel schedule) {
        _emptyLocalisation.setVisibility(View.GONE);
        _fieldLocalisation.setVisibility(View.VISIBLE);
        updateLocalisation(schedule);
    }

    public void setDataVehicule() {
        if (_listCar != null && _listCar.size() > 0) {
            _emptyVehiculeView.setVisibility(View.GONE);
            _fieldVehiculeView.setVisibility(View.VISIBLE);
            _imatAdapter.notifyDataSetChanged();
        } else {
            _emptyVehiculeView.setVisibility(View.VISIBLE);
            _fieldVehiculeView.setVisibility(View.GONE);
            _resumeVehiculeTextView.setVisibility(View.GONE);
            _imatAdapter.notifyDataSetChanged();
        }
    }

    public void extractImat() {
        _imatAdapter.notifyDataSetChanged();
    }

    public void resumeVehicule(int position) {
        _selectedCar = _listCar.get(position);
        _app.carSaveJson(_selectedCar.toString());
        _app.carSaveObject(_selectedCar);
        _marqueTextView.setText(_selectedCar.brand);
        _typeTextView.setText(_selectedCar.model);
        _couleurTextView.setText(_selectedCar.color);
        if (_selectedCar != null) {
            _resumeVehiculeTextView.setVisibility(View.VISIBLE);
            String resume = _selectedCar.brand + " " + _selectedCar.model + "\n" + _selectedCar.imat;
            _resumeVehiculeTextView.setText(resume);

            _typeCarburantTextview.setText(Strings.ucfirst(_selectedCar.fuelType));

            if (_selectedCar.fuelType.equalsIgnoreCase("diesel"))
                _priceLiterTextView.setText(getStringRes(R.string.text_price_liter) + " " + _app.pricePerLiterObject().pricePerLiterDiesel + " €");
            else if (_selectedCar.fuelType.equalsIgnoreCase("unleaded95"))
                _priceLiterTextView.setText(getStringRes(R.string.text_price_liter) + " " + _app.pricePerLiterObject().pricePerLiterUnleaded95 + " €");
            else if (_selectedCar.fuelType.equalsIgnoreCase("unleaded98"))
                _priceLiterTextView.setText(getStringRes(R.string.text_price_liter) + " " + _app.pricePerLiterObject().pricePerLiterUnleaded98 + " €");

            _enableSeekBar = true;
        } else {
            _resumeVehiculeTextView.setVisibility(View.GONE);
            _typeCarburantTextview.setText("");
            _enableSeekBar = false;
        }
    }

    public void updateListSchedule() {
        _contenerList.setVisibility(View.VISIBLE);
        _contenerList.removeAllViews();

        LinearLayout view = null;
        int listSize = _listSchedule != null ? _listSchedule.size() : 0;
        if (listSize > 0) {
            _emptyView.setVisibility(View.GONE);
            _emptyLocalisation.setVisibility(View.GONE);
            _commandeView.setVisibility(View.VISIBLE);
            _scheduleEmptyView.setVisibility(View.GONE);
            _scheduleEmptyCompanyView.setVisibility(View.GONE);

//            _parkingAdapter.notifyDataSetChanged();

            for (int index = 0; index < listSize; index++) {

                final ScheduleModel schedule = _listSchedule.get(index);
                final Integer position = index;
                view = (LinearLayout) _layoutInflater.inflate(R.layout.view_horaire_list_item, null);
                TextView dateTime = (TextView) view.findViewById(R.id.txv_horaire);
                LinearLayout layoutDateTime = (LinearLayout)view.findViewById(R.id.ui_container_date_time);

                String date = "";
                try {
                    Date dateScedule = CommonUtils.getDateWithTimeZone(schedule.date);
                    date = _dateListFormatString.format(dateScedule);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dateTime.setText(date);
                layoutDateTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        _parkingSpinner.setSelection(position);
                        _positionHoraire = position;
//                        updateDataLocalisation(position);
//                        _horaireValidate = true;
                        if (_layoutHoraire != null) {
                            _layoutHoraire.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_border_horaire));
                            _layoutHoraire = (LinearLayout) view;
                            _layoutHoraire.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_border_horaire_blue));
                        } else {
                            _layoutHoraire = (LinearLayout) view;
                            _layoutHoraire.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_border_horaire_blue));
                        }

                    }
                });

                _contenerList.addView(view);
            }

            _positionHoraire = _app.positionHoraire() != null ? _app.positionHoraire() : 0;
            updateScheduleByParking(_positionHoraire);
            setHoraire();
            openCommandeView();
        } else {
            _emptyView.setVisibility(View.GONE);
            _emptyLocalisation.setVisibility(View.GONE);
            _fieldLocalisation.setVisibility(View.GONE);
            _contenerList.setVisibility(View.GONE);

            _commandeView.setVisibility(View.GONE);
            if (_app.user() != null && _app.user().company != null && !_app.user().company.isEmpty()) {
                _scheduleEmptyView.setVisibility(View.GONE);
                _scheduleEmptyCompanyView.setVisibility(View.VISIBLE);
            } else {
                _scheduleEmptyView.setVisibility(View.VISIBLE);
                _scheduleEmptyCompanyView.setVisibility(View.GONE);
            }

        }

    }

    private void updateScheduleByParking(int position) {
        LinearLayout layout = (LinearLayout) _contenerList.getChildAt(position);
        //TextView dateTime = (TextView) layout.findViewById(R.id.txv_horaire);
        LinearLayout view = (LinearLayout) layout.findViewById(R.id.ui_container_date_time);
        if (_layoutHoraire != null) {
            _layoutHoraire.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_border_horaire));
            _layoutHoraire = view;
            _layoutHoraire.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_border_horaire_blue));
        } else {
            _layoutHoraire = view;
            _layoutHoraire.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_border_horaire_blue));
        }
    }

    private void updateLocalisation(ScheduleModel schedule) {
        _entrepriseEditText.setText(schedule.company.name);
        _parkingEditText.setText(schedule.location.name);
    }

    private void updateDataLocalisation(int position) {
        ScheduleModel schedule = _listSchedule.get(position);
//        _selectedSchedule = _listSchedule.get(position);
//        _app.scheduleSaveObject(_selectedSchedule);
//        _app.pricePerLiterSaveObject(_selectedSchedule.company.pricePerLiter);
        updateLocalisation(schedule);
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
                if (_action == "get_schedule")
                    runSchedules();
                else if (_action == "get_car")
                    runCarList();
                else
                    runSendMessage();
            }

            _poolAuth = 1;
        }

    }

    @OnItemSelected(R.id.spn_imat)
    public void changeDataVehicule(View view, int position) {
        if (view != null) {
            view.findViewById(R.id.item_marque).setVisibility(View.GONE);
            CommonUtils.log("view not null position " + position);
        } else {
            CommonUtils.log("view null position " + position);
        }
//        resumeVehicule(position);
//        _vehiculeValidate = true;
        _positionVehicule = position;
        updateDataVehicule();
    }

    @OnItemSelected(R.id.spn_parking)
    public void changeDataParking(View view, int position) {
        /*if (view != null) {
            view.findViewById(R.id.item_imat).setVisibility(View.GONE);
        }
        updateDataLocalisation(position);
        _localisationValidate = true;
        updateScheduleByParking(position);*/

    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (_enableSeekBar) {
                _valuePriceTextview.setText((i * 5 + 5) + " €");

                _fullRelativeLayout.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_circle_full_3));
                _fullTextView.setTextColor(ContextCompat.getColor(_mainActivity, R.color.gris));

                _resumeCommandeTextView.setVisibility(View.VISIBLE);
                String resume = _typeCarburantTextview.getText().toString() + "\n" + _valuePriceTextview.getText().toString();
                _resumeCommandeTextView.setText(resume);
                _commandeValidate = true;
                _orderType = "priceAmount";
                _amount = "" + (i * 5 + 5);

                /*if(i == 0)
                    seekBar.setProgress(10);*/

                /*if (_positionVehicule != null) {

                } else {
                    seekBar.setProgress(0);
                    _valuePriceTextview.setText("10 €");
                    _commandeValidate = false;
                    _resumeCommandeTextView.setVisibility(View.GONE);
                }*/

            } else {
                seekBar.setProgress(0);
                _valuePriceTextview.setText("5 €");
                _resumeCommandeTextView.setVisibility(View.GONE);
                _commandeValidate = false;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @OnClick(R.id.layout_full)
    public void full(){
        //if (_positionVehicule != null) {
            _fullRelativeLayout.setBackground(ContextCompat.getDrawable(_mainActivity, R.drawable.shape_circle_full_2));
            _fullTextView.setTextColor(ContextCompat.getColor(_mainActivity, R.color.white));
            _enableSeekBar = true;
            _valuePriceTextview.setText(getStringRes(R.string.text_full_resume));
            String resume = _positionVehicule != null ?_typeCarburantTextview.getText().toString() : " " + "\n" + getStringRes(R.string.text_full_resume);
            _resumeCommandeTextView.setText(resume);
            _commandeValidate = true;
            _resumeCommandeTextView.setVisibility(View.VISIBLE);
            _orderType = "fillUp";
            _amount = "";
        /*} else {
            _resumeCommandeTextView.setVisibility(View.GONE);
            _commandeValidate = false;
        }*/
    }

    @OnClick(R.id.btn_fill_up)
    public void fullCarburant(View view) {
        if (_positionVehicule != null) {
            _fullView.setVisibility(View.VISIBLE);
            _sliderView.setVisibility(View.GONE);

            _enableSeekBar = true;
            String resume = _typeCarburantTextview.getText().toString() + "\n" + getStringRes(R.string.text_full_resume);
            _resumeCommandeTextView.setText(resume);
            _commandeValidate = true;
            _resumeCommandeTextView.setVisibility(View.VISIBLE);
            _orderType = "fillUp";
            _amount = "";
        } else {
            _fullView.setVisibility(View.GONE);
            _sliderView.setVisibility(View.GONE);
            _resumeCommandeTextView.setVisibility(View.GONE);
            _commandeValidate = false;
        }
    }

    @OnClick(R.id.btn_price_amount)
    public void priceAmountCarburant(View view) {
        if (_positionVehicule != null) {
            _fullView.setVisibility(View.GONE);
            _sliderView.setVisibility(View.VISIBLE);

            _enableSeekBar = true;
            _priceSeekBar.setProgress(0);
            /*String resume = _typeCarburantTextview.getText().toString() + "\n" + getStringRes(R.string.text_full_resume);
            _resumeCommandeTextView.setText(resume);
            _commandeValidate = true;
            _resumeCommandeTextView.setVisibility(View.VISIBLE);*/
            _orderType = "priceAmount";
        } else {
            _fullView.setVisibility(View.GONE);
            _sliderView.setVisibility(View.GONE);
            _resumeCommandeTextView.setVisibility(View.GONE);
            _commandeValidate = false;
        }
    }

    @OnClick(R.id.header_localisation)
    public void headerLocation(View view) {

        YoYo.with(Techniques.SlideInDown).duration(700).playOn(_containerLocalisation);
        _headerLocalisation.setVisibility(View.GONE);
        _headerCommand.setVisibility(View.VISIBLE);
        _headerHoraire.setVisibility(View.VISIBLE);
        _headerVehicule.setVisibility(View.VISIBLE);


        _containerLocalisation.setVisibility(View.VISIBLE);
        _containerCommand.setVisibility(View.GONE);
        _containerHoraire.setVisibility(View.GONE);
        _containerVehicule.setVisibility(View.GONE);

        setHoraire();
        setVehicule();
        setCommande();
    }

    @OnClick(R.id.header_commande)
    public void headerCommande() {
        YoYo.with(Techniques.SlideInDown).duration(700).playOn(_containerCommand);
        _headerLocalisation.setVisibility(View.VISIBLE);
        _headerCommand.setVisibility(View.GONE);
        _headerHoraire.setVisibility(View.VISIBLE);
        _headerVehicule.setVisibility(View.VISIBLE);

        _containerLocalisation.setVisibility(View.GONE);
        _containerCommand.setVisibility(View.VISIBLE);
        _containerHoraire.setVisibility(View.GONE);
        _containerVehicule.setVisibility(View.GONE);

        updateDataCommande();
        setHoraire();
        setVehicule();
    }

    @OnClick(R.id.header_horaire)
    public void headerHoraire(View view) {
        YoYo.with(Techniques.SlideInDown).duration(700).playOn(_containerHoraire);
        _headerLocalisation.setVisibility(View.VISIBLE);
        _headerCommand.setVisibility(View.VISIBLE);
        _headerHoraire.setVisibility(View.GONE);
        _headerVehicule.setVisibility(View.VISIBLE);

        _containerLocalisation.setVisibility(View.GONE);
        _containerCommand.setVisibility(View.GONE);
        _containerHoraire.setVisibility(View.VISIBLE);
        _containerVehicule.setVisibility(View.GONE);

        setVehicule();
        setCommande();
    }

    @OnClick(R.id.header_vehicule)
    public void headerVehicule(View view) {
        YoYo.with(Techniques.SlideInDown).duration(700).playOn(_containerVehicule);
        _headerLocalisation.setVisibility(View.VISIBLE);
        _headerCommand.setVisibility(View.VISIBLE);
        _headerHoraire.setVisibility(View.VISIBLE);
        _headerVehicule.setVisibility(View.GONE);

        _containerLocalisation.setVisibility(View.GONE);
        _containerCommand.setVisibility(View.GONE);
        _containerHoraire.setVisibility(View.GONE);
        _containerVehicule.setVisibility(View.VISIBLE);

        updateDataVehicule();
        setHoraire();
        setCommande();
    }

    @OnClick({R.id.up_localisation_container, R.id.up_header_localisation})
    public void containerLocalisation(View view) {
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(_headerLocalisation);
        _headerLocalisation.setVisibility(View.VISIBLE);
        _containerLocalisation.setVisibility(View.GONE);

        setHoraire();
    }

    @OnClick({R.id.up_horaire_container, R.id.up_header_horaire})
    public void containerHoraire(View view) {
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(_headerHoraire);
        _headerHoraire.setVisibility(View.VISIBLE);
        _containerHoraire.setVisibility(View.GONE);

        setHoraire();
    }

    @OnClick({R.id.up_vehicule_container, R.id.up_header_vehicule})
    public void containerVehicule(View view) {
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(_headerVehicule);
        _headerVehicule.setVisibility(View.VISIBLE);
        _containerVehicule.setVisibility(View.GONE);

        setVehicule();
    }

    @OnClick(R.id.confirmation_button)
    public void confirmation(View view) {
        _mainActivity.hideKeyBoard();
        setHoraire();
        setVehicule();
        setCommande();
        if (_listSchedule != null && _listSchedule.size() > 0) {
            if (_positionHoraire != null && _positionVehicule != null && (_enableSeekBar && _commandeValidate)) {
                _app.scheduleSaveObject(_listSchedule.get(_positionHoraire));
                _app.carSaveObject(_listCar.get(_positionVehicule));
                _app.positionHoraireSaveInteger(_positionHoraire);
                _app.positionVehiculeSaveInteger(_positionVehicule);
                _commande.placeNumber = _placeEditText.getText().toString();
                _commande.orderType = _orderType;
                _commande.amount = _amount;
                _commande.currency = "EUR";
                _commande.idUser = _app.userObject().id;
                _commande.idSchedule = _listSchedule.get(_positionHoraire).id;
                _commande.idVehicle = _listCar.get(_positionVehicule).id;
                _app.commandeSaveObject(_commande);
                goToSummary();
            } else
                CommonUtils.alert(getStringRes(R.string.text_info_incomplet), "", 0, null, _mainActivity);
        } else
            CommonUtils.alert(getStringRes(R.string.empty_schedules), "", 0, null, _mainActivity);

//        goToSummary();
    }

    private void goToSummary() {
        SummaryFragment fragment = new SummaryFragment();
        _mainActivity.replaceMainFragment(fragment, true);
    }


    @OnClick({R.id.up_commande_container, R.id.up_header_commande})
    public void containerCommande(View view) {
        YoYo.with(Techniques.SlideInUp).duration(500).playOn(_headerCommand);
        _headerCommand.setVisibility(View.VISIBLE);
        _containerCommand.setVisibility(View.GONE);

        setCommande();
    }

    @OnClick(R.id.btn_message_1)
    public void sendMessageSansCompany(View view) {
        _mainActivity.hideKeyBoard();
        if (!_messageSansCompany.getText().toString().isEmpty()) {
            _poolAuth = 0;
            _message = _messageSansCompany.getText().toString();
            //CommonUtils.log("izy "+_messageSansCompany.getText().toString());
            runSendMessage();
        } else {
            CommonUtils.alert(getStringRes(R.string.empty_message_field), "", 0, null, _mainActivity);
        }
        _mainActivity.hideKeyBoard();
    }

    @OnClick(R.id.btn_message_2)
    public void sendMessageAvecCompany(View view) {
        _mainActivity.hideKeyBoard();
        if (!_messageAvecCompany.getText().toString().isEmpty()) {
            _poolAuth = 0;
            _message = _messageAvecCompany.getText().toString();
            //CommonUtils.log("izy "+_messageAvecCompany.getText().toString());
            runSendMessage();
        } else {
            CommonUtils.alert(getStringRes(R.string.empty_message_field), "", 0, null, _mainActivity);
        }
        _mainActivity.hideKeyBoard();
    }

    private void runSendMessage() {
        _action = "send_message";
        postMessage(_app.userObject().id, _message);
    }

    private void postMessage(String id, String message) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).postMessageLocation(id, message, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) {
                        getToken();
                    } else {
                        CommonUtils.alert(getResources().getString(R.string.fail_message), "", 0, null, _mainActivity);
                    }

                } else
                    CommonUtils.alert(getStringRes(R.string.faillure_request), "", 0, null, _mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                CommonUtils.alert(getResources().getString(R.string.succes_message), "", 0, null, _mainActivity);
            }
        });
    }

    public void setLocalisation() {
        if (_positionHoraire != null && _listSchedule != null) {
            updateDataLocalisation(_positionHoraire);
            _resumeLocalisation.setVisibility(View.VISIBLE);
            _resumeLocalisation.setText(_listSchedule.get(_positionHoraire).company.name + "\n" + _listSchedule.get(_positionHoraire).location.name);
        } else {
            _resumeLocalisation.setVisibility(View.GONE);
            _entrepriseEditText.setText("");
            _parkingEditText.setText("");
        }
    }

    public void setHoraire() {
        if (_positionHoraire != null && _listSchedule != null) {
            setLocalisation();
            _resumeHoraireTextView.setVisibility(View.VISIBLE);

            String date = "";
            try {
                Date dateScedule = CommonUtils.getDateWithTimeZone(_listSchedule.get(_positionHoraire).date);
                date = _dateResumeFormatString.format(dateScedule);
            } catch (Exception e) {
                e.printStackTrace();
            }

            _resumeHoraireTextView.setText(date);
        } else {
            _resumeHoraireTextView.setVisibility(View.GONE);
        }
    }

    public void setVehicule() {
        if (_positionVehicule != null && _listCar != null) {
            _resumeVehiculeTextView.setVisibility(View.VISIBLE);
            String resume = _listCar.get(_positionVehicule).brand + " " + _listCar.get(_positionVehicule).model + "\n" + _listCar.get(_positionVehicule).imat;
            _resumeVehiculeTextView.setText(resume);
        } else {
            _resumeVehiculeTextView.setVisibility(View.GONE);
        }
    }

    public void setCommande() {
        if (_enableSeekBar && _commandeValidate) {
            _resumeCommandeTextView.setVisibility(View.VISIBLE);
        } else {
            _resumeCommandeTextView.setVisibility(View.GONE);
        }
    }

    public void updateDataVehicule() {
        if (_positionVehicule != null && _listCar != null) {
            _marqueTextView.setText(_listCar.get(_positionVehicule).brand);
            _typeTextView.setText(_listCar.get(_positionVehicule).model);
            _couleurTextView.setText(_listCar.get(_positionVehicule).color);
            _imatSpinner.setSelection(_positionVehicule);

            _typeCarburantTextview.setText(Strings.ucfirst(_listCar.get(_positionVehicule).fuelType));
            if (_resumeCommandeTextView.getVisibility() == View.VISIBLE) {
                if (_orderType.equals("fillUp")) {
                    String resume = _typeCarburantTextview.getText().toString() + "\n" + getStringRes(R.string.text_full_resume);
                    _resumeCommandeTextView.setText(resume);
                } else {
                    String resume = _typeCarburantTextview.getText().toString() + "\n" + _valuePriceTextview.getText().toString();
                    _resumeCommandeTextView.setText(resume);
                }

            }
        }
    }

    public void updateDataCommande() {
        if (_positionVehicule != null && _listCar != null) {
            _typeCarburantTextview.setText(Strings.ucfirst(_listCar.get(_positionVehicule).fuelType));
            _enableSeekBar = true;
        } else {
            _typeCarburantTextview.setText("");
            _enableSeekBar = false;
        }

        if (_positionHoraire != null && _positionVehicule != null && _listSchedule != null && _listCar != null) {
            PricePerLiterModel pricePerLiterModel = _listSchedule.get(_positionHoraire).company.pricePerLiter;
            if (_listCar.get(_positionVehicule).fuelType.equalsIgnoreCase("diesel"))
                _priceLiterTextView.setText(getStringRes(R.string.text_price_liter) + " " + pricePerLiterModel.pricePerLiterDiesel + " €");
            else if (_listCar.get(_positionVehicule).fuelType.equalsIgnoreCase("unleaded95"))
                _priceLiterTextView.setText(getStringRes(R.string.text_price_liter) + " " + pricePerLiterModel.pricePerLiterUnleaded95 + " €");
            else if (_listCar.get(_positionVehicule).fuelType.equalsIgnoreCase("unleaded98"))
                _priceLiterTextView.setText(getStringRes(R.string.text_price_liter) + " " + pricePerLiterModel.pricePerLiterUnleaded98 + " €");
        } else {
            _priceLiterTextView.setText("");
        }
    }

    public void openCommandeView() {
        int sizeSchedule = _listSchedule != null ? _listSchedule.size() : 0;
        int sizeCar = _listCar != null ? _listCar.size() : 0;
        if (_positionHoraire != null && sizeSchedule > 0 && _positionVehicule != null && sizeCar > 0) {
            headerCommande();
        }
    }

    public class CarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return _listCar.size();
        }

        @Override
        public Object getItem(int i) {
            return _listCar.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CarViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_item, null);
                holder = new CarViewHolder();
                holder.marque = (TextView) convertView.findViewById(R.id.item_marque);
                holder.imat = (TextView) convertView.findViewById(R.id.item_imat);
                convertView.setTag(holder);
            } else {
                holder = (CarViewHolder) convertView.getTag();
            }
            holder.marque.setText(_listCar.get(position).brand);
            holder.imat.setText(_listCar.get(position).imat);
            return convertView;
        }
    }

    public class CarViewHolder {
        public TextView marque;
        public TextView imat;
    }

    public class ScheduleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return _listSchedule.size();
        }

        @Override
        public ScheduleModel getItem(int i) {
            return _listSchedule.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ScheduleViewHolder holder = null;
            ScheduleModel item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_item, null);
                holder = new ScheduleViewHolder();
                holder.parking = (TextView) convertView.findViewById(R.id.item_marque);
                holder.address = (TextView) convertView.findViewById(R.id.item_imat);
                convertView.setTag(holder);
            } else {
                holder = (ScheduleViewHolder) convertView.getTag();
            }
            holder.parking.setText(item.location.name);
            holder.address.setText(item.location.address);
            return convertView;
        }
    }

    public class ScheduleViewHolder {
        public TextView parking;
        public TextView address;
    }

}
