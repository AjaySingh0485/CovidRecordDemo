package com.example.covidrecord

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.example.covidrecord.api.ApiClient
import com.example.covidrecord.api.ApiInterface
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_layout.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var covidRecordAdapter: CovidRecordAdapter? = null
    private var selectedType: String? = null
    var recordlist: MutableList<CovidList>? = null
    private var skeleton: RecyclerViewSkeletonScreen? = null
    var recordlistDefault: MutableList<CovidList>? = null
    var item: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(this)

        getCovidRecordData()
    }

    private fun getCovidRecordData() {
        skeleton = Skeleton.bind(recyclerView)
            .adapter(covidRecordAdapter)
            .load(R.layout.skelton_item)
            .shimmer(true)
            .angle(30)
            .count(6)
            .show()
        val apiInterface =
            ApiClient.getRetrofitClientForAuth(this@MainActivity).create(ApiInterface::class.java)
        val observable = apiInterface.recordList
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MutableList<CovidList>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(covidList: MutableList<CovidList>) {
                    Log.e("TAG", "jsonObject1 : $covidList")
                    recordlist = covidList
                    recordlistDefault = covidList

                    skeleton?.hide()
                    recordlist?.reverse()
                    covidRecordAdapter = CovidRecordAdapter(applicationContext, recordlist)
                    recyclerView!!.adapter = covidRecordAdapter
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG@123", e.message.toString())
                    println(e.message)
                }

                override fun onComplete() {
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // item = menu!!.getItem(R.id.death)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.death -> {
                item.isChecked = true
                selectedType = "death"
            }
            R.id.confirmed -> {
                item.isChecked = true
                selectedType = "confirmed"
            }
            R.id.active -> {
                item.isChecked = true
                selectedType = "active"
            }
            R.id.recovered -> {
                item.isChecked = true
                selectedType = "recovered"
            }
            R.id.defaults -> {
                item.isChecked = true
                recordlist?.reverse()
                covidRecordAdapter = CovidRecordAdapter(applicationContext, recordlist)
                recyclerView!!.adapter = covidRecordAdapter
                covidRecordAdapter?.notifyDataSetChanged()
            }
            R.id.sort -> {
                sortAscenDesc("Ascending")
            }
            R.id.sortdesc -> {
                sortAscenDesc("Descending")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun sortAscenDesc(sortType: String) {
        if (sortType.equals("Ascending")) {
            recordlist?.sortWith(Comparator { lhs, rhs ->
                when (selectedType) {
                    "death" -> {
                        return@Comparator lhs.deaths.compareTo(rhs.deaths)
                    }
                    "confirmed" -> {
                        return@Comparator lhs.confirmed.compareTo(rhs.confirmed)
                    }
                    "active" -> {
                        return@Comparator lhs.active.compareTo(rhs.active)
                    }
                    "recovered" -> {
                        return@Comparator lhs.recovered.compareTo(rhs.recovered)
                    }
                    else -> return@Comparator lhs.date.compareTo(rhs.date)
                }
            })
            covidRecordAdapter?.notifyDataSetChanged()
        } else {
            recordlist?.sortWith(Comparator { lhs, rhs ->
                when (selectedType) {
                    "death" -> {
                        return@Comparator rhs.deaths.compareTo(lhs.deaths)
                    }
                    "confirmed" -> {
                        return@Comparator rhs.confirmed.compareTo(lhs.confirmed)
                    }
                    "active" -> {
                        return@Comparator rhs.active.compareTo(lhs.active)
                    }
                    "recovered" -> {
                        return@Comparator rhs.recovered.compareTo(lhs.recovered)
                    }
                    else -> return@Comparator rhs.date.compareTo(lhs.date)
                }
            })
            covidRecordAdapter?.notifyDataSetChanged()
        }
    }
}