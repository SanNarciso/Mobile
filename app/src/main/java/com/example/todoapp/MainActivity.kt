package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.todoapp.base.navigator.Navigator
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.views.tasks.TasksFragment


class MainActivity : AppCompatActivity(), Navigator {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            launchFragment(
                f = TasksFragment.newInstance(),
                addToBackStack = false
            )
        }
    }

    override fun launch(f: Fragment) {
        launchFragment(f)
    }

    override fun goBack() {
        onBackPressed()
    }

    private fun launchFragment(f: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .replace(R.id.fragment_holder, f)
            .commit()
    }

}