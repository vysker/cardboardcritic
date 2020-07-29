package com.cardboardcritic.db

import java.sql.ResultSet

interface FieldMapper<T> {
    T call(ResultSet rs, String column)
}
