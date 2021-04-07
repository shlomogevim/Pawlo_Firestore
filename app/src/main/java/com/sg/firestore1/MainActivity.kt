package com.sg.firestore1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.text.set
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.firestore1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun saveThoughtOnclick(view: View) {
        val title=binding.editTextTitle.text.toString().trim()
        val thought=binding.editTextThought.text.toString().trim()
        val data=HashMap<String,Any>()
        data.put(KEY_TITLE,title)
        data.put(KET_THOUGHT,thought)
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .document("First Thought")
                .set(data)
                .addOnSuccessListener {
                    Log.i(TAG,"Data send to Firestore successfully")
                }
                .addOnFailureListener {
                    Log.i(TAG,"Cannot send data because ${it.localizedMessage}")
                }



    }

    fun showThoughtOnclick(view: View) {
        var data=HashMap<String,Any>()
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .document("First Thought")
                .get()
                .addOnSuccessListener {snapshot->
                    if (snapshot.exists()){
                        data= snapshot.data as HashMap<String, Any>
                        val title=data[KEY_TITLE].toString()
                        val thought=data[KET_THOUGHT].toString()
                        binding.etRecivedTitle.text=title
                        binding.etRecivedThought.text=thought
                        Log.i(TAG,"Data get data from Firestore successfully, title=$title,thought=$thought")
                    }else{
                        Log.i(TAG,"No data exist")
                    }
                      }
                .addOnFailureListener {
                    Log.i(TAG,"Cannot get data because ${it.localizedMessage}")
                }

    }


}