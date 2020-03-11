package com.example.myapplicationandroidlabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private AppCompatActivity parentActivity;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle dataFromActivity;
        long id;

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("id");

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the message
        TextView message = result.findViewById(R.id.textMessage);
        message.setText(dataFromActivity.getString("message"));

        //show the id:
        TextView idView = result.findViewById(R.id.textID);
        idView.setText("ID= " + id);

        CheckBox box = result.findViewById(R.id.isSent);
        if(dataFromActivity.getBoolean("sent")){box.setChecked(true);}

        Button hide = result.findViewById(R.id.hide);
        hide.setOnClickListener(btn ->{
            if (parentActivity.getSupportFragmentManager().findFragmentById(R.id.frame1) != null) {
                parentActivity.finish();
            }
            else {
                parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
        });

        return result;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
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
