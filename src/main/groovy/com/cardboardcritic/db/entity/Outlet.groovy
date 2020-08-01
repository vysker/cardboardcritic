package com.cardboardcritic.db.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode
class Outlet extends Entity {
    String name, website
}
