package com.rentpath.motif;

import com.rentpath.motif.factory.StatusBarViewFactory;
import com.rentpath.motif.factory.ViewFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotifConfig {

    private static MotifConfig sInstance;

    /**
     * Set the default Motif Config
     *
     * @param motifConfig the config build using the builder.
     * @see MotifConfig.Builder
     */
    public static void initDefault(MotifConfig motifConfig) {
        sInstance = motifConfig;
    }

    /**
     * The current Motif Config.
     * If not set it will create a default config.
     */
    public static MotifConfig get() {
        if (sInstance == null)
            sInstance = new MotifConfig(new Builder());
        return sInstance;
    }

    /**
     * The custom view factory to handle view theme customization
     */
    private ViewFactory mOverridingViewFactory;
    /**
     * Any custom view factory implementations to listen for. These will be used when a view is inflated
     * that is the same class of a view factory registered to the config
     */
    private Map<Class, ViewFactory> mClassViewFactories = new HashMap<>();
    /**
     * Any custom view factory implementations to listen for based on resource id. These will be used when a view is inflated
     * that has the same id of of a factory registered with the resource id
     */
    private Map<List<Integer>, ViewFactory> mIdViewFactories = new HashMap<>();
    /**
     * Custom attribute set config used to modify your attributes before view inflation. Registering this
     * will receive callbacks so you can use {@link com.rentpath.motif.utils.MotifAttributeSetUtil} to modify attributes
     */
    private MotifAttributeSetConfig mAttributeSetConfig;
    /**
     * Status Bar View factory responsible for tinting the status bar of an activity, if a specific activity class is registered, it will be called instead
     */
    private StatusBarViewFactory mStatusBarViewFactory;
    /**
     * Status Bar View factories responsible for tingint the status bar of a specific activity instance
     */
    private Map<List<Class>, StatusBarViewFactory> mActivityStatusBarViewFactories = new HashMap<>();
    /**
     * Use Reflection to inject the private factory.
     */
    private final boolean mReflection;

    protected MotifConfig(Builder builder) {
        mOverridingViewFactory = builder.overridingViewFactory;
        mClassViewFactories = builder.classViewFactories;
        mIdViewFactories = builder.idViewFactories;
        mAttributeSetConfig = builder.attributeSetConfig;
        mStatusBarViewFactory = builder.statusBarViewFactory;
        mActivityStatusBarViewFactories = builder.activityStatusBarViewFactories;
        mReflection = builder.reflection;
    }

    public ViewFactory getOverridingViewFactory() {
        return mOverridingViewFactory;
    }

    public boolean hasRegisteredViewFactoryRegisteredForClass(Class clazz) {
        return mClassViewFactories.containsKey(clazz);
    }

    public ViewFactory getRegisteredViewFactoryForClass(Class clazz) {
        return mClassViewFactories.get(clazz);
    }

    public boolean hasRegisteredViewFactoryRegisteredForId(int resourceId) {
        for (List<Integer> ids : mIdViewFactories.keySet()) {
            if (ids.contains(resourceId)) {
                return true;
            }
        }
        return false;
    }

    public ViewFactory getRegisteredViewFactoryForId(int resourceId) {
        for (List<Integer> ids : mIdViewFactories.keySet()) {
            if (ids.contains(resourceId)) {
                return mIdViewFactories.get(ids);
            }
        }
        return null;
    }

    public MotifAttributeSetConfig getAttributeSetConfig() {
        return mAttributeSetConfig;
    }

    public StatusBarViewFactory getStatusBarViewFactory() {
        return mStatusBarViewFactory;
    }

    public boolean hasRegisteredStatusBarViewFactoryRegisteredForActivityClass(Class clazz) {
        for (List<Class> ids : mActivityStatusBarViewFactories.keySet()) {
            if (ids.contains(clazz)) {
                return true;
            }
        }
        return false;
    }

    public StatusBarViewFactory getRegisteredStatusBarViewFactoryForActivityClass(Class clazz) {
        for (List<Class> ids : mActivityStatusBarViewFactories.keySet()) {
            if (ids.contains(clazz)) {
                return mActivityStatusBarViewFactories.get(ids);
            }
        }
        return null;
    }

    public boolean isReflection() {
        return mReflection;
    }

    public static class Builder {
        /**
         * Set an overriding view factory that will be the sole caller when view inflation occurs
         */
        private ViewFactory overridingViewFactory;
        /**
         * Add a view factory for a specific view class, which will be called during view inflation
         */
        private Map<Class, ViewFactory> classViewFactories = new HashMap<>();
        /**
         * Add a view factory for a specific view id, which will be called during view inflation
         */
        private Map<List<Integer>, ViewFactory> idViewFactories = new HashMap<>();
        /**
         * IN DEVELOPMENT
         */
        private MotifAttributeSetConfig attributeSetConfig;
        /**
         * Add a status bar view factory to obtain the status bar color to be applied to an activity when started
         */
        private StatusBarViewFactory statusBarViewFactory;
        /**
         * Add a status bar view factory to obtain the status bar color to be applied to the set activities
         */
        private Map<List<Class>, StatusBarViewFactory> activityStatusBarViewFactories = new HashMap<>();
        /**
         * Use Reflection to inject the private factory. Doesn't exist pre HC. so defaults to false.
         */
        private boolean reflection = true;

        /**
         * Define your own custom view factory to replace the internal factories. Doing so will prevent any internal customization.
         *
         * @param overridingViewFactory the view factory to be used when inflating views.
         * @return this builder
         */
        public Builder registerOverridingViewFactory(ViewFactory overridingViewFactory) {
            this.overridingViewFactory = overridingViewFactory;
            return this;
        }

        /**
         *
         * @param clazz The class to look for when inflating views
         * @param viewFactory The view factory that should be called when the corresponding class is inflated
         * @return this builder
         */
        public Builder registerViewFactoryForClass(Class clazz, ViewFactory viewFactory) {
            classViewFactories.put(clazz, viewFactory);
            return this;
        }

        /**
         *
         * @param resourceIds The resource ids to look for when inflating views
         * @param viewFactory The view factory that should be called when the corresponding resource id is inflated
         * @return this builder
         */
        public Builder registerViewFactoryForIds(ViewFactory viewFactory, int... resourceIds) {
            List<Integer> ids = new ArrayList<>();
            for (int id : resourceIds) {
                ids.add(id);
            }
            idViewFactories.put(ids, viewFactory);
            return this;
        }

        /**
         *
         * @param attributeSetConfig The config to be called when view inflation occurs
         * @return this builder
         */
        public Builder registerAttributeSetConfig(MotifAttributeSetConfig attributeSetConfig) {
            throw new IllegalStateException("This option is not yet supported. It is currently in development.");
//            this.attributeSetConfig = attributeSetConfig;
//            return this;
        }

        public Builder registerStatusBarViewFactory(StatusBarViewFactory statusBarViewFactory) {
            this.statusBarViewFactory = statusBarViewFactory;
            return this;
        }

        public Builder registerStatusBarViewFactoryForActivities(StatusBarViewFactory statusBarViewFactory, Class... activityClasses) {
            List<Class> classes = new ArrayList<>();
            for (Class clazz : activityClasses) {
                classes.add(clazz);
            }
            activityStatusBarViewFactories.put(classes, statusBarViewFactory);
            return this;
        }

        /**
         * <p>Turn of the use of Reflection to inject the private factory.
         * This has operational consequences! Please read and understand before disabling.
         * <b>This is already disabled on pre Honeycomb devices. (API 11)</b></p>
         *
         * <p> If you disable this you will need to override your {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
         * as this is set as the {@link android.view.LayoutInflater} private factory.</p>
         * <br>
         * <b> Use the following code in the Activity if you disable FactoryInjection:</b>
         * <pre><code>
         * {@literal @}Override
         * {@literal @}TargetApi(Build.VERSION_CODES.HONEYCOMB)
         * public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
         *   return MotifContextWrapper.onActivityCreateView(this, parent, super.onCreateView(parent, name, context, attrs), name, context, attrs);
         * }
         * </code></pre>
         */
        public Builder disablePrivateFactoryInjection() {
            this.reflection = false;
            return this;
        }

        public MotifConfig build() {
            return new MotifConfig(this);
        }
    }
}
