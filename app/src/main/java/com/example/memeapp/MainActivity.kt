package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
        save.setOnClickListener{
            saveMeme()
        }
    }
    @Suppress("NAME_SHADOWING")
    private fun loadMeme(){
        // Instantiate the RequestQueue.
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentURL = response.getString("url")
                Glide.with(this).load(currentURL).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(memeImageView)
            },
            {
            }
        )
        mySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {
        if(progressBar.visibility==View.GONE) {
            val bitmapDrawable = memeImageView!!.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val bitmapPath =
                MediaStore.Images.Media.insertImage(contentResolver, bitmap, "meme_image", null)
            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            val chooser = Intent.createChooser(intent, "Share Image")
            startActivity(chooser)
        }
        else{
            Toast.makeText(this,"Please wait for the image to be loaded,then try again",Toast.LENGTH_LONG).show()
        }
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
    private fun saveMeme(){
        if(progressBar.visibility==View.GONE) {
            val bitmapDrawable = memeImageView!!.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val bitmapPath =
                MediaStore.Images.Media.insertImage(contentResolver, bitmap, "meme_image", null)
            val bitmapUri = Uri.parse(bitmapPath)
            Toast.makeText(this,"Save Successful",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this,"Please wait for the image to be loaded,then try again",Toast.LENGTH_LONG).show()
        }
    }
}