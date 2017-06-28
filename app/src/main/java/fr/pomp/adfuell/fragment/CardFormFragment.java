package fr.pomp.adfuell.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;

import butterknife.OnClick;
import fr.pomp.adfuell.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFormFragment extends BaseFragment implements OnCardFormSubmitListener,
        CardEditText.OnCardTypeChangedListener {

    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY };

    private SupportedCardTypesView mSupportedCardTypesView;

    protected CardForm mCardForm;
    CardForm cardForm;

    public CardFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_card_form, container, false);
        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_parainage));

        mSupportedCardTypesView = (SupportedCardTypesView) v.findViewById(R.id.supported_card_types);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);

        mCardForm = (CardForm) v.findViewById(R.id.card_form);
        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(true)
                .postalCodeRequired(true)
                //.mobileNumberRequired(true)
                //.mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                //.actionLabel(getString(R.string.purchase))
                .setup(_mainActivity);
        mCardForm.setOnCardFormSubmitListener(this);
        mCardForm.setOnCardTypeChangedListener(this);
        return v;
    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }

    @Override
    public void onCardFormSubmit() {
        if (mCardForm.isValid()) {
            Toast.makeText(_mainActivity, "Valide carte", Toast.LENGTH_SHORT).show();
        } else {
            mCardForm.validate();
            Toast.makeText(_mainActivity, "Invalides", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.scan_card)
    public void scan(Button btn){
        mCardForm.scanCard(_mainActivity);
    }


}
