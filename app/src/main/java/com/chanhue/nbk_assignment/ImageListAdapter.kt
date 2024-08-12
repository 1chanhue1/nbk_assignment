package com.chanhue.nbk_assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chanhue.nbk_assignment.databinding.ItemImageBinding

class ImageListAdapter(
    private val onHeartClick: (Image) -> Unit
) : ListAdapter<Image, ImageListAdapter.ImageViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image, onHeartClick)
    }

    class ImageViewHolder(
        private val binding: ItemImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image, onHeartClick: (Image) -> Unit) {
            Glide.with(binding.thumbnail.context)
                .load(image.thumbnailUrl)
                .into(binding.thumbnail)
            binding.sitename.text = image.displaySitename
            binding.datetime.text = image.datetime

            binding.heartIcon.setOnClickListener {
                onHeartClick(image)
            }

            //좋아요 관리
            if (image.isLiked) {
                binding.heartIcon.setImageResource(R.drawable.ic_heart_filled)
            } else {
                binding.heartIcon.setImageResource(R.drawable.ic_heart_empty)
            }
        }
    }

    class ImageDiffCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.thumbnailUrl == newItem.thumbnailUrl
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    }
}
