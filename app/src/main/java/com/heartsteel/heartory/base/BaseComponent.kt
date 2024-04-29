package com.example.healthcarecomp.base

import android.content.Context
import android.view.View

abstract class BaseComponent constructor(protected val context: Context) {
    abstract fun createView(): View
    abstract fun idComponent(): String
}