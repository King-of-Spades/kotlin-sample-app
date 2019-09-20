/*
 * Copyright 2019 vmadalin.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vmadalin.android

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.google.android.play.core.splitcompat.SplitCompat
import com.vmadalin.core.di.CoreComponent
import com.vmadalin.core.di.DaggerCoreComponent
import com.vmadalin.core.di.modules.ContextModule
import io.fabric.sdk.android.Fabric
import kotlin.random.Random
import timber.log.Timber

class SampleApp : Application() {

    lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initFabric()
        initRandomNightMode()
        initDependencyInjection()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

    private fun initDependencyInjection() {
        coreComponent = DaggerCoreComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }

    /**
     * Initialize log library Timber
     */
    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * Initialize crash report library Fabric
     */
    private fun initFabric() {
        if (BuildConfig.ENABLE_CRASHLYTICS) {
            Fabric.with(this, Crashlytics())
        }
    }

    /**
     * Initialize random nightMode to make developer aware of day/night themes
     */
    private fun initRandomNightMode() {
        if (BuildConfig.DEBUG) {
            val nightMode = when (Random.nextBoolean()) {
                true -> AppCompatDelegate.MODE_NIGHT_YES
                false -> AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    companion object {
        @JvmStatic
        fun coreComponent(context: Context) =
            (context.applicationContext as SampleApp).coreComponent
    }
}