package com.cardboardcritic.db.entity.meta;

public interface HasName<T> {
    String getName();

    T setName(String name);
}
