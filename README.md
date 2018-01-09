# Motif
Motif - mōˈtēf/ - a decorative design or pattern.

### Currently in development

### Usage

#### Initialize Config

Define your default theme colors using `MotifConfig`, in your `Application` class in the `#onCreate()` method.

```java
@Override
public void onCreate() {
    super.onCreate();
    MotifConfig.initDefault(new MotifConfig.Builder()
                            .setColorPrimary("#444444")
                            .setColorPrimaryDark("#000000")
                            .setColorAccent("#FF4141")
                            .setColorControlHighlight("#949494")
                            .build()
            );
    //....
}
```

Register custom ViewFactories or override Motif's ViewFactories
```java
MotifConfig.initDefault(new MotifConfig.Builder()
                            .setDisableInternalViewFactoryTheming(boolean) // disable Motif internal ViewFactories
                            .addCustomViewFactoryForId(R.id.<your_view_id>, ViewFactory) // add your own ViewFactory for a specific view resource
                            .addCustomViewFactoryForIds(new int[] {R.id.<your_view_id_1>, R.id.<your_view_id_2>, ...}, ViewFactory) // add your own ViewFactory for an array of view resources
                            .addCustomViewFactoryForClass(Class, ViewFactory) // add your own ViewFactory for a specific view class being inflated
                            .build()
            );
```

Note: You don't need to define `MotifConfig` but the library will apply
the default theme specified in your styles.xml.

#### Inject into Context

Wrap the `Activity` Context:

```java
@Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(MotifContextWrapper.wrap(newBase));
}
```

Or if you need to reference a LayoutInflater, use the MotifLayoutInflater:

```java
MotifLayoutInflater.from(Context).inflate(...);
```

_You're good to go!_

Installation:
------------

### Gradle/JitPack

- Add JitPack to your top-level build.gradle file
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
- Add Motif to your module's build.gradle file
```
dependencies {
     api 'com.rentpath.motif:Motif:<version>'
}
```

Note
-------
This library was created because it is currently not possible to dynamically change themes in Android.

License
-------
Copyright (c) 2018 RentPath. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
