package com.challenge.tweetsviewerapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.baseclasses.BaseFragment
import com.challenge.tweetsviewerapp.constants.Constants
import com.challenge.tweetsviewerapp.databinding.FragmentTweetsBinding
import com.challenge.tweetsviewerapp.interfaces.IItemClickListener
import com.challenge.tweetsviewerapp.repo.SharedPreference
import com.challenge.tweetsviewerapp.ui.activities.HomeActivity
import com.challenge.tweetsviewerapp.ui.activities.ImagesActivity
import com.challenge.tweetsviewerapp.ui.activities.VideoActivity
import com.challenge.tweetsviewerapp.ui.adapters.SearchAdapter
import com.challenge.tweetsviewerapp.utils.getVideoUrlType
import com.challenge.tweetsviewerapp.utils.openTweet
import com.challenge.tweetsviewerapp.viewModel.MainViewModel
import com.google.android.gms.maps.model.LatLng
import com.twitter.sdk.android.core.models.Tweet
import kotlin.collections.ArrayList

class TweetsFragment : BaseFragment<FragmentTweetsBinding, MainViewModel>(), IItemClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_tweets

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    private val tweetList = ArrayList<Tweet>()
    lateinit var adapter: SearchAdapter
    private lateinit var latLng: LatLng

    lateinit var actionBar: ActionBar

    private val sharedPref by lazy { SharedPreference(activity!!) }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionBar = (activity as HomeActivity).supportActionBar!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return fragmentBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        observeData("#music")
    }

    override fun onResume() {
        super.onResume()
        setUpActionBar()
    }

    private fun setUpActionBar() {
        actionBar.title = getString(R.string.all_tweets)
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        fragmentBinding?.rvTweets?.visibility = View.VISIBLE
        fragmentBinding?.rvTweets?.layoutManager = layoutManager

        fragmentBinding?.rvTweets?.addItemDecoration(
            DividerItemDecoration(
                activity,
                RecyclerView.VERTICAL
            )
        )
        adapter = SearchAdapter(tweetList, this)
        fragmentBinding?.rvTweets?.adapter = adapter
        fragmentBinding?.rvTweets?.setHasFixedSize(true)
    }


    private fun observeData(query: String) {
        getCurrentLocation()
        mainViewModel?.searchTweets(query, latLng, sharedPref.radius)
        mainViewModel?.searchTweetsList?.observe(this, Observer {
            tweetList.clear()
            tweetList.addAll(it)
            adapter.notifyDataSetChanged()
        })
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        latLng = LatLng(location!!.latitude, location.longitude)
    }


    override fun onItemClick(item: Any, type: Any) {

        when (type) {
            Constants.VIDEO -> {
                val tweet = item as Tweet
                val pair = tweet.getVideoUrlType()
                val intent = Intent(activity, VideoActivity::class.java)
                intent.putExtra(VideoActivity.VIDEO_URL_EXTRA, pair.first)
                intent.putExtra(VideoActivity.VIDEO_TYPE_EXTRA, pair.second)
                startActivity(intent)
            }
            Constants.PHOTO, Constants.MULTIPLE_PHOTOS -> {
                val images = item as ArrayList<String>
                val intent = Intent(activity, ImagesActivity::class.java)
                intent.putStringArrayListExtra(ImagesActivity.IMAGE_URLS_EXTRA, images)
                startActivity(intent)

            }
            else -> {
                val tweet = item as Tweet
                openTweet(activity!!, tweet)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)?.actionView as SearchView

        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel?.searchTweets(query, latLng, sharedPref.radius)
                actionBar.title = query
                searchItem.onActionViewCollapsed()
                return true
            }

            override fun onQueryTextChange(s: String) = false
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun favorite(tweet: Tweet) {
        mainViewModel?.favroties(tweet)
    }

    override fun unfavorite(tweet: Tweet) {
        mainViewModel?.unfavroties(tweet)
    }

    override fun retweet(tweet: Tweet) {
        mainViewModel?.retweet(tweet)
    }

    override fun unretweet(tweet: Tweet) {
        mainViewModel?.unretweet(tweet)
    }

}
