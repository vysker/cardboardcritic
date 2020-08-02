package com.cardboardcritic.db.entity

import com.cardboardcritic.domain.HasName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode
class Outlet extends Entity implements HasName {
    String website
}
