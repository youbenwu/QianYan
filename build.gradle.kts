buildscript {
//    ext {
//        compose_version = "1.2.1"
//        val accompanist_version = "0.25.1"
//        room_version = "2.4.2"
//    }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false

}