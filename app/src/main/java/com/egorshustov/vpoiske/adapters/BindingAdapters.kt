package com.egorshustov.vpoiske.adapters

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.util.Event
import com.egorshustov.vpoiske.util.extractInt

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
        if (countryPosition != selectedItemPosition) setSelection(countryPosition)
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
        if (cityPosition != selectedItemPosition) setSelection(cityPosition)
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

@BindingAdapter("app:selectedAgeFrom")
fun Spinner.setSelectedAgeFrom(ageFrom: Int?) {
    (adapter as? ArrayAdapter<String>)?.let {
        val ageFromArray = resources.getStringArray(R.array.ages_from)
        val ageFromString =
            ageFromArray.find { ageFrom.toString() in it.toString() } ?: ageFromArray[0]
        val ageFromStringPosition = it.getPosition(ageFromString)
        if (ageFromStringPosition != selectedItemPosition) setSelection(ageFromStringPosition)
    }
}

@InverseBindingAdapter(attribute = "app:selectedAgeFrom")
fun Spinner.getSelectedAgeFrom(): Int? =
    (adapter as? ArrayAdapter<String>)?.getItem(selectedItemPosition)?.extractInt()

@BindingAdapter("app:selectedAgeFromAttrChanged")
fun Spinner.setSelectedAgeFromListener(attrChange: InverseBindingListener) {
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

@BindingAdapter("app:selectedAgeTo")
fun Spinner.setSelectedAgeTo(ageTo: Int?) {
    (adapter as? ArrayAdapter<String>)?.let {
        val ageToArray = resources.getStringArray(R.array.ages_to)
        val ageToString = ageToArray.find { ageTo.toString() in it.toString() } ?: ageToArray[0]
        val ageToStringPosition = it.getPosition(ageToString)
        if (ageToStringPosition != selectedItemPosition) setSelection(ageToStringPosition)
    }
}

@InverseBindingAdapter(attribute = "app:selectedAgeTo")
fun Spinner.getSelectedAgeTo(): Int? =
    (adapter as? ArrayAdapter<String>)?.getItem(selectedItemPosition)?.extractInt()

@BindingAdapter("app:selectedAgeToAttrChanged")
fun Spinner.setSelectedAgeToListener(attrChange: InverseBindingListener) {
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

@BindingAdapter("app:searchesWithUsers")
fun RecyclerView.setSearchesWithUsers(searchListWithUsers: PagedList<SearchWithUsers>?) {
    (adapter as SearchWithUsersAdapter).submitList(searchListWithUsers)
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