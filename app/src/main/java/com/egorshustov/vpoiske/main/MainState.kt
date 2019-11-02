package com.egorshustov.vpoiske.main

import androidx.annotation.StyleRes
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseState

class MainState: BaseState() {
    var currentTheme = AppTheme.DARK_THEME
}

enum class AppTheme(@StyleRes val themeId: Int) {
    LIGHT_THEME(R.style.AppTheme_OverlapSystemBar_Light),
    DARK_THEME(R.style.AppTheme_OverlapSystemBar_Dark);

    fun getNext(): AppTheme {
        return values()[((ordinal + 1) % values().size)]
    }
}