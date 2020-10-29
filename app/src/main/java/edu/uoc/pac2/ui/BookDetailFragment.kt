package edu.uoc.pac2.ui

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import edu.uoc.pac2.R
import edu.uoc.pac2.data.ApplicationDatabase
import edu.uoc.pac2.data.Book
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.fragment_book_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

/**
 * A fragment representing a single Book detail screen.
 * This fragment is contained in a [BookDetailActivity].
 */
class BookDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get Book for this detail screen
        loadBook(view.context)
    }


    // TODO: Get Book for the given {@param ARG_ITEM_ID} Book id
    private fun loadBook(context: Context) {
        val db = Room.databaseBuilder(context, ApplicationDatabase::class.java, "book").build()

        arguments?.let {
            val uid = it.getInt(ARG_ITEM_ID)
            var book: Book? = null

            AsyncTask.execute {
                book = db.bookDao().getBookById(uid)
            }

            Handler(Looper.getMainLooper()).post {
                //code that runs in main
                initUI(book)
            }
        }

    }

    // TODO: Init UI with book details
    private fun initUI(book: Book?) {
        activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = book?.title//Title toolbar
        activity?.findViewById<TextView>(R.id.book_author)?.text = book?.author//Author Book
        activity?.findViewById<TextView>(R.id.book_date)?.text = book?.publicationDate//Publication Date Book
        activity?.findViewById<TextView>(R.id.book_detail)?.text = book?.description//Description Book
        Picasso.get().load(book?.urlImage).into(activity?.findViewById(R.id.book_image))
    }

    // TODO: Share Book Title and Image URL
    private fun shareContent(book: Book) {
        throw NotImplementedError()
    }

    companion object {
        /**
         * The fragment argument representing the item title that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "itemIdKey"

        fun newInstance(itemId: Int): BookDetailFragment {
            val fragment = BookDetailFragment()
            val arguments = Bundle()
            arguments.putInt(ARG_ITEM_ID, itemId)
            fragment.arguments = arguments
            return fragment
        }
    }
}