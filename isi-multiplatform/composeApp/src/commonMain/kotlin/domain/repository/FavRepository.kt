package domain.repository

import domain.entity.Fav

interface FavRepository {
    fun findAll(): List<Fav>
}