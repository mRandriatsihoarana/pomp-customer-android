package fr.pomp.adfuell.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment {

    @BindView(R.id.message)
    EditText _messageEditText;

    private int _poolAuth = 0;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_nous_contacter));
        return v;
    }

    @OnClick(R.id.envoyer_btn)
    public void clickEnvoyerBtn(View view) {
        _poolAuth = 0;
        _mainActivity.hideKeyBoard();
        if (!_messageEditText.getText().toString().isEmpty()) {
            _poolAuth = 0;
            runSendMessage();
        } else {
            CommonUtils.alert(getStringRes(R.string.empty_message_field), "", 0, null, _mainActivity);
        }

    }

    private void runSendMessage() {
        postMessage(_app.userObject().id, _messageEditText.getText().toString());
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

                }else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                CommonUtils.alert(getResources().getString(R.string.succes_message), "", 0, null, _mainActivity);
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
                runSendMessage();
            }

            _poolAuth = 1;
        }

    }

}
