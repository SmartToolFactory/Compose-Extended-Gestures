package com.smarttoolfactory.gesture

import androidx.compose.foundation.gestures.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import kotlin.math.PI
import kotlin.math.abs

enum class PointerRequisite {
    LessThan, EqualTo, GreaterThan, None
}

/**
 * Returns the rotation, in degrees, of the pointers between the
 * [PointerInputChange.previousPosition] and [PointerInputChange.position] states.
 *
 * Only number of pointers that equal to [numberOfPointersRequired] that are down
 * in both previous and current states are considered.
 *
 */
@Deprecated(
    message = "Use detectPointerTransformGestures which returns gesture " +
            "end and number of pointer that are down"
)
suspend fun PointerInputScope.detectMultiplePointerTransformGestures(
    panZoomLock: Boolean = false,
    numberOfPointersRequired: Int = 2,
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit
) {
    forEachGesture {
        awaitPointerEventScope {
            var rotation = 0f
            var zoom = 1f
            var pan = Offset.Zero
            var pastTouchSlop = false
            val touchSlop = viewConfiguration.touchSlop
            var lockedToPanZoom = false

            awaitFirstDown(requireUnconsumed = false)

            do {
                val event = awaitPointerEvent()

                val downPointerCount = event.changes.map {
                    it.pressed && it.previousPressed
                }.size

                // If any position change is consumed from another pointer or pointer
                // count that is pressed is not equal to pointerCount cancel this gesture
                val canceled = event.changes.any { it.positionChangeConsumed() }

                if (!canceled && downPointerCount == numberOfPointersRequired) {
                    val zoomChange = event.calculateZoom()
                    val rotationChange = event.calculateRotation()
                    val panChange = event.calculatePan()

                    if (!pastTouchSlop) {
                        zoom *= zoomChange
                        rotation += rotationChange
                        pan += panChange

                        val centroidSize = event.calculateCentroidSize(useCurrent = false)
                        val zoomMotion = abs(1 - zoom) * centroidSize
                        val rotationMotion =
                            abs(rotation * PI.toFloat() * centroidSize / 180f)
                        val panMotion = pan.getDistance()

                        if (zoomMotion > touchSlop ||
                            rotationMotion > touchSlop ||
                            panMotion > touchSlop
                        ) {
                            pastTouchSlop = true
                            lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                        }
                    }

                    if (pastTouchSlop) {
                        val centroid = event.calculateCentroid(useCurrent = false)
                        val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                        if (effectiveRotation != 0f ||
                            zoomChange != 1f ||
                            panChange != Offset.Zero
                        ) {
                            onGesture(centroid, panChange, zoomChange, effectiveRotation)
                        }
                        event.changes.forEach {
                            if (it.positionChanged()) {
                                it.consumeAllChanges()
                            }
                        }
                    }
                }
            } while (!canceled && event.changes.any { it.pressed })
        }
    }
}

/**
 * A gesture detector for rotation, panning, and zoom. Once touch slop has been reached, the
 * user can use rotation, panning and zoom gestures. [onGesture] will be called when any of the
 * rotation, zoom or pan occurs, passing the rotation angle in degrees, zoom in scale factor and
 * pan as an offset in pixels. Each of these changes is a difference between the previous call
 * and the current gesture. This will consume all position changes after touch slop has
 * been reached. [onGesture] will also provide centroid of all the pointers that are down.
 *
 * After gesture started  when last pointer is up [onGestureEnd] is triggered.
 *
 * @param numberOfPointers number of pointer required to be down for gestures to commence. Value
 * of this parameter cannot be lower than 1
 * @param requisite determines whether number of pointer down should be equal to less than or greater than
 * [numberOfPointers] for this gesture. If [PointerRequisite.None] is set [numberOfPointers] is
 * not taken into consideration
 * @param onGesture callback for passing centroid, pan, zoom, rotation and pointer size to
 * caller
 * @param onGestureEnd callback that notifies last pointer is up and gesture is ended if it's
 * started by fulfilling requisite.
 *
 */
