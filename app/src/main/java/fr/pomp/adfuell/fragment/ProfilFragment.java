package fr.pomp.adfuell.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import fr.pomp.adfuell.R;

import static fr.pomp.adfuell.utils.munix.MunixUtilities.context;

/**
 * Created by Mandimbisoa on 10/04/2017.
 */

public class ProfilFragment extends BaseFragment {

    @BindView(R.id.profil_name)
    public TextView _nameTextView;
    @BindView(R.id.profil_email)
    public TextView _emailTextView;
    @BindView(R.id.profil_phone)
    public TextView _phoneTextView;
    @BindView(R.id.profil_logout)
    public Button _logoutButton;
    @BindView(R.id.profil_picture)
    public RoundedImageView _pictureImageView;

    public ProfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(true);
        _mainActivity.lockedDrawer(false);
        _mainActivity.headerShowLeftBack();
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getString(R.string.profil));
        _mainActivity.changeHeaderBackground(R.color.gris);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(_app.user() != null){
            _nameTextView.setText(_app.user().firstname+" "+_app.user().lastname);
            _emailTextView.setText(_app.user().username);
            _phoneTextView.setText(_app.user().phone);
        }
        Picasso.with(context)
                .load("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png")
                .placeholder(R.drawable.avatar)
                .into(_pictureImageView);
    }

    @OnClick(R.id.profil_logout)
    public void logout(Button btn){
        _app.userSaveJson("");
        _app.carSaveJson("");
        _app.setTokenJson("");
        _app.carListSaveJson("");
        _mainActivity.changeHeaderBackground(R.color.transparent);
        goToAuthentication();
    }



    private void goToAuthentication() {
        AuthentFragment fragment = new AuthentFragment();
        _mainActivity.replaceMainFragment(fragment,false);
    }
}
