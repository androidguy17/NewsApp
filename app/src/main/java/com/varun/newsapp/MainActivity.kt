package com.varun.newsapp

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import com.varun.newsapp.model.ApiInterface
import com.varun.newsapp.model.Source
import com.varun.newsapp.model.data
import com.varun.newsapp.model.news
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error.*
import kotlinx.android.synthetic.main.error.view.*
import kotlinx.android.synthetic.main.token.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.lang.Exception


class MainActivity : AppCompatActivity(){

    val adapter = GroupAdapter<GroupieViewHolder>()
    val country:String= "in"
    val apikey :String = "92ab1ced93924a25981488bd9ea69d4d"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe.setOnRefreshListener{
            fetch()

        }


      //dummy()

        recylerview.adapter = adapter
        adapter.add(Headings())


        adapter.setOnItemClickListener{item, view ->
            var data = item as NewsCards

            var intent = Intent(this,webView::class.java)
            intent.putExtra("URL",data.weburl)
            startActivity(intent)

        }


        fetch()

    }


    fun fetch(){

        progressBar.isVisible=false
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        val call:Call<news> = apiInterface.getnews(country,apikey)
        call.enqueue(object : Callback<news>{
            override fun onFailure(call: Call<news>, t: Throwable) {
              swipe.isRefreshing = false
                progressBar.isVisible=false
                swipe.isVisible=false


                error.isVisible = true

                Toast.makeText(applicationContext,"something went wrong",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<news>, response: Response<news>) {

                swipe.isVisible=true

                progressBar.isVisible=false
                swipe.isRefreshing=true
                error.isVisible=false
                Toast.makeText(this@MainActivity,"updated",Toast.LENGTH_LONG).show()
                val news: news = response.body()!!


                    adapter.clear()
                adapter.add(Headings())

                for(i in news.articles){



                    var name = i.source?.name
                    var author = i.author
                    var title = i.title
                    var des = i.description
                    var pictureurl = i.urlToImage

                    var content = i.content
                    var date =i.publishedAt
                    var url = i.url


                    adapter.add(NewsCards(data(Source(name) ,author,title,des,url,pictureurl,content,date)))


                }

                swipe.isRefreshing=false


            }


        })
    }






    inner class NewsCards(val input:data):Item<GroupieViewHolder>(){


        var weburl:String ?=null
        fun splitDate( time:String?):String{
            var date: kotlin.Array<String> = time?.split('T')!!.toTypedArray()

            return date[0]

        }

        fun splitTime(time: String?):String{
            var date =time?.split('T','Z')!!.toTypedArray()

            return date[1]


        }

        override fun getLayout(): Int {
            return R.layout.token


        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {






            var p :ProgressBar = viewHolder.itemView.findViewById(R.id.progress_load_photo) as ProgressBar

            var Date = splitDate(input.publishedAt)
            var time = splitTime(input.publishedAt)




            viewHolder.itemView.title.text = input.title
            viewHolder.itemView.desc.text = input.description
            viewHolder.itemView.source.text = input.source?.name
            viewHolder.itemView.author.text=input.author
            weburl = input.url

            Picasso.get().load(input.urlToImage)
                .into(viewHolder.itemView.img, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {

                           p.visibility = View.GONE

                    }

                    override fun onError(e: Exception?) {

                    }

                })



            viewHolder.itemView.publishedAt.text = Date
            viewHolder.itemView.time.text=time
        }


    }

    class Headings:Item<GroupieViewHolder>(){
        override fun getLayout(): Int {
            return R.layout.heading
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        }


    }

    fun reload(view:View)
    {

        fetch()

    }

}
