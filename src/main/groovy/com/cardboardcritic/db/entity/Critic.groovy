package com.cardboardcritic.db.entity

import com.cardboardcritic.domain.HasName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode
class Critic extends Entity implements HasName {
}
