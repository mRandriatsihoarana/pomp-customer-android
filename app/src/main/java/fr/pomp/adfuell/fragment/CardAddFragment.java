package fr.pomp.adfuell.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CvvEditText;
import com.braintreepayments.cardform.view.ExpirationDateEditText;
import com.braintreepayments.cardform.view.PostalCodeEditText;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.CardListModel;
import fr.pomp.adfuell.model.CardModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.stripe.StripeUtils;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by edena on 31/03/2017.
 */

public class CardAddFragment extends BaseFragment {
    @BindView(R.id.add_carte_code_postal)
    PostalCodeEditText _postEditText;
    @BindView(R.id.add_carte_cvv)
    CvvEditText _cvvEditText;
    @BindView(R.id.add_carte_date_expiration)
    ExpirationDateEditText _dateEditText;
    @BindView(R.id.add_carte_numero)
    CardEditText _numberEditText;

    @BindView(R.id.add_carte_titulaire)
    EditText _onwEditText;
    @BindView(R.id.create_card)
    Button _createCard;


    private int _poolAuth = 0;
    Card _card = null;
    Token _token = null;

    private boolean _isUpdate = false;
    public CardListModel _cardUpdate = null;
    public boolean _beforeIsSummary = false;

    public CardAddFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_carte_add, container, false);
        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getString(R.string.button_ajouter_carte));

        _postEditText = (PostalCodeEditText) v.findViewById(R.id.add_carte_code_postal);
        _cvvEditText = (CvvEditText) v.findViewById(R.id.add_carte_cvv);
        _dateEditText = (ExpirationDateEditText) v.findViewById(R.id.add_carte_date_expiration);
        _dateEditText.useDialogForExpirationDateEntry(_mainActivity,true);
        _numberEditText = (CardEditText) v.findViewById(R.id.add_carte_numero);

        //_numberEditText.setText("4242424242424242");
        return v;
    }

    @OnClick(R.id.add_carte_scan_votre_carte)
    public void goToScan(Button btn){
        _mainActivity.hideKeyBoard();
        Intent scanIntent = new Intent(_mainActivity, CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
       // scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);

        startActivityForResult(scanIntent, 1001);
    }

    @Override
    public void onResume() {
        if(_cardUpdate != null) {
            _isUpdate = true;
            _createCard.setText(getStringRes(R.string.text_modifier));
            _onwEditText.setText(_cardUpdate.name);
            _mainActivity.changeHeaderTitle(getStringRes(R.string.text_modifier_la_carte));
            if(_cardUpdate.order != 0){
                _createCard.setVisibility(View.GONE);
            }
        }
        super.onResume();
    }

    Card getFormDataCarte(){
        String post = _postEditText.getText().toString().trim();
        String own = _onwEditText.getText().toString().trim();
        String number = _numberEditText.getText().toString().trim();
        String cvv = _cvvEditText.getText().toString().trim();
        String date = _dateEditText.getText().toString().trim();
        int month = 0;
        try{
            month = Integer.valueOf(_dateEditText.getMonth());
        }catch (Exception e){

        }
        int year = 0;
        try{
            year = Integer.valueOf(_dateEditText.getYear());
        }catch (Exception e){

        }

        return saveCarte(number,
                cvv,
                month,
                year,
                own,post);
    }

    Card saveCarte(String number,
                       String cvc,
                       int month,
                       int year,
                       String name,
                       String post){

        if(name.trim().equalsIgnoreCase("")){
            CommonUtils.alert(getStringRes(R.string.text_carte_invalide_name_vide),null,0,null,getContext());
            return null;
        }

        if(post.trim().equalsIgnoreCase("")){
            CommonUtils.alert(getStringRes(R.string.text_carte_invalide_code_postal),null,0,null,getContext());
            return null;
        }

        Card card = StripeUtils.createCard(number,month,year,cvc,post,name);

        if(StripeUtils.isCardValid(card,getContext())){
            CardModel cardModel = new CardModel();
            cardModel.cpostal = post;
            cardModel.cvv = String.valueOf(cvc);
            cardModel.date = month + "/" + year;
            cardModel.number = number;
            cardModel.own = name;
            //CardService.getIntance(_app).saveCard(cardModel);
            return card;
        }else return null;

    }




    @OnClick(R.id.create_card)
    public void createCarte(Button btn){
        _mainActivity.hideKeyBoard();
        _card = getFormDataCarte();
        if(_card != null){
            saveCardWs();
        }

    }

    void saveCardWs(){
        _mainActivity.loadingShow(true);
        StripeUtils.getInstance(getContext()).getTokenCard(_card, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                _mainActivity.loadingShow(false);
            }

            @Override
            public void onSuccess(Token token) {
                _token = token;
                _poolAuth = 0;
                _mainActivity.loadingShow(false);
                createCardTokenWs();
            }
        });
    }

    IWSDelegate delegate =  new IWSDelegate() {
        @Override
        public void onFaillure(Object obj) {
            _mainActivity.loadingShow(false);
            if (obj != null) {
                int codeError = (int) obj;
                CommonUtils.log("codeError " + codeError);
                if (codeError == 401) {
                    getToken();
                }else{
                    CommonUtils.alertCustom( getStringRes(R.string.text_server_error), null, R.drawable.ok, null, getContext());
                }
            }
        }

        @Override
        public void onSuccess(Object obj) {
            _mainActivity.loadingShow(false);
            String jsonStr = null;
            try{
                jsonStr = (String) obj;
                CommonUtils.log(jsonStr);
                String mess = "";
                if(_isUpdate){
                    mess = getStringRes(R.string.text_modifier_success);
                }else{
                    mess = getStringRes(R.string.succes_add_carte);
                }
                if(_beforeIsSummary)
                    _mainActivity.replaceMainFragment(new SummaryFragment(),false);
                else
                    _mainActivity.replaceMainFragment(new CardListFragment(),true);

                CommonUtils.alertCustom(mess, null, R.drawable.ok, null, getContext());


            }catch (Exception e){

            }

        }
    };

    void createCardTokenWs(){
        _mainActivity.loadingShow(true);
        if(_isUpdate){
            WSService.getInstance(_app.getRetrofitWithAuth()).updateCard(_cardUpdate.id,
                    _token.getId(), _card.getName(),delegate);
        }else{
            WSService.getInstance(_app.getRetrofitWithAuth()).createCard(_app.user().id,
                    _token.getId(), _card.getName(),delegate);
        }

    }

    @Override
    public void onSuccess(Object obj) {
        _mainActivity.loadingShow(false);
        if (obj != null) {
            TokenModel token = (TokenModel) obj;
            CommonUtils.log(token.accessToken);
            if (_poolAuth == 0)
                createCardTokenWs();
            _poolAuth = 1;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            String cardNumber = "";
            String cardDate = "";
            String cardCvv = "";
            String cardOwn = "";
            String cardPostal = "";

            String resultDisplayStr = "";
            CreditCard scanResult = null;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                 scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                cardNumber = scanResult.cardNumber;
                cardDate = scanResult.expiryMonth + "/" +scanResult.expiryYear;
                //cardDate = Integer.toString(scanResult.expiryMonth) + Integer.toString(scanResult.expiryYear);
                cardCvv = scanResult.cvv != null ? scanResult.cvv: "";
                cardPostal = scanResult.postalCode;
                resultDisplayStr = "Card Number: " +cardNumber + "\n";

                //if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
               // }

                if (scanResult.cvv != null) {
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                //if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                //}
            }
            else {
                resultDisplayStr = "Scan annulé";
            }

            CommonUtils.log(resultDisplayStr);
            CardModel cardModel = new CardModel();
            cardModel.cpostal = cardPostal;
            cardModel.cvv = cardCvv;
            cardModel.date = cardDate;
            cardModel.number = cardNumber;
            cardModel.own = cardOwn;

            _postEditText.setText(cardPostal);
            _numberEditText.setText(cardNumber);
            _cvvEditText.setText(cardCvv);
            _postEditText.setText(cardPostal);
            try {
                String month = StringUtils.leftPad(String.valueOf(scanResult.expiryMonth), 2, "0");

                _dateEditText.setText(month + scanResult.expiryYear);
            }catch (Exception e){

            }


        }
    }
}
