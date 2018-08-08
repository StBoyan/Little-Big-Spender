package com.boyanstoynov.littlebigspender.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Input Filter to filter user input decimal values using a pattern.
 * Digits before and after zero can be specified upon constructing the
 * object.
 *
 * @author Boyan Stoynov
 */
public class DecimalDigitsInputFilter implements InputFilter {

    private final Pattern pattern;

    /**
     * Constructor that takes digits before zero and after zero
     * that will be used for filtering.
     * @param digitsBeforeZero digits before zero
     * @param digitsAfterZero digits after zero
     */
    public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        pattern = Pattern.compile("-?[0-9]{0," + (digitsBeforeZero) + "}+((\\.[0-9]{0," + (digitsAfterZero)
                + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        String replacement = source.subSequence(start, end).toString();
        String newValue = dest.subSequence(0, dStart).toString() + replacement
                + dest.subSequence(dEnd, dest.length()).toString();

        Matcher matcher = pattern.matcher(newValue);
        if (matcher.matches())
            return null;

        if (TextUtils.isEmpty(source))
            return dest.subSequence(dStart, dEnd);
        else
            return "";
    }
}
