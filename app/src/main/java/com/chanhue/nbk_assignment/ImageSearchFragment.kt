package com.chanhue.nbk_assignment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImageSearchFragment : Fragment() {

    private val viewModel: ImageViewModel by activityViewModels {
        ImageViewModel.Factory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val searchButton = view.findViewById<Button>(R.id.searchButton)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val adapter = ImageListAdapter(
            onHeartClick = { image ->
                viewModel.toggleHeart(image)
            }
        )

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter


        //검색어 설정
        viewModel.query.observe(viewLifecycleOwner) { query ->
            searchEditText.setText(query)
        }

        viewModel.images.observe(viewLifecycleOwner) { images ->
            adapter.submitList(images)
        }

        searchButton.setOnClickListener {
            viewModel.query.value = searchEditText.text.toString()
            viewModel.searchImages()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}
