package com.intellilanghub.server.service

import com.intellilanghub.server.exception.EntityNotFoundException
import com.intellilanghub.server.model.InjectionPack
import com.intellilanghub.server.repository.InjectionPackRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InjectionPackService(
    private val injectionPackRepository: InjectionPackRepository,
    private val mongoTemplate: MongoTemplate
) {
    fun getInjectionPackLibraries(): List<String> {
        return mongoTemplate.getCollection("injectionPack").distinct("_id", String::class.java)
            .toList()
    }


    fun getInjectionPack(library: String): InjectionPack {
        val query = Query()
        query.addCriteria(Criteria.where(InjectionPack::library.name).`is`(library))
        return injectionPackRepository.findByLibrary(library)
            ?: throw EntityNotFoundException("Injection pack not found for library: $library")
    }
}