package org.uqac.android.projet.rpgsheet.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.uqac.android.projet.rpgsheet.R;

/**
 * Created by Bruno.J on 16/03/2017.
 */

public class TraitsFrag extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.traits_view, container, false);
    }

}