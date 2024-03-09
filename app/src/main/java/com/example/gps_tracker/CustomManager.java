package com.example.gps_tracker;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CustomManager{
    private FragmentManager fragmentManager = null;
    private Class<? extends Fragment> previousReplaceFragmentClass = null;//HomeFragment.class;
    private final int fragmentContainerId;

    public CustomManager(FragmentManager fragmentManager,int fragmentContainerId){
        this.fragmentManager= fragmentManager;
        this.fragmentContainerId = fragmentContainerId;
    }
    public void replaceFragment(Class<? extends Fragment> replaceFragmentClass){
        if(previousReplaceFragmentClass != replaceFragmentClass) {
            previousReplaceFragmentClass = replaceFragmentClass;
            fragmentManager.beginTransaction()
                   /* .setCustomAnimations(
                            R.anim.silde_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out)// popExit)*/
                    .replace(fragmentContainerId,replaceFragmentClass, null)// gets the first animations
                    .addToBackStack(null)
                    .commit();
        }


    }

    public void replaceFragment(Fragment replaceFragment){

            fragmentManager.beginTransaction()
                    /* .setCustomAnimations(
                             R.anim.silde_in,  // enter
                             R.anim.fade_out,  // exit
                             R.anim.fade_in,   // popEnter
                             R.anim.slide_out)// popExit)*/
                    .replace(fragmentContainerId, replaceFragment, null)// gets the first animations
                    .addToBackStack(null)
                    .commit();


    }



    public View.OnClickListener listenFragmentBtn(Class<? extends Fragment> fragmentClass, String action) {
        View.OnClickListener fragmentListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (action){
                    case "replace":
                        replaceFragment(fragmentClass);
                        break;
                }
            }
        };
        return  fragmentListener;
    }
}
