package com.cardboardcritic.db

class DbUtil {
    /**
     * Takes a camelCased field and turns it into snake_case, e.g. 'shortDescription' becomes 'short_description'.
     *
     * @param fieldName camelCased field name
     * @return snake_cased field name
     */
    static String snake_case(String fieldName) {
        fieldName.replaceAll('([A-Z])', '_$1').toLowerCase()
    }

    /**
     * Inherited properties look like this: 'com_cardboardcritic_domain__has_name__name'. This method turns such strings
     * into the actual property name, e.g. 'com_cardboardcritic_domain__has_name__name' becomes 'name'.
     *
     * @param fieldName field name to clean up
     * @return clean up name
     */
    static String cleanFieldName(String fieldName) {
        fieldName.contains('__')
                ? fieldName.substring(fieldName.lastIndexOf('__') + 2)
                : fieldName
    }
}
