package com.srinath.mediaview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.srinath.mediaview.databinding.ViewMediaBinding

class MediaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewMediaBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.MediaView) {
            val scaleTypeIndex = getInt(R.styleable.MediaView_android_scaleType, 3)
            val scaleType = getScaleTypeFromIndex(scaleTypeIndex)
            binding.imageView.scaleType = scaleType
            binding.lottieView.scaleType = scaleType

            val loaderTint = getColor(R.styleable.MediaView_loaderTint, -1)
            if (loaderTint != -1) {
                binding.loader.indeterminateTintList = android.content.res.ColorStateList.valueOf(loaderTint)
            }

            val radius = getDimension(R.styleable.MediaView_mediaCornerRadius, 0f)
            binding.cardView.radius = radius

            val elevation = getDimension(R.styleable.MediaView_mediaElevation, 0f)
            binding.cardView.cardElevation = elevation

            val strokeColor = getColor(R.styleable.MediaView_strokeColor, android.graphics.Color.TRANSPARENT)
            binding.cardView.strokeColor = strokeColor

            val strokeWidth = getDimensionPixelSize(R.styleable.MediaView_strokeWidth, 0)
            binding.cardView.strokeWidth = strokeWidth
        }
    }


    fun load(data: String?) {

        if (data.isNullOrEmpty()) {
            binding.loader.isVisible = true
            binding.imageView.isVisible = false
            binding.lottieView.isVisible = false
            return
        }

        binding.loader.isVisible = true

        this.isVisible = true

        val isLottie = data.endsWith(".json", true)
        val isGif = data.endsWith(".gif", true)

        when {
            isLottie -> loadLottie(data)
            isGif -> loadGif(data)
            else -> loadImage(data)
        }

    }

    private fun loadLottie(url: String) {


        binding.imageView.isVisible = false
        binding.lottieView.isVisible = true

        binding.lottieView.apply {

            cancelAnimation()

            setFailureListener {
                binding.loader.isVisible = false
            }

            repeatCount = LottieDrawable.INFINITE

            addLottieOnCompositionLoadedListener {
                binding.loader.isVisible = false
                playAnimation()
            }

            setAnimationFromUrl(url)
        }
    }

    private fun loadImage(url: String) {

        binding.lottieView.cancelAnimation()
        binding.lottieView.isVisible = false

        binding.imageView.isVisible = true


        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.loader.isVisible = true
                    binding.imageView.isVisible = false

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.loader.isVisible = false
                    binding.imageView.isVisible = true

                    return false
                }
            })
            .into(binding.imageView)
    }


    private fun loadGif(url: String) {

        binding.lottieView.cancelAnimation()
        binding.lottieView.isVisible = false

        binding.imageView.isVisible = true

        Glide.with(this)
            .asGif()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .listener(object : RequestListener<GifDrawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.loader.isVisible = true
                    binding.imageView.isVisible = false

                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable,
                    model: Any,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.loader.isVisible = false
                    binding.imageView.isVisible = true

                    return false
                }
            })
            .into(binding.imageView)
    }

    private fun getScaleTypeFromIndex(index: Int): ImageView.ScaleType {
        return when (index) {
            0 -> ImageView.ScaleType.MATRIX
            1 -> ImageView.ScaleType.FIT_XY
            2 -> ImageView.ScaleType.FIT_START
            3 -> ImageView.ScaleType.FIT_CENTER
            4 -> ImageView.ScaleType.FIT_END
            5 -> ImageView.ScaleType.CENTER
            6 -> ImageView.ScaleType.CENTER_CROP
            7 -> ImageView.ScaleType.CENTER_INSIDE
            else -> ImageView.ScaleType.FIT_CENTER
        }
    }
}