package edu.uoc.pac2

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.AsyncTask
import com.google.android.gms.ads.MobileAds
import edu.uoc.pac2.data.BooksInteractor
import edu.uoc.pac2.data.FirestoreBookData

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    private lateinit var booksInteractor: BooksInteractor

    override fun onCreate() {
        super.onCreate()
        //Add books in Firestore
        FirestoreBookData.addBooksDataToFirestoreDatabase()
        // TODO: Init Room Database
//        Room.databaseBuilder(this, ApplicationDatabase::class.java, "book").build()
        // TODO: Init BooksInteractor
//        getBooksInteractor()
        MobileAds.initialize(this)

    }

    fun getBooksInteractor(): BooksInteractor {
        return booksInteractor
    }

    fun hasInternetConnection(context: Context): Boolean {
        // TODO: Add Internet Check logic.
        var isNetworkConnected = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        AsyncTask.execute {
            // Background code
            cm?.registerNetworkCallback(
                    builder.build(),
                    object : ConnectivityManager.NetworkCallback() {

                        override fun onAvailable(network: Network) {
                            isNetworkConnected = true
                        }

                        override fun onLost(network: Network) {
                            isNetworkConnected = false
                        }
                    })
        }

        // getActiveNetworkInfo()  --DEPRECATED
//        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//        return activeNetwork?.isConnectedOrConnecting == true

        return isNetworkConnected
    }
}