package com.billarapp.partida.verPartidas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.billarapp.databinding.FragmentObjectBinding


class ObjectFragment : Fragment() {


    private var binding: FragmentObjectBinding? = null
    //private val binding get() = _binding!!


    companion object {
        private const val ARG_OBJECT = "object"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentObjectBinding.inflate(layoutInflater)
        return binding?.root

        //return inflater.inflate(R.layout.fragment_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {

            binding?.txVer?.text = "Fragmento"+getInt(ARG_OBJECT).toString()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}