package com.danke.android.template.core.component.sortlist

import java.util.regex.Pattern

/**
 * @author danke
 * @date 2018/7/23
 */

fun getFirstLetter(str: String?): String {
    if (str == null) {
        return "#"
    }
    if (str.trim().isEmpty()) {
        return "#"
    }
    val c = str.trim().substring(0, 1)[0]
    // 正则表达式匹配
    val pattern = Pattern.compile("^[A-Za-z]+$")
    return if (pattern.matcher(c + "").matches()) {
        c.toString().toUpperCase()
    } else {
        "#"
    }
}