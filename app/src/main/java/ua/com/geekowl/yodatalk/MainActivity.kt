package ua.com.geekowl.yodatalk

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.NetworkInfo
import android.widget.ShareActionProvider
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    val LOG_TAG = "YODA TALK APP"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // adding base and header to request
        FuelManager.instance.basePath = "https://yoda.p.mashape.com/yoda?sentence="
        FuelManager.instance.baseHeaders = mapOf(
                "X-Mashape-Key" to getString(R.string.YODA_SPEACH_TOKEN),
                "Accept" to "text/plain")
        sendButton.setOnClickListener {
            if (!isConnected()) {
                Toast.makeText(applicationContext, R.string.network_unavailable,
                        Toast.LENGTH_LONG).show()
            } else {
                val userSentence = sentenceTextView.text
                if (!inputIsValid("$userSentence")) {
                    Toast.makeText(applicationContext, R.string.wrong_input,
                            Toast.LENGTH_LONG).show()
                } else {
                    Fuel.get("$userSentence").responseString { request, response, result ->
                        result.fold({ serverData ->
                            generatedSentence.text = serverData
                            // Log.v(LOG_TAG, serverData.toString())
                        }, { err ->
                            Toast.makeText(applicationContext,  "Response with error ${err.message}",
                                    Toast.LENGTH_LONG).show()
                            Log.e(LOG_TAG, err.message)
                        })
                    }
                }
            }

        }
    }
    // Checking network access
    private fun isConnected(): Boolean {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if(activeNetwork != null){
            return activeNetwork.isConnectedOrConnecting
        } else {
            return false
        }
    }
    private fun inputIsValid(text: String): Boolean{
        val regEx  = Regex("^[a-zA-Z ]+$")
        if(regEx.matchEntire(text) ==  null){ return false; }
        Log.d(LOG_TAG,text.split("\\s+").size.toString())// TODO check functionality
        return true;
    }
}
