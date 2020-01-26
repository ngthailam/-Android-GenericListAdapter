package com.example.genericlistadapter.utils.animation

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import com.example.genericlistadapter.R

abstract class EnterAnimType(@AnimatorRes @AnimRes val animRes: Int)

class DefaultEnterAnimType : EnterAnimType(R.anim.gla_slide_up_fade_in)
