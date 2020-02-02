package com.egorshustov.vpoiske.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.util.DEFAULT_CITY_TITLE
import javax.inject.Inject

class CitiesAdapter @Inject constructor(
    context: Context,
    @LayoutRes private val layoutResource: Int
) : ArrayAdapter<City>(context, layoutResource) {

    fun setCities(values: List<City>?) {
        clear()
        add(DEFAULT_CITY_TITLE)
        values?.let { addAll(it) }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return bindData(
            getItem(position),
            createViewFromResource(convertView, parent, layoutResource)
        )
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
        bindData(
            getItem(position), createViewFromResource(
                convertView,
                parent,
                R.layout.item_spinner
            )
        )

    private fun createViewFromResource(
        convertView: View?,
        parent: ViewGroup,
        layoutResource: Int
    ) = (convertView ?: LayoutInflater.from(parent.context).inflate(
        layoutResource,
        parent,
        false
    )) as TextView

    private fun bindData(value: City?, view: TextView) = view.apply { text = value?.title }
}