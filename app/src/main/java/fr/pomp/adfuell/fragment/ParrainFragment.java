package fr.pomp.adfuell.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;
import fr.pomp.adfuell.R;

import static fr.pomp.adfuell.utils.munix.MunixUtilities.context;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParrainFragment extends BaseFragment  {



    public ParrainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_parainage, container, false);
        _edButterKniff.view(this,v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_parainage));
        return v;
    }

    @OnClick(R.id.pay)
    public void pay(Button view){
       // initPayment();
    }
/*
    public void initPayment() {
        ConfigLoader configLoader = new ConfigLoader();
        JSONObject configuration = configLoader.loadJsonConfiguration();
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        try {
            checkoutRequest.setBrandColor(R.color.nespresso_grey);
            checkoutRequest.setBrandLogo(R.mipmap.nespresso_logo);
            checkoutRequest.setCheckoutAmount(10f);
            checkoutRequest.setCurrency(Currency.EUR);
            checkoutRequest.setToken(configuration.getString("userToken"));
            checkoutRequest.setTestBackend(true);

            Intent intent = new PaymentActivity.PaymentActivityBuilder(checkoutRequest).build(this, context);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (CheckoutRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkoutAuthorizedPayment(CheckoutResponse checkoutResponse) {
        CommonUtils.log("Response: ", checkoutResponse.toString());
    }

    @Override
    public void checkoutFailedWithError(String errorMessage) {

    }
*/
}
