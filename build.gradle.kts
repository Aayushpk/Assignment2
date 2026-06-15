// Top-level build file.
// We opt OUT of AGP 9's built-in Kotlin (see gradle.properties: android.builtInKotlin=false)
// and use the traditional Kotlin Android plugin so that KSP + Hilt work reliably.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}
