package com.egorshustov.vpoiske.newsearch

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.CitiesAdapter
import com.egorshustov.vpoiske.adapters.CountriesAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentNewSearchBinding
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.extractInt
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_new_search.*
import javax.inject.Inject

class NewSearchFragment :
    BaseFragment<NewSearchState, NewSearchViewModel, FragmentNewSearchBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_new_search
    override val viewModel by viewModels<NewSearchViewModel> { viewModelFactory }

    private lateinit var countriesAdapter: CountriesAdapter
    private lateinit var citiesAdapter: CitiesAdapter

    @Inject
    lateinit var applicationContext: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        setupSpinners()
        setupAgeFromSpinnerListener()
        setupAgeToSpinnerListener()
        observeCountries()
        observeCities()
        observeLiveSnackBarMessage()
        observeResetAgeFromCommand()
        observeResetAgeToCommand()
    }

    private fun initAdapters() {
        countriesAdapter = CountriesAdapter(
            applicationContext,
            R.layout.item_spinner
        )
        citiesAdapter = CitiesAdapter(
            applicationContext,
            R.layout.item_spinner
        )
    }

    private fun setupSpinners() {
        spinner_countries.adapter = countriesAdapter.apply { setCountries(emptyList()) }
        spinner_cities.apply {
            isEnabled = false
            isClickable = false
            adapter = citiesAdapter.apply { setCities(emptyList()) }
        }

        context?.let {
            spinner_age_from.adapter = ArrayAdapter<String>(
                it,
                R.layout.item_spinner,
                resources.getStringArray(R.array.ages_from)
            )

            spinner_age_to.adapter = ArrayAdapter<String>(
                it,
                R.layout.item_spinner,
                resources.getStringArray(R.array.ages_to)
            )
        }
    }

    private fun setupCountriesSpinnerListener() {
        spinner_countries.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    countriesAdapter.getItem(position)?.let { viewModel.onCountrySelected(it) }
                }

            }
        }
    }

    private fun setupCitiesSpinnerListener() {
        spinner_cities.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    citiesAdapter.getItem(position)?.let { viewModel.onCitySelected(it) }
                }

            }
        }
    }

    private fun setupAgeFromSpinnerListener() {
        spinner_age_from.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val ageFrom = parent?.getItemAtPosition(position)?.toString()?.extractInt()
                    viewModel.onAgeFromSelected(ageFrom)
                }

            }
        }
    }

    private fun setupAgeToSpinnerListener() {
        spinner_age_to.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val ageTo = parent?.getItemAtPosition(position)?.toString()?.extractInt()
                    viewModel.onAgeToSelected(ageTo)
                }

            }
        }
    }

    private fun observeCountries() {
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            countriesAdapter.setCountries(it)
            spinner_countries.setSelection(countriesAdapter.getPosition(viewModel.currentCountry.value))
            setupCountriesSpinnerListener()
        })
    }

    private fun observeCities() {
        viewModel.cities.observe(viewLifecycleOwner, Observer {
            spinner_cities.isEnabled = it.isNotEmpty()
            spinner_cities.isClickable = it.isNotEmpty()
            citiesAdapter.setCities(it)
            spinner_cities.setSelection(citiesAdapter.getPosition(viewModel.currentCity.value))
            setupCitiesSpinnerListener()
        })
    }

    private fun observeResetAgeFromCommand() {
        viewModel.resetAgeFromCommand.observe(viewLifecycleOwner, Observer {
            spinner_age_from.setSelection(spinner_age_to.selectedItemPosition)
        })
    }

    private fun observeResetAgeToCommand() {
        viewModel.resetAgeToCommand.observe(viewLifecycleOwner, Observer {
            spinner_age_to.setSelection(spinner_age_from.selectedItemPosition)
        })
    }

    private fun observeLiveSnackBarMessage() {
        viewModel.snackBarMessage.observe(viewLifecycleOwner, EventObserver { exception ->
            view?.let { Snackbar.make(it, exception, Snackbar.LENGTH_LONG).show() }
        })
    }
}
