package com.example.genericlistadapter.adapter.model

abstract class BaseItem {
    abstract fun areItemsTheSame(other: Any?): Boolean
    abstract fun areContentsTheSame(other: Any?): Boolean
}
