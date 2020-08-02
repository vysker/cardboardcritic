package com.cardboardcritic.db.entity

import com.cardboardcritic.db.entity.meta.Column
import com.cardboardcritic.db.entity.meta.Entity
import com.cardboardcritic.db.entity.meta.HasName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode
class Outlet extends Entity implements HasName {
    @Column String website
}
