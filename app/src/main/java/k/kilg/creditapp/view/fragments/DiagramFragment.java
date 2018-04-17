package k.kilg.creditapp.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import k.kilg.creditapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDiagramFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DiagramFragment extends Fragment {

    private OnDiagramFragmentInteractionListener mListener;

    private TableLayout mTableLayout;
    private TextView mTvCreditName;

    public DiagramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diagram, container, false);
        mTableLayout = v.findViewById(R.id.tablelayout);
        mTvCreditName = v.findViewById(R.id.tvDiagramCreditName);
        if (getArguments() != null) {
            mTvCreditName.setText(getArguments().getString("TEST"));
        }
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        /*TextView tv1 = new TextView(getContext());
        tv1.setText("tv1");
        TextView tv2 = new TextView(getContext());
        tv2.setText("tv2");
        tableRow.addView(tv1, 0);
        tableRow.addView(tv2, 1);
        mTableLayout.addView(tableRow, 0);*/
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onDiagramFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDiagramFragmentInteractionListener) {
            mListener = (OnDiagramFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDiagramFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnDiagramFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDiagramFragmentInteraction();
    }
}
