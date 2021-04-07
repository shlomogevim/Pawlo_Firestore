package com.sg.firestore1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.text.set
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.firestore1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
   lateinit var refThought:DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        refThought=FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
        .document("First Thought")
    }

    override fun onStart() {
        super.onStart()
        refThought.addSnapshotListener(this) { value, error ->
                    if (error != null) {
                        Toast.makeText(this, "Something go wrong", Toast.LENGTH_LONG).show()
                    }
                    if (value != null && value.exists()){
                        val title = value[KEY_TITLE].toString()
                        val thought = value[KET_THOUGHT].toString()
                        binding.etRecivedTitle.text = title
                        binding.etRecivedThought.text = thought
                    }
                }
    }


    fun saveThoughtOnclick(view: View) {
        val title = binding.editTextTitle.text.toString().trim()
        val thought = binding.editTextThought.text.toString().trim()
        val data = HashMap<String, Any>()
        data.put(KEY_TITLE, title)
        data.put(KET_THOUGHT, thought)
        refThought.set(data)
                .addOnSuccessListener {
                    Log.i(TAG, "Data send to Firestore successfully")
                }
                .addOnFailureListener {
                    Log.i(TAG, "Cannot send data because ${it.localizedMessage}")
                }
    }

    fun showThoughtOnclick(view: View) {
        var data = HashMap<String, Any>()
        refThought.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        data = snapshot.data as HashMap<String, Any>
                        val title = data[KEY_TITLE].toString()
                        val thought = data[KET_THOUGHT].toString()
                        binding.etRecivedTitle.text = title
                        binding.etRecivedThought.text = thought
                        Log.i(TAG, "Data get data from Firestore successfully, title=$title,thought=$thought")
                    } else {
                        Log.i(TAG, "No data exist")
                    }
                }
                .addOnFailureListener {
                    Log.i(TAG, "Cannot get data because ${it.localizedMessage}")
                }

    }


}