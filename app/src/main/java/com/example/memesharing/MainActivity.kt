package com.example.memesharing

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sharememes.MySingleton

class MainActivity : AppCompatActivity() {
    var currentImageurl:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()

        var share:Button=findViewById(R.id.share)
        var next:Button=findViewById(R.id.next)


        share.setOnClickListener{
            var intent=Intent(Intent.ACTION_SEND)
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"checkout this cool meme ${currentImageurl}")
            var chooser=Intent.createChooser(intent,"Share this meme using.....")
            startActivity(chooser)
        }
        next.setOnClickListener{
            Log.d("in next","next clicked")
            loadMeme()
        }
    }



    private fun loadMeme(){
            var memeImage:ImageView=findViewById(R.id.memeImage)
            // Instantiate the RequestQueue.

            val queue = Volley.newRequestQueue(this)
            val url = "https://meme-api.herokuapp.com/gimme"
            //progress bar
            var progressBar:ProgressBar=findViewById(R.id.progressBar)
            progressBar.visibility= View.VISIBLE


            val JSONRequest = JsonObjectRequest(
                Request.Method.GET, url,null,
                { response ->
                    //fetching data from response

                    currentImageurl=response.getString("url")
                    Log.d("currentImageUrl",currentImageurl)
//                    currentImageurl=url
                    Log.d("SUCCESS","API called successfully")

                    //glide -image
                    Glide.with(this).load(currentImageurl).listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility=View.GONE
                            return false

                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility=View.GONE
                            return false
                        }
                    })
                        .into(memeImage)


                },
                {
                    //error
                    Log.d("ERROR","error occurred")
                })

            // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(JSONRequest)
        }

}