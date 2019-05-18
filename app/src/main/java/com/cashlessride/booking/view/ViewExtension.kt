package com.cashlessride.booking.view

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Created on 5/18/2019.
 */

val TextInputEditText.inputLayout: TextInputLayout?
    get() = parent.parent as? TextInputLayout