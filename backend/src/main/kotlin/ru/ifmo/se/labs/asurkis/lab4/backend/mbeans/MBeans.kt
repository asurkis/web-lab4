package ru.ifmo.se.labs.asurkis.lab4.backend.mbeans

import org.springframework.jmx.export.annotation.ManagedAttribute
import org.springframework.jmx.export.annotation.ManagedResource
import org.springframework.jmx.export.notification.NotificationPublisher
import org.springframework.jmx.export.notification.NotificationPublisherAware
import org.springframework.stereotype.Component
import ru.ifmo.se.labs.asurkis.lab4.backend.data.Result
import ru.ifmo.se.labs.asurkis.lab4.backend.repositories.ResultRepository
import javax.management.Notification

@Component
@ManagedResource
class TotalCounter(val resultRepository: ResultRepository) : NotificationPublisherAware {
    val countAll
        @ManagedAttribute
        get() = resultRepository.count()

    val countMismatched
        @ManagedAttribute
        get() = resultRepository.findAll().filter { !it.result }.count()

    private var notificationPublisher: NotificationPublisher? = null

    override fun setNotificationPublisher(notificationPublisher: NotificationPublisher) {
        this.notificationPublisher = notificationPublisher
    }

    private var counter = 0
    private var sequenceNumber = 0L
    private val counterLimit = 4

    @Synchronized
    fun onResultAdded(result: Result) {
        if (result.result) {
            counter = 0
        } else {
            if (++counter >= counterLimit) {
                notificationPublisher?.sendNotification(Notification(
                        "miss", this, sequenceNumber++,
                        "User missed $counterLimit times in a row"))
            }
        }
    }
}

@Component
@ManagedResource
class RatioCounter(val resultRepository: ResultRepository) {
    val ratio
        @ManagedAttribute
        get() = resultRepository.findAll().filter { it.result }.count().toDouble() /
                resultRepository.count().toDouble()
}
