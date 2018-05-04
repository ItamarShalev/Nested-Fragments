package challenge.com.nested_fragment.utils;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import challenge.com.nested_fragment.R;
import challenge.com.nested_fragment.fragments.NestedFragment;

public class NestedFragmentManger {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";
    private static final String TAG_OLD_COUNT = "TAG_OLD_COUNT";
    private static final int START_POSITION = 1;
    private int currentCount;
    private FragmentManager fragmentManager;

    public NestedFragmentManger(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }


    /**
     * @param newCount the nested fragment to create, if have before destroy and after create
     */
    public void changeAmountNestedFragment(int newCount) {
        if (currentCount != 0) {
            removeAllFragments();
        }
        addFragments(newCount - 1, START_POSITION, fragmentManager, R.id.fragments_container_frame);
        currentCount = newCount;
    }

    /**
     * @param outState adding to bundle the parent of all fragment and the currentCount of the total nested fragment
     */
    public void onSaveInstanceState(Bundle outState) {
        NestedFragment parentFragments = getParentNestedFragment();
        if (parentFragments != null) {
            fragmentManager.putFragment(outState, TAG_FRAGMENT, parentFragments);
            outState.putInt(NestedFragment.TAG_NUMBERS_FRAGMENTS_TO_CREATE, currentCount);
            outState.putInt(TAG_OLD_COUNT, currentCount);
        }
    }


    /**
     * @param inState get from the bundle the parent fragment with all the children and the number total nested fragments.
     */
    public void onRestoreInstanceState(Bundle inState) {
        fragmentManager.getFragment(inState, TAG_FRAGMENT);
        currentCount = inState.getInt(TAG_OLD_COUNT);
    }


    /**
     * @return Parent nested fragment, if it does not exist return null.
     */
    private NestedFragment getParentNestedFragment() {
        return (NestedFragment) fragmentManager.findFragmentByTag(String.valueOf(START_POSITION));
    }

    /**
     * @param numbersFragmentsToCreate the total number fragment to make, if exits before, destroy, and after create
     * @param position                 With which start position number (one size per automatic creation)
     * @param idContainer              Which view to add the fragment to
     */
    private void addFragments(int numbersFragmentsToCreate, int position, @NonNull FragmentManager fragmentManager, @IdRes int idContainer) {
        NestedFragment nestedFragment = NestedFragment.newInstance(numbersFragmentsToCreate, position);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(idContainer, nestedFragment, String.valueOf(position));
        fragmentTransaction.commit();
    }


    /**
     * @return true if have fragment to destroy, false if not have fragments.
     */
    public boolean haveFragmentToDestroy() {
        return currentCount > 0;
    }


    /**
     * Remove all fragments with the parent fragments (when the parent fragment is destroy, everyone will be destroy)
     */
    public void removeAllFragments() {
        Fragment parentFragment = getParentNestedFragment();
        if (parentFragment != null) {
            fragmentManager.beginTransaction().remove(parentFragment).commit();
            currentCount = 0;
        }
    }
}
