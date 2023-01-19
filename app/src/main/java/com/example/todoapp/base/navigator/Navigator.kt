package com.example.todoapp.base.navigator

import androidx.fragment.app.Fragment

interface Navigator {

    fun launch(f: Fragment)

    fun goBack()

}