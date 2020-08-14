package com.cardboardcritic.db.repository

import com.cardboardcritic.db.entity.Outlet
import io.quarkus.hibernate.orm.panache.PanacheRepository

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class OutletRepository implements PanacheRepository<Outlet> {
}
