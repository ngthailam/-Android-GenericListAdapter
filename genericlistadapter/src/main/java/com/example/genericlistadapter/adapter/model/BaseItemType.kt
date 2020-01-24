package com.example.genericlistadapter.adapter.model

abstract class BaseItemType {
    abstract fun getId(): String
    abstract fun getViewType(): Int
    abstract fun areItemsTheSame(other: Any?): Boolean
    abstract fun areContentsTheSame(other: Any?): Boolean
}
