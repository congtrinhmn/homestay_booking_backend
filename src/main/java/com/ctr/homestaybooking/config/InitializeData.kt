package com.ctr.homestaybooking.config

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.stereotype.Component
import javax.sql.DataSource


/**
 * Created by at-trinhnguyen2 on 2020/10/26
 */
@Component
class InitializeData(private val dataSource: DataSource) {

    @EventListener(ApplicationReadyEvent::class)
    fun loadData() {
        val resourceDatabasePopulator = ResourceDatabasePopulator(false, false, "UTF-8", ClassPathResource("data.sql"))
        resourceDatabasePopulator.execute(dataSource)
    }
}
