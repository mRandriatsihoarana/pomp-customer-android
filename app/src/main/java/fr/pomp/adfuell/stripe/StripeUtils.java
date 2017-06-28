package fr.pomp.adfuell.stripe;

import android.content.Context;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;

import fr.pomp.adfuell.R;
import fr.pomp.adfuell.utils.comon.CommonUtils;

/**
 * Created by edena on 05/05/2017.
 */

public class StripeUtils {

    static StripeUtils _instance;
    Stripe _stripe;
    Context _context;

    private StripeUtils(Context context){
        _context = context;
        _stripe = new Stripe(_context);
        _stripe.setDefaultPublishableKey(Config.KEY_PUBLISH);
    }

    public static StripeUtils getInstance(Context context){
        if(_instance == null) _instance = new StripeUtils(context);
        return _instance;
    }

    public static String getStringRes(Context context,int idRes){
        return context.getResources().getString(idRes);
    }

    public static boolean isCardValid(Card card, Context context){
        if(!card.validateNumber()){
            CommonUtils.alert(getStringRes(context, R.string.text_carte_invalide_number),null,0,null,context);
            return false;
        }
        if(!card.validateCVC()){
            CommonUtils.alert(getStringRes(context, R.string.text_carte_invalide_cvc),null,0,null,context);
            return false;
        }
        if(!card.validateExpiryDate()){
            CommonUtils.alert(getStringRes(context, R.string.text_carte_invalide_date),null,0,null,context);
            return false;
        }
        return true;
    }

    public static Card createCard(
            String number
            , int month
            , int year
            , String cvc
            ,String zipAddress
            ,String nameCard){

        Card card =   new Card(number, month, year, cvc);
        if(zipAddress != null)
            card.setAddressZip(zipAddress);
        if(nameCard != null)
            card.setName(nameCard);

        return card;
    }

    public void getTokenCard(Card card, TokenCallback delegate){
        if(isCardValid(card,_context)){
            _stripe.createToken(card,delegate);
        }
    }

}
