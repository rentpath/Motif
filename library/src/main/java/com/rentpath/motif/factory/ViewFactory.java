package com.rentpath.motif.factory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.rentpath.motif.MotifConfig;

public abstract class ViewFactory<T> {

    public abstract void onViewCreated(MotifFactory motifFactory, Context context, T view, AttributeSet attrs);

    /**
     * Use to match a view against a potential view id. Such as ActionBar title etc.
     *
     * @param view    not null view you want to see has resource matching name.
     * @param matches not null resource name to match against. Its not case sensitive.
     * @return true if matches false otherwise.
     */
    protected static boolean matchesResourceIdName(View view, String matches) {
        if (view.getId() == View.NO_ID) return false;
        final String resourceEntryName = view.getResources().getResourceEntryName(view.getId());
        return resourceEntryName.equalsIgnoreCase(matches);
    }

    protected static MotifConfig getConfig() {
        return MotifConfig.get();
    }
}
