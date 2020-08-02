package com.cardboardcritic.db.entity

import com.cardboardcritic.db.entity.meta.Entity
import com.cardboardcritic.db.entity.meta.HasName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode
class Critic extends Entity implements HasName {
}
