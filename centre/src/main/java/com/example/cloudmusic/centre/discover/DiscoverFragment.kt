package com.example.cloudmusic.centre.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cloudmusic.centre.databinding.FragmentDiscoverBinding
import com.example.cloudmusic.centre.discover.DiscoverViewModel

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val discoverViewModel =
            ViewModelProvider(this)[DiscoverViewModel::class.java]

        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDiscover
        discoverViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}