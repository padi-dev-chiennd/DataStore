package com.example.datastore

import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datastore.data.AppPreferences
import com.example.datastore.data.Video
import com.example.datastore.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var createFileLauncher: ActivityResultLauncher<Intent>? = null
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 123
    private val REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 456
    private val videoList = mutableListOf<Video>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = VideoAdapter(videoList)
        binding.recyclerView.adapter = adapter

        // Thực hiện truy vấn và cập nhật danh sách video
        queryVideos()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&  Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q
        ) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
        createFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Xử lý khi người dùng đã chọn nơi lưu tệp thành công
                // Ví dụ: Ghi nội dung vào tệp sau khi đã có Uri
                val uri: Uri? = result.data?.data
                uri?.let { saveTextToFile(it, binding.inputText.text.toString()) }
            }
        }

        binding.SAF.setOnClickListener {
            // Gọi hàm để tạo tệp
            createFile()
        }
        binding.internalStorage.setOnClickListener {
            saveTextToFile(this,binding.inputFileName.text.toString(),binding.inputText.text.toString())
        }
        binding.externalStorage.setOnClickListener {
            saveImageToExternalStorage()
        }
        binding.mediaStore.setOnClickListener {
            val yourBitmap: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.img_test)
            val imageName = "example_image.png" // Set the desired image name

            saveImageToGallery(this,yourBitmap,imageName)
        }
        val isDarkMode = AppPreferences.getThemeMode(this)
        if (isDarkMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.btnSwitch.isChecked = isDarkMode
        binding.btnSwitch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            // Save the updated theme mode when the switch is toggled
            AppPreferences.setThemeMode(this, isChecked)
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            recreate()
        }

    }
    private fun saveTextToFile(context: Context, fileName: String, content: String) {
        try {
            // Lấy đường dẫn đến thư mục internal storage của ứng dụng
            val internalStorageDir = context.filesDir

            // Mở hoặc tạo một file trong thư mục internal storage
            val file = File(internalStorageDir, fileName)

            // Mở một luồng ghi dữ liệu vào file
            val outputStream = FileOutputStream(file)
            outputStream.write(content.toByteArray())

            // Đóng luồng sau khi ghi xong
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
//            type = "text/plain"
//            putExtra(Intent.EXTRA_TITLE, "example.txt")
            type = "application/pdf" // Set MIME type to create a PDF file
            putExtra(Intent.EXTRA_TITLE, "example.pdf")

        }

        createFileLauncher?.launch(intent)
    }

    private fun saveTextToFile(uri: Uri, content: String) {
        try {
            contentResolver.openFileDescriptor(uri, "w")?.use { fileDescriptor ->
                val outputStream: OutputStream = FileOutputStream(fileDescriptor.fileDescriptor)
                outputStream.write(content.toByteArray())
                outputStream.close()
                Toast.makeText(this, "File saved successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show()
        }
    }
    private fun saveImageToGallery(context: Context, bitmap: Bitmap, displayName: String): Boolean {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Change MIME type if needed
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/") // Set the directory
        }

        // Use MediaStore to insert the image into the gallery
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let { imageUri ->
            try {
                resolver.openOutputStream(imageUri)?.use { outputStream: OutputStream ->
                    // Compress the bitmap to JPEG format
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }
    private fun saveImageToExternalStorage() {
        val imageFileName = "JPEG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
            Date()
        ) + ".jpg"
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        try {
            val imageFile = File(storageDir, imageFileName)
            val fos: OutputStream = FileOutputStream(imageFile)

            // yourBitmap is the Bitmap object you want to save
            val yourBitmap: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.img_test)
            yourBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

            // Inform the system about the new file
            MediaScannerConnection.scanFile(
                this,
                arrayOf(imageFile.toString()),
                null,
                null
            )

            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
        }
    }

     //Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
            }
        }
    }
    private fun queryVideos() {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
        )

        val selection = "${MediaStore.Audio.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES).toString()
        )

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val query = contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                videoList.add(Video(contentUri, name, duration, size))
            }

            binding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }
}