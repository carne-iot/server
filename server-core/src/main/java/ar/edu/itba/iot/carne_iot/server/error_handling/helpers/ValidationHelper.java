package ar.edu.itba.iot.carne_iot.server.error_handling.helpers;

import ar.edu.itba.iot.carne_iot.server.error_handling.errros.ValidationError;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class containing static helper methods used for validation.
 */
public class ValidationHelper {

    /**
     * Checks that the given {@code object} is null.
     *
     * @param object            The object to be checked.
     * @param errorList         The list containing the errors, in which new possible errors will be added.
     * @param notNullFieldError The error to be added on not null value.
     */
    public static void objectNull(Object object, List<ValidationError> errorList, ValidationError notNullFieldError) {
        if (object != null) {
            errorList.add(notNullFieldError);
        }
    }

    /**
     * Checks that the given {@code object} is not null.
     *
     * @param object         The object to be checked.
     * @param errorList      The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError The error to be added on null value.
     */
    public static void objectNotNull(Object object, List<ValidationError> errorList, ValidationError nullFieldError) {
        if (object == null) {
            errorList.add(nullFieldError);
        }
    }

    /**
     * Checks that the given {@code number} is not null, and that it is between {@code min} and {@code max}.
     *
     * @param number         The number to be checked.
     * @param min            The min. value.
     * @param max            The max. value.
     * @param errorList      The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError The error to be added on null value.
     * @param belowMinError  The error to be added on "below min" situation.
     * @param aboveMaxError  The error to be added on "above max" situation.
     */
    public static void intNotNullAndLengthBetweenTwoValues(Integer number, int min, int max,
                                                           List<ValidationError> errorList,
                                                           ValidationError nullFieldError,
                                                           ValidationError belowMinError,
                                                           ValidationError aboveMaxError) {
        if (number == null) {
            errorList.add(nullFieldError);
        } else {
            intIsBetweenTwoValues(number, min, max, errorList, belowMinError, aboveMaxError);
        }
    }

    /**
     * Checks that the given {@code number} is between {@code min} and {@code max}, if it's not null.
     *
     * @param number        The number to be checked.
     * @param min           The min. value.
     * @param max           The max. value.
     * @param errorList     The list containing the errors, in which new possible errors will be added.
     * @param belowMinError The error to be added on "too short" length.
     * @param aboveMaxError The error to be added on "too long" length.
     */
    public static void intNullOrLengthBetweenTwoValues(Integer number, int min, int max,
                                                       List<ValidationError> errorList,
                                                       ValidationError belowMinError,
                                                       ValidationError aboveMaxError) {
        if (number != null) {
            intIsBetweenTwoValues(number, min, max, errorList, belowMinError, aboveMaxError);
        }
    }

    /**
     * Checks that the given {@code text} is not null,
     * and that its length is between {@code minLength} and {@code maxLength}.
     *
     * @param text           The text to be checked.
     * @param minLength      The text's min. length.
     * @param maxLength      The text's max. length.
     * @param errorList      The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError The error to be added on null value.
     * @param tooShortError  The error to be added on "too short" length.
     * @param tooLongError   The error to be added on "too long" length.
     */
    public static void stringNotNullAndLengthBetweenTwoValues(String text, int minLength, int maxLength,
                                                              List<ValidationError> errorList,
                                                              ValidationError nullFieldError,
                                                              ValidationError tooShortError,
                                                              ValidationError tooLongError) {
        if (text == null) {
            errorList.add(nullFieldError);
        } else {
            intIsBetweenTwoValues(text.length(), minLength, maxLength, errorList, tooShortError, tooLongError);
        }
    }

    /**
     * Checks that the given {@code text}'s length is between {@code minLength} and {@code maxLength},
     * if it's not null.
     *
     * @param text          The text to be checked.
     * @param minLength     The text's min. length.
     * @param maxLength     The text's max. length.
     * @param errorList     The list containing the errors, in which new possible errors will be added.
     * @param tooShortError The error to be added no "too short" length.
     * @param tooLongError  The error to be added no "too long" length.
     */
    public static void stringNullOrLengthBetweenTwoValues(String text, int minLength, int maxLength,
                                                          List<ValidationError> errorList,
                                                          ValidationError tooShortError, ValidationError tooLongError) {
        if (text != null) {
            intIsBetweenTwoValues(text.length(), minLength, maxLength, errorList, tooShortError, tooLongError);
        }
    }

    /**
     * Checks that the given {@code number} is between {@code min} and {@code max}.
     *
     * @param number        The number to be checked.
     * @param min           The min. number.
     * @param max           The max. number.
     * @param errorList     The list containing the errors, in which new possible errors will be added.
     * @param tooShortError The error to be added on below {@code min} number.
     * @param tooLongError  The error to be added on above {@code max} number.
     */
    public static void intIsBetweenTwoValues(int number, int min, int max, List<ValidationError> errorList,
                                             ValidationError tooShortError, ValidationError tooLongError) {
        if (number < min) {
            errorList.add(tooShortError);
        } else if (number > max) {
            errorList.add(tooLongError);
        }
    }

