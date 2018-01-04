package com.rentpath.motif.factory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

class StandardViewFactory extends ViewFactory<View> {

    @Override
    public void onViewCreated(MotifFactory motifFactory, Context context, View view, AttributeSet attrs) {
        if (view.getParent() instanceof RecyclerView || view.getParent() instanceof ListView) {
            // we should update the background to use config themes
        }
    }
}
