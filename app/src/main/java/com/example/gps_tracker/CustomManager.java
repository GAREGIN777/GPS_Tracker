package com.example.gps_tracker;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CustomManager{
    private FragmentManager fragmentManager = null;
    public static final String REPLACE_ACTION = "replace";
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



    public void listenFragmentBtn(Class<? extends Fragment> fragmentClass, String action) {
        View.OnClickListener fragmentListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (action){
                    case REPLACE_ACTION:
                        replaceFragment(fragmentClass);
                        break;
                }
            }
        };
    }
    public void listenFragmentBtn(Fragment fragment, String action) {
       // View.OnClickListener fragmentListener = new View.OnClickListener() {
           // public void onClick(View v) {
                switch (action){
                    case REPLACE_ACTION:
                        replaceFragment(fragment);
                        break;
                }
          //  }
      //  };
    }

}
