package com.judge.app.fragments.topic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.judge.R

/**
 * @author: jaffa
 * @date: 2019/8/22
 * 发帖模块
 */
class SendTopicFragment : Fragment() {
    lateinit var sign: TextView
    lateinit var post: TextView
    lateinit var quit: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sendtopic, container, false).apply {
            sign = findViewById(R.id.tv_sign)
            post = findViewById(R.id.tv_post)
            quit = findViewById(R.id.btn_quit)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        post.setOnClickListener {
            findNavController().navigate(R.id.action_sendTopicFragment_to_postFragment)
        }
        sign.setOnClickListener {
            findNavController().navigate(R.id.action_sendTopicFragment_to_signFragment)
        }
        quit.setOnClickListener { activity?.finish() }
    }
}