package com.example.datastore

import android.app.Application
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainViewModel(application: Application): AndroidViewModel(application) {

//    fun onSaveClick(){
//        viewModelScope.launch(Dispatchers.IO) {
//            val textToValue = textToSaved.value.orEmpty()
//            val saveFile = saveToFile(textToValue)
//            _fileSaved.postValue(saveFile)
//        }
//
//    }
//    private fun saveToFile(data: String): File{
//        val fileName ="abc.txt"
//        val file = File(getApplication<Application>().filesDir, fileName)
//        FileOutputStream(file).use { it.write(data.toByteArray()) }
//        _fileSaved.postValue(file)
////        Toast.makeText(getApplication(),"File saved successfully",Toast.LENGTH_SHORT).show()
//        return file
//    }


}