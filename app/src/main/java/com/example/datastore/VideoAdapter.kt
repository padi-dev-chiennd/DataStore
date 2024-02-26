package com.example.datastore

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datastore.data.Video
import java.util.concurrent.TimeUnit


class VideoAdapter(private val context: Context,private val audioList: List<Video>) :
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
        // Trong ViewHolder
        holder.itemView.setOnClickListener {
            showRenameFileDialog(audio)
        }
    }

    override fun getItemCount(): Int {
        return audioList.size
    }

    private fun formatDuration(duration: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun showRenameFileDialog(audio: Video) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Rename File")

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(audio.name)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val newName = input.text.toString().trim()
            if (newName.isNotEmpty()) {
                // Cập nhật tên file
                audio.name = newName
                // TODO: Lưu thay đổi vào cơ sở dữ liệu hoặc danh sách tệp tin
                updateMediaStoreItem(context,audio.id,newName)
                // Cập nhật giao diện người dùng
                notifyDataSetChanged()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
    private fun updateMediaStoreItem(context: Context, mediaId: Long, newDisplayName: String): Int {
        val resolver: ContentResolver = context.contentResolver

        // Tạo selection để chỉ định mục cần cập nhật
        val selection = "${MediaStore.Audio.Media._ID} = ?"

        // Truyền giá trị cho selection
        val selectionArgs = arrayOf(mediaId.toString())

        // Tạo Uri cho mục cần cập nhật
        val uri: Uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaId)

        // Tạo ContentValues chứa thông tin cập nhật
        val updatedMediaDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, newDisplayName)
        }

        // Sử dụng phương thức update để thực hiện cập nhật
        return resolver.update(uri, updatedMediaDetails, selection, selectionArgs)
    }


}