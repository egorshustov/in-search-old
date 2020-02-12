package com.egorshustov.vpoiske.searchparams

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.CitiesAdapter
import com.egorshustov.vpoiske.adapters.CountriesAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchParamsBinding
import com.egorshustov.vpoiske.search.SearchViewModel
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.Relation
import com.egorshustov.vpoiske.util.snackBar
import kotlinx.android.synthetic.main.fragment_search_params.*

class SearchParamsFragment : BaseFragment<SearchParamsViewModel, FragmentSearchParamsBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_search_params
    override val viewModel by viewModels<SearchParamsViewModel> { viewModelFactory }
    private val mainViewModel by activityViewModels<SearchViewModel> { viewModelFactory }

    private lateinit var relationAdapter: ArrayAdapter<Relation>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinners()
        setRelationSpinnerListener()
        setSeekBarsListeners()
        setCheckBoxListener()
        setSearchButtonListener()
        observeSelectedCountry()
        observeMessage()
        observeCurrentAgeFrom()
        observeCurrentAgeTo()
        observeNewSearchId()
    }

    private fun setupSpinners() = with(binding) {
        relationAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, Relation.values())
        spinnerCountries.adapter = CountriesAdapter(requireContext(), R.layout.item_spinner)
        spinnerCities.adapter = CitiesAdapter(requireContext(), R.layout.item_spinner)
        spinnerAgeFrom.adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            resources.getStringArray(R.array.ages_from)
        )
        spinnerAgeTo.adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            resources.getStringArray(R.array.ages_to)
        )
        spinnerRelation.adapter = relationAdapter
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

    private fun setSearchButtonListener() =
        button_search.setOnClickListener { viewModel.onSearchButtonClicked() }

    private fun seekProgressToUsersLimit(seekProgress: Int) = (seekProgress + 1) * 10

    private fun seekProgressToDaysInterval(seekProgress: Int) = seekProgress + 1

    private fun seekProgressToDaysFriendsMaxCount(seekProgress: Int) =
        seekProgress + viewModel.defaultFriendsMinCount

    private fun observeSelectedCountry() {
        viewModel.currentCountry.observe(viewLifecycleOwner, EventObserver {
            viewModel.onCountrySelected(it.id)
        })
    }

    private fun observeCurrentAgeFrom() {
        viewModel.currentAgeFrom.observe(viewLifecycleOwner) { viewModel.onAgeFromChanged(it) }
    }

    private fun observeCurrentAgeTo() {
        viewModel.currentAgeTo.observe(viewLifecycleOwner) { viewModel.onAgeToChanged(it) }
    }

    private fun observeMessage() {
        viewModel.message.observe(viewLifecycleOwner, EventObserver { view?.snackBar(it) })
    }

    private fun observeNewSearchId() {
        viewModel.newSearchId.observe(viewLifecycleOwner, EventObserver { newSearchId ->
            newSearchId?.let {
                mainViewModel.onSearchButtonClicked(it)
                findNavController().popBackStack(R.id.searchFragment, false)
            }
        })
    }
}
