package com.smarttoolfactory.gesture

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*

/**
 * Create a modifier for processing pointer motion input within the region of the modified element.
 *
 * After [AwaitPointerEventScope.awaitFirstDown] returned a [PointerInputChange] then
 * [onDown] is called at first pointer contact.
 * Moving any pointer causes [AwaitPointerEventScope.awaitPointerEvent] then [onMove] is called.
 * When last pointer is up [onUp] is called.
 *
 * To prevent other pointer functions that call [awaitFirstDown]
 * or [AwaitPointerEventScope.awaitPointerEvent]
 * (scroll, swipe, detect functions)
 * receiving changes call [PointerInputChange.consume]  in [onMove]  or call
 * [PointerInputChange.consume] in [onDown] to prevent events
 * that check first pointer interaction.
 *
 * @param onDown is invoked when first pointer is down initially.
 * @param onMove is invoked when one or multiple pointers are being moved on screen.
 * @param onUp is invoked when last pointer is up
 * @param delayAfterDownInMillis is optional delay after [onDown] This delay might be
 * required Composables like **Canvas** to process [onDown] before [onMove]
 * @param requireUnconsumed is `true` and the first
 * down is consumed in the [PointerEventPass.Main] pass, that gesture is ignored.
 *
 * The pointer input handling block will be cancelled and re-started when pointerInput
 * is recomposed with a different [key1].
 */
fun Modifier.pointerMotionEvents(
    key1: Any? = Unit,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (PointerInputChange) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L,
    requireUnconsumed: Boolean = true
) = this.then(
    Modifier.pointerInput(key1) {
        detectMotionEvents(onDown, onMove, onUp, delayAfterDownInMillis, requireUnconsumed)
    }
)

/**
 * Create a modifier for processing pointer motion input within the region of the modified element.
 *
 * After [AwaitPointerEventScope.awaitFirstDown] returned a [PointerInputChange] then
 * [onDown] is called at first pointer contact.
 * Moving any pointer causes [AwaitPointerEventScope.awaitPointerEvent] then [onMove] is called.
 * When last pointer is up [onUp] is called.
 *
 * To prevent other pointer functions that call [awaitFirstDown]
 * or [AwaitPointerEventScope.awaitPointerEvent]
 * (scroll, swipe, detect functions)
 * receiving changes call [PointerInputChange.consume]  in [onMove]  or call
 * [PointerInputChange.consume] in [onDown] to prevent events
 * that check first pointer interaction.
 *
 * @param onDown is invoked when first pointer is down initially.
 * @param onMove is invoked when one or multiple pointers are being moved on screen.
 * @param onUp is invoked when last pointer is up
 * @param delayAfterDownInMillis is optional delay after [onDown] This delay might be
 * required Composables like **Canvas** to process [onDown] before [onMove]
 * @param requireUnconsumed is `true` and the first
 * down is consumed in the [PointerEventPass.Main] pass, that gesture is ignored.
 *
 * The pointer input handling block will be cancelled and re-started when pointerInput
 * is recomposed with a different [key1] or [key2].
 */
fun Modifier.pointerMotionEvents(
    key1: Any?,
    key2: Any?,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (PointerInputChange) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L,
    requireUnconsumed: Boolean = true
) = this.then(
    Modifier.pointerInput(key1, key2) {
        detectMotionEvents(onDown, onMove, onUp, delayAfterDownInMillis, requireUnconsumed)
    }
)

/**
 * Create a modifier for processing pointer motion input within the region of the modified element.
 *
 * After [AwaitPointerEventScope.awaitFirstDown] returned a [PointerInputChange] then
 * [onDown] is called at first pointer contact.
 * Moving any pointer causes [AwaitPointerEventScope.awaitPointerEvent] then [onMove] is called.
 * When last pointer is up [onUp] is called.
 *
 * To prevent other pointer functions that call [awaitFirstDown]
 * or [AwaitPointerEventScope.awaitPointerEvent]
 * (scroll, swipe, detect functions)
 * receiving changes call [PointerInputChange.consume]  in [onMove]  or call
 * [PointerInputChange.consume] in [onDown] to prevent events
 * that check first pointer interaction.
 *
 * @param onDown is invoked when first pointer is down initially.
 * @param onMove is invoked when one or multiple pointers are being moved on screen.
 * @param onUp is invoked when last pointer is up
 * @param delayAfterDownInMillis is optional delay after [onDown] This delay might be
 * required Composables like **Canvas** to process [onDown] before [onMove]
 * @param requireUnconsumed is `true` and the first
 * down is consumed in the [PointerEventPass.Main] pass, that gesture is ignored.
 *
 * The pointer input handling block will be cancelled and re-started when pointerInput
 * is recomposed with any different [keys].
 */
fun Modifier.pointerMotionEvents(
    vararg keys: Any?,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (PointerInputChange) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L,
    requireUnconsumed: Boolean = true
) = this.then(
    Modifier.pointerInput(keys) {
        detectMotionEvents(onDown, onMove, onUp, delayAfterDownInMillis, requireUnconsumed)
    }
)

