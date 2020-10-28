package edu.uoc.pac2.ui

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BookDao
import edu.uoc.pac2.data.BooksInteractor
import edu.uoc.pac2.data.ApplicationDatabase as ApplicationDatabase


/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"

    private lateinit var adapter: BooksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        // Init UI
        initToolbar()
        initRecyclerView()

        // Get Books
        getBooks()

        // TODO: Add books data to Firestore [Use once for new projects with empty Firestore Database]
    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    // TODO: Get Books and Update UI
    private fun getBooks() {
        loadBooksFromLocalDb()//offline db

        if (MyApplication().hasInternetConnection(this)) {
            val db = FirebaseFirestore.getInstance()
            db.collection("books").addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val books: List<Book> = value!!.mapNotNull { it.toObject(Book::class.java) }

                AsyncTask.execute {
                    // Background code
                    saveBooksToLocalDatabase(books)
                }

                runOnUiThread {
                    // Main code
                    adapter.setBooks(books)//Updating RecyclerView
                }

            }
        }


    }

    // TODO: Load Books from Room
    private fun loadBooksFromLocalDb() {
        val db = Room.databaseBuilder(this, ApplicationDatabase::class.java, "book").build()

        AsyncTask.execute{
            adapter.setBooks(BooksInteractor(db.bookDao()).getAllBooks())
        }

    }

    // TODO: Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        val db = Room.databaseBuilder(this, ApplicationDatabase::class.java, "book").build()
        BooksInteractor(db.bookDao()).saveBooks(books)
    }
}