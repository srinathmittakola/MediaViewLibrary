package com.srinath.mediaview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.srinath.mediaview.databinding.ViewMediaBinding

class MediaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val TAG = "MediaView_Debug"

    private val binding = ViewMediaBinding.inflate(LayoutInflater.from(context), this, true)

    fun load(data: String?) {


        if (data.isNullOrEmpty()) {
            binding.loader.isVisible = false
            return
        }

        binding.loader.isVisible = true

        // Ensure the view itself is visible
        this.isVisible = true

        val isLottie = data.endsWith(".json", true) || data.contains("lottie", true)

        if (isLottie) {
            loadLottie(data)
        } else {
            loadImage(data)
        }
    }

    private fun loadLottie(url: String) {
        binding.imageView.isVisible = false
        binding.lottieView.isVisible = true

        binding.lottieView.apply {
            setFailureListener { e ->
                binding.loader.isVisible = false
            }
            setAnimationFromUrl(url)
            repeatCount = LottieDrawable.INFINITE
            addLottieOnCompositionLoadedListener {
                binding.loader.isVisible = false
                playAnimation()
            }
        }
    }

    private fun loadImage(url: String) {
        binding.lottieView.isVisible = false
        binding.imageView.isVisible = true

        // Use applicationContext to bypass potential Activity lifecycle issues
        // and override(Target.SIZE_ORIGINAL) to force Glide to start even if layout isn't ready
        Glide.with(context.applicationContext)
            .load(url)
            .override(Target.SIZE_ORIGINAL)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.loader.post { binding.loader.isVisible = false }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable?>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.loader.post { binding.loader.isVisible = false }
                    return false
                }


            })
            .into(binding.imageView)
    }

    fun stopAnimation() {
        binding.lottieView.cancelAnimation()
    }
}