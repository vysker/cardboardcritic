package com.cardboardcritic.db.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode
class Critic extends Entity {
    int outletId
    String name
}
