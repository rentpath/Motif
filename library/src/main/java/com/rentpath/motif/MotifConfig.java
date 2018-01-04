package com.rentpath.motif;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.rentpath.motif.utils.MotifUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MotifConfig {

    /**
     * The default styles for the factory to lookup. The builder builds an extended immutable
     * map of this with any additional custom styles.
     */
    private static final Map<Class<? extends View>, Integer> DEFAULT_STYLES = new HashMap<>();

    static {
        {
            DEFAULT_STYLES.put(TextView.class, android.R.attr.textViewStyle);
            DEFAULT_STYLES.put(Button.class, android.R.attr.buttonStyle);
            DEFAULT_STYLES.put(EditText.class, android.R.attr.editTextStyle);
            DEFAULT_STYLES.put(AutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            DEFAULT_STYLES.put(MultiAutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            DEFAULT_STYLES.put(CheckBox.class, android.R.attr.checkboxStyle);
            DEFAULT_STYLES.put(RadioButton.class, android.R.attr.radioButtonStyle);
            DEFAULT_STYLES.put(ToggleButton.class, android.R.attr.buttonStyleToggle);
            if (MotifUtils.canAddV7AppCompatViews()) {
                addAppCompatViews();
            }
        }
    }

    /**
     * AppCompat will inflate special versions of views for Material tinting etc,
     * this adds those classes to the style lookup map
     */
    private static void addAppCompatViews() {
        DEFAULT_STYLES.put(android.support.v7.widget.AppCompatTextView.class, android.R.attr.textViewStyle);
        DEFAULT_STYLES.put(android.support.v7.widget.AppCompatButton.class, android.R.attr.buttonStyle);
        DEFAULT_STYLES.put(android.support.v7.widget.AppCompatEditText.class, android.R.attr.editTextStyle);
        DEFAULT_STYLES.put(android.support.v7.widget.AppCompatAutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
        DEFAULT_STYLES.put(android.support.v7.widget.AppCompatMultiAutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
        DEFAULT_STYLES.put(android.support.v7.widget.AppCompatCheckBox.class, android.R.attr.checkboxStyle);
        DEFAULT_STYLES.put(android.support.v7.widget.AppCompatRadioButton.class, android.R.attr.radioButtonStyle);
        DEFAULT_STYLES.put(android.support.v7.widget.AppCompatCheckedTextView.class, android.R.attr.checkedTextViewStyle);
    }

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
     * The default primary color
     */
    private int mColorPrimary;
    /**
     * The default primary dark color
     */
    private int mColorPrimaryDark;
    /**
     * The default accent color
     */
    private int mColorAccent;
    /**
     * The default highlight color
     */
    private int mColorControlHighlight;
    /**
     * Use Reflection to inject the private factory.
     */
    private final boolean mReflection;
    /**
     * Use Reflection to intercept CustomView inflation with the correct Context.
     */
    private final boolean mCustomViewCreation;
    /**
     * Class Styles. Build from DEFAULT_STYLES and the builder.
     */
    private final Map<Class<? extends View>, Integer> mClassStyleAttributeMap;

    protected MotifConfig(Builder builder) {
        mColorPrimary = builder.colorPrimary;
        mColorPrimaryDark = builder.colorPrimaryDark;
        mColorAccent = builder.colorAccent;
        mColorControlHighlight = builder.colorControlHighlight;
        mReflection = builder.reflection;
        mCustomViewCreation = builder.customViewCreation;
        final Map<Class<? extends View>, Integer> tempMap = new HashMap<>(DEFAULT_STYLES);
        tempMap.putAll(builder.mStyleClassMap);
        mClassStyleAttributeMap = Collections.unmodifiableMap(tempMap);
    }

    public int getColorPrimary() {
        return mColorPrimary;
    }

    public void setColorPrimary(int colorPrimary) {
        this.mColorPrimary = colorPrimary;
    }

    public int getColorPrimaryDark() {
        return mColorPrimaryDark;
    }

    public void setColorPrimaryDark(int colorPrimaryDark) {
        this.mColorPrimaryDark = colorPrimaryDark;
    }

    public int getColorAccent() {
        return mColorAccent;
    }

    public void setColorAccent(int colorAccent) {
        this.mColorAccent = colorAccent;
    }

    public int getColorControlHighlight() {
        return mColorControlHighlight;
    }

    public void setColorControlHighlight(int colorControlHighlight) {
        this.mColorControlHighlight = colorControlHighlight;
    }

    public boolean isReflection() {
        return mReflection;
    }

    public boolean isCustomViewCreation() {
        return mCustomViewCreation;
    }

    public Map<Class<? extends View>, Integer> getClassStyles() {
        return mClassStyleAttributeMap;
    }

    public static class Builder {
        /**
         * Default AttrID if not set.
         */
        public static final int INVALID_ATTR_ID = -1;
        /**
         *
         */
        private int colorPrimary = INVALID_ATTR_ID;
        /**
         *
         */
        private int colorPrimaryDark = INVALID_ATTR_ID;
        /**
         *
         */
        private int colorAccent = INVALID_ATTR_ID;
        /**
         *
         */
        private int colorControlHighlight = INVALID_ATTR_ID;
        /**
         * Use Reflection to inject the private factory. Doesn't exist pre HC. so defaults to false.
         */
        private boolean reflection = true;
        /**
         * Use Reflection to intercept CustomView inflation with the correct Context.
         */
        private boolean customViewCreation = true;
        /**
         * Additional Class Styles. Can be empty.
         */
        private Map<Class<? extends View>, Integer> mStyleClassMap = new HashMap<>();

        /**
         * Set the default primary color to be used app wide
         *
         * @param colorPrimary the hex code for the primary color
         * @return this builder.
         */
        public Builder setColorPrimary(String colorPrimary) {
            this.colorPrimary = Color.parseColor(colorPrimary);
            return this;
        }

        /**
         * Set the default primary color to be used app wide
         *
         * @param context context used to obtain color code
         * @param colorPrimaryResId resource id of the primary color
         * @return this builder
         */
        public Builder setColorPrimary(Context context, int colorPrimaryResId) {
            this.colorPrimary = ContextCompat.getColor(context, colorPrimaryResId);
            return this;
        }

        /**
         * Set the default primary dark color to be used app wide
         *
         * @param colorPrimaryDark the hex code for the primary dark color
         * @return this builder.
         */
        public Builder setColorPrimaryDark(String colorPrimaryDark) {
            this.colorPrimaryDark = Color.parseColor(colorPrimaryDark);
            return this;
        }

        /**
         * Set the default primary dark color to be used app wide
         *
         * @param context context used to obtain color code
         * @param colorPrimaryDarkResId resource id of the primary dark color
         * @return this builder
         */
        public Builder setColorPrimaryDark(Context context, int colorPrimaryDarkResId) {
            this.colorPrimaryDark = ContextCompat.getColor(context, colorPrimaryDarkResId);
            return this;
        }

        /**
         * Set the default accent color to be used app wide
         *
         * @param colorAccent the hex code for the accent color
         * @return this builder.
         */
        public Builder setColorAccent(String colorAccent) {
            this.colorAccent = Color.parseColor(colorAccent);
            return this;
        }

        /**
         * Set the default accent color to be used app wide
         *
         * @param context context used to obtain color code
         * @param colorAccentResId resource id of the accent color
         * @return this builder
         */
        public Builder setColorAccent(Context context, int colorAccentResId) {
            this.colorAccent = ContextCompat.getColor(context, colorAccentResId);
            return this;
        }

        /**
         * Set the default highlight color to be used app wide
         *
         * @param colorControlHighlight the hex code for the highlight color
         * @return this builder.
         */
        public Builder setColorControlHighlight(String colorControlHighlight) {
            this.colorControlHighlight = Color.parseColor(colorControlHighlight);
            return this;
        }

        /**
         * Set the default highlight color to be used app wide
         *
         * @param context context used to obtain color code
         * @param colorControlHighlightResId resource id of the highlight color
         * @return this builder
         */
        public Builder setColorControlHighlight(Context context, int colorControlHighlightResId) {
            this.colorControlHighlight = ContextCompat.getColor(context, colorControlHighlightResId);
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

        /**
         * Due to the poor inflation order where custom views are created and never returned inside an
         * {@code onCreateView(...)} method. We have to create CustomView's at the latest point in the
         * overrideable injection flow.
         *
         * On HoneyComb+ this is inside the {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
         * Pre HoneyComb this is in the {@link android.view.LayoutInflater.Factory#onCreateView(String, android.util.AttributeSet)}
         *
         * We wrap base implementations, so if you LayoutInflater/Factory/Activity creates the
         * custom view before we get to this point, your view is used. (Such is the case with the
         * TintEditText etc)
         *
         * The problem is, the native methods pass there parents context to the constructor in a really
         * specific place. We have to mimic this in {@link MotifLayoutInflater#createCustomViewInternal(android.view.View, android.view.View, String, android.content.Context, android.util.AttributeSet)}
         * To mimic this we have to use reflection as the Class constructor args are hidden to us.
         *
         * We have discussed other means of doing this but this is the only semi-clean way of doing it.
         * (Without having to do proxy classes etc).
         *
         * Calling this will of course speed up inflation by turning off reflection, but not by much,
         * But if you want Motif to inject the correct typeface then you will need to make sure your CustomView's
         * are created before reaching the LayoutInflater onViewCreated.
         */
        public Builder disableCustomViewInflation() {
            this.customViewCreation = false;
            return this;
        }

        /**
         * Add a custom style to get looked up. If you use a custom class that has a parent style
         * which is not part of the default android styles you will need to add it here.
         *
         * The Motif inflater is unaware of custom styles in your custom classes. We use
         * the class type to look up the style attribute in the theme resources.
         *
         * So if you had a {@code MyTextField.class} which looked up it's default style as
         * {@code R.attr.textFieldStyle} you would add those here.
         *
         * {@code builder.addCustomStyle(MyTextField.class,R.attr.textFieldStyle}
         *
         * @param styleClass             the class that related to the parent styleResource. null is ignored.
         * @param styleResourceAttribute e.g. {@code R.attr.textFieldStyle}, 0 is ignored.
         * @return this builder.
         */
        public Builder addCustomStyle(final Class<? extends View> styleClass, final int styleResourceAttribute) {
            if (styleClass == null || styleResourceAttribute == 0) return this;
            mStyleClassMap.put(styleClass, styleResourceAttribute);
            return this;
        }

        public MotifConfig build() {
            return new MotifConfig(this);
        }
    }
}
