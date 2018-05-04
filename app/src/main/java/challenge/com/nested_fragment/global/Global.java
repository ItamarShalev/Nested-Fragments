package challenge.com.nested_fragment.global;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;

public class Global {

    private static final Random random;

    static {
        random = new Random(System.currentTimeMillis());
    }

    public static int getBackgroundColor(int color) {
        color %= 255;
        return Color.argb(255, color, color, color);
    }


    public static int getBackgroundColorFromView(View view) {
        return ((ColorDrawable) view.getBackground()).getColor();
    }


    public static int getRandomColor() {
        int randomNumber = random.nextInt();
        return Color.argb(255, randomNumber >>> 16, randomNumber >>> 8, (byte) randomNumber);
    }


    public static void disableKeyboardFromActivity(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    public static void ifConditionFalseThrow(boolean condition, String message) throws ExceptionConditionFalse {
        if (!condition) {
            throw new ExceptionConditionFalse(message);
        }
    }

    public static class ExceptionConditionFalse extends Exception {

        ExceptionConditionFalse(String message) {
            super(message);
        }

    }


}