    /**
     * Checks that there are at most {@code maxAmountOfNulls} null values in the given {@code values}.
     *
     * @param maxAmountOfNulls     The max. amount of nulls allowed.
     * @param errorList            The list containing the errors, in which new possible errors will be added.
     * @param NOT_AT_LEAST_M_NULLS The error to be added on breaking the rule of max. amount of nulls.
     * @param values               The values to be checked.
     */
    public static void maxAmountOfNulls(int maxAmountOfNulls, List<ValidationError> errorList,
                                        ValidationError NOT_AT_LEAST_M_NULLS, Object... values) {
        if (Arrays.stream(values).filter(Objects::isNull)
                .collect(Collectors.toList()).size() > maxAmountOfNulls) {
            errorList.add(NOT_AT_LEAST_M_NULLS);
        }
    }

    /**
     * Checks that any of the given {@code values} are null.
     *
     * @param errorList    The list containing the errors, in which new possible errors will be added.
     * @param anyNullError The error to be added on breaking the rule of max. amount of nulls.
     * @param values       The values to be checked.
     */
    public static void anyNull(List<ValidationError> errorList, ValidationError anyNullError, Object... values) {
        maxAmountOfNulls(0, errorList, anyNullError, values);
    }

    /**
     * Checks that the given array is not {@code null}
     * and that its length is between the given {@code minLength} and {@code maxLength}
     *
     * @param array          The array to be checked.
     * @param minLength      The min length.
     * @param maxLength      The max length.
     * @param errorList      The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError The error to be added on null value.
     * @param tooShortError  The error to be added on "below min" situation.
     * @param tooLongError   The error to be added on "above max" situation.
     * @param <T>            The array type.
     */
    public static <T> void arrayNotNullAndLengthBetweenTwoNumbers(T[] array, int minLength, int maxLength,
                                                                  List<ValidationError> errorList,
                                                                  ValidationError nullFieldError,
                                                                  ValidationError tooShortError,
                                                                  ValidationError tooLongError) {
        if (array == null) {
            errorList.add(nullFieldError);
        } else {
            intIsBetweenTwoValues(array.length, minLength, maxLength, errorList, tooShortError, tooLongError);
        }
    }

    /**
     * Checks that the given array is {@code null}
     * or that its length is between the given {@code minLength} and {@code maxLength}
     *
     * @param array         The array to be checked.
     * @param minLength     The min length.
     * @param maxLength     The max length.
     * @param errorList     The list containing the errors, in which new possible errors will be added.
     * @param tooShortError The error to be added on "below min" situation.
     * @param tooLongError  The error to be added on "above max" situation.
     * @param <T>           The array type.
     */
    public static <T> void arrayNullOrLengthBetweenTwoNumbers(T[] array, int minLength, int maxLength,
                                                              List<ValidationError> errorList,
                                                              ValidationError tooShortError,
                                                              ValidationError tooLongError) {
        if (array != null) {
            intIsBetweenTwoValues(array.length, minLength, maxLength, errorList, tooShortError, tooLongError);
        }
    }

    /**
     * Checks that the given array is not {@code null}
     * and that its length is between the given {@code minLength} and {@code maxLength}
     *
     * @param array          The array to be checked.
     * @param minLength      The min length.
     * @param maxLength      The max length.
     * @param errorList      The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError The error to be added on null value.
     * @param tooShortError  The error to be added on "below min" situation.
     * @param tooLongError   The error to be added on "above max" situation.
     */
    public static void arrayNotNullAndLengthBetweenTwoNumbers(byte[] array, int minLength, int maxLength,
                                                              List<ValidationError> errorList,
                                                              ValidationError nullFieldError,
                                                              ValidationError tooShortError,
                                                              ValidationError tooLongError) {
        if (array == null) {
            errorList.add(nullFieldError);
        } else {
            intIsBetweenTwoValues(array.length, minLength, maxLength, errorList, tooShortError, tooLongError);
        }
    }

    /**
     * Checks that the given array is {@code null}
     * or that its length is between the given {@code minLength} and {@code maxLength}
     *
     * @param array         The array to be checked.
     * @param minLength     The min length.
     * @param maxLength     The max length.
     * @param errorList     The list containing the errors, in which new possible errors will be added.
     * @param tooShortError The error to be added on "below min" situation.
     * @param tooLongError  The error to be added on "above max" situation.
     */
    public static void arrayNullOrLengthBetweenTwoNumbers(byte[] array, int minLength, int maxLength,
                                                          List<ValidationError> errorList,
                                                          ValidationError tooShortError,
                                                          ValidationError tooLongError) {
        if (array != null) {
            intIsBetweenTwoValues(array.length, minLength, maxLength, errorList, tooShortError, tooLongError);
        }
    }

    /**
     * Checks that the given {@code email} is valid and between the given lengths.
     *
     * @param email             The email to be checked
     * @param minLength         The email's min. length.
     * @param maxLength         The email's max. length.
     * @param errorList         The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError    The error to be added on null value.
     * @param tooShortError     The error to be added on below {@code min} number.
     * @param tooLongError      The error to be added on above {@code max} number.
     * @param invalidEmailError The error to be added on invalid email.
     */
    public static void checkEmailNotNullAndValid(String email, int minLength, int maxLength,
                                                 List<ValidationError> errorList,
                                                 ValidationError nullFieldError, ValidationError tooShortError,
                                                 ValidationError tooLongError, ValidationError invalidEmailError) {

        int listLength = errorList.size();
        stringNotNullAndLengthBetweenTwoValues(email, minLength, maxLength, errorList,
                nullFieldError, tooShortError, tooLongError);
        // if listLength is equal to the size of the error list, the email's length is between min and max length.
        if (listLength == errorList.size() && !EmailValidator.getInstance().isValid(email)) {
            errorList.add(invalidEmailError);
        }
    }
}
