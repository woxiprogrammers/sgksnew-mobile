package com.woxi.sgks_member.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxi.sgks_member.R;

/**
 * A simple {@link Fragment} subclass.
 * Created By Sharvari
 */
public class ClassifiedHomeNewFragment extends Fragment {
    public ClassifiedHomeNewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }
}
