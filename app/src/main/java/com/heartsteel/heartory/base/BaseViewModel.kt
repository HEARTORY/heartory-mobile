package com.example.healthcarecomp.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

}