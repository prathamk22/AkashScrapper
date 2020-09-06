package com.example.akashscrapper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_generic_bottom_sheet.*

class GenericBottomSheet(
    val listener: GenericOnClickListener
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generic_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setList(list: Array<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            listener.onClick(i, list[i])
            dismiss()
        }
    }
}

interface GenericOnClickListener {
    fun onClick(position: Int, value: String)
}