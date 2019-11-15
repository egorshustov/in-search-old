package com.egorshustov.vpoiske.newsearch

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.CitiesAdapter
import com.egorshustov.vpoiske.adapters.CountriesAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentNewSearchBinding
import com.egorshustov.vpoiske.main.MainViewModel
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.Relation
import com.egorshustov.vpoiske.util.Sex
import com.egorshustov.vpoiske.util.extractInt
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_new_search.*
import javax.inject.Inject

class NewSearchFragment :
    BaseFragment<NewSearchState, NewSearchViewModel, FragmentNewSearchBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_new_search
    override val viewModel by viewModels<NewSearchViewModel> { viewModelFactory }
    private val mainViewModel by activityViewModels<MainViewModel> { viewModelFactory }

    private lateinit var countriesAdapter: CountriesAdapter
    private lateinit var citiesAdapter: CitiesAdapter
    private lateinit var ageFromAdapter: ArrayAdapter<String>
    private lateinit var ageToAdapter: ArrayAdapter<String>
    private lateinit var relationAdapter: ArrayAdapter<Relation>

    @Inject
    lateinit var applicationContext: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        setupSpinners()
        setAgeFromSpinnerListener()
        setAgeToSpinnerListener()
        setRelationSpinnerListener()
        setSeekBarsListeners()
        setCheckBoxListener()
        setSearchButtonListener()
        observeCountries()
        observeCities()
        observeLiveSnackBarMessage()
        observeResetAgeFromCommand()
        observeResetAgeToCommand()
    }

    private fun initAdapters() = context?.let {
        countriesAdapter = CountriesAdapter(it, R.layout.item_spinner)
        citiesAdapter = CitiesAdapter(it, R.layout.item_spinner)
        ageFromAdapter =
            ArrayAdapter(it, R.layout.item_spinner, resources.getStringArray(R.array.ages_from))
        ageToAdapter =
            ArrayAdapter(it, R.layout.item_spinner, resources.getStringArray(R.array.ages_to))
        relationAdapter = ArrayAdapter(it, R.layout.item_spinner, Relation.values())
    }

    private fun setupSpinners() {
        spinner_countries.adapter = countriesAdapter.apply { setCountries(emptyList()) }
        spinner_cities.apply {
            isEnabled = false
            isClickable = false
            adapter = citiesAdapter.apply { setCities(emptyList()) }
        }
        spinner_age_from.adapter = ageFromAdapter
        spinner_age_to.adapter = ageToAdapter
        spinner_relation.adapter = relationAdapter
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

    private fun setAgeFromSpinnerListener() {
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

    private fun setAgeToSpinnerListener() {
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

    private fun setRelationSpinnerListener() {
        spinner_relation.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    relationAdapter.getItem(position)?.let { viewModel.onRelationSelected(it) }
                }

            }
        }
    }

    private fun setSeekBarsListeners() {
        //todo create custom seekBar with range
        seek_founded_users_limit.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onFoundedUsersLimitChanged(seekProgressToUsersLimit(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seek_friends_max_count.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.onFriendsMaxCountChanged(
                    seekProgressToDaysFriendsMaxCount(
                        progress
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seek_followers_max_count.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.onFollowersMaxCountChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seek_days_interval.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.onDaysIntervalChanged(seekProgressToDaysInterval(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setCheckBoxListener() {
        check_set_friends_limits.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSetFriendsLimitsChanged(isChecked)
        }
    }

    private fun setSearchButtonListener() {
        button_search.setOnClickListener {
            val countryId = viewModel.currentCountry.value?.id
            val cityId = viewModel.currentCity.value?.id
            val ageFrom = viewModel.currentAgeFrom.value
            val ageTo = viewModel.currentAgeTo.value
            val relation = viewModel.currentRelation.value?.value
            val sex = Sex.FEMALE.value
            val withPhoneOnly = check_with_phone_only.isChecked
            val foundedUsersLimit = viewModel.currentFoundedUsersLimit.value
            val friendsMinCount = viewModel.currentFriendsMinCount.value
            val friendsMaxCount = viewModel.currentFriendsMaxCount.value
            val followersMinCount = viewModel.currentFollowersMinCount.value
            val followersMaxCount = viewModel.currentFollowersMaxCount.value
            val daysInterval = viewModel.currentDaysInterval.value
            if (
                countryId != null
                && cityId != null
                && foundedUsersLimit != null
                && followersMinCount != null
                && followersMaxCount != null
                && daysInterval != null
            ) {
                mainViewModel.onSearchButtonClicked(
                    countryId,
                    cityId,
                    ageFrom,
                    ageTo,
                    relation,
                    sex,
                    withPhoneOnly,
                    foundedUsersLimit,
                    friendsMinCount,
                    friendsMaxCount,
                    followersMinCount,
                    followersMaxCount,
                    daysInterval
                )
                findNavController().popBackStack(R.id.mainViewPagerFragment, false)
            }
        }
    }

    private fun seekProgressToUsersLimit(seekProgress: Int) = (seekProgress + 1) * 10

    private fun seekProgressToDaysInterval(seekProgress: Int) = seekProgress + 1

    private fun seekProgressToDaysFriendsMaxCount(seekProgress: Int) =
        seekProgress + viewModel.defaultFriendsMinCount

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
        viewModel.resetAgeFromCommand.observe(viewLifecycleOwner, EventObserver {
            spinner_age_from.setSelection(spinner_age_to.selectedItemPosition)
        })
    }

    private fun observeResetAgeToCommand() {
        viewModel.resetAgeToCommand.observe(viewLifecycleOwner, EventObserver {
            spinner_age_to.setSelection(spinner_age_from.selectedItemPosition)
        })
    }

    private fun observeLiveSnackBarMessage() {
        viewModel.snackBarMessage.observe(viewLifecycleOwner, EventObserver { exception ->
            view?.let { Snackbar.make(it, exception, Snackbar.LENGTH_LONG).show() }
        })
    }
}
