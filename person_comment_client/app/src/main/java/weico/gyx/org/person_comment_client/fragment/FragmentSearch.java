package weico.gyx.org.person_comment_client.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.activity.NearByMapActivity;

public class FragmentSearch extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, null);
        startActivity(new Intent(getActivity(), NearByMapActivity.class));
        return view;
    }


}
