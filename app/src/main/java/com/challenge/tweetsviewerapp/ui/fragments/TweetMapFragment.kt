package com.challenge.tweetsviewerapp.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.baseclasses.BaseFragment
import com.challenge.tweetsviewerapp.constants.Constants
import com.challenge.tweetsviewerapp.databinding.FragmentTweetMapBinding
import com.challenge.tweetsviewerapp.interfaces.IItemClickListener
import com.challenge.tweetsviewerapp.repo.SharedPreference
import com.challenge.tweetsviewerapp.ui.activities.HomeActivity
import com.challenge.tweetsviewerapp.ui.activities.ImagesActivity
import com.challenge.tweetsviewerapp.ui.activities.VideoActivity
import com.challenge.tweetsviewerapp.ui.adapters.MarkerAdapter
import com.challenge.tweetsviewerapp.utils.*
import com.challenge.tweetsviewerapp.viewModel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.twitter.sdk.android.core.models.Tweet

class TweetMapFragment : BaseFragment<FragmentTweetMapBinding, MainViewModel>(),
    OnMapReadyCallback, IItemClickListener {

    private lateinit var latLng: LatLng
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var tweetList: List<Tweet>
    private var location: Location? = null
    private val sharedPref by lazy { SharedPreference(activity!!) }

    private val PERMISSION_FILE_LOCATION by lazy { 101 }

    override fun getLayoutId(): Int = R.layout.fragment_tweet_map

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java


    lateinit var actionBar: ActionBar

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

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap!!
        setMarkerOnMap()
        setUpSeekBar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!checkSelfPermissions()) {
            requestPermission()
        } else {
            setupUI()
        }
    }

    override fun onResume() {
        super.onResume()
        setUpActionBar()
    }

    private fun setUpActionBar() {
        actionBar.title = getString(R.string.tweets_on_maps)
        actionBar.setHomeButtonEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(false)
    }


    private fun setMarkerOnMap() {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        mGoogleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                getZoomLevel(sharedPref.radius.toDouble() * 1000)
            )
        )

        mGoogleMap.isMyLocationEnabled = true
        mainViewModel?.getTweetsByLocation(
            latLng,
            sharedPref.radius
        )
    }

    private fun setupUI() {
        getCurrentLocation()
        initMap()
        observeChanges()
    }

    private fun observeChanges() {
        mainViewModel?.tweetsList?.observe(this, Observer {
            tweetList = it
            displayTweets(it)
        })
    }

    private fun displayTweets(list: List<Tweet>) {
        mGoogleMap.clear()
        val adapter = MarkerAdapter(activity!!, this)
        mGoogleMap.setInfoWindowAdapter(adapter)
        mGoogleMap.setOnInfoWindowClickListener(adapter)
        list
            .forEach {
                val text = MarkerAdapter.encodedTweet(it)
                val latLng = LatLng(it.coordinates.latitude, it.coordinates.longitude)
                mGoogleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(it.user.name)
                        .snippet(text)
                )
            }
    }

    private fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_map, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (R.id.action_search_btn == item.itemId) {
            val action = TweetMapFragmentDirections.actionTweetMapFragmentToTweetsFragment()
            findNavController().navigate(action)
            true
        } else
            super.onOptionsItemSelected(item)
    }


    private fun setUpSeekBar() {

        val currentLocation = location?.let { LatLng(location!!.latitude, location!!.longitude) }
            ?: LatLng(45.5017, 73.5673)

        val circle: Circle = mGoogleMap.addCircle(
            CircleOptions()
                .center(currentLocation)
                .radius(sharedPref.radius.toDouble() * 1000) // Converting KM into Meters...
                .strokeColor(activity?.getColor(R.color.colorAccent)!!)
                .strokeWidth(2f)
        )
        fragmentBinding?.seekBarDistance?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                circle.radius = progress.toDouble() + SharedPreference.MIN_RADIUS * 1000
                mGoogleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLocation,
                        getZoomLevel(circle)
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                circle.isVisible = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {


                mainViewModel?.getTweetsByLocation(latLng, (circle.radius / 1000).toInt())
                sharedPref.radius = seekBar.progress + SharedPreference.MIN_RADIUS.toInt()

            }
        })
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {

        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        latLng = LatLng(location!!.latitude, location!!.longitude)
    }

    private fun checkSelfPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun requestPermission() {
        requestPermissions(

            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_FILE_LOCATION
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_FILE_LOCATION -> {

                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (!locationAccepted) {
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        showMessageOKCancel(activity!!, DialogInterface.OnClickListener { _, _ ->
                            requestPermissions(
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                PERMISSION_FILE_LOCATION
                            )
                        })


                    }
                } else {
                    setupUI()
                }
            }
        }
    }

    override fun onItemClick(item: Any, type: Any) {

        val tweet = getTweetFromId(item as Long)
        when (type) {
            Constants.VIDEO -> {
                val pair = tweet?.getVideoUrlType()
                val intent = Intent(activity, VideoActivity::class.java)
                intent.putExtra(VideoActivity.VIDEO_URL_EXTRA, pair?.first)
                intent.putExtra(VideoActivity.VIDEO_TYPE_EXTRA, pair?.second)
                startActivity(intent)
            }
            Constants.PHOTO -> {
                val images = arrayListOf(tweet?.getImageUrl()!!)
                val intent = Intent(activity, ImagesActivity::class.java)
                intent.putStringArrayListExtra(ImagesActivity.IMAGE_URLS_EXTRA, images)
                startActivity(intent)
            }
            Constants.MULTIPLE_PHOTOS -> {
                val images = getUrlList(tweet?.extendedEntities?.media!!) as ArrayList
                val intent = Intent(activity, ImagesActivity::class.java)
                intent.putStringArrayListExtra(ImagesActivity.IMAGE_URLS_EXTRA, images)
                startActivity(intent)

            }
            else -> {
                openTweet(activity!!, tweet!!)
            }
        }
    }

    private fun getTweetFromId(id: Long): Tweet? {

        var tweet: Tweet? = null
        for (item in tweetList) {
            if (item.getId() == id) {
                tweet = item
            }
        }
        return tweet
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