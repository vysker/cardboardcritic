package com.cardboardcritic.db

class DbUtil {
    static String snake_case(String fieldName) {
        fieldName.replaceAll('([A-Z])', '_$1').toLowerCase()
    }
}
