package com.egorshustov.vpoiske.searchparams

import android.os.Bundle
import android.view.View
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
import com.egorshustov.vpoiske.main.MainViewModel
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.Relation
import com.egorshustov.vpoiske.util.snackBar

class SearchParamsFragment : BaseFragment<SearchParamsViewModel, FragmentSearchParamsBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_search_params

    override val viewModel by viewModels<SearchParamsViewModel> { viewModelFactory }

    private val mainViewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinners()
        setSeekBarsListeners()
        setCheckBoxListener()
        observeSelectedCountry()
        observeMessage()
        observeCurrentAgeFrom()
        observeCurrentAgeTo()
        observeNewSearchId()
    }

    private fun setupSpinners() = with(binding) {
        spinnerRelation.adapter =
            ArrayAdapter(requireContext(), R.layout.item_spinner, Relation.values())
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
    }

    private fun setSeekBarsListeners() = with(binding) {
        seekFoundedUsersLimit.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onFoundedUsersLimitChanged(seekProgressToUsersLimit(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        //todo create custom seekBar with range
        seekFriendsMaxCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        seekFollowersMaxCount.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.onFollowersMaxCountChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seekDaysInterval.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.onDaysIntervalChanged(seekProgressToDaysInterval(progress))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setCheckBoxListener() {
        binding.checkSetFriendsLimits.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSetFriendsLimitsChanged(isChecked)
        }
    }

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
                findNavController().popBackStack(R.id.mainFragment, false)
            }
        })
    }
}
