plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.financetracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.financetracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Lint configuration for CI: don't fail the build; reports are still generated
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kotlinOptions {
        // Kotlin 1.9 requires at least JVM 1.8; project uses Java 17 toolchain for AGP 8.x
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xjvm-default=all"
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // Enable core library desugaring to use java.time APIs on older Android versions
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    // Compose BOM for version alignment
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.9.3")

    // Compose UI and Material3
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    // Optional: Navigation Compose if needed later
    // implementation("androidx.navigation:navigation-compose:2.8.3")

    // Optional: kotlinx-datetime if used for date handling (currently using java.time)
    // implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    // Debug/Testing
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Unit test dependencies
    testImplementation("junit:junit:4.13.2")

    // Core library desugaring for java.time on minSdk 24
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")

    // Android instrumented test dependencies
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
