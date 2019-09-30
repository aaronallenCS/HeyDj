package com.song.request.heydj.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.song.request.heydj.DjLanding;
import com.song.request.heydj.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutMeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutMeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutMeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Toolbar toolbarT;
//    DjLanding djLanding;
//    Fragment myFragment=this;

    private OnFragmentInteractionListener mListener;

    public AboutMeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutMeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutMeFragment newInstance(String param1, String param2) {
        AboutMeFragment fragment = new AboutMeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    //    djLanding= new DjLanding();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View itemView = inflater.inflate(R.layout.fragment_about_me, container, false);

       // setHasOptionsMenu(true);

//        toolbarT = itemView.findViewById(R.id.toolbar_abt);
//        toolbarT.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//       // toolbarT.inflateMenu(R.menu.toolbar_back);
//
//        toolbarT.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent ii = new Intent(getContext(), DjLanding.class);
////                String myDj = RegisterDJActivity.getDefaults("DjName", itemView.getContext());
////                ii.putExtra("djName", myDj);
//               getActivity().finish();
//                startActivity(ii);
//                return false;
//            }
//        });

        itemView.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getContext(), DjLanding.class);
                getActivity().finish();
                startActivity(ii);
            }
        });


        TextView textView = (TextView) itemView.findViewById(R.id.sc_link);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "www.soundcloud.com/octxbrr";
        textView.setText(Html.fromHtml(text));


        // Inflate the layout for this fragment
        return itemView;
    }



//    public static void setDefaults(String key, String value, Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//    public static String getDefaults(String key, Context context)
//    {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return preferences.getString(key, null);
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_back, menu);
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
