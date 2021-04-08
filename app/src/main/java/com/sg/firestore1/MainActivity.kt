package com.sg.firestore1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.firestore1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
   lateinit var thoughtRef:DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        thoughtRef=FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
        .document("First Thought")
    }

    override fun onStart() {
        super.onStart()
        thoughtRef.addSnapshotListener(this) { value, error ->
                    if (error != null) {
                        Toast.makeText(this, "Something go wrong", Toast.LENGTH_LONG).show()
                    }
                    if (value != null && value.exists()){
                        var title = value[KEY_TITLE].toString()
                        var thought = value[KET_THOUGHT].toString()
                        if (title=="null") title="no data"
                        if (thought=="null") thought="no data"
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
        thoughtRef.set(data)
                .addOnSuccessListener {
                    Log.i(TAG, "Data send to Firestore successfully")
                }
                .addOnFailureListener {
                    Log.i(TAG, "Cannot send data because ${it.localizedMessage}")
                }
    }

    fun showThoughtOnclick(view: View) {
        var data = HashMap<String, Any>()
        thoughtRef.get()
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

    fun updateThoughtOnclick(view: View) {
        val title=binding.editTextTitle.text.toString().trim()
        val thought=binding.editTextThought.text.toString().trim()
        var data=HashMap<String,Any>()
        data.put(KEY_TITLE,title)
        data.put(KET_THOUGHT,thought)
        thoughtRef.update(data)
                .addOnSuccessListener {
                    Log.i(TAG, "Data update successfully")
                }
                .addOnFailureListener {
                    Log.i(TAG, "Cannot update because ${it.localizedMessage}")
                }
    }

    fun deleteThoughtOnclick(view: View) {
        var data=HashMap<String,Any>()
        data.put(KEY_TITLE,FieldValue.delete())    // delete onle the title
        data.put(KET_THOUGHT,FieldValue.delete())  // delete only the thought


        thoughtRef.update(data)
                .addOnSuccessListener {
                    Log.i(TAG, "Data delete successfully")
                }
                .addOnFailureListener {
                    Log.i(TAG, "Cannot delete because ${it.localizedMessage}")
                }

      //   thoughtRef.delete()      //  delete all the ref
    }

}