suspend fun PointerInputScope.detectPointerTransformGestures(
    panZoomLock: Boolean = false,
    numberOfPointers: Int = 1,
    requisite: PointerRequisite = PointerRequisite.None,
    onGesture:
        (centroid: Offset, pan: Offset, zoom: Float, rotation: Float, pointerSize: Int) -> Unit,
    onGestureEnd: (() -> Unit)? = null
) {

    require(numberOfPointers > 0)

    forEachGesture {
        awaitPointerEventScope {
            var rotation = 0f
            var zoom = 1f
            var pan = Offset.Zero
            var pastTouchSlop = false
            val touchSlop = viewConfiguration.touchSlop
            var lockedToPanZoom = false

            var gestureStarted = false

            awaitFirstDown(requireUnconsumed = false)

            do {
                val event = awaitPointerEvent()

                val downPointerCount = event.changes.map {
                    it.pressed
                }.size

                val requirementFulfilled = when (requisite) {
                    PointerRequisite.LessThan -> {
                        (downPointerCount < numberOfPointers)
                    }
                    PointerRequisite.EqualTo -> {
                        (downPointerCount == numberOfPointers)
                    }
                    PointerRequisite.GreaterThan -> {
                        (downPointerCount > numberOfPointers)
                    }
                    else -> true
                }

                // If any position change is consumed from another PointerInputChange
                // or pointer count requirement is not fulfilled
                val canceled =
                    event.changes.any { it.positionChangeConsumed() }

                if (!canceled && requirementFulfilled) {
                    gestureStarted = true
                    val zoomChange = event.calculateZoom()
                    val rotationChange = event.calculateRotation()
                    val panChange = event.calculatePan()

                    if (!pastTouchSlop) {
                        zoom *= zoomChange
                        rotation += rotationChange
                        pan += panChange

                        val centroidSize = event.calculateCentroidSize(useCurrent = false)
                        val zoomMotion = abs(1 - zoom) * centroidSize
                        val rotationMotion =
                            abs(rotation * PI.toFloat() * centroidSize / 180f)
                        val panMotion = pan.getDistance()

                        if (zoomMotion > touchSlop ||
                            rotationMotion > touchSlop ||
                            panMotion > touchSlop
                        ) {
                            pastTouchSlop = true
                            lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                        }
                    }

                    if (pastTouchSlop) {
                        val centroid = event.calculateCentroid(useCurrent = false)
                        val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                        if (effectiveRotation != 0f ||
                            zoomChange != 1f ||
                            panChange != Offset.Zero
                        ) {
                            onGesture(
                                centroid,
                                panChange,
                                zoomChange,
                                effectiveRotation,
                                downPointerCount
                            )
                        }
                        event.changes.forEach {
                            if (it.positionChanged()) {
                                it.consumeAllChanges()
                            }
                        }
                    }
                }
            } while (!canceled && event.changes.any { it.pressed })

            if (gestureStarted) {
                onGestureEnd?.invoke()
            }
        }
    }
}


/**
 * A gesture detector for rotation, panning, and zoom. Once touch slop has been reached, the
 * user can use rotation, panning and zoom gestures. [onGesture] will be called when any of the
 * rotation, zoom or pan occurs, passing the rotation angle in degrees, zoom in scale factor and
 * pan as an offset in pixels. Each of these changes is a difference between the previous call
 * and the current gesture. This will consume all position changes after touch slop has
 * been reached. [onGesture] will also provide centroid of all the pointers that are down.
 *
 * After gesture started  when last pointer is up [onGestureEnd] is triggered.
 *
 * If [panZoomLock] is `true`, rotation is allowed only if touch slop is detected for rotation
 * before pan or zoom motions. If not, pan and zoom gestures will be detected, but rotation
 * gestures will not be. If [panZoomLock] is `false`, once touch slop is reached, all three
 * gestures are detected
 *
 * @param onGesture callback for passing centroid, pan, zoom, rotation and [List] of
 * [PointerInputChange] to  caller
 * @param onGestureEnd callback that notifies last pointer is up and gesture is ended if it's
 * started by fulfilling requisite.
 */
suspend fun PointerInputScope.detectTransformGesturesAndChanges(
    panZoomLock: Boolean = false,
    onGesture: (
        centroid: Offset,
        pan: Offset,
        zoom: Float,
        rotation: Float,
        changes: List<PointerInputChange>
    ) -> Unit,
    onGestureEnd: (() -> Unit)? = null
) {
    forEachGesture {
        awaitPointerEventScope {
            var rotation = 0f
            var zoom = 1f
            var pan = Offset.Zero
            var pastTouchSlop = false
            val touchSlop = viewConfiguration.touchSlop
            var lockedToPanZoom = false

            var gestureStarted = false

            awaitFirstDown(requireUnconsumed = false)

            do {
                val event = awaitPointerEvent()

                // If any position change is consumed from another PointerInputChange
                // or pointer count requirement is not fulfilled
                val canceled =
                    event.changes.any { it.positionChangeConsumed() }

                if (!canceled) {
                    gestureStarted = true
                    val zoomChange = event.calculateZoom()
                    val rotationChange = event.calculateRotation()
                    val panChange = event.calculatePan()

                    if (!pastTouchSlop) {
                        zoom *= zoomChange
                        rotation += rotationChange
                        pan += panChange

                        val centroidSize = event.calculateCentroidSize(useCurrent = false)
                        val zoomMotion = abs(1 - zoom) * centroidSize
                        val rotationMotion =
                            abs(rotation * PI.toFloat() * centroidSize / 180f)
                        val panMotion = pan.getDistance()

                        if (zoomMotion > touchSlop ||
                            rotationMotion > touchSlop ||
                            panMotion > touchSlop
                        ) {
                            pastTouchSlop = true
                            lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                        }
                    }

                    if (pastTouchSlop) {
                        val centroid = event.calculateCentroid(useCurrent = false)
                        val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                        if (effectiveRotation != 0f ||
                            zoomChange != 1f ||
                            panChange != Offset.Zero
                        ) {
                            onGesture(
                                centroid,
                                panChange,
                                zoomChange,
                                effectiveRotation,
                                event.changes
                            )
                        }
                        event.changes.forEach {
                            if (it.positionChanged()) {
                                it.consumeAllChanges()
                            }
                        }
                    }
                }
            } while (!canceled && event.changes.any { it.pressed })

            if (gestureStarted) {
                onGestureEnd?.invoke()
            }
        }
    }
}

