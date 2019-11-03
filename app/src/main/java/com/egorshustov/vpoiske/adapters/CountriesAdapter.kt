package com.egorshustov.vpoiske.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.util.DEFAULT_COUNTRY
import javax.inject.Inject

class CountriesAdapter @Inject constructor(
    context: Context,
    @LayoutRes private val layoutResource: Int
) : ArrayAdapter<Country>(context, layoutResource) {

    fun setCountries(values: List<Country>) {
        clear()
        add(DEFAULT_COUNTRY)
        addAll(values)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return bindData(
            getItem(position),
            createViewFromResource(convertView, parent, layoutResource)
        )
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return bindData(
            getItem(position), createViewFromResource(
                convertView,
                parent,
                R.layout.item_spinner
            )
        )
    }

    private fun createViewFromResource(
        convertView: View?,
        parent: ViewGroup,
        layoutResource: Int
    ): TextView {
        return (convertView ?: LayoutInflater.from(parent.context).inflate(
            layoutResource,
            parent,
            false
        )) as TextView
    }

    private fun bindData(value: Country?, view: TextView): TextView {
        return view.apply {
            text = value?.title
        }
    }
}