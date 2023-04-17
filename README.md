# AndroidGalleryView

Gallery view for Image and Video in Android.

Features:
Image View

Video View

Image Zoom

Video Preview

Video Play/Pause Control with Full Screen

How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

allprojects {
repositories {
...
maven { url 'https://jitpack.io' }
}
}

Step 2. Add the dependency
dependencies {
implementation 'com.github.AdilHypeteq:AndroidGalleryView:1.0.0'
}

Step 3. Use where you want to show gallery
ArrayList<String> paths = new ArrayList<>();
paths.add("Path1");
paths.add("Path2");
paths.add("Path3");
GalleryView.show(this,paths);
Preview: