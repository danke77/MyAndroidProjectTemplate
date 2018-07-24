package com.danke.android.template.core.component.sortlist

/**
 * @author danke
 * @date 2018/7/20
 */
class SortComparator<T : SortComparator.ISort> : Comparator<T> {

    override fun compare(o1: T?, o2: T?): Int {
        if (o1 == null || o2 == null) {
            return -1
        }

        val s1 = getFirstLetter(o1.getSelling())
        val s2 = getFirstLetter(o2.getSelling())

        return if (s1 == "#" && s2 == "#") {
            o1.getSelling().compareTo(o2.getSelling())
        } else if (s1 == "@" && s2 == "@") {
            o1.getSelling().compareTo(o2.getSelling())
        } else if (s1 == "@" || s2 == "#") {
            -1
        } else if (s1 == "#" || s2 == "@") {
            1
        } else {
            o1.getSelling().compareTo(o2.getSelling())
        }
    }

    interface ISort {
        fun getSelling(): String
    }
}