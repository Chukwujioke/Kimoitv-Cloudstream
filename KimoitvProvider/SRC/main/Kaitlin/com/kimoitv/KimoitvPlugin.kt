package com.kimoitv

import com.lagradost.cloudstream3.plugins.Plugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class KimoitvPlugin : Plugin() {
    override fun load() {
        registerMainAPI(KimoitvProvider())
    }
}
