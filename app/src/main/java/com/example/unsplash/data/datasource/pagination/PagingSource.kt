package com.example.unsplash.data.datasource.pagination


import com.example.unsplash.data.entities.UImages
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.data.datasource.web.webds.ImageWebDS
import com.example.unsplash.sys.util.Constants.Companion.PER_PAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PagingSource(
    private val webDS: ImageWebDS,
    private var query: String,
    private var clientId: String
) : PagingSource<Int, UImages>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UImages> {
        return try {
            val page = params.key ?: 1
            val images = withContext(Dispatchers.IO) {
                webDS.getImages(query, clientId, page, PER_PAGE)
            }


            LoadResult.Page(
                data = images,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (images.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UImages>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}