/**
 * Create a modifier for processing pointer motion input within the region of the modified element.
 *
 * After [AwaitPointerEventScope.awaitFirstDown] returned a [PointerInputChange] then
 * [onDown] is called at first pointer contact.
 * Moving any pointer causes [AwaitPointerEventScope.awaitPointerEvent] then [onMove] is called.
 * When last pointer is up [onUp] is called.
 *
 * To prevent other pointer functions that call [awaitFirstDown]
 * or [AwaitPointerEventScope.awaitPointerEvent]
 * (scroll, swipe, detect functions)
 * receiving changes call [PointerInputChange.consume]  in [onMove]  or call
 * [PointerInputChange.consume] in [onDown] to prevent events
 * that check first pointer interaction.
 *
 * @param onDown is invoked when first pointer is down initially.
 * @param onMove is invoked when one or multiple pointers are being moved on screen.
 * @param onUp is invoked when last pointer is up
 * @param delayAfterDownInMillis is optional delay after [onDown] This delay might be
 * required Composables like **Canvas** to process [onDown] before [onMove]
 * @param requireUnconsumed is `true` and the first
 * down is consumed in the [PointerEventPass.Main] pass, that gesture is ignored.
 *
 *  The pointer input handling block will be cancelled and re-started when pointerInput
 *  is recomposed with a different [key1].
 */
fun Modifier.pointerMotionEventList(
    key1: Any? = Unit,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (List<PointerInputChange>) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L,
    requireUnconsumed: Boolean = true
) = this.then(
    Modifier.pointerInput(key1) {
        detectMotionEventsAsList(onDown, onMove, onUp, delayAfterDownInMillis, requireUnconsumed)
    }
)

/**
 * Create a modifier for processing pointer motion input within the region of the modified element.
 *
 * After [AwaitPointerEventScope.awaitFirstDown] returned a [PointerInputChange] then
 * [onDown] is called at first pointer contact.
 * Moving any pointer causes [AwaitPointerEventScope.awaitPointerEvent] then [onMove] is called.
 * When last pointer is up [onUp] is called.
 *
 * To prevent other pointer functions that call [awaitFirstDown]
 * or [AwaitPointerEventScope.awaitPointerEvent]
 * (scroll, swipe, detect functions)
 * receiving changes call [PointerInputChange.consume]  in [onMove]  or call
 * [PointerInputChange.consume] in [onDown] to prevent events
 * that check first pointer interaction.
 *
 * @param onDown is invoked when first pointer is down initially.
 * @param onMove is invoked when one or multiple pointers are being moved on screen.
 * @param onUp is invoked when last pointer is up
 * @param delayAfterDownInMillis is optional delay after [onDown] This delay might be
 * required Composables like **Canvas** to process [onDown] before [onMove]
 * @param requireUnconsumed is `true` and the first
 * down is consumed in the [PointerEventPass.Main] pass, that gesture is ignored.
 *
 * The pointer input handling block will be cancelled and re-started when pointerInput
 * is recomposed with a different [key1] or [key2].
 */
fun Modifier.pointerMotionEventList(
    key1: Any? = Unit,
    key2: Any? = Unit,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (List<PointerInputChange>) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L,
    requireUnconsumed: Boolean = true
) = this.then(
    Modifier.pointerInput(key1, key2) {
        detectMotionEventsAsList(onDown, onMove, onUp, delayAfterDownInMillis, requireUnconsumed)
    }
)

/**
 * Create a modifier for processing pointer motion input within the region of the modified element.
 *
 * After [AwaitPointerEventScope.awaitFirstDown] returned a [PointerInputChange] then
 * [onDown] is called at first pointer contact.
 * Moving any pointer causes [AwaitPointerEventScope.awaitPointerEvent] then [onMove] is called.
 * When last pointer is up [onUp] is called.
 *
 * To prevent other pointer functions that call [awaitFirstDown]
 * or [AwaitPointerEventScope.awaitPointerEvent]
 * (scroll, swipe, detect functions)
 * receiving changes call [PointerInputChange.consume]  in [onMove]  or call
 * [PointerInputChange.consume] in [onDown] to prevent events
 * that check first pointer interaction.
 *
 * @param onDown is invoked when first pointer is down initially.
 * @param onMove is invoked when one or multiple pointers are being moved on screen.
 * @param onUp is invoked when last pointer is up
 * @param delayAfterDownInMillis is optional delay after [onDown] This delay might be
 * required Composables like **Canvas** to process [onDown] before [onMove]
 * @param requireUnconsumed is `true` and the first
 * down is consumed in the [PointerEventPass.Main] pass, that gesture is ignored.
 *
 * The pointer input handling block will be cancelled and re-started when pointerInput
 * is recomposed with any different [keys].
 */
fun Modifier.pointerMotionEventList(
    vararg keys: Any?,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (List<PointerInputChange>) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L,
    requireUnconsumed: Boolean = true
) = this.then(
    Modifier.pointerInput(keys) {
        detectMotionEventsAsList(onDown, onMove, onUp, delayAfterDownInMillis, requireUnconsumed)
    }
)
