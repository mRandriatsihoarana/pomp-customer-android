package fr.pomp.adfuell.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.utils.munix.Strings;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;

/**
 * Created by edena on 28/03/2017.
 */

public class AuthentFragment extends BaseFragment {
    @BindView(R.id.login_button)
    Button _connection;
    @BindView(R.id.login_email)
    AutoCompleteTextView _emailEditText;
    @BindView(R.id.login_password)
    EditText _passEditText;
    @BindView(R.id.login_view_logo)
    View _logoView;
    @BindView(R.id.main_layout)
    View _mainView;

    String _mail = "";


    private int _poolAuth = 0;
    public String _action = "";

    public AuthentFragment() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            _mainActivity.headerShow(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_authent, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(false);
        _mainActivity.lockedDrawer(true);
        _emailEditText.setText("augustin@bicworld.com");
        _passEditText.setText("augustin@bicworld.com");
        /*_emailEditText.setText("dev@pompfuel.com");
        _passEditText.setText("dev@pompfuel.com");*/

        _mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = _mainView.getRootView().getHeight() - _mainView.getHeight();
                if (heightDiff > dpToPx(_mainActivity, 200)) {
                    CommonUtils.log("soft visible true");
                    _logoView.setVisibility(View.GONE);
                }else {
                    CommonUtils.log("soft visible false");
                    _logoView.setVisibility(View.VISIBLE);
                }
            }
        });
        return v;
    }

    public float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @OnClick(R.id.login_creer_compte)
    public void creeCompte(Button btn) {
        _mainActivity.hideKeyBoard();
        goToCreateCompte();
    }

    @OnClick(R.id.login_button)
    public void connection(Button btn) {
        _poolAuth = 0;
        _action = "authentication";

        _mainActivity.hideKeyBoard();

        if (verifyEditText())
            runAuth();
    }

    @OnClick(R.id.login_password_oublie)
    public void passOublie(View view) {
        _poolAuth = 0;
        _action = "init_pass";
        _mainActivity.hideKeyBoard();
        runResetPass();
    }

    @Override
    public void onResume() {
        Set<String> list = _app.getListAutoCompleteLoging();
        String[] arr = list.toArray(new String[list.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, arr);
        _emailEditText.setAdapter(adapter);
        super.onResume();
    }

    private void runResetPass() {
        if (!_emailEditText.getText().toString().isEmpty()) {
            if (Strings.isValidEmail(_emailEditText.getText().toString())) {
                resetPass(_emailEditText.getText().toString(), "fr");
            } else {
                CommonUtils.alert(getString(R.string.invalid_email), null, 0, null, getContext());
            }
        } else {
            String msg = getStringRes(R.string.empty_field) + " " + getStringRes(R.string.hint_email) + " " + getStringRes(R.string.empty_email_suite);
            CommonUtils.alert(msg, null, 0, null, getContext());
        }

    }

    private void resetPass(String email, String locale) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).resetPass(email, locale, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) {
                        getToken();
                    } else {
                        CommonUtils.alert(getString(R.string.text_pass_message), getString(R.string.text_pass_title), 0, null, _mainActivity);
                    }

                }else {
                    CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                }
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                CommonUtils.alert(getResources().getString(R.string.text_pass_message), getResources().getString(R.string.text_pass_title), 0, null, _mainActivity);
            }
        });
    }

    private void goToCreateCompte() {
        AuthentCreateFragment fragment = new AuthentCreateFragment();
        _mainActivity.replaceMainFragment(fragment, true);
    }


    private void runAuth() {
        //"wylog@wylog.com","wylog@wylog.com"
        _mail = _emailEditText.getText().toString().trim();
        String pass = _passEditText.getText().toString().trim();
        //auth("augustin@bicworld.com","augustin@bicworld.com");
        auth(_mail, pass);


    }

    private void auth(String mail, String pass) {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).authent(mail, pass, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _poolAuth == 0) {
                        getToken();
                    } else
                        CommonUtils.alert(getString(R.string.invalid_email_pass), null, 0, null, getContext());

                }else {
                    CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                }

            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {

                    String jsonStr = (String) obj;
                    _app.userSaveJson(jsonStr);
                    CommonUtils.log(jsonStr);
                    if (_app.user() != null)
                        _app.userSaveObject(_app.user());

                    _app.addListAutoCompleteLoging(_mail);
                    goToOrder();
                }

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
                if (_action == "authentication")
                    runAuth();
                else
                    runResetPass();
            }

            _poolAuth = 1;
        }

    }

    private void goToOrder() {
        OrderHomeFragment fragment = new OrderHomeFragment();
        _mainActivity.replaceMainFragment(fragment, true);
        _mainActivity.initCompteName();
    }

    public boolean verifyEditText() {
        boolean isOk = false;
        String message = "";
        int emptyField = 0;
        if (_emailEditText.getText().toString().isEmpty()) {
            emptyField++;
            if (emptyField > 1) {
                message = message + ", " + getStringRes(R.string.hint_email);
            } else {
                message = getStringRes(R.string.hint_email);
            }
        }
        if (_passEditText.getText().toString().isEmpty()) {
            emptyField++;
            if (emptyField > 1) {
                message = message + ", " + getStringRes(R.string.hint_password);
            } else {
                message = getStringRes(R.string.hint_password);
            }
        }
        if (emptyField > 0) {
            if (emptyField > 1) {
                message = getStringRes(R.string.empty_fields) + " " + message;
                CommonUtils.alert(message, null, 0, null, getContext());
            } else {
                message = getStringRes(R.string.empty_field) + " " + message;
                CommonUtils.alert(message, null, 0, null, getContext());
            }
        } else {
            if (Strings.isValidEmail(_emailEditText.getText().toString())) {
                if (_passEditText.getText().toString().length() >= 8) {
                    isOk = true;
                } else {
                    CommonUtils.alert(getString(R.string.invalid_password), null, 0, null, getContext());
                }
            } else {
                CommonUtils.alert(getString(R.string.invalid_email), null, 0, null, getContext());
            }
        }
        return isOk;
    }

}
