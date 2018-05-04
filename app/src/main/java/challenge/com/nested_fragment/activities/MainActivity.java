package challenge.com.nested_fragment.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import challenge.com.nested_fragment.R;
import challenge.com.nested_fragment.global.Global;
import challenge.com.nested_fragment.utils.NestedFragmentManger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG_BACKGROUND_COLOR = "TAG_BACKGROUND_COLOR";
    private static final String TAG_TEXT_COLOR = "TAG_TEXT_COLOR";
    private static final String TAG_TEXT_EDIT_TEXT = "TAG_TEXT_EDIT_TEXT";
    private EditText fragmentNumberEditText;
    private Button addFragmentsButton;
    private View contentView;
    private NestedFragmentManger nestedFragmentManger;
    private RelativeLayout mainLayoutRelative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        addFragmentsButton.setOnClickListener(this);
        nestedFragmentManger = new NestedFragmentManger(getSupportFragmentManager());
    }

    /**
     * Initializing the views (with findViewByID())
     */
    private void initViews() {
        contentView = findViewById(android.R.id.content);
        fragmentNumberEditText = findViewById(R.id.fragment_number_edit_text);
        addFragmentsButton = findViewById(R.id.add_fragments_button);
        mainLayoutRelative = findViewById(R.id.main_layout_relative);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        nestedFragmentManger.onSaveInstanceState(outState);
        outState.putInt(TAG_BACKGROUND_COLOR, Global.getBackgroundColorFromView(mainLayoutRelative));
        outState.putInt(TAG_TEXT_COLOR,fragmentNumberEditText.getCurrentTextColor());
        outState.putString(TAG_TEXT_EDIT_TEXT,fragmentNumberEditText.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        nestedFragmentManger.onRestoreInstanceState(inState);
        mainLayoutRelative.setBackgroundColor(inState.getInt(TAG_BACKGROUND_COLOR));
        fragmentNumberEditText.setTextColor(inState.getInt(TAG_TEXT_COLOR));
        fragmentNumberEditText.setText(inState.getString(TAG_TEXT_EDIT_TEXT));
    }





    /**
     * Get and check the number input from user if valid number and make nested fragment by the number with #nestedFragmentManger
     */
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.add_fragments_button) {
            try {
                Global.disableKeyboardFromActivity(this);
                String numbers = fragmentNumberEditText.getText().toString();
                fragmentNumberEditText.setText("");
                Global.ifConditionFalseThrow(!numbers.isEmpty(), getString(R.string.field_total_cant_be_empty));

                int countNumber = Integer.parseInt(numbers);
                Global.ifConditionFalseThrow(countNumber <= 15 && countNumber > 0, getString(R.string.invalid_number));

                nestedFragmentManger.changeAmountNestedFragment(countNumber);
                fragmentNumberEditText.setTextColor(Color.WHITE);

                mainLayoutRelative.setBackgroundColor(Global.getBackgroundColor(50));
            } catch (Global.ExceptionConditionFalse exceptionConditionFalse) {
                Snackbar.make(contentView, exceptionConditionFalse.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

        }
    }



    /**
     * Check if have fragment on the activity, if have destroy, if not, exit from activity
     */
    @Override
    public void onBackPressed() {
        if (nestedFragmentManger.haveFragmentToDestroy()) {
            nestedFragmentManger.removeAllFragments();
            mainLayoutRelative.setBackgroundColor(ContextCompat.getColor(this, android.R.color.background_light));
            fragmentNumberEditText.setTextColor(Color.BLACK);
        } else {
            super.onBackPressed();
        }
    }


}
