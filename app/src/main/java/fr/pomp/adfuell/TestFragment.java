package fr.pomp.adfuell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import fr.pomp.adfuell.utils.edena.EDButterKniff;
import fr.pomp.adfuell.utils.edena.EDEventBus;
import fr.pomp.adfuell.utils.munix.Logs;


public class TestFragment extends Fragment {


    EDButterKniff edButterKniff;
    EDEventBus edEventBus;
    @BindView(R.id.textViewFragment)
    TextView textView;

    public TestFragment() {

        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edButterKniff = EDButterKniff.getInstance();
        edEventBus = EDEventBus.getInstance(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_test, container, false);
        edButterKniff.view(this,v);
        return v;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(EDEventBus intentServiceResult) {
        //textView.set_text((String)intentServiceResult.getResultValue());
        //Toast.makeText(this, intentServiceResult.getResultValue(), Toast.LENGTH_SHORT).show();
        if(intentServiceResult.getResultValue() instanceof Integer)
        Logs.info("download","download.."+(Integer)intentServiceResult.getResultValue());
    }

    @Override
    public void onPause() {
        super.onPause();
        edEventBus.unRegister();
    }

    @Override
    public void onResume() {
        super.onResume();
        edEventBus.register();
    }

}
