package com.egorshustov.vpoiske.adapters

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.util.Event

@BindingAdapter("app:users")
fun RecyclerView.setUsers(userList: List<User>?) {
    (adapter as UsersAdapter).submitList(userList)
}

@BindingAdapter("app:countries")
fun Spinner.setCountries(countryList: List<Country>?) {
    (adapter as? CountriesAdapter)?.setCountries(countryList)
}

@BindingAdapter("app:selectedCountry")
fun Spinner.setSelectedCountry(country: Event<Country>) {
    (adapter as CountriesAdapter?)?.let {
        val countryPosition = it.getPosition(country.peekContent())
        if (countryPosition != selectedItemPosition) setSelection(it.getPosition(country.peekContent()))
    }
}

@InverseBindingAdapter(attribute = "app:selectedCountry")
fun Spinner.getSelectedCountry() =
    Event((adapter as CountriesAdapter?)?.getItem(selectedItemPosition))

@BindingAdapter("app:selectedCountryAttrChanged")
fun Spinner.setSelectedCountryListener(attrChange: InverseBindingListener) {
    val newOnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
            attrChange.onChange()
    }
    val oldOnItemSelectedListener =
        ListenerUtil.trackListener(this, newOnItemSelectedListener, R.id.onCountrySelectedListener)
    if (oldOnItemSelectedListener != null) onItemSelectedListener = null
    onItemSelectedListener = newOnItemSelectedListener
}

@BindingAdapter("app:cities")
fun Spinner.setCities(cityList: List<City>?) {
    isEnabled = !cityList.isNullOrEmpty()
    isClickable = !cityList.isNullOrEmpty()
    (adapter as? CitiesAdapter)?.setCities(cityList)
}

@BindingAdapter("app:selectedCity")
fun Spinner.setSelectedCity(city: City?) {
    (adapter as CitiesAdapter?)?.let {
        val cityPosition = it.getPosition(city)
        if (cityPosition != selectedItemPosition) setSelection(it.getPosition(city))
    }
}

@InverseBindingAdapter(attribute = "app:selectedCity")
fun Spinner.getSelectedCity() =
    (adapter as CitiesAdapter?)?.getItem(selectedItemPosition)

@BindingAdapter("app:selectedCityAttrChanged")
fun Spinner.setSelectedCityListener(attrChange: InverseBindingListener) {
    val newOnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
            attrChange.onChange()
    }
    val oldOnItemSelectedListener =
        ListenerUtil.trackListener(this, newOnItemSelectedListener, R.id.onCitySelectedListener)
    if (oldOnItemSelectedListener != null) onItemSelectedListener = null
    onItemSelectedListener = newOnItemSelectedListener
}

@BindingAdapter("app:imageFromUrl")
fun ImageView.bindImageFromUrl(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}