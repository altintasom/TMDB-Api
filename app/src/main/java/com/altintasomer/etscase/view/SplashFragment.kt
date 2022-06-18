package com.altintasomer.etscase.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.altintasomer.etscase.R
import com.altintasomer.etscase.databinding.FragmentSplashBinding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

private const val TAG = "SplashFragment"
@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var binding : FragmentSplashBinding
    private var actionBar: ActionBar? = null
    private var job : Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

      job =  CoroutineScope(Dispatchers.Main+exceptionHandler).launch {
          delay(3000)
          if (isConnected())
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment2)
        }
        binding.title = firebaseRemoteConfig.getString("splash_title")
        if (isConnected()){
            Toast.makeText(requireContext(),"Connected",Toast.LENGTH_LONG).show()
        }
        else
            Toast.makeText(requireContext(),"Disconnected",Toast.LENGTH_LONG).show()
    }

    private fun init(view: View) {

        binding = FragmentSplashBinding.bind(view)
        actionBar = (requireActivity() as AppCompatActivity).supportActionBar

    }

    private fun isConnected() : Boolean{
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            networkCapabilities?.let {
                if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                    return true
                else if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true
                else if (it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                    return true
            }

        }else{
                try {
                    val networkInfo = connectivityManager.activeNetworkInfo
                    networkInfo?.let {
                        if (it.isConnected){
                            return true
                        }
                    }
                }catch ( e: Exception){
                    e.printStackTrace()
                    return false
                }
        }
        return false
    }
    

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    override fun onResume() {
        super.onResume()
        actionBar?.hide()

    }

    override fun onStop() {
        super.onStop()
        actionBar?.show()
    }


}