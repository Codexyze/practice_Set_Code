package com.nutrino.data.di.mappers

import com.nutrino.data.di.remote.MemeDto
import com.nutrino.data.di.remote.MemeXDto
import com.nutrino.domain.mappers.Meme
import com.nutrino.domain.mappers.MemeX


fun MemeDto.toDomain(): Meme{
    return Meme(
        count = count,
        memes = memes.map {
            it.toDomain()
        }
    )
}
fun MemeXDto.toDomain(): MemeX{
    return MemeX(
        author = author,
        nsfw = nsfw,
        postLink = postLink,
        preview = preview,
        spoiler = spoiler,
        subreddit = subreddit,
        title = title,
        ups = ups,
        url = url
    )

}