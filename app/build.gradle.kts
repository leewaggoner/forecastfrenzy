import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

val weatherApiKey: String = gradleLocalProperties(rootDir).getProperty("WEATHER_API_KEY")
val cityApiKey: String = gradleLocalProperties(rootDir).getProperty("CITY_API_KEY")

android {
    namespace = "com.wreckingballsoftware.forecastfrenzy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wreckingballsoftware.forecastfrenzy"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField(name = "WEATHER_AUTH_KEY", type = "String", value = "\"$weatherApiKey\"")
            buildConfigField(name = "WEATHER_URL", type = "String", value = "\"https://www.thecocktaildb.com/api/\"")

            buildConfigField(name = "CITY_AUTH_KEY", type = "String", value = "\"$cityApiKey\"")
            buildConfigField(name = "CITY_URL", type = "String", value = "\"https://www.thecocktaildb.com/api/\"")
        }
        release {
            buildConfigField(name = "WEATHER_AUTH_KEY", type = "String", value = "\"$weatherApiKey\"")
            buildConfigField(name = "WEATHER_URL", type = "String", value = "\"https://www.thecocktaildb.com/api/\"")

            buildConfigField(name = "CITY_AUTH_KEY", type = "String", value = "\"$cityApiKey\"")
            buildConfigField(name = "CITY_URL", type = "String", value = "\"https://www.thecocktaildb.com/api/\"")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}