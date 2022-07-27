package com.altintasomer.tmdbapi.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.altintasomer.tmdbapi.R
import com.altintasomer.tmdbapi.databinding.FragmentSplashBinding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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
        if (!isConnected()){
            alertShow()
        }

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

    fun alertShow(){
        val builder = AlertDialog.Builder(requireContext(),R.style.AlertDialogTheme)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.layout_warning_dialog, requireActivity().findViewById<ConstraintLayout>(R.id.layoutDialogContainer))
        builder.setView(view)
        val alertDialog = builder.create()
        view.findViewById<TextView>(R.id.btnNo).setOnClickListener {
            alertDialog.dismiss()
            requireActivity().finishAndRemoveTask()
        }

        view.findViewById<TextView>(R.id.btnOk).setOnClickListener {
            if (isConnected()){
                alertDialog.dismiss()
                job?.cancel()
                job =  CoroutineScope(Dispatchers.Main+exceptionHandler).launch {
                    delay(3000)
                        findNavController().navigate(R.id.action_splashFragment_to_mainFragment2)
                }
            }else {
                alertDialog.dismiss()
                alertShow()
            }
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)
    }

}