package challenge.com.nested_fragment.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.MissingFormatArgumentException;

import challenge.com.nested_fragment.R;
import challenge.com.nested_fragment.global.Global;

public class NestedFragment extends Fragment {

    public static final String TAG_NUMBERS_FRAGMENTS_TO_CREATE = "TAG_NUMBERS_FRAGMENTS_TO_CREATE";
    public static final String TAG_POSITION = "TAG_POSITION";
    private static final String TAG_COLOR = "TAG_COLOR";
    private int numbersFragmentsToCreate, position;
    private View mainView;


    /**
     * @throws NullPointerException           if arguments bundle is null
     * @throws MissingFormatArgumentException if the amount child or position missing
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initVarFromArgumentsBundle(getArguments());
        mainView = inflater.inflate(R.layout.fragment_nested, container, false);
        return mainView;
    }


    /**
     * Initializing the var from arguments bundle (with findViewByID())
     */
    private void initVarFromArgumentsBundle(Bundle argumentsBundle) {
        if (argumentsBundle == null) {
            throw new NullPointerException(getString(R.string.arguments_bundle_cannot_be_null));
        }
        numbersFragmentsToCreate = argumentsBundle.getInt(TAG_NUMBERS_FRAGMENTS_TO_CREATE);
        position = argumentsBundle.getInt(TAG_POSITION);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView fragmentNoText = view.findViewById(R.id.fragment_no_text);
        Button changeColorButton = view.findViewById(R.id.change_color_button);
        fragmentNoText.setText((getString(R.string.fragment_no) + " " + (position)));

        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.setBackgroundColor(Global.getRandomColor());
            }
        });
        int backgroundColor = -1;
        if (savedInstanceState != null) {
            backgroundColor = savedInstanceState.getInt(TAG_COLOR, -1);
            numbersFragmentsToCreate = 0;
        }
        if (backgroundColor == -1) {
            int colorByte = ((position * 10) + 50);
            backgroundColor = Global.getBackgroundColor(colorByte);
        }
        view.setBackgroundColor(backgroundColor);
        createChild();
    }


    /**
     * Save the background color
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAG_COLOR, Global.getBackgroundColorFromView(mainView));
    }


    /**
     * Create child of nested fragments by #numbersFragmentsToCreate
     */
    private void createChild() {
        if (numbersFragmentsToCreate > 0) {
            Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                public void run() {
                    NestedFragment nestedFragment = newInstance(numbersFragmentsToCreate - 1, position + 1);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.add(R.id.fragments_child_frame, nestedFragment).commit();
                }
            };
            handler.post(runnable);
        }
    }

    /**
     * @param numbersFragmentsToCreate Number of nested fragment to create
     * @param position                 With which start position number (one size per automatic creation)
     * @return New NestedFragment object with arguments to add to fragmentManager
     */
    public static NestedFragment newInstance(int numbersFragmentsToCreate, int position) {
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(TAG_NUMBERS_FRAGMENTS_TO_CREATE, numbersFragmentsToCreate);
        argumentsBundle.putInt(TAG_POSITION, position);

        NestedFragment nestedFragment = new NestedFragment();
        nestedFragment.setArguments(argumentsBundle);
        return nestedFragment;
    }


}
