package com.elhaffar.exoformbackend.common.utils;

import org.springframework.data.domain.Sort;

public class SortUtils {

    private SortUtils() {} // empêche l'instanciation — classe utilitaire

    public static Sort buildSort(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
    }
}
