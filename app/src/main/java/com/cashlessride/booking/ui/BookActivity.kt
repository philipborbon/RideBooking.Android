package com.cashlessride.booking.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.cashlessride.booking.R
import com.cashlessride.booking.data.*
import com.cashlessride.booking.util.Util
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.layout_book_item.view.*
import kotlinx.android.synthetic.main.layout_type_count.view.*

class BookActivity : BaseActivity() {
    companion object {
        const val DATA_SCHEDULE = "data-schedule"
    }

    private var rideSchedule: RideSchedule? = null
    private var typeList: ArrayList<PassengerType>? = null

    private var inputs: HashMap<Route, HashMap<PassengerType, TextInputEditText>> = HashMap()

    private val countChangeListener = object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            display_amount.text = Util.formatAmount(getTotal())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        rideSchedule = intent.getParcelableExtra(DATA_SCHEDULE)

        button_book.setOnClickListener {
            validateBooking()
        }

        getPassengerTypes()
    }

    private fun getPassengerTypes(){
        view_loading.visibility = View.VISIBLE

        apiManager.getPassengerTypes { response ->
            main.post { view_loading.visibility = View.GONE }

            if (response.success == true) {
                typeList = response.data

                main.post { layoutForm() }
            } else {
                main.post { showToast(response.getErrorMessage()) }
            }
        }
    }

    private fun layoutForm(){
        inputs.clear()
        container_booking.removeAllViews()

        rideSchedule?.routes?.let {
            val rideSchedules = ArrayList(it.toMutableList())
            val mainScheduleRoute = RideSchedule.getMainScheduleRoute(rideSchedules)

            mainScheduleRoute?.let {
                rideSchedules.remove(mainScheduleRoute)
            }

            typeList?.let {
                container_booking.addView(createRouteItem(mainScheduleRoute?.route, typeList))

                for (rideSchedule in rideSchedules){
                    container_booking.addView(createRouteItem(rideSchedule.route, typeList))
                }
            }
        }
    }

    private fun createRouteItem(route: Route?, types: ArrayList<PassengerType>?): View {
        val typeInputs = HashMap<PassengerType, TextInputEditText>()
        val formItem = LayoutInflater.from(this).inflate(R.layout.layout_book_item, null)

        route?.let { route ->
            formItem.display_route.text = route.description
            formItem.display_fare.text = Util.formatAmount(route.regularfare)

            types?.let {
                for (type in it) {
                    val typeInputView = createTypeItem(route, type)
                    formItem.container_type.addView(typeInputView)

                    typeInputs[type] = typeInputView.input_count
                }
            }

            inputs[route] = typeInputs
        }

        return formItem
    }

    private fun createTypeItem(route: Route, type: PassengerType): View {
        val discount = ((type.discount ?: 0).toDouble() / 100) * (route.regularfare ?: 0.0)

        val formItem = LayoutInflater.from(this).inflate(R.layout.layout_type_count, null)
        formItem.display_type.text = type.name
        formItem.display_discount.text = Util.formatAmount(discount)
        formItem.input_count.addTextChangedListener(countChangeListener)

        return formItem
    }

    private fun createBookingFormData(): BookingForm {
        val bookingForm = BookingForm()
        bookingForm.ridescheduleid = rideSchedule?.id

        val seats = arrayListOf<BookingSeatForm>()

        for ((route, typesInput) in inputs) {
            for ((type, input) in typesInput) {
                if (input.text.toString().isNotBlank()) {
                    if (input.text.toString().toInt() != 0) {
                        val bookingSeatForm = BookingSeatForm()
                        bookingSeatForm.routeid = route.id
                        bookingSeatForm.typeid = type.id
                        bookingSeatForm.count = input.text.toString().toInt()

                        seats.add(bookingSeatForm)
                    }
                }
            }
        }

        bookingForm.seats = seats

        return bookingForm
    }

    @SuppressLint("InlinedApi")
    private fun createBooking(){
        view_loading.visibility = View.VISIBLE

        val bookingForm = createBookingFormData()

        apiManager.createBooking(bookingForm) {
            main.post { view_loading.visibility = View.GONE }

            if (it.success == true) {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Booking Success")
                dialog.setMessage(HtmlCompat.fromHtml("Before boarding your ride, please present your booking code to the dispatcher for confirmation.<br><br>Booking Code: <b>${it.data?.bookingcode}<\\b>", Html.FROM_HTML_MODE_LEGACY))
                dialog.setCancelable(false)
                dialog.setPositiveButton("OK") { dialog, which ->
                    val intent = Intent(this, BookingHistory::class.java)
                    startActivity(intent)

                    finish()
                }

                dialog.show()
            } else {
                showToast(it.getErrorMessage())
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun validateBooking(){
        val bookingForm = createBookingFormData()
        if (bookingForm.seats?.isEmpty() == true) {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Zero Passenger Count")
            dialog.setMessage("Please provide the count of passengers in your booking.")
            dialog.setPositiveButton("OK", null)

            dialog.show()
        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Confirm Booking")
            dialog.setMessage(HtmlCompat.fromHtml("You are about to book a ride with an amount of <b>${Util.formatAmount(getTotal())}<\\b>.", Html.FROM_HTML_MODE_LEGACY))
            dialog.setPositiveButton("Create Booking") { dialog, which ->
                createBooking()
            }

            dialog.show()
        }
    }

    private fun getTotal(): Double {
        var total = 0.0

        for ((route, typeInputs) in inputs) {
            for ((type, input) in typeInputs) {
                var count = 0
                if (input.text.toString().isNotBlank()) {
                    if (input.text.toString().toInt() != 0) {
                        count = input.text.toString().toInt()
                    }
                }

                total += ((route.regularfare ?: 0.0) - ((type.discount ?: 0).toDouble() / 100) * (route.regularfare ?: 0.0)) * count
            }
        }

        return total
    }
}
