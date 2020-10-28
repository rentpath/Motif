package com.rentpath.motif;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.core.os.BuildCompat;

import com.rentpath.motif.factory.MotifActivityFactory;
import com.rentpath.motif.factory.MotifFactory;
import com.rentpath.motif.factory.StatusBarViewFactory;
import com.rentpath.motif.utils.ReflectionUtils;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MotifLayoutInflater extends LayoutInflater implements MotifActivityFactory {

    private static final String TAG = "MotifLayoutInflater";

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit."
    };
    private boolean IS_AT_LEAST_Q = Build.VERSION.SDK_INT > Build.VERSION_CODES.P || BuildCompat.isAtLeastQ();
    private final MotifFactory mMotifFactory;
    // Reflection Hax
    private boolean mSetPrivateFactory = false;
    private Field mConstructorArgs = null;

    public static MotifLayoutInflater from(Context context) {
        return new MotifLayoutInflater(context);
    }

    protected MotifLayoutInflater(Context context) {
        super(context);
        mMotifFactory = new MotifFactory();
        setUpLayoutFactories(false);
    }

    protected MotifLayoutInflater(LayoutInflater original, Context newContext, final boolean cloned) {
        super(original, newContext);
        mMotifFactory = new MotifFactory();
        setUpLayoutFactories(cloned);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new MotifLayoutInflater(this, newContext, true);
    }

    // ===
    // Wrapping goodies
    // ===


    @Override
    public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getContext() instanceof Activity) {
            MotifConfig config = MotifConfig.get();
            Activity activity = (Activity) getContext();
            Window window = activity.getWindow();
            StatusBarViewFactory statusBarViewFactory = null;
            if (config.hasRegisteredStatusBarViewFactoryRegisteredForActivityClass(activity.getClass())) {
                statusBarViewFactory = config.getRegisteredStatusBarViewFactoryForActivityClass(activity.getClass());
            } else if (config.getStatusBarViewFactory() != null) {
                statusBarViewFactory = config.getStatusBarViewFactory();
            }
            if (statusBarViewFactory != null) {
                window.addFlags(statusBarViewFactory.getWindowFlags());
                window.setStatusBarColor(statusBarViewFactory.getStatusBarColor(activity));
            }
        }

        setPrivateFactoryInternal();
        return super.inflate(parser, root, attachToRoot);
    }

    /**
     * We don't want to unnecessary create/set our factories if there are none there. We try to be
     * as lazy as possible.
     */
    private void setUpLayoutFactories(boolean cloned) {
        if (cloned) return;
        // If we are HC+ we get and set Factory2 otherwise we just wrap Factory1
        if (getFactory2() != null && !(getFactory2() instanceof WrapperFactory2)) {
            // Sets both Factory/Factory2
            setFactory2(getFactory2());
        }
        // We can do this as setFactory2 is used for both methods.
        if (getFactory() != null && !(getFactory() instanceof WrapperFactory)) {
            setFactory(getFactory());
        }
    }

    @Override
    public void setFactory(Factory factory) {
        // Only set our factory and wrap calls to the Factory trying to be set!
        if (!(factory instanceof WrapperFactory)) {
            super.setFactory(new WrapperFactory(factory, this, mMotifFactory));
        } else {
            super.setFactory(factory);
        }
    }

    @Override
    public void setFactory2(Factory2 factory2) {
        // Only set our factory and wrap calls to the Factory2 trying to be set!
        if (!(factory2 instanceof WrapperFactory2)) {
//            LayoutInflaterCompat.setFactory(this, new WrapperFactory2(factory2, mMotifFactory));
            super.setFactory2(new WrapperFactory2(factory2, mMotifFactory));
        } else {
            super.setFactory2(factory2);
        }
    }

    private void setPrivateFactoryInternal() {
        // Already tried to set the factory.
        if (mSetPrivateFactory) return;
        // Reflection (Or Old Device) skip.
        if (!getConfig().isReflection()) return;
        // Skip if not attached to an activity.
        if (!(getContext() instanceof Factory2)) {
            mSetPrivateFactory = true;
            return;
        }

        final Method setPrivateFactoryMethod = ReflectionUtils
                .getMethod(LayoutInflater.class, "setPrivateFactory");

        if (setPrivateFactoryMethod != null) {
            ReflectionUtils.invokeMethod(this,
                    setPrivateFactoryMethod,
                    new PrivateWrapperFactory2((Factory2) getContext(), this, mMotifFactory));
        }
        mSetPrivateFactory = true;
    }

    // ===
    // LayoutInflater ViewCreators
    // Works in order of inflation
    // ===

    /**
     * The Activity onCreateView (PrivateFactory) is the third port of call for LayoutInflation.
     * We opted to manual injection over aggressive reflection, this should be less fragile.
     */
    @Override
    public View onActivityCreateView(View parent, View view, String name, Context context, AttributeSet attrs) {
        AttributeSet newAttrs = attrs;
        MotifConfig config = MotifConfig.get();
        if (config.getAttributeSetConfig() != null) {
            newAttrs = config.getAttributeSetConfig().getConfiguredAttributeSet(view, name, context, attrs);
        }
        return mMotifFactory.onViewCreated(createCustomViewInternal(parent, view, name, context, newAttrs), context, newAttrs);
    }

    /**
     * The LayoutInflater onCreateView is the fourth port of call for LayoutInflation.
     * BUT only for none CustomViews.
     */
    @Override
    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        AttributeSet newAttrs = attrs;
        MotifConfig config = MotifConfig.get();
        if (config.getAttributeSetConfig() != null) {
            try {
                newAttrs = config.getAttributeSetConfig().getConfiguredAttributeSet(null, name, getContext(), attrs);
            } catch (Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        }
        return mMotifFactory.onViewCreated(super.onCreateView(parent, name, newAttrs),
                getContext(), attrs);
    }

    /**
     * The LayoutInflater onCreateView is the fourth port of call for LayoutInflation.
     * BUT only for none CustomViews.
     * Basically if this method doesn't inflate the View nothing probably will.
     */
    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        AttributeSet newAttrs = attrs;
        MotifConfig config = MotifConfig.get();
        if (config.getAttributeSetConfig() != null) {
            newAttrs = config.getAttributeSetConfig().getConfiguredAttributeSet(null, name, getContext(), attrs);
        }

        // This mimics the {@code PhoneLayoutInflater} in the way it tries to inflate the base
        // classes, if this fails its pretty certain the app will fail at this point.
        View view = null;
        for (String prefix : sClassPrefixList) {
            try {
                view = createView(name, prefix, newAttrs);
            } catch (ClassNotFoundException ignored) {
            }
        }
        // In this case we want to let the base class take a crack
        // at it.
        if (view == null) view = super.onCreateView(name, newAttrs);

        return mMotifFactory.onViewCreated(view, view.getContext(), newAttrs);
    }

    /**
     * Nasty method to inflate custom layouts that haven't been handled else where. If this fails it
     * will fall back through to the PhoneLayoutInflater method of inflating custom views where
     * Motif will NOT have a hook into.
     *
     * @param parent      parent view
     * @param view        view if it has been inflated by this point, if this is not null this method
     *                    just returns this value.
     * @param name        name of the thing to inflate.
     * @param viewContext Context to inflate by if parent is null
     * @param attrs       Attr for this view which we can steal fontPath from too.
     * @return view or the View we inflate in here.
     */
    private View createCustomViewInternal(View parent, View view, String name, Context viewContext, AttributeSet attrs) {
        // I by no means advise anyone to do this normally, but Google have locked down access to
        // the createView() method, so we never get a callback with attributes at the end of the
        // createViewFromTag chain (which would solve all this unnecessary rubbish).
        // We at the very least try to optimise this as much as possible.
        // We only call for customViews (As they are the ones that never go through onCreateView(...)).
        // We also maintain the Field reference and make it accessible which will make a pretty
        // significant difference to performance on Android 4.0+.

        if (view == null && name.indexOf('.') > -1) {
            if (IS_AT_LEAST_Q) {
                try {
                    view = cloneInContext(viewContext).createView(name, null, attrs);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                mConstructorArgs = ReflectionUtils.getField(LayoutInflater.class, "mConstructorArgs");

                final Object[] mConstructorArgsArr = (Object[]) ReflectionUtils.getValue(mConstructorArgs, this);
                final Object lastContext = mConstructorArgsArr[0];
                // The LayoutInflater actually finds out the correct context to use. We just need to set
                // it on the mConstructor for the internal method.
                // Set the constructor ars up for the createView, not sure why we can't pass these in.
                mConstructorArgsArr[0] = viewContext;
                ReflectionUtils.setValue(mConstructorArgs, this, mConstructorArgsArr);
                try {
                    view = createView(name, null, attrs);
                } catch (ClassNotFoundException ignored) {
                } finally {
                    mConstructorArgsArr[0] = lastContext;
                    ReflectionUtils.setValue(mConstructorArgs, this, mConstructorArgsArr);
                }
            }
        }
        return view;
    }

    // ===
    // Wrapper Factories for Pre/Post HC
    // ===

    /**
     * Factory 1 is the first port of call for LayoutInflation
     */
    private static class WrapperFactory implements Factory {

        private final Factory mFactory;
        private final MotifLayoutInflater mInflater;
        private final MotifFactory mMotifFactory;

        public WrapperFactory(Factory factory, MotifLayoutInflater inflater, MotifFactory motifFactory) {
            mFactory = factory;
            mInflater = inflater;
            mMotifFactory = motifFactory;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            AttributeSet newAttrs = attrs;
            MotifConfig config = MotifConfig.get();
            if (config.getAttributeSetConfig() != null) {
                newAttrs = config.getAttributeSetConfig().getConfiguredAttributeSet(null, name, context, attrs);
            }

            return mMotifFactory.onViewCreated(
                    mFactory.onCreateView(name, context, newAttrs),
                    context, newAttrs
            );
        }
    }

    /**
     * Factory 2 is the second port of call for LayoutInflation
     */
    private static class WrapperFactory2 implements Factory2 {
        protected final Factory2 mFactory2;
        protected final MotifFactory mMotifFactory;

        public WrapperFactory2(Factory2 factory2, MotifFactory motifFactory) {
            mFactory2 = factory2;
            mMotifFactory = motifFactory;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            AttributeSet newAttrs = attrs;
            MotifConfig config = MotifConfig.get();
            if (config.getAttributeSetConfig() != null) {
                newAttrs = config.getAttributeSetConfig().getConfiguredAttributeSet(null, name, context, attrs);
            }

            return mMotifFactory.onViewCreated(
                    mFactory2.onCreateView(name, context, newAttrs),
                    context, newAttrs);
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            AttributeSet newAttrs = attrs;
            MotifConfig config = MotifConfig.get();
            if (config.getAttributeSetConfig() != null) {
                newAttrs = config.getAttributeSetConfig().getConfiguredAttributeSet(null, name, context, attrs);
            }

            return mMotifFactory.onViewCreated(
                    mFactory2.onCreateView(parent, name, context, newAttrs),
                    context, newAttrs);
        }
    }

    /**
     * Private factory is step three for Activity Inflation, this is what is attached to the
     * Activity on HC+ devices.
     */
    private static class PrivateWrapperFactory2 extends WrapperFactory2 {

        private final MotifLayoutInflater mInflater;

        public PrivateWrapperFactory2(Factory2 factory2, MotifLayoutInflater inflater, MotifFactory motifFactory) {
            super(factory2, motifFactory);
            mInflater = inflater;
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            AttributeSet newAttrs = attrs;
            MotifConfig config = MotifConfig.get();
            if (config.getAttributeSetConfig() != null) {
                newAttrs = config.getAttributeSetConfig().getConfiguredAttributeSet(null, name, context, attrs);
            }

            return mMotifFactory.onViewCreated(
                    mInflater.createCustomViewInternal(parent,
                            mFactory2.onCreateView(parent, name, context, newAttrs),
                            name, context, newAttrs
                    ),
                    context, newAttrs
            );
        }
    }

    private MotifConfig getConfig() {
        return MotifConfig.get();
    }
}