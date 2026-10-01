package com.kimoitv

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class KimoitvProvider : MainAPI() {
    override var mainUrl = "https://kimoitv.com"
    override var name = "KimoiTV"
    override var lang = "en"
    override val hasMainPage = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime
    )

    override suspend fun search(
        query: String
    ): List<SearchResponse> {
        val document = app.get(
            "$mainUrl/?s=${
                query.replace(" ", "+")
            }"
        ).document

        return document.select("a[href]")
            .mapNotNull { element ->
                val title = element.text().trim()
                val href = element.attr("abs:href")

                if (
                    title.isBlank() ||
                    href.isBlank() ||
                    !href.startsWith(mainUrl)
                ) {
                    null
                } else {
                    newMovieSearchResponse(
                        title,
                        href,
                        TvType.Movie
                    )
                }
            }
            .distinctBy { it.url }
    }

    override suspend fun load(
        url: String
    ): LoadResponse? {
        val document = app.get(url).document
        val title = document
            .selectFirst("h1")
            ?.text()
            ?.trim()
            ?: return null

        val poster = document
            .selectFirst("meta[property=og:image]")
            ?.attr("content")

        return newMovieLoadResponse(
            title,
            url,
            TvType.Movie,
            url
        ) {
            posterUrl = poster
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        return false
    }
}
