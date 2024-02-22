package com.example.datastore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datastore.data.Video
import java.util.concurrent.TimeUnit

class VideoAdapter(private val audioList: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val audioNameTextView: TextView = itemView.findViewById(R.id.audioNameTextView)
        val audioDurationTextView: TextView = itemView.findViewById(R.id.audioDurationTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio = audioList[position]

        holder.audioNameTextView.text = audio.name
        holder.audioDurationTextView.text = formatDuration(audio.duration)
    }

    override fun getItemCount(): Int {
        return audioList.size
    }

    private fun formatDuration(duration: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}