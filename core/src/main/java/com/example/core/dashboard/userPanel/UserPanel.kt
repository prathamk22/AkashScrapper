package com.example.core.dashboard.userPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.core.dashboard.DashboardViewModel
import com.example.core.R
import com.example.core.utils.observer
import com.example.core.utils.setRv
import kotlinx.android.synthetic.main.fragment_user_panel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserPanel : Fragment() {

    val downloadAdapter = UserBaseAdapter()
    val wishlistAdapter = UserBaseAdapter()
    val vm: DashboardViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        downloadRecyclerView.setRv(
            requireActivity(),
            downloadAdapter
        )

        wishlistRecyclerView.setRv(
            requireActivity(),
            wishlistAdapter
        )

        vm.getDownloadList().observer(viewLifecycleOwner) {
            downloadAdapter.submitList(it)
        }

        vm.getWishlisted().observer(viewLifecycleOwner) {
            wishlistAdapter.submitList(it)
        }
    }
}