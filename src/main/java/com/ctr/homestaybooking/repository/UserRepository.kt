package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.transaction.Transactional

@Transactional
interface UserRepository : JpaRepository<UserEntity, Int> {
    fun findByEmail(email: String): UserEntity?

    @Query(value = """SELECT * FROM users where email like CONCAT('%', :keyword , '%')
                        or first_name like CONCAT('%', :keyword ,'%')
                        or last_name like CONCAT('%', :keyword ,'%')
                        """, nativeQuery = true)
    fun findUsersByKeyword(@Param("keyword") keyword: String): List<UserEntity>
}
