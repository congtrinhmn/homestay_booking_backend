package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.config.NotFoundException
import com.ctr.homestaybooking.controller.place.PlaceNotFoundException
import com.ctr.homestaybooking.controller.place.PlaceSearchNotFoundException
import com.ctr.homestaybooking.controller.place.dto.RecommendResponse
import com.ctr.homestaybooking.controller.user.UserNotFoundException
import com.ctr.homestaybooking.entity.*
import com.ctr.homestaybooking.repository.BookingRepository
import com.ctr.homestaybooking.repository.PlaceRepository
import com.ctr.homestaybooking.repository.UserRepository
import com.ctr.homestaybooking.shared.*
import com.ctr.homestaybooking.shared.enums.DateStatus
import com.ctr.homestaybooking.shared.enums.PlaceStatus
import com.ctr.homestaybooking.shared.model.PagingHeaders
import com.ctr.homestaybooking.shared.model.PagingResponse
import com.google.gson.Gson
import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.*
import net.fortuna.ical4j.util.UidGenerator
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


/**
 * Created by at-trinhnguyen2 on 2020/10/19
 */
@Service
class PlaceService(private val placeRepository: PlaceRepository,
                   private val userRepository: UserRepository,
                   private val bookingRepository: BookingRepository
) {
    fun getAllPlace(): List<PlaceEntity> {
        return placeRepository.findByStatus(PlaceStatus.LISTED)
    }

    fun getAllPlace(page: Int, size: Int, sortBy: String): List<PlaceEntity> {
        val paging: Pageable = PageRequest.of(page, size, Sort.by(sortBy))
        return placeRepository.findByStatus(PlaceStatus.LISTED, paging)
    }

    fun getPlaceByID(id: Int): PlaceEntity =
            placeRepository.findById(id).toNullable() ?: throw  PlaceNotFoundException(id)

    fun getBookingByPlaceId(id: Int): List<BookingEntity> =
            placeRepository.findById(id).toNullable()?.bookingEntities?.toList() ?: throw  PlaceNotFoundException(id)

    fun getPlacesByIDs(ids: Set<Int>) =
            placeRepository.findAllById(ids).filterIsInstance<PlaceEntity>().toSet()

    fun getPlacesByWardEntity(wardEntity: WardEntity): Set<PlaceEntity> =
            placeRepository.findByWardEntity(wardEntity)

    fun getPlacesByUserEntity(userEntity: UserEntity): List<PlaceEntity> =
            placeRepository.findByHostEntity(userEntity).filter { it.status != PlaceStatus.DELETED }

    fun getPlacesByDistrictId(id: Int): Set<PlaceEntity> =
            placeRepository.findByDistrictId(id) ?: throw  NotFoundException("District", id)

    fun addPlace(placeEntity: PlaceEntity): PlaceEntity {
        placeEntity.id = 0
        return placeRepository.save(placeEntity)
    }

    fun editPlace(placeEntity: PlaceEntity): PlaceEntity {
        return if (placeEntity.id == 0) {
            placeRepository.save(placeEntity)
        } else {
            (placeRepository.findById(placeEntity.id).toNullable()
                    ?: throw PlaceNotFoundException(placeEntity.id)).apply {
                placeEntity.reviewEntities = reviewEntities
                placeEntity.bookingEntities = bookingEntities
                placeEntity.hostEntity = hostEntity
            }
            placeRepository.save(placeEntity)
        }
    }

    fun deletePlaceByID(id: Int): PlaceEntity {
        val placeEntity: PlaceEntity = placeRepository.findById(id).toNullable()
                ?: throw PlaceNotFoundException(id)
        placeEntity.status = PlaceStatus.DELETED
        return placeRepository.save(placeEntity)
    }

    fun reversePlaceStatusByID(id: Int): PlaceEntity {
        val placeEntity: PlaceEntity = placeRepository.findById(id).toNullable()
                ?: throw PlaceNotFoundException(id)
        if (placeEntity.status == PlaceStatus.UNLISTED) {
            placeEntity.status = PlaceStatus.LISTED
        } else {
            placeEntity.status = PlaceStatus.UNLISTED
        }
        return placeRepository.save(placeEntity)
    }

    fun updateBookingSlotById(id: Int, bookingDates: Set<Date>, dateStatus: DateStatus = DateStatus.BOOKED): PlaceEntity {
        val placeEntity = placeRepository.findById(id).toNullable() ?: throw PlaceNotFoundException(id)
        placeEntity.bookingSlotEntities?.apply {
            val dates = map { it.date }
            // add booking date if bookingSlots don't have
            bookingDates.forEach {
                if (!dates.isContain(it)) {
                    add(BookingSlotEntity(date = it))
                }
            }
            // change status of bookingDates to booked
            filter { bookingDates.isContain(it.date) }.forEach { it.status = dateStatus }
        }
        return placeRepository.save(placeEntity)
    }

    fun getRecommendPlaceForUser(id: Int): List<PlaceEntity> {
        userRepository.findById(id).toNullable() ?: throw UserNotFoundException(id)
        val uri = "${BASE_URL_RECOMMEND}/recommend/${id}"

        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject(uri, String::class.java)
        Gson().fromJson(response, RecommendResponse::class.java)
                ?.recommends
                ?.take(20)
                ?.map { it.placeId }
                ?.let {
                    return placeRepository.findAllById(it)
                }
        return listOf()
    }

    fun getCalendarById(id: Int): String {
        val placeEntity = placeRepository.findById(id).toNullable() ?: throw PlaceNotFoundException(id)
        placeEntity.apply {
            val iCalendarName = "place_$id.ics"

            val iCalendar = net.fortuna.ical4j.model.Calendar()
            iCalendar.properties.apply {
                add(ProdId("-//Homestay Booking//iCal4j 1.0//EN"))
                add(Version.VERSION_2_0)
                add(CalScale.GREGORIAN)
            }

            getBookingByPlaceId(id).forEach {
                val event = VEvent(net.fortuna.ical4j.model.Date(it.startDate.add(1)),
                        net.fortuna.ical4j.model.Date(it.endDate.add(1)),
                        "Đơn đặt chỗ mã ${it.id} của ${it.userEntity.getName()} ${it.totalPaid}đ")
                event.properties.apply {
                    add(UidGenerator {
                        Uid("${it.startDate.format(FORMAT_DATE)}-${it.endDate.add(-1).format(FORMAT_DATE)}-${id}-homestay-booking")
                    }.generateUid())
                    add(Contact(it.userEntity.phoneNumber))
                    add(Description(name))
                    add(Location(address))
                }
                iCalendar.components.add(event)
            }

            bookingSlotEntities?.filter { it.status == DateStatus.UNAVAILABLE }
                    ?.map { it.date }
                    ?.consecutive()
                    ?.apply { log.info { this } }
                    ?.forEach {
                        if (it.isNotEmpty()) {
                            val event = VEvent(net.fortuna.ical4j.model.Date(it.first().add(1)),
                                    net.fortuna.ical4j.model.Date(it.last().add(2)),
                                    "Không có sẵn")
                            event.properties.apply {
                                add(UidGenerator { Uid("${it.firstOrNull()?.format(FORMAT_DATE)}-${it.lastOrNull()?.format(FORMAT_DATE)}-${id}-homestay-booking") }.generateUid())
                                add(Description(name))
                                add(Location(address))
                            }
                            iCalendar.components.add(event)
                        }
                    }
            val fout = FileOutputStream(iCalendarName)
            val outputter = CalendarOutputter()
            outputter.isValidating = false
            outputter.output(iCalendar, fout)

            val file = File(iCalendarName)
            val fin = FileInputStream(file)

            return Uploader.upload(iCalendarName, fin).apply {
                fout.close()
                fin.close()
                file.delete()
            }
        }
    }

    /**
     * get element using Criteria.
     *
     * @param spec    *
     * @param headers pagination data
     * @param sort    sort criteria
     * @return retrieve elements with pagination
     */
    fun get(spec: Specification<PlaceEntity>, headers: HttpHeaders, sort: Sort): PagingResponse {
        return if (isRequestPaged(headers)) {
            get(spec, buildPageRequest(headers, sort))
        } else {
            val entities = get(spec, sort)
            PagingResponse(entities.size.toLong(), 0, 0, 0L, 0, entities)
        }
    }

    private fun isRequestPaged(headers: HttpHeaders): Boolean {
        return headers.containsKey(PagingHeaders.PAGE_NUMBER.pageName) && headers.containsKey(PagingHeaders.PAGE_SIZE.pageName)
    }

    private fun buildPageRequest(headers: HttpHeaders, sort: Sort): Pageable {
        val page = headers[PagingHeaders.PAGE_NUMBER.pageName]?.get(0)?.toInt() ?: 0
        val size = headers[PagingHeaders.PAGE_SIZE.pageName]?.get(0)?.toInt() ?: 20
        return PageRequest.of(page, size, sort)
    }

    /**
     * get elements using Criteria.
     *
     * @param spec     *
     * @param pageable pagination data
     * @return retrieve elements with pagination
     */
    private fun get(spec: Specification<PlaceEntity>, pageable: Pageable): PagingResponse {
        val page = placeRepository.findAll(spec, pageable)
        val content = page.content
        return PagingResponse(page.totalElements, page.number, page.numberOfElements, pageable.offset, page.totalPages, content)
    }

    /**
     * get elements using Criteria.
     *
     * @param spec *
     * @return elements
     */
    private fun get(spec: Specification<PlaceEntity>, sort: Sort): MutableList<PlaceEntity> {
        return placeRepository.findAll(spec, sort)
    }

    fun searchPlace(text: String): List<PlaceEntity> {
        return placeRepository.searchPlace(text) ?: throw PlaceSearchNotFoundException(text)
    }
}

