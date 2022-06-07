package com.intellilanghub.server.repository

import com.intellilanghub.server.model.InjectionPack
import org.springframework.data.mongodb.repository.MongoRepository


interface InjectionPackRepository : MongoRepository<InjectionPack, String> {
    fun findByLibrary(library: String): InjectionPack?
}
