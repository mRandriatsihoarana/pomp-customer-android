package fr.pomp.adfuell.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import fr.pomp.adfuell.R;
import fr.pomp.adfuell.model.HistoryModel;
import fr.pomp.adfuell.model.TokenModel;
import fr.pomp.adfuell.utils.comon.CommonUtils;
import fr.pomp.adfuell.ws.IWSDelegate;
import fr.pomp.adfuell.ws.WSService;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends BaseFragment {


    @BindView(R.id.story_list)
    ListView _list;
    @BindView(R.id.story_list_empty_msg)
    RelativeLayout _storyListEmpty;
    List<HistoryModel> _data = new ArrayList<>();
    AdapterList _adapter;

    private int _pool = 0;

    public static final String FORMAT_DATE_SCHEDULE = "yyyy-MM-dd'T'HH:mm:ss";
    SimpleDateFormat _dateFormatString = null;
    SimpleDateFormat _dateFormatScedule = new SimpleDateFormat(FORMAT_DATE_SCHEDULE);

    public StoryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_story, container, false);
        _edButterKniff.view(this, v);
        _mainActivity.headerShow(true);
        _mainActivity.headerRightBtnShow(false);
        _mainActivity.changeHeaderTitle(getStringRes(R.string.text_historique));
        _dateFormatString = new SimpleDateFormat(getStringRes(R.string.date_format_history));//dd/MM/yyyy hh:mm
        _adapter = new AdapterList();

        _list.setAdapter(_adapter);
        _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                StoryDetailFragment fragment = new StoryDetailFragment();
                fragment._history = _data.get(position);
                _mainActivity.replaceMainFragment(fragment, true);
            }
        });
        getData();
        return v;
    }

    void getData() {

        _pool = 0;
        getOrder();
    }

    @Override
    public void onSuccess(Object obj) {
        _mainActivity.loadingShow(false);
        if (obj != null) {
            TokenModel token = (TokenModel) obj;
            CommonUtils.log(token.accessToken);
            if (_pool == 0)
                getOrder();
            _pool = 1;
        }

    }

    void getOrder() {
        _mainActivity.loadingShow(true);
        WSService.getInstance(_app.getRetrofitWithAuth()).orderHistory(_app.user().id, new IWSDelegate() {
            @Override
            public void onFaillure(Object obj) {
                _mainActivity.loadingShow(false);
                if (obj != null) {
                    int codeError = (int) obj;
                    CommonUtils.log("codeError " + codeError);
                    if (codeError == 401 && _pool == 0) getToken();
                    else CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                }else {
                    CommonUtils.alert(getStringRes(R.string.faillure_request),"",0,null,_mainActivity);
                }

            }

            @Override
            public void onSuccess(Object obj) {
                _mainActivity.loadingShow(false);
                try {
                    if (obj != null) {

                        String jsonStr = (String) obj;
                        CommonUtils.log("succes "+jsonStr);
                        _data = _app._json.deSerializeArray(jsonStr, HistoryModel.class);
                        int size = _data != null ? _data.size() : 0;
                        if(size > 0){
                            _list.setVisibility(View.VISIBLE);
                            _storyListEmpty.setVisibility(View.GONE);
                            _adapter.notifyDataSetChanged();
                        }else {
                            _list.setVisibility(View.GONE);
                            _storyListEmpty.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    class AdapterList extends BaseAdapter {


        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public Object getItem(int position) {
            return _data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HistoryModel item = _data.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_story_list, parent, false);
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.card = (TextView) convertView.findViewById(R.id.story_list_card);
                viewHolder.address = (TextView) convertView.findViewById(R.id.story_list_adress);
                viewHolder.date = (TextView) convertView.findViewById(R.id.story_list_date);
                convertView.setTag(viewHolder);
            }
            viewHolder.card.setText("Réf " + item.id);
            try {
                String adress = item.schedule.location.name + "\n" + item.schedule.location.address;
                viewHolder.address.setText(adress);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            String date = "";
            try {

                Date dateScedule = null;

                dateScedule = CommonUtils.getDateWithTimeZone(item.schedule.date);
                date = _dateFormatString.format(dateScedule);
                date = date + "\t \t";
                if(item.orderType != null && item.orderType.equals("fillUp")){
                    date = date + getStringRes(R.string.text_fillup_commande);
                }else {
                    date = date + (item.amount != null ? item.amount + (item.currency != null ? item.currency : "€") : "");
                }
                date = date + "\n" + (item.status != null ? valueStatusString(item.status) : "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            viewHolder.date.setText(date);


            return convertView;
        }
    }

    class ViewHolder {
        public TextView card;
        public TextView address;
        public TextView date;
    }

}
