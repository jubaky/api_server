package org.jaram.jubaky.util

import java.text.DateFormat
import java.text.FieldPosition
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.*

class TimestampDateFormat : DateFormat() {

    init {
        if (calendar == null) {
            calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA)
        }

        if (numberFormat == null) {
            numberFormat = NumberFormat.getIntegerInstance(Locale.KOREA)
            numberFormat.isGroupingUsed = false
        }
    }

    override fun parse(source: String, pos: ParsePosition): Date? {
        return source.toLongOrNull()?.let { Date(it) }
    }

    override fun format(date: Date, toAppendTo: StringBuffer, fieldPosition: FieldPosition): StringBuffer {
        return toAppendTo.append(date.time)
    }
}