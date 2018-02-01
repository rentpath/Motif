# Motif
Motif - mōˈtēf/ - a decorative design or pattern.

Motif allows integrating applications to utilize the view inflation process to update specific view styles.

### Usage

#### Initialize Config

Define your default view factories using `MotifConfig`, in your `Application` class in the `#onCreate()` method.

```java
MotifConfig.initDefault(new MotifConfig.Builder()
                            .registerViewFactoryForIds(new MyViewFactory(), R.id.<your_view_id_1>, R.id.<your_view_id_2>, ...) // add your own ViewFactory for an array of view resources
                            .registerViewFactoryForClass(Class, new MyViewFactory()) // add your own ViewFactory for a specific view class being inflated
                            .build()
            );
```

ViewFactory

```java
public class MyViewFactory extends ViewFactory {

    @Override
    public void onViewCreated(MotifFactory motifFactory, Context context, View view, AttributeSet attrs) {
        // apply custom styling to specific view class or view id
    }
}
```

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
