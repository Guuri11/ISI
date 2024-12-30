package org.isi.domain.repository

import org.isi.domain.models.Fav

interface FavRepository {
    fun findAll(): List<Fav>
}