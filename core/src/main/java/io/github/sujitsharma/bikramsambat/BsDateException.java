package io.github.sujitsharma.bikramsambat;

import java.time.DateTimeException;

/**
 * Thrown when a Bikram Sambat date is invalid, e.g. an out-of-range year,
 * a month outside 1-12, or a day that does not exist in the given BS month.
 */
public class BsDateException extends DateTimeException {

    public BsDateException(String message) {
        super(message);
    }
}