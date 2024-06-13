package com.ossipeeriver.ravenandroidawarenesskit.ui.location

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ossipeeriver.ravenandroidawarenesskit.R

class AddNewLocationActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.add_new_location_activity)
        val editSavedLocationDescription = findViewById<EditText>(R.id.edit_description)

        val saveButton = findViewById<Button>(R.id.button_save_this_location)
        saveButton.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editSavedLocationDescription.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val description = editSavedLocationDescription.text.toString()
                Log.d("AddNewLocationActivity", "Setting result with description: $description")
                replyIntent.putExtra(EXTRA_REPLY, description)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.ossipeeriver.RavenAndroidAwarenessKit.locationlistsql.REPLY"
    }
}